// Exact-only Phase15W libUE4 Puffer callback registration export.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.listing.Data;
import ghidra.program.model.listing.DataIterator;
import ghidra.program.model.listing.Function;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ApexPhase15WLibUE4RegistrationExport extends GhidraScript {
    private static final String[] EXACT_NAMES = {
        "CreatePufferCallBack",
        "CreatePuffer"
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

        File outputFile = new File(outputDir, "libUE4_registration_export.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("LANGUAGE", currentProgram.getLanguageID().toString());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());

            Set<Function> owners = new LinkedHashSet<>();
            scanExactSymbols(owners);
            scanExactStrings(owners);
            Set<Function> selected = boundedClosure(owners);
            line("DIRECT_OWNER_COUNT", Integer.toString(owners.size()));
            line("SELECTED_FUNCTION_COUNT", Integer.toString(selected.size()));
            for (Function function : selected) {
                exportFunction(function, owners.contains(function));
            }

            println("PHASE15W_LIBUE4_REGISTRATION_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private void scanExactSymbols(Set<Function> owners) {
        SymbolIterator symbols = currentProgram.getSymbolTable().getAllSymbols(true);
        while (symbols.hasNext()) {
            Symbol symbol = symbols.next();
            if (!matchesExact(symbol.getName())) {
                continue;
            }
            line("EXACT_SYMBOL", symbol.getName(true), symbol.getAddress().toString(),
                symbol.getSymbolType().toString(), symbol.getSource().toString());
            collectReferences(symbol.getAddress(), "SYMBOL_REF", owners);
        }
    }

    private void scanExactStrings(Set<Function> owners) {
        DataIterator dataIterator = currentProgram.getListing().getDefinedData(true);
        while (dataIterator.hasNext()) {
            Data data = dataIterator.next();
            Object valueObject = data.getValue();
            if (!(valueObject instanceof String)) {
                continue;
            }
            String value = (String) valueObject;
            if (!matchesExact(value)) {
                continue;
            }
            line("EXACT_STRING", value, data.getAddress().toString());
            collectReferences(data.getAddress(), "STRING_REF", owners);
        }
    }

    private boolean matchesExact(String value) {
        for (String expected : EXACT_NAMES) {
            if (expected.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private void collectReferences(ghidra.program.model.address.Address address, String kind,
            Set<Function> owners) {
        ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(address);
        int count = 0;
        while (refs.hasNext() && count++ < 128) {
            Reference ref = refs.next();
            Function owner = currentProgram.getFunctionManager().getFunctionContaining(ref.getFromAddress());
            String ownerText = owner == null ? "" : owner.getName(true) + "@" + owner.getEntryPoint();
            line(kind, address.toString(), ref.getFromAddress().toString(),
                ref.getReferenceType().toString(), ownerText);
            if (owner != null) {
                owners.add(owner);
            }
        }
    }

    private Set<Function> boundedClosure(Set<Function> seeds) {
        Set<Function> selected = new LinkedHashSet<>(seeds);
        Map<Function, Integer> depthByFunction = new LinkedHashMap<>();
        ArrayDeque<Function> queue = new ArrayDeque<>();
        for (Function seed : seeds) {
            depthByFunction.put(seed, 0);
            queue.add(seed);
        }

        while (!queue.isEmpty()) {
            Function function = queue.removeFirst();
            int depth = depthByFunction.get(function);
            if (depth >= 2) {
                continue;
            }
            Set<Function> neighbors = new LinkedHashSet<>();
            neighbors.addAll(function.getCallingFunctions(TaskMonitor.DUMMY));
            neighbors.addAll(function.getCalledFunctions(TaskMonitor.DUMMY));
            for (Function neighbor : neighbors) {
                if (selected.size() >= 120) {
                    line("CLOSURE_TRUNCATED", "120");
                    return selected;
                }
                int nextDepth = depth + 1;
                Integer prior = depthByFunction.get(neighbor);
                selected.add(neighbor);
                if (prior == null || nextDepth < prior) {
                    depthByFunction.put(neighbor, nextDepth);
                    queue.add(neighbor);
                }
            }
        }
        return selected;
    }

    private void exportFunction(Function function, boolean directOwner) {
        line("FUNCTION_BEGIN", function.getName(true), function.getEntryPoint().toString(),
            directOwner ? "DIRECT_OWNER" : "BOUNDED_NEIGHBOR");
        DecompileResults results = decompiler.decompileFunction(function, 120, TaskMonitor.DUMMY);
        if (!results.decompileCompleted()) {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        } else {
            out.println("DECOMPILE_BEGIN");
            String[] lines = results.getDecompiledFunction().getC().split("\\R", -1);
            int limit = Math.min(lines.length, 600);
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
        if (value == null) {
            return "";
        }
        return value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
    }
}
