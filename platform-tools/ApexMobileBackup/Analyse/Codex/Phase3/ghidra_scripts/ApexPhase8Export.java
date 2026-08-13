// Targeted Phase8 virtual-file and Lua-path export for Apex Mobile libUE4.so.
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
import ghidra.program.model.lang.Register;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.scalar.Scalar;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ApexPhase8Export extends GhidraScript {
    private static final long IMAGE_BASE = 0x100000L;
    private static final long[] VTABLES = {
        0xaf710e0L, // fallback platform-file object
        0xaf712c8L, // registered-file handle wrapper
        0xaf721e8L  // Lua loader path/file facade
    };
    private static final long[] FUNCTIONS = {
        0x49a8b54L, // Lua file loader
        0x49a9694L, // fallback full-file read
        0x46355e8L, // platform-file singleton
        0x49825b8L, // backend OpenRead thunk
        0x49825bcL, // backend OpenRead body
        0x48415b8L, // facade OpenRead
        0x82693bcL, // facade path resolver
        0x46095e4L, // path normalization/splitting
        0x488b6c8L, // normalized-path predicate
        0x4609488L, // registered path lookup
        0x4166f64L, // path composition helper
        0x4165f24L, // string/path composition helper
        0x45a4384L, // runtime prefix source
        0x6be427cL, // dynamic Lua dispatch bridge
        0x6be3f4cL, // event emitter
        0x49829e0L, // physical-file handle constructor
        0x825240cL, // Android-asset handle constructor
        0x82598dcL, // runtime path-prefix setter
        0x583fc18L  // optional-provider reset/teardown
    };
    private static final long[] GLOBALS = {
        0xb697528L, // optional Lua byte provider
        0xb697530L, // optional provider state
        0xb7c7020L, // runtime path-prefix storage
        0xb7c7028L, // runtime path-prefix length/state
        0xb7cf9b0L  // fallback platform-file singleton
    };
    private static final long[] DATA_POINTS = {
        0x25a793eL, 0x2825f86L, 0x26924c8L, 0x2833f32L,
        0x2633dc8L, 0x24d4f50L, 0x2725814L, 0x28752a4L, 0x22964fdL
    };

    private Memory memory;
    private DecompInterface decompiler;
    private PrintWriter out;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase8 output directory argument is required");
        }
        if (currentProgram.getImageBase().getOffset() != IMAGE_BASE) {
            throw new IllegalStateException("Unexpected image base");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase8 output directory");
        }
        memory = currentProgram.getMemory();
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);
        out = new PrintWriter(new File(outputDir, "phase8_ghidra_evidence.txt"), StandardCharsets.UTF_8.name());

        try {
            line("META", "generated", LocalDate.now().toString());
            line("META", "program", currentProgram.getName());
            line("META", "image_base", hex(IMAGE_BASE));
            for (long vtable : VTABLES) {
                exportVtable(vtable);
            }
            for (long global : GLOBALS) {
                exportReferences("GLOBAL", global);
            }
            if (args.length < 2 || !"quick".equalsIgnoreCase(args[1])) {
                scanCodeReferences();
            }
            for (long point : DATA_POINTS) {
                exportDataPoint(point);
            }
            for (long entry : FUNCTIONS) {
                exportFunction(entry);
            }
            println("PHASE8_TARGETED_EXPORT_OK");
            println(outputDir.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private void exportVtable(long center) {
        line("VTABLE", hex(center), "elf=" + hex(center - IMAGE_BASE), "block=" + block(center));
        exportReferences("VTABLE_REF", center);
        for (long slot = center - 0x20; slot <= center + 0x200; slot += 8) {
            try {
                long value = memory.getLong(addr(slot));
                Function exact = currentProgram.getFunctionManager().getFunctionAt(addr(value));
                Function containing = currentProgram.getFunctionManager().getFunctionContaining(addr(value));
                String function = exact != null ? exact.getName()
                    : containing != null ? containing.getName() + "+" + hex(value - containing.getEntryPoint().getOffset()) : "";
                line("VTABLE_SLOT", hex(center), signed(slot - center), hex(slot), hex(value), classify(value), function);
            } catch (Exception e) {
                line("VTABLE_SLOT", hex(center), signed(slot - center), hex(slot), "UNREADABLE");
            }
        }
    }

    private void exportFunction(long entry) {
        Function function = currentProgram.getFunctionManager().getFunctionAt(addr(entry));
        if (function == null) {
            line("FUNCTION_MISSING", hex(entry));
            return;
        }
        line("FUNCTION", function.getName(), hex(entry), "elf=" + hex(entry - IMAGE_BASE),
            "size=" + function.getBody().getNumAddresses());
        exportReferences("FUNCTION_REF", entry);

        Set<String> callers = new LinkedHashSet<>();
        for (Function caller : function.getCallingFunctions(TaskMonitor.DUMMY)) {
            callers.add(caller.getName() + "@" + hex(caller.getEntryPoint().getOffset()));
        }
        line("CALLERS", hex(entry), String.join(",", callers));

        Set<String> callees = new LinkedHashSet<>();
        for (Function callee : function.getCalledFunctions(TaskMonitor.DUMMY)) {
            callees.add(callee.getName() + "@" + hex(callee.getEntryPoint().getOffset()));
        }
        line("CALLEES", hex(entry), String.join(",", callees));
        exportReferencedData(function);

        DecompileResults result = decompiler.decompileFunction(function, 60, TaskMonitor.DUMMY);
        if (!result.decompileCompleted()) {
            line("DECOMPILE_ERROR", hex(entry), clean(result.getErrorMessage()));
            return;
        }
        line("DECOMPILE_BEGIN", hex(entry));
        String[] lines = result.getDecompiledFunction().getC().split("\\R", -1);
        int limit = Math.min(lines.length, 900);
        for (int i = 0; i < limit; i++) {
            out.println(clean(lines[i]));
        }
        if (limit < lines.length) {
            out.println("/* TRUNCATED */");
        }
        line("DECOMPILE_END", hex(entry));
    }

    private void exportReferencedData(Function function) {
        InstructionIterator instructions = currentProgram.getListing().getInstructions(function.getBody(), true);
        Set<String> emitted = new LinkedHashSet<>();
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            for (Reference reference : instruction.getReferencesFrom()) {
                Address target = reference.getToAddress();
                MemoryBlock targetBlock = memory.getBlock(target);
                if (targetBlock == null || targetBlock.isExecute()) {
                    continue;
                }
                String ascii = readAscii(target, 160);
                String utf16 = readUtf16(target, 160);
                if (ascii == null && utf16 == null) {
                    continue;
                }
                String row = hex(instruction.getAddress().getOffset()) + "|" + hex(target.getOffset()) + "|"
                    + clean(ascii) + "|" + clean(utf16);
                if (emitted.add(row)) {
                    line("DATA_REF", hex(function.getEntryPoint().getOffset()), row);
                }
            }
        }
    }

    private void exportReferences(String kind, long target) {
        ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(addr(target));
        int count = 0;
        while (refs.hasNext() && count++ < 500) {
            Reference ref = refs.next();
            Function owner = currentProgram.getFunctionManager().getFunctionContaining(ref.getFromAddress());
            line(kind, hex(target), hex(ref.getFromAddress().getOffset()), ref.getReferenceType().toString(),
                owner == null ? "" : owner.getName() + "@" + hex(owner.getEntryPoint().getOffset()));
        }
    }

    private void exportDataPoint(long point) {
        StringBuilder bytes = new StringBuilder();
        StringBuilder units = new StringBuilder();
        try {
            for (int i = 0; i < 32; i++) {
                if (i > 0) bytes.append(' ');
                bytes.append(String.format("%02x", memory.getByte(addr(point + i)) & 0xff));
            }
            for (int i = 0; i < 16; i++) {
                if (i > 0) units.append(' ');
                units.append(String.format("%04x", memory.getShort(addr(point + i * 2L)) & 0xffff));
            }
        } catch (Exception e) {
            line("DATA_POINT_ERROR", hex(point), e.toString());
            return;
        }
        line("DATA_POINT", hex(point), "bytes=" + bytes, "utf16_units=" + units,
            "ascii=" + clean(readAscii(addr(point), 160)), "utf16=" + clean(readUtf16(addr(point), 160)));
    }

    private void scanCodeReferences() {
        Set<Long> targets = new LinkedHashSet<>();
        for (long value : VTABLES) targets.add(value);
        for (long value : GLOBALS) targets.add(value);

        FunctionIterator functions = currentProgram.getFunctionManager().getFunctions(true);
        while (functions.hasNext()) {
            Function function = functions.next();
            Map<String, Long> registers = new HashMap<>();
            InstructionIterator instructions = currentProgram.getListing().getInstructions(function.getBody(), true);
            while (instructions.hasNext()) {
                Instruction instruction = instructions.next();
                String mnemonic = instruction.getMnemonicString().toUpperCase();
                String destination = registerName(firstRegister(instruction, 0));
                Long resolved = null;

                if ("ADRP".equals(mnemonic) || "ADR".equals(mnemonic)) {
                    resolved = operandValue(instruction, 1);
                    if (destination != null && resolved != null) registers.put(destination, resolved);
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
                } else if (mnemonic.startsWith("LDR") || mnemonic.startsWith("STR")) {
                    String baseName = registerName(firstRegister(instruction, 1));
                    Long base = baseName == null ? null : registers.get(baseName);
                    Long displacement = operandValue(instruction, 1);
                    if (base != null) resolved = base + (displacement == null ? 0L : displacement);

                    String valueRegister = registerName(firstRegister(instruction, 0));
                    Long value = valueRegister == null ? null : registers.get(valueRegister);
                    if (value != null && targets.contains(value)) {
                        line("CODE_VALUE_REF", hex(value), hex(instruction.getAddress().getOffset()),
                            instruction.toString(), function.getName() + "@" + hex(function.getEntryPoint().getOffset()));
                    }
                    if (mnemonic.startsWith("LDR") && destination != null) registers.remove(destination);
                } else if (destination != null && writesFirstOperand(mnemonic)) {
                    registers.remove(destination);
                }

                if (resolved != null && targets.contains(resolved)) {
                    line("CODE_ADDRESS_REF", hex(resolved), hex(instruction.getAddress().getOffset()),
                        instruction.toString(), function.getName() + "@" + hex(function.getEntryPoint().getOffset()));
                }
            }
        }
    }

    private Register firstRegister(Instruction instruction, int operandIndex) {
        for (Object value : instruction.getOpObjects(operandIndex)) {
            if (value instanceof Register) return (Register)value;
        }
        return null;
    }

    private Long operandValue(Instruction instruction, int operandIndex) {
        Long scalar = null;
        for (Object value : instruction.getOpObjects(operandIndex)) {
            if (value instanceof Address) return ((Address)value).getOffset();
            if (value instanceof Scalar) scalar = ((Scalar)value).getSignedValue();
        }
        return scalar;
    }

    private boolean writesFirstOperand(String mnemonic) {
        return !(mnemonic.startsWith("B") || mnemonic.startsWith("STR") || "CMP".equals(mnemonic)
            || "CMN".equals(mnemonic) || "TST".equals(mnemonic));
    }

    private String registerName(Register register) {
        return register == null ? null : register.getName().toLowerCase();
    }

    private String classify(long value) {
        if (value == 0) return "NULL";
        MemoryBlock target = memory.getBlock(addr(value));
        if (target == null) return value < IMAGE_BASE ? "INTEGER_OR_OFFSET" : "UNMAPPED";
        if (target.isExecute()) return "EXECUTABLE_POINTER";
        return "POINTER_" + target.getName();
    }

    private String readAscii(Address start, int max) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < max; i++) {
            try {
                int value = memory.getByte(start.add(i)) & 0xff;
                if (value == 0) break;
                if (value < 0x20 || value > 0x7e) return result.length() >= 4 ? result.toString() : null;
                result.append((char)value);
            } catch (Exception e) {
                return null;
            }
        }
        return result.length() >= 4 ? result.toString() : null;
    }

    private String readUtf16(Address start, int maxUnits) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxUnits; i++) {
            try {
                int value = memory.getShort(start.add(i * 2L)) & 0xffff;
                if (value == 0) break;
                if (value < 0x20 || value > 0x7e) return result.length() >= 4 ? result.toString() : null;
                result.append((char)value);
            } catch (Exception e) {
                return null;
            }
        }
        return result.length() >= 4 ? result.toString() : null;
    }

    private String block(long value) {
        MemoryBlock target = memory.getBlock(addr(value));
        return target == null ? "" : target.getName();
    }

    private Address addr(long value) {
        return currentProgram.getAddressFactory().getDefaultAddressSpace().getAddress(value);
    }

    private String hex(long value) {
        return "0x" + Long.toHexString(value);
    }

    private String signed(long value) {
        return value < 0 ? "-0x" + Long.toHexString(-value) : "+0x" + Long.toHexString(value);
    }

    private String clean(String value) {
        if (value == null) return "";
        return value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ')
            .replaceAll("(?i)C:\\\\Users\\\\[A-Za-z0-9._-]+", "C:\\\\Users\\\\<REDACTED>")
            .replaceAll("(?i)(token|ticket|session|cookie)[A-Za-z0-9_./=+:-]{8,}", "$1<REDACTED>");
    }

    private void line(String... fields) {
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) out.print('\t');
            out.print(clean(fields[i]));
        }
        out.println();
    }
}
