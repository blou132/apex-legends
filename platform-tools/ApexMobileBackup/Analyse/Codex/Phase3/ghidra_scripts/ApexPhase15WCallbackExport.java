// Targeted Phase15W callback-object and vtable provenance export.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Data;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.scalar.Scalar;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.program.model.symbol.Symbol;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ApexPhase15WCallbackExport extends GhidraScript {
    private static final long[] SEEDS = {
        0x004ee1dcL, // CPufferInitActionResult::ProcessResult
        0x004ee318L, // CPufferInitActionResult constructor
        0x004fcaf4L, // failure result construction/submission
        0x00501d0cL  // CPufferInitAction::run
    };

    private static final long[] TARGET_FUNCTIONS = {
        0x004ee1dcL,
        0x004ee318L,
        0x004fcaf4L,
        0x00501d0cL,
        0x00501be8L, // CPufferInitAction::StartAction
        0x004fca7cL, // success result construction/submission
        0x00500854L, // action destructor/vtable reset witness
        0x00500704L, // CPufferInitAction constructor
        0x004f4c5cL, // direct constructor caller
        0x005e0b90L, // direct constructor caller
        0x004ee464L, // exact CreatePufferCallBack factory
        0x004f3918L, // callback base constructor
        0x004f3950L, // callback concrete constructor
        0x004f3824L, // CPufferPluginCallBack slot +0x10
        0x004f3894L, // CPufferPluginCallBack slot +0x20
        0x004f535cL, // manager slot +0x10
        0x004f4df0L, // manager slot +0x18
        0x004f4ef0L, // manager slot +0x20
        0x004f4cc4L, // manager method adjacent to action creation
        0x004f4d28L, // manager method adjacent to action creation
        0x004f4d90L, // manager method adjacent to action creation
        0x004f63ecL, // manager constructor
        0x004f02dcL, // direct manager-constructor caller
        0x004ef0f4L, // CPufferMgrImp slot +0x18
        0x004f0258L, // CPufferMgrImp slot +0x20
        0x004f4418L, // scheduler processing reached by CPufferMgrImp +0x20
        0x004f42d4L, // action scheduler constructor from manager Init
        0x004f3d64L, // CPufferActionCallBackImp slot +0x10
        0x004f3990L, // CPufferActionCallBackImp slot +0x18
        0x004f3a14L, // CPufferActionCallBackImp slot +0x20
        0x004f3ab4L, // adjacent scheduler result-queue method
        0x004f3bccL, // adjacent scheduler callback enqueue method
        0x004f3c8cL  // scheduler process-loop candidate
    };

    private static final long[] VTABLES = {
        0x00977c70L, // CPufferInitActionResult address point
        0x00977e80L, // CPufferMgrImp address point
        0x00978180L, // CreatePufferCallBack product address point
        0x009781f0L, // action scheduler address point from manager Init
        0x00978270L, // manager address point containing action creation
        0x00978440L, // CPufferInitAction primary address point
        0x00978490L  // CPufferInitAction secondary address point
    };

    private static final long[] ERROR_CODES = {
        0x0430002eL,
        0x0430002fL,
        0x04300030L,
        0x04300031L,
        0x04300032L
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

        File outputFile = new File(outputDir, "libgcloud_callback_export.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("LANGUAGE", currentProgram.getLanguageID().toString());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());

            for (long address : VTABLES) {
                dumpVtable(address);
            }
            dumpPointerRange(0x00977e40L, 0x00977ed0L);
            dumpPointerRange(0x00978200L, 0x00978390L);

            Set<Function> selected = buildBoundedSelection();
            line("SELECTED_FUNCTION_COUNT", Integer.toString(selected.size()));
            for (Function function : selected) {
                exportFunction(function);
            }

            println("PHASE15W_CALLBACK_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private Set<Function> buildBoundedSelection() {
        Set<Function> selected = new LinkedHashSet<>();
        Map<Function, Integer> queuedDepth = new LinkedHashMap<>();
        ArrayDeque<Function> queue = new ArrayDeque<>();

        for (long raw : SEEDS) {
            Function function = functionAt(raw);
            if (function != null) {
                selected.add(function);
                queuedDepth.put(function, 0);
                queue.add(function);
            }
        }

        // Keep the callgraph expansion bounded to two levels from the exact seeds.
        while (!queue.isEmpty()) {
            Function function = queue.removeFirst();
            int depth = queuedDepth.get(function);
            if (depth >= 2) {
                continue;
            }
            Set<Function> neighbors = new LinkedHashSet<>();
            neighbors.addAll(function.getCallingFunctions(TaskMonitor.DUMMY));
            neighbors.addAll(function.getCalledFunctions(TaskMonitor.DUMMY));
            for (Function neighbor : neighbors) {
                if (selected.size() >= 160) {
                    line("CALLGRAPH_SELECTION_TRUNCATED", "160");
                    queue.clear();
                    break;
                }
                int nextDepth = depth + 1;
                Integer priorDepth = queuedDepth.get(neighbor);
                selected.add(neighbor);
                if (priorDepth == null || nextDepth < priorDepth) {
                    queuedDepth.put(neighbor, nextDepth);
                    queue.add(neighbor);
                }
            }
        }

        for (long raw : TARGET_FUNCTIONS) {
            Function function = functionAt(raw);
            if (function != null) {
                selected.add(function);
            }
        }

        // Add only functions directly referencing the three exact address points.
        for (long raw : VTABLES) {
            ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(toAddr(raw));
            int count = 0;
            while (refs.hasNext() && count++ < 64) {
                Reference ref = refs.next();
                Function owner = currentProgram.getFunctionManager().getFunctionContaining(ref.getFromAddress());
                if (owner != null) {
                    selected.add(owner);
                }
            }
        }
        return selected;
    }

    private void dumpVtable(long rawAddress) {
        Address addressPoint = toAddr(rawAddress);
        line("VTABLE_BEGIN", addressPoint.toString(), symbolText(addressPoint));
        for (long offset = -0x10; offset <= 0x20; offset += 8) {
            Address slot = addressPoint.add(offset);
            String valueText = "UNREADABLE";
            String targetText = "";
            try {
                long value = currentProgram.getMemory().getLong(slot);
                Address target = toAddr(value);
                valueText = target.toString();
                Function function = currentProgram.getFunctionManager().getFunctionAt(target);
                Data data = currentProgram.getListing().getDataAt(target);
                targetText = function != null
                    ? function.getName(true) + "@" + function.getEntryPoint()
                    : symbolText(target) + (data == null ? "" : ":" + clean(data.getDefaultValueRepresentation()));
                dumpPointerTarget(slot, target);
            } catch (Exception error) {
                targetText = clean(error.getMessage());
            }
            line("VTABLE_SLOT", addressPoint.toString(), signedHex(offset), slot.toString(), valueText, targetText);
        }
        dumpReferences(addressPoint, "VTABLE_REF");
        line("VTABLE_END", addressPoint.toString());
    }

    private void dumpPointerRange(long startRaw, long endRaw) {
        line("POINTER_RANGE_BEGIN", toAddr(startRaw).toString(), toAddr(endRaw).toString());
        for (long raw = startRaw; raw <= endRaw; raw += 8) {
            Address address = toAddr(raw);
            try {
                Address target = toAddr(currentProgram.getMemory().getLong(address));
                Function function = currentProgram.getFunctionManager().getFunctionAt(target);
                String targetText = function == null
                    ? symbolText(target) + ":" + dataText(target)
                    : function.getName(true) + "@" + function.getEntryPoint();
                line("POINTER_RANGE_ENTRY", address.toString(), symbolText(address),
                    target.toString(), targetText);
            } catch (Exception error) {
                line("POINTER_RANGE_ENTRY", address.toString(), symbolText(address),
                    "UNREADABLE", clean(error.getMessage()));
            }
        }
        line("POINTER_RANGE_END", toAddr(startRaw).toString(), toAddr(endRaw).toString());
    }

    private void dumpPointerTarget(Address source, Address target) {
        line("POINTER_TARGET", source.toString(), target.toString(), symbolText(target), dataText(target));
        try {
            Address nested = toAddr(currentProgram.getMemory().getLong(target.add(8)));
            line("POINTER_TARGET_PLUS_8", target.toString(), nested.toString(), symbolText(nested),
                dataText(nested), readAscii(nested, 128));
        } catch (Exception ignored) {
            // Not every target is readable data.
        }
    }

    private void dumpReferences(Address address, String kind) {
        ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(address);
        int count = 0;
        while (refs.hasNext() && count++ < 96) {
            Reference ref = refs.next();
            Function owner = currentProgram.getFunctionManager().getFunctionContaining(ref.getFromAddress());
            String ownerText = owner == null ? "" : owner.getName(true) + "@" + owner.getEntryPoint();
            line(kind, address.toString(), ref.getFromAddress().toString(),
                ref.getReferenceType().toString(), ownerText);
        }
    }

    private void exportFunction(Function function) {
        line("FUNCTION_BEGIN", function.getName(true), function.getEntryPoint().toString());
        dumpReferences(function.getEntryPoint(), "FUNCTION_REF");

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

        boolean emitDisassembly = function.getEntryPoint().getOffset() == 0x004f4418L;
        if (emitDisassembly) {
            line("DISASSEMBLY_BEGIN", function.getName(true), function.getEntryPoint().toString());
        }
        InstructionIterator instructions = currentProgram.getListing().getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            if (emitDisassembly) {
                line("INSTRUCTION", instruction.getAddress().toString(), instruction.toString());
            }
            for (int operand = 0; operand < instruction.getNumOperands(); operand++) {
                for (Object object : instruction.getOpObjects(operand)) {
                    if (!(object instanceof Scalar)) {
                        continue;
                    }
                    long value = ((Scalar) object).getUnsignedValue();
                    for (long expected : ERROR_CODES) {
                        if (value == expected) {
                            line("ERROR_SCALAR", instruction.getAddress().toString(),
                                hex(value), instruction.toString(), function.getName(true));
                        }
                    }
                }
            }
        }
        if (emitDisassembly) {
            line("DISASSEMBLY_END", function.getName(true), function.getEntryPoint().toString());
        }

        DecompileResults results = decompiler.decompileFunction(function, 90, TaskMonitor.DUMMY);
        if (!results.decompileCompleted()) {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        } else {
            out.println("DECOMPILE_BEGIN");
            String[] lines = results.getDecompiledFunction().getC().split("\\R", -1);
            int limit = Math.min(lines.length, 500);
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

    private Function functionAt(long rawAddress) {
        return currentProgram.getFunctionManager().getFunctionContaining(toAddr(rawAddress));
    }

    private String symbolText(Address address) {
        Symbol symbol = currentProgram.getSymbolTable().getPrimarySymbol(address);
        if (symbol != null) {
            return symbol.getName(true);
        }
        Symbol[] symbols = currentProgram.getSymbolTable().getSymbols(address);
        return symbols.length > 0 ? symbols[0].getName(true) : "";
    }

    private String dataText(Address address) {
        Data data = currentProgram.getListing().getDataAt(address);
        return data == null ? "" : clean(data.getDefaultValueRepresentation());
    }

    private String readAscii(Address address, int maximum) {
        StringBuilder result = new StringBuilder();
        Memory memory = currentProgram.getMemory();
        try {
            for (int i = 0; i < maximum; i++) {
                int value = memory.getByte(address.add(i)) & 0xff;
                if (value == 0) {
                    break;
                }
                if (value < 0x20 || value > 0x7e) {
                    return result.length() == 0 ? "" : result + "<NON_ASCII>";
                }
                result.append((char) value);
            }
        } catch (Exception ignored) {
            return "";
        }
        return result.toString();
    }

    private String signedHex(long value) {
        return value < 0 ? "-0x" + Long.toHexString(-value) : "+0x" + Long.toHexString(value);
    }

    private String hex(long value) {
        return "0x" + Long.toHexString(value);
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
