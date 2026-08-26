// Exact Phase15W CreatePuffer factory export with direct references only.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.address.Address;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

public class ApexPhase15WCreatePufferExport extends GhidraScript {
    private static final long[] PRODUCT_VTABLES = {0x009784c0L, 0x009785f8L};
    private static final long[] EVIDENCE_FUNCTIONS = {
        0x004ee1dcL, 0x004ee318L, 0x004fcaf4L, 0x004f02dcL,
        0x004f42d4L, 0x004f3a14L, 0x004f4418L
    };
    private DecompInterface decompiler;
    private PrintWriter out;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length != 1) {
            throw new IllegalArgumentException("Phase15W output directory argument is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase15W output directory");
        }

        File outputFile = new File(outputDir, "libgcloud_create_puffer_export.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());
            Set<Function> selected = new LinkedHashSet<>();
            SymbolIterator symbols = currentProgram.getSymbolTable().getAllSymbols(true);
            while (symbols.hasNext()) {
                Symbol symbol = symbols.next();
                if (!"CreatePuffer".equals(symbol.getName())) {
                    continue;
                }
                line("EXACT_SYMBOL", symbol.getName(true), symbol.getAddress().toString(),
                    symbol.getSymbolType().toString(), symbol.getSource().toString());
                Function target = currentProgram.getFunctionManager().getFunctionAt(symbol.getAddress());
                if (target != null) {
                    selected.add(target);
                    selected.addAll(target.getCalledFunctions(TaskMonitor.DUMMY));
                }
                ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(symbol.getAddress());
                while (refs.hasNext()) {
                    Reference ref = refs.next();
                    Function owner = currentProgram.getFunctionManager()
                        .getFunctionContaining(ref.getFromAddress());
                    line("DIRECT_REF", ref.getFromAddress().toString(),
                        ref.getReferenceType().toString(), functionText(owner));
                    if (owner != null) {
                        selected.add(owner);
                    }
                }
            }

            for (long raw : PRODUCT_VTABLES) {
                dumpVtable(raw, selected);
            }
            for (long raw : EVIDENCE_FUNCTIONS) {
                Function function = currentProgram.getFunctionManager().getFunctionAt(toAddr(raw));
                if (function != null) {
                    selected.add(function);
                }
            }

            line("SELECTED_FUNCTION_COUNT", Integer.toString(selected.size()));
            for (Function function : selected) {
                exportFunction(function);
            }
            println("PHASE15W_CREATE_PUFFER_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private void dumpVtable(long raw, Set<Function> selected) {
        Address addressPoint = toAddr(raw);
        line("VTABLE_BEGIN", addressPoint.toString());
        for (long offset = -0x10; offset <= 0x30; offset += 8) {
            Address slot = addressPoint.add(offset);
            try {
                Address target = toAddr(currentProgram.getMemory().getLong(slot));
                Function function = currentProgram.getFunctionManager().getFunctionAt(target);
                line("VTABLE_SLOT", signedHex(offset), slot.toString(), target.toString(),
                    functionText(function));
                if (offset >= 0 && function != null) {
                    selected.add(function);
                    if (offset == 0x10 || offset == 0x20) {
                        selected.addAll(function.getCalledFunctions(TaskMonitor.DUMMY));
                    }
                }
            } catch (Exception error) {
                line("VTABLE_SLOT", signedHex(offset), slot.toString(), "UNREADABLE",
                    clean(error.getMessage()));
            }
        }
        try {
            Address typeInfo = toAddr(currentProgram.getMemory().getLong(addressPoint.subtract(8)));
            Address typeName = toAddr(currentProgram.getMemory().getLong(typeInfo.add(8)));
            line("RTTI", typeInfo.toString(), typeName.toString(), readAscii(typeName, 160));
            int baseCount = currentProgram.getMemory().getInt(typeInfo.add(0x14));
            line("RTTI_BASE_COUNT", Integer.toString(baseCount));
            if (baseCount >= 0 && baseCount <= 8) {
                for (int index = 0; index < baseCount; index++) {
                    Address descriptor = typeInfo.add(0x18L + index * 0x10L);
                    Address baseTypeInfo = toAddr(currentProgram.getMemory().getLong(descriptor));
                    Address baseName = toAddr(currentProgram.getMemory().getLong(baseTypeInfo.add(8)));
                    long offsetFlags = currentProgram.getMemory().getLong(descriptor.add(8));
                    line("RTTI_BASE", Integer.toString(index), baseTypeInfo.toString(),
                        baseName.toString(), readAscii(baseName, 160), "0x" + Long.toHexString(offsetFlags));
                }
            }
        } catch (Exception error) {
            line("RTTI", "UNREADABLE", clean(error.getMessage()));
        }
        line("VTABLE_END", addressPoint.toString());
    }

    private String readAscii(Address address, int limit) throws Exception {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            int current = currentProgram.getMemory().getByte(address.add(i)) & 0xff;
            if (current == 0) {
                break;
            }
            if (current < 0x20 || current > 0x7e) {
                return value + "<non-ascii>";
            }
            value.append((char) current);
        }
        return value.toString();
    }

    private void exportFunction(Function function) {
        line("FUNCTION_BEGIN", function.getName(true), function.getEntryPoint().toString());
        out.println("DISASSEMBLY_BEGIN");
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        int count = 0;
        while (instructions.hasNext() && count++ < 500) {
            Instruction instruction = instructions.next();
            line("INSTRUCTION", instruction.getAddress().toString(), instruction.toString());
        }
        out.println("DISASSEMBLY_END");

        DecompileResults results = decompiler.decompileFunction(function, 120, TaskMonitor.DUMMY);
        if (results.decompileCompleted()) {
            out.println("DECOMPILE_BEGIN");
            out.println(results.getDecompiledFunction().getC());
            out.println("DECOMPILE_END");
        } else {
            line("DECOMPILE_ERROR", results.getErrorMessage());
        }
        line("FUNCTION_END", function.getName(true), function.getEntryPoint().toString());
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
