// Targeted Phase15V Puffer ownership and failure-chain export.
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
import ghidra.program.model.scalar.Scalar;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ApexPhase15VPufferExport extends GhidraScript {
    private static final String[] TERMS = {
        "cpufferinitaction::makesuregeturlfromserver",
        "makesuregeturlfromserver",
        "cpufferinitaction",
        "puffer_init_action.cpp",
        "get url from server failed",
        "connect server timeout",
        "networknotreachable",
        "pufferupdateservice",
        "resupdatecallback",
        "requpdateversion"
    };

    private static final long ERROR_714 = 54140714L;
    private static final long ERROR_715 = 54140715L;
    private static final long STAGE_COUNT = 17L;

    private DecompInterface decompiler;
    private PrintWriter out;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase15V local output directory argument is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase15V output directory");
        }

        File outputFile = new File(outputDir, "libgcloud_puffer_export.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("LANGUAGE", currentProgram.getLanguageID().toString());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());

            Map<Function, Set<String>> directOwners = new LinkedHashMap<>();
            int stringHits = scanStrings(directOwners);
            line("MATCHED_STRING_COUNT", Integer.toString(stringHits));
            line("DIRECT_OWNER_COUNT", Integer.toString(directOwners.size()));

            Set<Function> closure = new LinkedHashSet<>(directOwners.keySet());
            List<Function> seeds = new ArrayList<>(directOwners.keySet());
            for (Function seed : seeds) {
                closure.addAll(seed.getCallingFunctions(TaskMonitor.DUMMY));
                closure.addAll(seed.getCalledFunctions(TaskMonitor.DUMMY));
            }

            for (int i = 1; i < args.length; i++) {
                String rawAddress = args[i].replaceFirst("^0[xX]", "");
                Address address = toAddr(Long.parseUnsignedLong(rawAddress, 16));
                Function requested = currentProgram.getFunctionManager().getFunctionContaining(address);
                if (requested == null) {
                    line("REQUESTED_FUNCTION_MISSING", args[i]);
                } else {
                    closure.add(requested);
                    closure.addAll(requested.getCallingFunctions(TaskMonitor.DUMMY));
                    closure.addAll(requested.getCalledFunctions(TaskMonitor.DUMMY));
                }
            }

            line("CLOSURE_FUNCTION_COUNT", Integer.toString(closure.size()));
            int exported = 0;
            for (Function function : closure) {
                if (exported++ >= 220) {
                    line("FUNCTION_EXPORT_TRUNCATED", "220");
                    break;
                }
                exportFunction(function, directOwners.get(function));
            }

            println("PHASE15V_PUFFER_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private int scanStrings(Map<Function, Set<String>> owners) {
        DataIterator iterator = currentProgram.getListing().getDefinedData(true);
        int hitCount = 0;
        while (iterator.hasNext()) {
            Data data = iterator.next();
            String value = data.getValue() instanceof String
                ? (String) data.getValue()
                : data.getDefaultValueRepresentation();
            if (value == null || value.length() < 4) {
                continue;
            }
            String matched = match(value.toLowerCase(Locale.ROOT));
            if (matched == null) {
                continue;
            }

            hitCount++;
            line("STRING", data.getAddress().toString(), matched, clean(value));
            ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(data.getAddress());
            int refCount = 0;
            while (refs.hasNext() && refCount++ < 100) {
                Reference ref = refs.next();
                Function owner = currentProgram.getFunctionManager().getFunctionContaining(ref.getFromAddress());
                String ownerText = owner == null ? "" : owner.getName(true) + "@" + owner.getEntryPoint();
                line("STRING_REF", data.getAddress().toString(), ref.getFromAddress().toString(),
                    ref.getReferenceType().toString(), ownerText);
                if (owner != null) {
                    owners.computeIfAbsent(owner, ignored -> new LinkedHashSet<>()).add(matched);
                }
            }
        }
        return hitCount;
    }

    private String match(String lower) {
        for (String term : TERMS) {
            if (lower.contains(term)) {
                return term;
            }
        }
        return null;
    }

    private void exportFunction(Function function, Set<String> reasons) {
        line("FUNCTION_BEGIN", function.getName(true), function.getEntryPoint().toString(),
            "reasons=" + (reasons == null ? "NEIGHBOR" : String.join(",", reasons)));

        Set<String> entryRefs = new LinkedHashSet<>();
        ReferenceIterator entryRefIterator = currentProgram.getReferenceManager()
            .getReferencesTo(function.getEntryPoint());
        while (entryRefIterator.hasNext()) {
            Reference ref = entryRefIterator.next();
            entryRefs.add(ref.getFromAddress() + ":" + ref.getReferenceType());
        }
        line("ENTRY_REFS", String.join(",", entryRefs));

        Set<String> callers = new LinkedHashSet<>();
        for (Function caller : function.getCallingFunctions(TaskMonitor.DUMMY)) {
            callers.add(caller.getName(true) + "@" + caller.getEntryPoint());
        }
        line("CALLERS", String.join(",", callers));

        Set<String> callees = new LinkedHashSet<>();
        for (Function callee : function.getCalledFunctions(TaskMonitor.DUMMY)) {
            callees.add(callee.getName(true) + "@" + callee.getEntryPoint());
        }
        line("CALLEES", String.join(",", callees));

        InstructionIterator instructions = currentProgram.getListing().getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            for (int operand = 0; operand < instruction.getNumOperands(); operand++) {
                for (Object object : instruction.getOpObjects(operand)) {
                    if (!(object instanceof Scalar)) {
                        continue;
                    }
                    long value = ((Scalar) object).getUnsignedValue();
                    if (value == ERROR_714 || value == ERROR_715 || value == STAGE_COUNT) {
                        line("SCALAR_HIT", instruction.getAddress().toString(),
                            Long.toUnsignedString(value), instruction.toString());
                    }
                }
            }
        }

        DecompileResults results = decompiler.decompileFunction(function, 90, TaskMonitor.DUMMY);
        if (!results.decompileCompleted()) {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        } else {
            out.println("DECOMPILE_BEGIN");
            String[] lines = results.getDecompiledFunction().getC().split("\\R", -1);
            int limit = Math.min(lines.length, 650);
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

    private String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
    }
}
