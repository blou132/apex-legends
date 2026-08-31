// Bounded Phase16K CreateDolphin owner and caller export.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.lang.Register;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.listing.Data;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.scalar.Scalar;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ApexPhase16KCreateDolphinOwnerExport extends GhidraScript {
    private static final String EXACT_SYMBOL = "CreateDolphin";
    private static final String PRECEDING_PLT_SYMBOL = "_ZN6GCloud7IGCloud11GetInstanceEv";
    private static final int MAX_CALLER_DEPTH = 2;
    private static final int MAX_SELECTED_FUNCTIONS = 48;
    private static final int MAX_REFS_PER_TARGET = 128;

    private DecompInterface decompiler;
    private PrintWriter out;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length != 1) {
            throw new IllegalArgumentException("Phase16K output directory argument is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase16K output directory");
        }

        File outputFile = new File(outputDir, "libUE4_createdolphin_owner.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("EXECUTABLE_MD5", clean(currentProgram.getExecutableMD5()));
            line("EXECUTABLE_SHA256", clean(currentProgram.getExecutableSHA256()));
            line("LANGUAGE", currentProgram.getLanguageID().toString());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());

            Set<Function> directOwners = new LinkedHashSet<>();
            Set<Address> dataReferences = new LinkedHashSet<>();
            int exactSymbolCount = scanExactSymbols(directOwners, dataReferences);
            line("EXACT_SYMBOL_COUNT", Integer.toString(exactSymbolCount));
            Set<Function> precedingPltOwners = scanPrecedingPltNeighbor();

            for (Address dataAddress : dataReferences) {
                dumpDataCell(dataAddress);
            }

            Map<Function, Integer> selected = collectCallers(directOwners);
            Map<Function, Integer> precedingSelected = collectCallers(precedingPltOwners);
            line("DIRECT_OWNER_COUNT", Integer.toString(directOwners.size()));
            line("SELECTED_FUNCTION_COUNT", Integer.toString(selected.size()));
            for (Map.Entry<Function, Integer> entry : selected.entrySet()) {
                exportFunction(entry.getKey(), entry.getValue(), directOwners.contains(entry.getKey()));
            }
            for (Map.Entry<Function, Integer> entry : precedingSelected.entrySet()) {
                if (!selected.containsKey(entry.getKey())) {
                    exportFunction(entry.getKey(), entry.getValue(), false);
                }
            }

            println("PHASE16K_CREATEDOLPHIN_OWNER_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private int scanExactSymbols(Set<Function> directOwners, Set<Address> dataReferences) {
        SymbolIterator symbols = currentProgram.getSymbolTable().getAllSymbols(true);
        int count = 0;
        while (symbols.hasNext()) {
            Symbol symbol = symbols.next();
            if (!EXACT_SYMBOL.equals(symbol.getName())) {
                continue;
            }
            count++;
            Address address = symbol.getAddress();
            Function function = currentProgram.getFunctionManager().getFunctionAt(address);
            line("EXACT_SYMBOL", symbol.getName(true), address.toString(),
                symbol.getSymbolType().toString(), symbol.getSource().toString(), functionText(function));
            collectReferences(address, directOwners, dataReferences);
        }
        return count;
    }

    private Set<Function> scanPrecedingPltNeighbor() {
        SymbolIterator symbols = currentProgram.getSymbolTable().getAllSymbols(true);
        Set<Function> owners = new LinkedHashSet<>();
        int symbolCount = 0;
        int referenceCount = 0;
        while (symbols.hasNext()) {
            Symbol symbol = symbols.next();
            if (!PRECEDING_PLT_SYMBOL.equals(symbol.getName())) {
                continue;
            }
            symbolCount++;
            line("PRECEDING_PLT_SYMBOL", symbol.getName(true), symbol.getAddress().toString(),
                symbol.getSymbolType().toString(), symbol.getSource().toString());
            ReferenceIterator refs = currentProgram.getReferenceManager()
                .getReferencesTo(symbol.getAddress());
            while (refs.hasNext() && referenceCount < MAX_REFS_PER_TARGET) {
                Reference ref = refs.next();
                referenceCount++;
                Function owner = currentProgram.getFunctionManager()
                    .getFunctionContaining(ref.getFromAddress());
                line("PRECEDING_PLT_REF", symbol.getAddress().toString(),
                    ref.getFromAddress().toString(), ref.getReferenceType().toString(),
                    functionText(owner));
                if (owner != null && !owner.isThunk()) {
                    owners.add(owner);
                }
            }
        }
        line("PRECEDING_PLT_SYMBOL_COUNT", Integer.toString(symbolCount));
        line("PRECEDING_PLT_REF_COUNT", Integer.toString(referenceCount));
        return owners;
    }

    private void collectReferences(Address target, Set<Function> directOwners,
            Set<Address> dataReferences) {
        ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(target);
        int count = 0;
        while (refs.hasNext() && count < MAX_REFS_PER_TARGET) {
            Reference ref = refs.next();
            count++;
            Address from = ref.getFromAddress();
            Function owner = currentProgram.getFunctionManager().getFunctionContaining(from);
            line("EXACT_REF", target.toString(), from.toString(),
                ref.getReferenceType().toString(), functionText(owner));
            if (owner != null) {
                directOwners.add(owner);
            } else if (currentProgram.getMemory().contains(from)) {
                dataReferences.add(from);
            }
        }
        line("EXACT_REF_COUNT", target.toString(), Integer.toString(count));
        if (refs.hasNext()) {
            line("EXACT_REF_TRUNCATED", target.toString(), Integer.toString(MAX_REFS_PER_TARGET));
        }
    }

    private Map<Function, Integer> collectCallers(Set<Function> seeds) {
        Map<Function, Integer> depth = new LinkedHashMap<>();
        ArrayDeque<Function> queue = new ArrayDeque<>();
        for (Function seed : seeds) {
            depth.put(seed, 0);
            queue.add(seed);
        }

        while (!queue.isEmpty()) {
            Function current = queue.removeFirst();
            int currentDepth = depth.get(current);
            Set<Function> callers = new LinkedHashSet<>(
                current.getCallingFunctions(TaskMonitor.DUMMY));
            line("DIRECT_CALLER_COUNT", functionText(current), Integer.toString(callers.size()));
            dumpEntryReferences(current);
            if (currentDepth >= MAX_CALLER_DEPTH) {
                continue;
            }
            for (Function caller : callers) {
                if (depth.size() >= MAX_SELECTED_FUNCTIONS) {
                    line("CALLER_SELECTION_TRUNCATED", Integer.toString(MAX_SELECTED_FUNCTIONS));
                    return depth;
                }
                if (!depth.containsKey(caller)) {
                    depth.put(caller, currentDepth + 1);
                    queue.add(caller);
                }
            }
        }
        return depth;
    }

    private void dumpEntryReferences(Function function) {
        ReferenceIterator refs = currentProgram.getReferenceManager()
            .getReferencesTo(function.getEntryPoint());
        int count = 0;
        while (refs.hasNext() && count < MAX_REFS_PER_TARGET) {
            Reference ref = refs.next();
            count++;
            Function owner = currentProgram.getFunctionManager()
                .getFunctionContaining(ref.getFromAddress());
            line("FUNCTION_ENTRY_REF", functionText(function), ref.getFromAddress().toString(),
                ref.getReferenceType().toString(), functionText(owner));
            if (owner == null && currentProgram.getMemory().contains(ref.getFromAddress())) {
                dumpDataCell(ref.getFromAddress());
            }
        }
    }

    private void dumpDataCell(Address address) {
        line("DATA_REF_BEGIN", address.toString());
        Memory memory = currentProgram.getMemory();
        for (long delta = -0x20; delta <= 0x20; delta += 8) {
            Address cell = address.add(delta);
            try {
                long raw = memory.getLong(cell);
                Address value = toAddr(raw);
                Function function = currentProgram.getFunctionManager().getFunctionAt(value);
                Symbol symbol = currentProgram.getSymbolTable().getPrimarySymbol(value);
                line("DATA_VALUE", signedHex(delta), cell.toString(), value.toString(),
                    functionText(function), symbol == null ? "" : symbol.getName(true));
            } catch (Exception error) {
                line("DATA_VALUE", signedHex(delta), cell.toString(), "UNREADABLE");
            }
        }
        line("DATA_REF_END", address.toString());
    }

    private void exportFunction(Function function, int depth, boolean directOwner) {
        line("FUNCTION_BEGIN", functionText(function), "DEPTH_" + depth,
            directOwner ? "DIRECT_OWNER" : "CALLER");
        out.println("DISASSEMBLY_BEGIN");
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        int instructionCount = 0;
        while (instructions.hasNext() && instructionCount < 1000) {
            Instruction instruction = instructions.next();
            instructionCount++;
            line("INSTRUCTION", instruction.getAddress().toString(), instruction.toString());
        }
        if (instructions.hasNext()) {
            line("DISASSEMBLY_TRUNCATED", "1000");
        }
        out.println("DISASSEMBLY_END");
        dumpReferencedStrings(function);
        dumpResolvedLiteralStrings(function);

        DecompileResults results = decompiler.decompileFunction(function, 180, TaskMonitor.DUMMY);
        if (results.decompileCompleted()) {
            out.println("DECOMPILE_BEGIN");
            String[] lines = results.getDecompiledFunction().getC().split("\\R", -1);
            int limit = Math.min(lines.length, 1000);
            for (int i = 0; i < limit; i++) {
                out.println(clean(lines[i]));
            }
            if (limit < lines.length) {
                line("DECOMPILE_TRUNCATED", "1000");
            }
            out.println("DECOMPILE_END");
        } else {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        }
        line("FUNCTION_END", functionText(function));
    }

    private void dumpReferencedStrings(Function function) {
        Set<String> emitted = new LinkedHashSet<>();
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            for (Reference ref : currentProgram.getReferenceManager()
                    .getReferencesFrom(instruction.getAddress())) {
                Data data = currentProgram.getListing().getDefinedDataContaining(ref.getToAddress());
                if (data == null || !(data.getValue() instanceof String)) {
                    continue;
                }
                String value = (String) data.getValue();
                String key = data.getAddress() + "|" + value;
                if (emitted.add(key)) {
                    line("REFERENCED_STRING", functionText(function), instruction.getAddress().toString(),
                        data.getAddress().toString(), value);
                }
            }
        }
        line("REFERENCED_STRING_COUNT", functionText(function), Integer.toString(emitted.size()));
    }

    private void dumpResolvedLiteralStrings(Function function) {
        Map<String, Long> registers = new HashMap<>();
        Set<String> emitted = new LinkedHashSet<>();
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            String mnemonic = instruction.getMnemonicString().toUpperCase();
            String destination = registerName(firstRegister(instruction, 0));
            Long resolved = null;

            if ("ADRP".equals(mnemonic) || "ADR".equals(mnemonic)) {
                resolved = operandValue(instruction, 1);
                if (destination != null && resolved != null) {
                    registers.put(destination, resolved);
                }
            } else if ("ADD".equals(mnemonic)) {
                String source = registerName(firstRegister(instruction, 1));
                Long base = source == null ? null : registers.get(source);
                Long immediate = operandValue(instruction, 2);
                if (destination != null && base != null && immediate != null) {
                    resolved = base + immediate;
                    registers.put(destination, resolved);
                } else if (destination != null) {
                    registers.remove(destination);
                }
            } else if (destination != null && writesFirstOperand(mnemonic)) {
                registers.remove(destination);
            }

            if (resolved == null || !currentProgram.getMemory().contains(toAddr(resolved))) {
                continue;
            }
            String ascii = readBoundedAscii(toAddr(resolved), 240);
            String utf16 = readBoundedUtf16(toAddr(resolved), 240);
            if (ascii.length() >= 4) {
                String key = "A|" + resolved + "|" + ascii;
                if (emitted.add(key)) {
                    line("RESOLVED_ASCII", functionText(function), instruction.getAddress().toString(),
                        toAddr(resolved).toString(), ascii);
                }
            }
            if (utf16.length() >= 4) {
                String key = "U|" + resolved + "|" + utf16;
                if (emitted.add(key)) {
                    line("RESOLVED_UTF16", functionText(function), instruction.getAddress().toString(),
                        toAddr(resolved).toString(), utf16);
                }
            }
        }
        line("RESOLVED_LITERAL_COUNT", functionText(function), Integer.toString(emitted.size()));
    }

    private String readBoundedAscii(Address start, int maximumLength) {
        StringBuilder value = new StringBuilder();
        for (int index = 0; index < maximumLength; index++) {
            int current;
            try {
                current = currentProgram.getMemory().getByte(start.add(index)) & 0xff;
            } catch (Exception error) {
                return "";
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

    private String readBoundedUtf16(Address start, int maximumLength) {
        StringBuilder value = new StringBuilder();
        for (int index = 0; index < maximumLength; index++) {
            int current;
            try {
                current = currentProgram.getMemory().getShort(start.add(index * 2L)) & 0xffff;
            } catch (Exception error) {
                return "";
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

    private Register firstRegister(Instruction instruction, int operandIndex) {
        for (Object object : instruction.getOpObjects(operandIndex)) {
            if (object instanceof Register) {
                return (Register) object;
            }
        }
        return null;
    }

    private Long operandValue(Instruction instruction, int operandIndex) {
        Long scalar = null;
        for (Object object : instruction.getOpObjects(operandIndex)) {
            if (object instanceof Address) {
                return ((Address) object).getOffset();
            }
            if (object instanceof Scalar) {
                scalar = ((Scalar) object).getSignedValue();
            }
        }
        return scalar;
    }

    private boolean writesFirstOperand(String mnemonic) {
        return !(mnemonic.startsWith("B") || mnemonic.startsWith("STR") ||
            "CMP".equals(mnemonic) || "CMN".equals(mnemonic) || "TST".equals(mnemonic));
    }

    private String registerName(Register register) {
        return register == null ? null : register.getName().toLowerCase();
    }

    private static String functionText(Function function) {
        return function == null ? "" : function.getName(true) + "@" + function.getEntryPoint();
    }

    private static String signedHex(long value) {
        return value < 0 ? "-0x" + Long.toHexString(-value) : "+0x" + Long.toHexString(value);
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

    private static String clean(String value) {
        return value == null ? "" : value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
    }
}
