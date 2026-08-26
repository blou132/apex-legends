// Exact runtime-anchor follow-up for Phase15Y.
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
import ghidra.program.model.symbol.Symbol;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ApexPhase15YExactAnchorExport extends GhidraScript {
    private static final String[] EXACT_TERMS = {
        "GemReportHelper",
        "UpdateResult",
        "version_mgr_imp.cpp",
        "GcloudDolphinVersionAction.cpp",
        "ProcessActionError",
        "action_mgr.cpp"
    };

    private static final long EXACT_RUNTIME_CODE = 154140714L;
    private PrintWriter out;
    private DecompInterface decompiler;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase15Y local output directory argument is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase15Y output directory");
        }

        File outputFile = new File(
            outputDir,
            currentProgram.getName().replaceAll("[^A-Za-z0-9._-]", "_") + "_exact_anchors.txt"
        );
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());

            Map<Function, Set<String>> owners = new LinkedHashMap<>();
            scanExactStrings(owners);
            line("DIRECT_OWNER_COUNT", Integer.toString(owners.size()));

            Map<Function, Set<String>> selected = new LinkedHashMap<>(owners);
            for (Map.Entry<Function, Set<String>> entry : owners.entrySet()) {
                if (!entry.getValue().contains("UpdateResult")) {
                    continue;
                }
                Function anchor = entry.getKey();
                ReferenceIterator references = currentProgram.getReferenceManager()
                    .getReferencesTo(anchor.getEntryPoint());
                while (references.hasNext()) {
                    Reference reference = references.next();
                    Function referenceOwner = currentProgram.getFunctionManager()
                        .getFunctionContaining(reference.getFromAddress());
                    if (referenceOwner != null) {
                        selected.computeIfAbsent(referenceOwner, ignored -> new LinkedHashSet<>())
                            .add("DIRECT_REF_TO_" + anchor.getName());
                    }
                }
                for (Function callee : anchor.getCalledFunctions(TaskMonitor.DUMMY)) {
                    selected.computeIfAbsent(callee, ignored -> new LinkedHashSet<>())
                        .add("DIRECT_CALLEE_OF_" + anchor.getName());
                }
            }

            line("BOUNDED_FUNCTION_COUNT", Integer.toString(selected.size()));
            for (Map.Entry<Function, Set<String>> entry : selected.entrySet()) {
                exportFunction(entry.getKey(), entry.getValue());
            }

            println("PHASE15Y_EXACT_ANCHOR_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private void scanExactStrings(Map<Function, Set<String>> owners) {
        DataIterator iterator = currentProgram.getListing().getDefinedData(true);
        int hitCount = 0;
        while (iterator.hasNext()) {
            Data data = iterator.next();
            String value = data.getValue() instanceof String
                ? (String) data.getValue()
                : data.getDefaultValueRepresentation();
            if (value == null) {
                continue;
            }

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
                        owner == null ? "" : owner.getName(true) + "@" + owner.getEntryPoint()
                    );
                    if (owner != null) {
                        owners.computeIfAbsent(owner, ignored -> new LinkedHashSet<>()).add(term);
                    }
                }
            }
        }
        line("EXACT_STRING_HIT_COUNT", Integer.toString(hitCount));
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
                owner == null ? "" : owner.getName(true) + "@" + owner.getEntryPoint()
            );
            if (reference.getReferenceType().isData() ||
                reference.getReferenceType().isIndirect()) {
                dumpPointerWindow(reference.getFromAddress());
            }
        }

        Set<String> callers = new LinkedHashSet<>();
        for (Function caller : function.getCallingFunctions(TaskMonitor.DUMMY)) {
            callers.add(caller.getName(true) + "@" + caller.getEntryPoint());
        }
        line("DIRECT_CALLERS", String.join(",", callers));

        Set<String> callees = new LinkedHashSet<>();
        for (Function callee : function.getCalledFunctions(TaskMonitor.DUMMY)) {
            callees.add(callee.getName(true) + "@" + callee.getEntryPoint());
        }
        line("DIRECT_CALLEES", String.join(",", callees));

        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            for (int operand = 0; operand < instruction.getNumOperands(); operand++) {
                for (Object object : instruction.getOpObjects(operand)) {
                    if (object instanceof Scalar &&
                        ((Scalar) object).getUnsignedValue() == EXACT_RUNTIME_CODE) {
                        line("EXACT_SCALAR", instruction.getAddress().toString(), instruction.toString());
                    }
                }
            }
        }

        DecompileResults results = decompiler.decompileFunction(function, 90, TaskMonitor.DUMMY);
        if (results.decompileCompleted()) {
            out.println("DECOMPILE_BEGIN");
            out.println(clean(results.getDecompiledFunction().getC()));
            out.println("DECOMPILE_END");
        } else {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        }
        line("FUNCTION_END", function.getName(true), function.getEntryPoint().toString());
    }

    private void dumpPointerWindow(Address center) {
        line("POINTER_WINDOW_BEGIN", center.toString());
        for (long offset = -0x20; offset <= 0x30; offset += 8) {
            Address slot = center.add(offset);
            try {
                Address target = toAddr(currentProgram.getMemory().getLong(slot));
                Function function = currentProgram.getFunctionManager().getFunctionAt(target);
                Symbol symbol = currentProgram.getSymbolTable().getPrimarySymbol(target);
                String targetName = function != null
                    ? function.getName(true) + "@" + function.getEntryPoint()
                    : symbol == null ? "" : symbol.getName(true);
                line(
                    "POINTER_WINDOW_ENTRY",
                    slot.toString(),
                    target.toString(),
                    targetName
                );
            } catch (Exception error) {
                line("POINTER_WINDOW_ENTRY", slot.toString(), "UNREADABLE", clean(error.getMessage()));
            }
        }
        line("POINTER_WINDOW_END", center.toString());
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
