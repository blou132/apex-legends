// Bounded Phase15X CreatePuffer return-value and direct-caller export.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ApexPhase15XObjectFlowExport extends GhidraScript {
    private static final long WRAPPER = 0x080d1ac8L;
    private static final long[] WRAPPER_DATA_REFS = {0x02aa1af0L, 0x0347c4e8L};
    private static final int MAX_DEPTH = 2;
    private static final int MAX_FUNCTIONS = 80;

    private DecompInterface decompiler;
    private PrintWriter out;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length != 1) {
            throw new IllegalArgumentException("Phase15X output directory argument is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase15X output directory");
        }
        File outputFile = new File(outputDir, "libUE4_createpuffer_object_flow.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            Function seed = currentProgram.getFunctionManager().getFunctionAt(toAddr(WRAPPER));
            if (seed == null) {
                throw new IllegalStateException("FUN_080d1ac8 is missing");
            }

            line("PROGRAM", currentProgram.getName());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());
            dumpReferences(seed.getEntryPoint());
            for (long raw : WRAPPER_DATA_REFS) {
                dumpDataCell(toAddr(raw));
            }

            Map<Function, Integer> depth = collectCallers(seed);
            line("SELECTED_FUNCTION_COUNT", Integer.toString(depth.size()));
            for (Map.Entry<Function, Integer> entry : depth.entrySet()) {
                exportFunction(entry.getKey(), entry.getValue());
            }

            println("PHASE15X_OBJECT_FLOW_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private void dumpDataCell(Address address) {
        line("DATA_CELL_BEGIN", address.toString(), symbolText(address));
        for (long offset = -0x20; offset <= 0x20; offset += 8) {
            Address cell = address.add(offset);
            try {
                Address value = toAddr(currentProgram.getMemory().getLong(cell));
                Function function = currentProgram.getFunctionManager().getFunctionAt(value);
                line("DATA_CELL_VALUE", signedHex(offset), cell.toString(), value.toString(),
                    functionText(function), symbolText(value));
            } catch (Exception error) {
                line("DATA_CELL_VALUE", signedHex(offset), cell.toString(), "UNREADABLE",
                    clean(error.getMessage()));
            }
        }

        ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(address);
        int count = 0;
        while (refs.hasNext() && count++ < 64) {
            Reference ref = refs.next();
            Function owner = currentProgram.getFunctionManager()
                .getFunctionContaining(ref.getFromAddress());
            line("DATA_CELL_REF", ref.getFromAddress().toString(),
                ref.getReferenceType().toString(), functionText(owner));
        }
        line("DATA_CELL_REF_COUNT", Integer.toString(count));
        line("DATA_CELL_END", address.toString());
    }

    private Map<Function, Integer> collectCallers(Function seed) {
        Map<Function, Integer> depth = new LinkedHashMap<>();
        ArrayDeque<Function> queue = new ArrayDeque<>();
        depth.put(seed, 0);
        queue.add(seed);

        while (!queue.isEmpty()) {
            Function current = queue.removeFirst();
            int currentDepth = depth.get(current);
            if (currentDepth >= MAX_DEPTH) {
                continue;
            }
            Set<Function> callers = new LinkedHashSet<>(
                current.getCallingFunctions(TaskMonitor.DUMMY));
            line("DIRECT_CALLER_COUNT", current.getName(true),
                current.getEntryPoint().toString(), Integer.toString(callers.size()));
            for (Function caller : callers) {
                if (depth.size() >= MAX_FUNCTIONS) {
                    line("CALLER_SELECTION_TRUNCATED", Integer.toString(MAX_FUNCTIONS));
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

    private void dumpReferences(Address target) {
        ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(target);
        int count = 0;
        while (refs.hasNext() && count++ < 128) {
            Reference ref = refs.next();
            Function owner = currentProgram.getFunctionManager()
                .getFunctionContaining(ref.getFromAddress());
            line("WRAPPER_REF", ref.getFromAddress().toString(),
                ref.getReferenceType().toString(), functionText(owner));
        }
    }

    private void exportFunction(Function function, int depth) {
        line("FUNCTION_BEGIN", function.getName(true), function.getEntryPoint().toString(),
            "DEPTH_" + depth);
        out.println("DISASSEMBLY_BEGIN");
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        int instructionCount = 0;
        while (instructions.hasNext() && instructionCount++ < 1200) {
            Instruction instruction = instructions.next();
            line("INSTRUCTION", instruction.getAddress().toString(), instruction.toString());
        }
        if (instructions.hasNext()) {
            line("DISASSEMBLY_TRUNCATED", "1200");
        }
        out.println("DISASSEMBLY_END");

        DecompileResults results = decompiler.decompileFunction(function, 180, TaskMonitor.DUMMY);
        if (results.decompileCompleted()) {
            out.println("DECOMPILE_BEGIN");
            String[] lines = results.getDecompiledFunction().getC().split("\\R", -1);
            int limit = Math.min(lines.length, 1200);
            for (int i = 0; i < limit; i++) {
                out.println(clean(lines[i]));
            }
            if (limit < lines.length) {
                line("DECOMPILE_TRUNCATED", "1200");
            }
            out.println("DECOMPILE_END");
        } else {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        }
        line("FUNCTION_END", function.getName(true), function.getEntryPoint().toString());
    }

    private static String functionText(Function function) {
        return function == null ? "" : function.getName(true) + "@" + function.getEntryPoint();
    }

    private String symbolText(Address address) {
        ghidra.program.model.symbol.Symbol symbol = currentProgram.getSymbolTable()
            .getPrimarySymbol(address);
        return symbol == null ? "" : symbol.getName(true);
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
