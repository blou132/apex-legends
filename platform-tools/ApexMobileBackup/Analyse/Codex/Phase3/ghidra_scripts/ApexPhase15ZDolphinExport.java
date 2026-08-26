// Bounded Dolphin error-path export for Phase15Z.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Data;
import ghidra.program.model.listing.DataIterator;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.scalar.Scalar;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.program.model.symbol.Symbol;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ApexPhase15ZDolphinExport extends GhidraScript {
    private static final long RAW_ERROR = 154140714L;
    private static final long REMOVE_BASE = 100000000L;
    private static final long VISIBLE_SUFFIX = 54140714L;
    private static final String PROCESS_ACTION_ERROR = "005be71c";

    private static final String[] EXACT_TERMS = {
        "NormalConnectVersionSvr",
        "OnActionError",
        "ProcessActionError",
        "ClientEvent",
        "UpdateResult",
        "GemReportHelper",
        "version_mgr_imp.cpp",
        "GcloudDolphinVersionAction.cpp"
    };

    private PrintWriter out;
    private DecompInterface decompiler;
    private final Map<Function, Set<String>> selected = new LinkedHashMap<>();

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase15Z local output directory argument is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase15Z output directory");
        }

        File outputFile = new File(outputDir, "libgcloud_dolphin_export.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());
            line("RAW_ERROR_DECIMAL", Long.toString(RAW_ERROR));
            line("RAW_ERROR_HEX", String.format("0x%08x", RAW_ERROR));

            addAddressTarget(PROCESS_ACTION_ERROR, "PROCESS_ACTION_ERROR_ROOT");
            for (int index = 1; index < args.length; index++) {
                if (args[index].startsWith("range:")) {
                    String[] bounds = args[index].substring("range:".length()).split(":", 2);
                    if (bounds.length == 2) {
                        dumpInstructionRange(toAddr(bounds[0]), toAddr(bounds[1]));
                    }
                } else if (args[index].startsWith("data:")) {
                    Address dataAddress = toAddr(args[index].substring("data:".length()));
                    line("EXPLICIT_DATA_TARGET", dataAddress.toString());
                    dumpReferenceWindow(dataAddress);
                    exportReferencesToData(dataAddress);
                } else {
                    addAddressTarget(args[index], "EXPLICIT_FOLLOWUP");
                }
            }

            scanExactStrings();
            scanExactInstructionScalars();
            scanExactRawBytes();

            line("SELECTED_FUNCTION_COUNT", Integer.toString(selected.size()));
            for (Map.Entry<Function, Set<String>> entry : selected.entrySet()) {
                exportFunction(entry.getKey(), entry.getValue());
            }

            println("PHASE15Z_DOLPHIN_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private void addAddressTarget(String value, String reason) {
        Address address;
        try {
            address = toAddr(value.replace("0x", ""));
        } catch (Exception error) {
            line("INVALID_EXPLICIT_ADDRESS", value, clean(error.getMessage()));
            return;
        }
        Function function = currentProgram.getFunctionManager().getFunctionAt(address);
        if (function == null) {
            function = currentProgram.getFunctionManager().getFunctionContaining(address);
        }
        if (function == null) {
            line("MISSING_FUNCTION_AT", address.toString(), reason);
            return;
        }
        select(function, reason);
    }

    private void scanExactStrings() {
        DataIterator iterator = currentProgram.getListing().getDefinedData(true);
        int hitCount = 0;
        while (iterator.hasNext()) {
            Data data = iterator.next();
            Object object = data.getValue();
            if (!(object instanceof String)) {
                continue;
            }
            String value = (String) object;
            for (String term : EXACT_TERMS) {
                if (!value.contains(term)) {
                    continue;
                }
                hitCount++;
                line("EXACT_STRING", term, data.getAddress().toString(), clean(value));
                ReferenceIterator references = currentProgram.getReferenceManager()
                    .getReferencesTo(data.getAddress());
                while (references.hasNext()) {
                    Reference reference = references.next();
                    Function owner = currentProgram.getFunctionManager()
                        .getFunctionContaining(reference.getFromAddress());
                    line(
                        "EXACT_STRING_REF",
                        term,
                        reference.getFromAddress().toString(),
                        describe(owner)
                    );
                    if (owner != null && shouldSelectStringOwner(term)) {
                        select(owner, "EXACT_STRING_" + term);
                    }
                }
            }
        }
        line("EXACT_STRING_HIT_COUNT", Integer.toString(hitCount));
    }

    private boolean shouldSelectStringOwner(String term) {
        return term.equals("NormalConnectVersionSvr") ||
            term.equals("OnActionError") ||
            term.equals("ProcessActionError") ||
            term.equals("ClientEvent") ||
            term.equals("UpdateResult") ||
            term.equals("GemReportHelper");
    }

    private void scanExactInstructionScalars() {
        InstructionIterator instructions = currentProgram.getListing().getInstructions(true);
        int hitCount = 0;
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            boolean matched = false;
            for (int operand = 0; operand < instruction.getNumOperands(); operand++) {
                for (Object object : instruction.getOpObjects(operand)) {
                    if (object instanceof Scalar &&
                        ((Scalar) object).getUnsignedValue() == RAW_ERROR) {
                        matched = true;
                    }
                }
            }
            if (!matched) {
                continue;
            }
            hitCount++;
            Function owner = currentProgram.getFunctionManager()
                .getFunctionContaining(instruction.getAddress());
            line(
                "EXACT_SCALAR_INSTRUCTION",
                instruction.getAddress().toString(),
                clean(instruction.toString()),
                describe(owner)
            );
            if (owner != null) {
                select(owner, "EXACT_RAW_ERROR_INSTRUCTION");
                exportBoundedGraph(owner, "RAW_ERROR", 2, new LinkedHashSet<>());
            }
        }
        line("EXACT_SCALAR_INSTRUCTION_HIT_COUNT", Integer.toString(hitCount));
    }

    private void scanExactRawBytes() {
        byte[] pattern32 = {
            (byte) 0x2a, (byte) 0x00, (byte) 0x30, (byte) 0x09
        };
        byte[] pattern64 = {
            (byte) 0x2a, (byte) 0x00, (byte) 0x30, (byte) 0x09,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        scanPattern("RAW_ERROR_LE32", pattern32);
        scanPattern("RAW_ERROR_LE64", pattern64);
    }

    private void scanPattern(String label, byte[] pattern) {
        int hitCount = 0;
        for (MemoryBlock block : currentProgram.getMemory().getBlocks()) {
            if (!block.isInitialized()) {
                continue;
            }
            Address cursor = block.getStart();
            while (cursor != null && cursor.compareTo(block.getEnd()) <= 0) {
                Address hit;
                try {
                    hit = currentProgram.getMemory().findBytes(
                        cursor,
                        block.getEnd(),
                        pattern,
                        null,
                        true,
                        TaskMonitor.DUMMY
                    );
                } catch (Exception error) {
                    line("RAW_PATTERN_ERROR", label, block.getName(), clean(error.getMessage()));
                    break;
                }
                if (hit == null) {
                    break;
                }
                hitCount++;
                Function owner = currentProgram.getFunctionManager().getFunctionContaining(hit);
                line("RAW_PATTERN_HIT", label, hit.toString(), block.getName(), describe(owner));
                if (owner != null) {
                    select(owner, label);
                    exportBoundedGraph(owner, label, 2, new LinkedHashSet<>());
                }
                ReferenceIterator references = currentProgram.getReferenceManager().getReferencesTo(hit);
                while (references.hasNext()) {
                    Reference reference = references.next();
                    Function referenceOwner = currentProgram.getFunctionManager()
                        .getFunctionContaining(reference.getFromAddress());
                    line(
                        "RAW_PATTERN_REF",
                        label,
                        reference.getFromAddress().toString(),
                        reference.getReferenceType().toString(),
                        describe(referenceOwner)
                    );
                    if (referenceOwner != null) {
                        select(referenceOwner, label + "_DIRECT_REF");
                    }
                }
                cursor = hit.next();
            }
        }
        line("RAW_PATTERN_HIT_COUNT", label, Integer.toString(hitCount));
    }

    private void exportBoundedGraph(
        Function function,
        String root,
        int remainingDepth,
        Set<Address> visited
    ) {
        if (function == null || !visited.add(function.getEntryPoint())) {
            return;
        }
        line(
            "BOUNDED_GRAPH_NODE",
            root,
            Integer.toString(remainingDepth),
            describe(function)
        );
        if (remainingDepth == 0) {
            return;
        }
        for (Function caller : function.getCallingFunctions(TaskMonitor.DUMMY)) {
            line("BOUNDED_GRAPH_EDGE", root, "CALLER", describe(caller), describe(function));
            exportBoundedGraph(caller, root, remainingDepth - 1, visited);
        }
        for (Function callee : function.getCalledFunctions(TaskMonitor.DUMMY)) {
            line("BOUNDED_GRAPH_EDGE", root, "CALLEE", describe(function), describe(callee));
            exportBoundedGraph(callee, root, remainingDepth - 1, visited);
        }
    }

    private void exportFunction(Function function, Set<String> reasons) {
        line(
            "FUNCTION_BEGIN",
            function.getName(true),
            function.getEntryPoint().toString(),
            "reasons=" + String.join(",", reasons)
        );

        ReferenceIterator entryReferences = currentProgram.getReferenceManager()
            .getReferencesTo(function.getEntryPoint());
        while (entryReferences.hasNext()) {
            Reference reference = entryReferences.next();
            Function owner = currentProgram.getFunctionManager()
                .getFunctionContaining(reference.getFromAddress());
            line(
                "ENTRY_REF",
                reference.getFromAddress().toString(),
                reference.getReferenceType().toString(),
                describe(owner)
            );
            if (reference.getReferenceType().isData() ||
                reference.getReferenceType().isIndirect()) {
                dumpReferenceWindow(reference.getFromAddress());
            }
        }

        Set<String> callers = new LinkedHashSet<>();
        for (Function caller : function.getCallingFunctions(TaskMonitor.DUMMY)) {
            callers.add(describe(caller));
        }
        line("DIRECT_CALLERS", String.join(",", callers));

        Set<String> callees = new LinkedHashSet<>();
        for (Function callee : function.getCalledFunctions(TaskMonitor.DUMMY)) {
            callees.add(describe(callee));
        }
        line("DIRECT_CALLEES", String.join(",", callees));

        inspectConstants(function);
        inspectReferencedStrings(function);

        DecompileResults results = decompiler.decompileFunction(function, 120, TaskMonitor.DUMMY);
        if (results.decompileCompleted()) {
            out.println("DECOMPILE_BEGIN");
            out.println(clean(results.getDecompiledFunction().getC()));
            out.println("DECOMPILE_END");
        } else {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        }
        line("FUNCTION_END", function.getName(true), function.getEntryPoint().toString());
    }

    private void dumpReferenceWindow(Address center) {
        line("REFERENCE_WINDOW_BEGIN", center.toString());
        for (long offset = -0x20; offset <= 0x40; offset += 8) {
            Address slot;
            try {
                slot = center.add(offset);
            } catch (Exception error) {
                continue;
            }
            String raw = "";
            long rawValue = 0;
            boolean readable = false;
            try {
                rawValue = currentProgram.getMemory().getLong(slot);
                raw = String.format("0x%016x", rawValue);
                readable = true;
            } catch (Exception ignored) {
                raw = "UNREADABLE";
            }
            Symbol symbol = currentProgram.getSymbolTable().getPrimarySymbol(slot);
            line(
                "REFERENCE_WINDOW_SLOT",
                slot.toString(),
                raw,
                symbol == null ? "" : symbol.getName(true)
            );
            if (readable) {
                try {
                    Address rawTarget = toAddr(rawValue);
                    Data rawData = currentProgram.getListing().getDefinedDataContaining(rawTarget);
                    Symbol rawSymbol = currentProgram.getSymbolTable().getPrimarySymbol(rawTarget);
                    line(
                        "REFERENCE_WINDOW_RAW_TARGET",
                        slot.toString(),
                        rawTarget.toString(),
                        rawSymbol == null ? "" : rawSymbol.getName(true),
                        rawData != null && rawData.getValue() instanceof String
                            ? clean((String) rawData.getValue())
                            : readBoundedAscii(rawTarget, 160)
                    );
                } catch (Exception ignored) {
                    // The raw word is not necessarily an address.
                }
            }
            for (Reference slotReference : currentProgram.getReferenceManager().getReferencesFrom(slot)) {
                Function target = currentProgram.getFunctionManager()
                    .getFunctionAt(slotReference.getToAddress());
                line(
                    "REFERENCE_WINDOW_TARGET",
                    slot.toString(),
                    slotReference.getReferenceType().toString(),
                    slotReference.getToAddress().toString(),
                    describe(target)
                );
            }
        }
        line("REFERENCE_WINDOW_END", center.toString());
    }

    private void exportReferencesToData(Address target) {
        ReferenceIterator references = currentProgram.getReferenceManager().getReferencesTo(target);
        while (references.hasNext()) {
            Reference reference = references.next();
            Function owner = currentProgram.getFunctionManager()
                .getFunctionContaining(reference.getFromAddress());
            line(
                "EXPLICIT_DATA_REF",
                target.toString(),
                reference.getFromAddress().toString(),
                reference.getReferenceType().toString(),
                describe(owner)
            );
            if (owner != null) {
                select(owner, "EXPLICIT_DATA_REF_" + target);
            }
        }
    }

    private void dumpInstructionRange(Address start, Address end) {
        line("INSTRUCTION_RANGE_BEGIN", start.toString(), end.toString());
        Instruction instruction = currentProgram.getListing().getInstructionAt(start);
        if (instruction == null) {
            instruction = currentProgram.getListing().getInstructionAfter(start);
        }
        while (instruction != null && instruction.getAddress().compareTo(end) <= 0) {
            line(
                "INSTRUCTION_RANGE_ENTRY",
                instruction.getAddress().toString(),
                clean(instruction.toString())
            );
            instruction = currentProgram.getListing().getInstructionAfter(instruction.getAddress());
        }
        line("INSTRUCTION_RANGE_END", start.toString(), end.toString());
    }

    private String readBoundedAscii(Address start, int maximumLength) {
        StringBuilder value = new StringBuilder();
        for (int index = 0; index < maximumLength; index++) {
            int current;
            try {
                current = currentProgram.getMemory().getByte(start.add(index)) & 0xff;
            } catch (Exception error) {
                break;
            }
            if (current == 0) {
                break;
            }
            if (current < 0x20 || current > 0x7e) {
                return "";
            }
            value.append((char) current);
        }
        return value.toString();
    }

    private void inspectConstants(Function function) {
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            for (int operand = 0; operand < instruction.getNumOperands(); operand++) {
                for (Object object : instruction.getOpObjects(operand)) {
                    if (!(object instanceof Scalar)) {
                        continue;
                    }
                    long value = ((Scalar) object).getUnsignedValue();
                    if (value == RAW_ERROR || value == REMOVE_BASE || value == VISIBLE_SUFFIX) {
                        line(
                            "RELEVANT_CONSTANT",
                            instruction.getAddress().toString(),
                            Long.toString(value),
                            String.format("0x%x", value),
                            clean(instruction.toString())
                        );
                    }
                }
            }
        }
    }

    private void inspectReferencedStrings(Function function) {
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        Set<String> emitted = new LinkedHashSet<>();
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            for (Reference reference : currentProgram.getReferenceManager()
                .getReferencesFrom(instruction.getAddress())) {
                Data data = currentProgram.getListing().getDefinedDataContaining(reference.getToAddress());
                if (data == null || !(data.getValue() instanceof String)) {
                    continue;
                }
                String value = (String) data.getValue();
                if (!isRelevantConnectedString(value)) {
                    continue;
                }
                String key = data.getAddress() + "|" + value;
                if (emitted.add(key)) {
                    line(
                        "CONNECTED_STRING",
                        instruction.getAddress().toString(),
                        data.getAddress().toString(),
                        clean(value)
                    );
                }
            }
        }
    }

    private boolean isRelevantConnectedString(String value) {
        String lower = value.toLowerCase();
        if (value.equals("I") || value.contains("%d") || value.contains("%u") ||
            value.contains("%s") || value.contains("54140714") ||
            value.contains("100000000")) {
            return true;
        }
        if (lower.contains("error") || lower.contains("result") ||
            lower.contains("clientevent") || lower.contains("normalconnectversionsvr") ||
            lower.contains("onactionerror") || lower.contains("processactionerror")) {
            return true;
        }
        for (String term : EXACT_TERMS) {
            if (value.contains(term)) {
                return true;
            }
        }
        return false;
    }

    private void select(Function function, String reason) {
        selected.computeIfAbsent(function, ignored -> new LinkedHashSet<>()).add(reason);
    }

    private String describe(Function function) {
        return function == null
            ? ""
            : function.getName(true) + "@" + function.getEntryPoint();
    }

    private void line(String... values) {
        for (int index = 0; index < values.length; index++) {
            if (index > 0) {
                out.print('\t');
            }
            out.print(clean(values[index]));
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
