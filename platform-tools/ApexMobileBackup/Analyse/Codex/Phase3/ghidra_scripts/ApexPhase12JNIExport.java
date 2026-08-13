// Targeted Phase12 JNI_OnLoad and GameActivity registration export.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.symbol.FlowType;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApexPhase12JNIExport extends GhidraScript {
    private static final long IMAGE_BASE = 0x100000L;
    private static final String TARGET = "JNI_OnLoad";
    private static final int MAX_FUNCTIONS = 96;
    private static final int MAX_DECOMPILE_LINES = 700;

    private Memory memory;
    private PrintWriter out;
    private DecompInterface decompiler;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase12 local output directory is required");
        }
        if (currentProgram.getImageBase().getOffset() != IMAGE_BASE) {
            throw new IllegalStateException("Unexpected image base: " + currentProgram.getImageBase());
        }
        int maxDepth = args.length > 1 ? Integer.parseInt(args[1]) : 5;
        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase12 output directory");
        }

        File outputFile = new File(outputDir, "phase12_jni_export.txt");
        memory = currentProgram.getMemory();
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);
        try {
            line("PROGRAM", currentProgram.getName());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());
            line("TARGET", TARGET);

            Set<Function> candidates = new LinkedHashSet<>();
            collectGhidraCandidates(candidates);
            collectDynamicSymbolCandidates(candidates);
            collectInitArrayEvidence();
            Set<Function> getEnvCandidates = collectConventionalGetEnvCandidates();
            line("CONVENTIONAL_GETENV_CANDIDATE_COUNT",
                Integer.toString(getEnvCandidates.size()));
            for (Function candidate : getEnvCandidates) {
                line("CONVENTIONAL_GETENV_CANDIDATE", candidate.getName(true),
                    candidate.getEntryPoint().toString(), elf(candidate.getEntryPoint()),
                    candidate.getSignature().getPrototypeString());
            }
            if (candidates.isEmpty() && getEnvCandidates.size() == 1) {
                candidates.add(getEnvCandidates.iterator().next());
                line("CONVENTIONAL_GETENV_PROMOTION", "UNIQUE_PATTERN_CANDIDATE");
            }
            line("JNI_ONLOAD_CANDIDATE_COUNT", Integer.toString(candidates.size()));
            for (Function candidate : candidates) {
                line("JNI_ONLOAD_CANDIDATE", candidate.getName(true),
                    candidate.getEntryPoint().toString(), elf(candidate.getEntryPoint()),
                    candidate.getSignature().getPrototypeString());
            }
            if (candidates.size() != 1) {
                line("JNI_ONLOAD_RESOLUTION", "NOT_UNIQUE");
                return;
            }

            Function root = candidates.iterator().next();
            line("JNI_ONLOAD_RESOLUTION", "UNIQUE");
            line("JNI_ONLOAD_FUNCTION", root.getName(true), root.getEntryPoint().toString(),
                elf(root.getEntryPoint()), root.getSignature().getPrototypeString());

            Map<Function, Integer> graph = buildGraph(root, maxDepth);
            line("CALLGRAPH_DEPTH", Integer.toString(maxDepth));
            line("CALLGRAPH_FUNCTION_COUNT", Integer.toString(graph.size()));
            writeEdges(graph);
            List<Map.Entry<Function, Integer>> ordered = new ArrayList<>(graph.entrySet());
            ordered.sort(Comparator
                .comparingInt((Map.Entry<Function, Integer> entry) -> entry.getValue())
                .thenComparingLong(entry -> entry.getKey().getEntryPoint().getOffset()));
            for (Map.Entry<Function, Integer> entry : ordered) {
                exportFunction(entry.getKey(), entry.getValue());
            }
            line("PHASE12_JNI_EXPORT", "OK");
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private void collectGhidraCandidates(Set<Function> candidates) {
        SymbolIterator symbols = currentProgram.getSymbolTable().getSymbols(TARGET);
        int count = 0;
        while (symbols.hasNext()) {
            Symbol symbol = symbols.next();
            if (!TARGET.equals(symbol.getName())) {
                continue;
            }
            count++;
            Function function = currentProgram.getFunctionManager().getFunctionAt(symbol.getAddress());
            if (function == null) {
                function = currentProgram.getFunctionManager().getFunctionContaining(symbol.getAddress());
            }
            line("GHIDRA_SYMBOL", symbol.getName(true), symbol.getAddress().toString(),
                symbol.getSymbolType().toString(), symbol.getSource().toString(),
                function == null ? "NO_FUNCTION" : function.getName(true));
            if (function != null && function.getEntryPoint().equals(symbol.getAddress())) {
                candidates.add(function);
            }
        }
        line("GHIDRA_EXACT_SYMBOL_COUNT", Integer.toString(count));

        int namedFunctionCount = 0;
        FunctionIterator functions = currentProgram.getFunctionManager().getFunctions(true);
        while (functions.hasNext()) {
            Function function = functions.next();
            if (TARGET.equals(function.getName()) || TARGET.equals(function.getName(true))) {
                namedFunctionCount++;
                candidates.add(function);
            }
        }
        line("GHIDRA_NAMED_FUNCTION_COUNT", Integer.toString(namedFunctionCount));
    }

    private void collectDynamicSymbolCandidates(Set<Function> candidates) throws Exception {
        MemoryBlock dynamic = findBlock(".dynamic");
        if (dynamic == null) {
            line("ELF_DYNAMIC_STATUS", "BLOCK_MISSING");
            return;
        }
        Map<Long, Long> tags = new HashMap<>();
        long dynamicSize = dynamic.getEnd().subtract(dynamic.getStart()) + 1;
        for (long offset = 0; offset + 16 <= dynamicSize; offset += 16) {
            Address entry = dynamic.getStart().add(offset);
            long tag = memory.getLong(entry);
            long value = memory.getLong(entry.add(8));
            if (tag == 0) {
                break;
            }
            tags.put(tag, value);
        }
        Long strtabRaw = tags.get(5L);
        Long symtabRaw = tags.get(6L);
        Long hashRaw = tags.get(4L);
        Long syment = tags.get(11L);
        if (strtabRaw == null || symtabRaw == null || hashRaw == null ||
                syment == null || syment != 24) {
            line("ELF_DYNAMIC_STATUS", "REQUIRED_TAG_MISSING");
            return;
        }
        Address strtab = toAddr(strtabRaw + IMAGE_BASE);
        Address symtab = toAddr(symtabRaw + IMAGE_BASE);
        Address hash = toAddr(hashRaw + IMAGE_BASE);
        long symbolCount = memory.getInt(hash.add(4)) & 0xffffffffL;
        line("ELF_DYNAMIC_LAYOUT", strtab.toString(), symtab.toString(), hash.toString(),
            Long.toString(symbolCount));
        int matches = 0;
        for (long index = 0; index < symbolCount; index++) {
            Address entry = symtab.add(index * 24);
            long nameOffset = memory.getInt(entry) & 0xffffffffL;
            if (nameOffset == 0) {
                continue;
            }
            String name = readCString(strtab.add(nameOffset), 128);
            if (!TARGET.equals(name)) {
                continue;
            }
            matches++;
            int info = memory.getByte(entry.add(4)) & 0xff;
            int section = memory.getShort(entry.add(6)) & 0xffff;
            long rawValue = memory.getLong(entry.add(8));
            long size = memory.getLong(entry.add(16));
            Address address = toAddr(rawValue + IMAGE_BASE);
            Function function = currentProgram.getFunctionManager().getFunctionAt(address);
            if (function == null) {
                function = currentProgram.getFunctionManager().getFunctionContaining(address);
            }
            MemoryBlock block = memory.getBlock(address);
            boolean executable = block != null && block.isExecute();
            line("ELF_DYNAMIC_SYMBOL", Long.toString(index), entry.toString(),
                Integer.toString(info), Integer.toString(section), hx(rawValue), hx(size),
                address.toString(), Boolean.toString(executable),
                function == null ? "NO_FUNCTION" : function.getName(true),
                function == null ? "" : function.getEntryPoint().toString());
            if (executable && function != null && function.getEntryPoint().equals(address)) {
                candidates.add(function);
            }
        }
        line("ELF_DYNAMIC_TARGET_MATCH_COUNT", Integer.toString(matches));
    }

    private void collectInitArrayEvidence() throws Exception {
        MemoryBlock dynamic = findBlock(".dynamic");
        if (dynamic == null) {
            return;
        }
        Map<Long, Long> tags = new HashMap<>();
        long dynamicSize = dynamic.getEnd().subtract(dynamic.getStart()) + 1;
        for (long offset = 0; offset + 16 <= dynamicSize; offset += 16) {
            Address entry = dynamic.getStart().add(offset);
            long tag = memory.getLong(entry);
            long value = memory.getLong(entry.add(8));
            if (tag == 0) {
                break;
            }
            tags.put(tag, value);
        }
        Long initRaw = tags.get(12L);
        if (initRaw != null && initRaw != 0) {
            Address initAddress = toAddr(initRaw + IMAGE_BASE);
            Function init = currentProgram.getFunctionManager().getFunctionAt(initAddress);
            line("DT_INIT_FUNCTION", hx(initRaw), initAddress.toString(),
                init == null ? "NO_FUNCTION" : init.getName(true));
        }
        Long arrayRaw = tags.get(25L);
        Long arraySize = tags.get(27L);
        if (arrayRaw == null || arraySize == null || arraySize <= 0) {
            line("DT_INIT_ARRAY_STATUS", "MISSING");
            return;
        }
        Address arrayAddress = toGhidraAddress(arrayRaw);
        long count = Math.min(arraySize / 8, 512);
        line("DT_INIT_ARRAY", hx(arrayRaw), arrayAddress.toString(), hx(arraySize),
            Long.toString(count));
        for (long index = 0; index < count; index++) {
            Address slot = arrayAddress.add(index * 8);
            long rawPointer = memory.getLong(slot);
            Address target = toGhidraAddress(rawPointer);
            Function function = currentProgram.getFunctionManager().getFunctionAt(target);
            line("DT_INIT_ARRAY_ENTRY", Long.toString(index), slot.toString(), hx(rawPointer),
                target.toString(), function == null ? "NO_FUNCTION" : function.getName(true));
        }
    }

    private Set<Function> collectConventionalGetEnvCandidates() {
        Set<Function> candidates = new LinkedHashSet<>();
        ArrayDeque<Instruction> window = new ArrayDeque<>();
        InstructionIterator instructions = currentProgram.getListing().getInstructions(true);
        Pattern vtableLoad = Pattern.compile("ldr(x(?:[12]?[0-9]|30)),\\[(x(?:[12]?[0-9]|30))\\]");
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            MemoryBlock block = memory.getBlock(instruction.getAddress());
            if (block == null || !block.isExecute()) {
                continue;
            }
            window.addLast(instruction);
            while (window.size() > 14) {
                window.removeFirst();
            }
            String current = normalized(instruction.toString());
            if (!current.startsWith("blr")) {
                continue;
            }
            String callRegister = current.substring(3).trim();
            List<Instruction> prior = new ArrayList<>(window);
            Function callOwner = currentProgram.getFunctionManager()
                .getFunctionContaining(instruction.getAddress());
            if (callOwner == null || !hasJniVersionArgument(prior, callOwner)) {
                continue;
            }
            for (int slotIndex = prior.size() - 2; slotIndex >= 0; slotIndex--) {
                String slotText = normalized(prior.get(slotIndex).toString());
                String expectedPrefix = "ldr" + callRegister + ",[";
                if (!slotText.startsWith(expectedPrefix) || !slotText.endsWith(",#0x30]")) {
                    continue;
                }
                int bracket = slotText.indexOf('[');
                int comma = slotText.indexOf(',', bracket);
                if (bracket < 0 || comma < 0) {
                    continue;
                }
                String tableRegister = slotText.substring(bracket + 1, comma);
                boolean tableLoadedFromVm = false;
                for (int baseIndex = slotIndex - 1; baseIndex >= 0; baseIndex--) {
                    String baseText = normalized(prior.get(baseIndex).toString());
                    Matcher matcher = vtableLoad.matcher(baseText);
                    if (matcher.matches() && matcher.group(1).equals(tableRegister)) {
                        String vmRegister = matcher.group(2);
                        if ("x0".equals(vmRegister) || hasX0Alias(prior, baseIndex, vmRegister)) {
                            tableLoadedFromVm = true;
                            break;
                        }
                    }
                }
                if (!tableLoadedFromVm) {
                    continue;
                }
                Function owner = callOwner;
                line("CONVENTIONAL_GETENV_SITE", instruction.getAddress().toString(),
                    prior.get(slotIndex).getAddress().toString(),
                    owner == null ? "NO_FUNCTION" : owner.getName(true),
                    owner == null ? "" : owner.getEntryPoint().toString());
                if (owner != null) {
                    candidates.add(owner);
                }
            }
        }
        return candidates;
    }

    private boolean hasJniVersionArgument(List<Instruction> instructions, Function owner) {
        boolean direct = false;
        boolean lowSix = false;
        boolean highOne = false;
        for (Instruction instruction : instructions) {
            if (!owner.getBody().contains(instruction.getAddress())) {
                continue;
            }
            String text = normalized(instruction.toString());
            if (text.contains("w2,#0x10006") || text.contains("x2,#0x10006")) {
                direct = true;
            }
            if ((text.startsWith("movw2,#0x6") || text.startsWith("movx2,#0x6")) &&
                    !text.contains("lsl")) {
                lowSix = true;
            }
            if ((text.startsWith("movkw2,#0x1") || text.startsWith("movkx2,#0x1")) &&
                    text.contains("lsl#0x10")) {
                highOne = true;
            }
        }
        return direct || (lowSix && highOne);
    }

    private boolean hasX0Alias(List<Instruction> instructions, int beforeIndex,
            String register) {
        String expected = "mov" + register + ",x0";
        for (int index = beforeIndex - 1; index >= 0; index--) {
            if (expected.equals(normalized(instructions.get(index).toString()))) {
                return true;
            }
        }
        return false;
    }

    private String normalized(String value) {
        return value.toLowerCase().replace(" ", "");
    }

    private Address toGhidraAddress(long raw) {
        Address direct = toAddr(raw);
        if (memory.contains(direct)) {
            return direct;
        }
        return toAddr(raw + IMAGE_BASE);
    }

    private Map<Function, Integer> buildGraph(Function root, int maxDepth) {
        Map<Function, Integer> depths = new HashMap<>();
        ArrayDeque<Function> queue = new ArrayDeque<>();
        depths.put(root, 0);
        queue.add(root);
        while (!queue.isEmpty() && depths.size() < MAX_FUNCTIONS) {
            Function current = queue.removeFirst();
            int depth = depths.get(current);
            if (depth >= maxDepth) {
                continue;
            }
            for (Function callee : current.getCalledFunctions(TaskMonitor.DUMMY)) {
                if (!depths.containsKey(callee)) {
                    depths.put(callee, depth + 1);
                    queue.addLast(callee);
                    if (depths.size() >= MAX_FUNCTIONS) {
                        break;
                    }
                }
            }
        }
        return depths;
    }

    private void writeEdges(Map<Function, Integer> graph) {
        for (Map.Entry<Function, Integer> entry : graph.entrySet()) {
            Function caller = entry.getKey();
            for (Function callee : caller.getCalledFunctions(TaskMonitor.DUMMY)) {
                Integer calleeDepth = graph.get(callee);
                line("EDGE", Integer.toString(entry.getValue()), caller.getName(true),
                    caller.getEntryPoint().toString(), callee.getName(true),
                    callee.getEntryPoint().toString(),
                    calleeDepth == null ? "OUTSIDE_LIMIT" : Integer.toString(calleeDepth));
            }
        }
    }

    private void exportFunction(Function function, int depth) {
        line("FUNCTION_BEGIN", Integer.toString(depth), function.getName(true),
            function.getEntryPoint().toString(), elf(function.getEntryPoint()),
            function.getSignature().getPrototypeString());
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            FlowType flow = instruction.getFlowType();
            if (flow.isCall() || flow.isJump()) {
                line("FLOW", instruction.getAddress().toString(), instruction.toString(),
                    flow.toString(), joinAddresses(instruction.getFlows()));
            }
            for (Reference reference : instruction.getReferencesFrom()) {
                if (reference.getReferenceType().isData()) {
                    String stringValue = readCString(reference.getToAddress(), 256);
                    line("DATA_REF", instruction.getAddress().toString(),
                        reference.getToAddress().toString(), reference.getReferenceType().toString(),
                        stringValue);
                }
            }
        }
        DecompileResults results = decompiler.decompileFunction(function, 90, TaskMonitor.DUMMY);
        if (!results.decompileCompleted()) {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        } else {
            out.println("DECOMPILE_BEGIN");
            String[] lines = results.getDecompiledFunction().getC().split("\\R", -1);
            int limit = Math.min(lines.length, MAX_DECOMPILE_LINES);
            for (int i = 0; i < limit; i++) {
                out.println(clean(lines[i]));
            }
            if (limit < lines.length) {
                out.println("/* TRUNCATED */");
            }
            out.println("DECOMPILE_END");
        }
        line("FUNCTION_END", function.getName(true), function.getEntryPoint().toString());
    }

    private MemoryBlock findBlock(String name) {
        for (MemoryBlock block : memory.getBlocks()) {
            if (name.equals(block.getName())) {
                return block;
            }
        }
        return null;
    }

    private String readCString(Address address, int maxLength) {
        try {
            if (!memory.contains(address)) {
                return "";
            }
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < maxLength; i++) {
                int current = memory.getByte(address.add(i)) & 0xff;
                if (current == 0) {
                    break;
                }
                if (current < 0x20 || current > 0x7e) {
                    return "";
                }
                value.append((char) current);
            }
            return value.toString();
        } catch (Exception exception) {
            return "";
        }
    }

    private String joinAddresses(Address[] addresses) {
        List<String> values = new ArrayList<>();
        for (Address address : addresses) {
            values.add(address.toString());
        }
        return String.join(",", values);
    }

    private String elf(Address address) {
        return hx(address.getOffset() - IMAGE_BASE);
    }

    private String hx(long value) {
        return String.format("0x%x", value);
    }

    private void line(String... values) {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                out.print('\t');
            }
            out.print(clean(values[i]));
        }
        out.println();
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
    }
}
