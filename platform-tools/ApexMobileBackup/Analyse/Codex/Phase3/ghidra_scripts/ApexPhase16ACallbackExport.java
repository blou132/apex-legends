// Bounded CActionMgr callback-field export for Phase16A.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.lang.Register;
import ghidra.program.model.scalar.Scalar;
import ghidra.program.model.symbol.Reference;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApexPhase16ACallbackExport extends GhidraScript {
    private static final long CALLBACK_FIELD_OFFSET = 0x3b8L;
    private static final long REGISTRATION_VTABLE_SLOT = 0xe0L;
    private static final String CACTIONMGR_CONSTRUCTOR = "005bfb10";
    private static final String PROCESS_ACTION_ERROR = "005be71c";
    private static final String CALLBACK_IMPLEMENTATION = "0050cb38";
    private static final String CALLBACK_SUPPLIER = "00576890";

    private PrintWriter out;
    private DecompInterface decompiler;
    private final Map<Function, Set<String>> selected = new LinkedHashMap<>();
    private final List<OffsetHit> hits = new ArrayList<>();

    private static class OffsetHit {
        final Instruction instruction;
        final Function function;
        final String classification;

        OffsetHit(Instruction instruction, Function function, String classification) {
            this.instruction = instruction;
            this.function = function;
            this.classification = classification;
        }
    }

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase16A local output directory argument is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase16A output directory");
        }

        File outputFile = new File(outputDir, "libgcloud_cactionmgr_callback_export.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());
            line("CALLBACK_FIELD_OFFSET", String.format("0x%x", CALLBACK_FIELD_OFFSET));
            line("REGISTRATION_VTABLE_SLOT", String.format("0x%x", REGISTRATION_VTABLE_SLOT));

            selectAddress(CACTIONMGR_CONSTRUCTOR, "CACTIONMGR_CONSTRUCTOR");
            selectAddress(PROCESS_ACTION_ERROR, "PROCESS_ACTION_ERROR_READER");
            for (int index = 1; index < args.length; index++) {
                selectAddress(args[index], "EXPLICIT_FOLLOWUP");
            }

            scanExactOffset();
            line("OFFSET_HIT_COUNT", Integer.toString(hits.size()));
            for (OffsetHit hit : hits) {
                exportHit(hit);
            }
            scanVirtualSlotDispatches(REGISTRATION_VTABLE_SLOT, "SLOT_E0");
            scanFunctionVirtualSlotDispatches(
                CALLBACK_IMPLEMENTATION,
                0x28L,
                "CALLBACK_EXTERNAL_SLOT_28"
            );
            scanFunctionVirtualSlotDispatches(
                CALLBACK_SUPPLIER,
                0x48L,
                "SUPPLIER_STRATEGY_SLOT_48"
            );

            line("SELECTED_FUNCTION_COUNT", Integer.toString(selected.size()));
            for (Map.Entry<Function, Set<String>> entry : selected.entrySet()) {
                exportFunction(entry.getKey(), entry.getValue());
            }

            println("PHASE16A_CALLBACK_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private void scanVirtualSlotDispatches(long slot, String label) {
        int count = 0;
        InstructionIterator instructions = currentProgram.getListing().getInstructions(true);
        while (instructions.hasNext() && !monitor.isCancelled()) {
            Instruction instruction = instructions.next();
            MemoryBlock block = currentProgram.getMemory().getBlock(instruction.getAddress());
            if (block == null || !block.isExecute() ||
                !instruction.getMnemonicString().toLowerCase().startsWith("ldr") ||
                !containsScalar(instruction, slot)) {
                continue;
            }

            Register loadedRegister = firstRegister(instruction, 0);
            if (loadedRegister == null) {
                continue;
            }
            Instruction branch = findRegisterBranch(instruction, loadedRegister, 8);
            if (branch == null) {
                continue;
            }

            Function function = currentProgram.getFunctionManager()
                .getFunctionContaining(instruction.getAddress());
            line(
                label + "_DISPATCH",
                instruction.getAddress().toString(),
                branch.getAddress().toString(),
                loadedRegister.getName(),
                describe(function),
                clean(instruction.toString()),
                clean(branch.toString())
            );
            dumpInstructionWindow(instruction, 10);
            if (function != null) {
                select(function, label + "_DISPATCH");
            }
            count++;
        }
        line(label + "_DISPATCH_COUNT", Integer.toString(count));
    }

    private void scanFunctionVirtualSlotDispatches(String entry, long slot, String label) {
        Function function = currentProgram.getFunctionManager().getFunctionAt(toAddr(entry));
        if (function == null) {
            line(label + "_FUNCTION_NOT_FOUND", entry);
            return;
        }

        int count = 0;
        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        while (instructions.hasNext() && !monitor.isCancelled()) {
            Instruction instruction = instructions.next();
            if (!instruction.getMnemonicString().toLowerCase().startsWith("ldr") ||
                !containsScalar(instruction, slot)) {
                continue;
            }

            Register loadedRegister = firstRegister(instruction, 0);
            if (loadedRegister == null) {
                continue;
            }
            Instruction branch = findRegisterBranch(instruction, loadedRegister, 8);
            if (branch == null) {
                continue;
            }

            line(
                label + "_DISPATCH",
                instruction.getAddress().toString(),
                branch.getAddress().toString(),
                loadedRegister.getName(),
                describe(function),
                clean(instruction.toString()),
                clean(branch.toString())
            );
            dumpInstructionWindow(instruction, 10);
            count++;
        }
        line(label + "_DISPATCH_COUNT", Integer.toString(count));
    }

    private void scanExactOffset() {
        InstructionIterator instructions = currentProgram.getListing().getInstructions(true);
        while (instructions.hasNext() && !monitor.isCancelled()) {
            Instruction instruction = instructions.next();
            MemoryBlock block = currentProgram.getMemory().getBlock(instruction.getAddress());
            if (block == null || !block.isExecute() || !containsExactScalar(instruction)) {
                continue;
            }

            Function function = currentProgram.getFunctionManager()
                .getFunctionContaining(instruction.getAddress());
            String classification = classify(instruction);
            hits.add(new OffsetHit(instruction, function, classification));
            if (function != null) {
                select(function, "EXACT_0X3B8_" + classification);
            }
        }
    }

    private boolean containsExactScalar(Instruction instruction) {
        return containsScalar(instruction, CALLBACK_FIELD_OFFSET);
    }

    private boolean containsScalar(Instruction instruction, long expected) {
        for (int operand = 0; operand < instruction.getNumOperands(); operand++) {
            for (Object object : instruction.getOpObjects(operand)) {
                if (object instanceof Scalar) {
                    Scalar scalar = (Scalar) object;
                    if (scalar.getUnsignedValue() == expected) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Register firstRegister(Instruction instruction, int operand) {
        for (Object object : instruction.getOpObjects(operand)) {
            if (object instanceof Register) {
                return (Register) object;
            }
        }
        return null;
    }

    private Instruction findRegisterBranch(Instruction load, Register register, int limit) {
        Instruction current = load;
        for (int count = 0; count < limit; count++) {
            current = currentProgram.getListing().getInstructionAfter(current.getAddress());
            if (current == null || !sameFunction(load, current)) {
                return null;
            }
            String mnemonic = current.getMnemonicString().toLowerCase();
            if ((mnemonic.equals("blr") || mnemonic.equals("br")) &&
                register.equals(firstRegister(current, 0))) {
                return current;
            }
        }
        return null;
    }

    private String classify(Instruction instruction) {
        String mnemonic = instruction.getMnemonicString().toLowerCase();
        if (mnemonic.startsWith("str") || mnemonic.startsWith("stp") ||
            mnemonic.startsWith("stur") || mnemonic.startsWith("stlr")) {
            return "STORE_CANDIDATE";
        }
        if (mnemonic.equals("add") || mnemonic.equals("sub")) {
            return "ADDRESS_CANDIDATE";
        }
        if (mnemonic.startsWith("ldr") || mnemonic.startsWith("ldp") ||
            mnemonic.startsWith("ldur") || mnemonic.startsWith("ldar")) {
            return "READ_CANDIDATE";
        }
        return "SCALAR_OTHER";
    }

    private void exportHit(OffsetHit hit) {
        line(
            "OFFSET_HIT",
            hit.instruction.getAddress().toString(),
            hit.classification,
            describe(hit.function),
            clean(hit.instruction.toString())
        );
        dumpInstructionWindow(hit.instruction, 8);
    }

    private void dumpInstructionWindow(Instruction center, int radius) {
        Instruction first = center;
        for (int count = 0; count < radius; count++) {
            Instruction previous = currentProgram.getListing().getInstructionBefore(first.getAddress());
            if (previous == null || !sameFunction(center, previous)) {
                break;
            }
            first = previous;
        }

        line("WINDOW_BEGIN", center.getAddress().toString());
        Instruction current = first;
        int emittedAfterCenter = 0;
        boolean passedCenter = false;
        while (current != null && sameFunction(center, current)) {
            line(
                current.getAddress().equals(center.getAddress()) ? "WINDOW_HIT" : "WINDOW_INSN",
                current.getAddress().toString(),
                clean(current.toString())
            );
            if (passedCenter) {
                emittedAfterCenter++;
                if (emittedAfterCenter >= radius) {
                    break;
                }
            }
            if (current.getAddress().equals(center.getAddress())) {
                passedCenter = true;
            }
            current = currentProgram.getListing().getInstructionAfter(current.getAddress());
        }
        line("WINDOW_END", center.getAddress().toString());
    }

    private boolean sameFunction(Instruction left, Instruction right) {
        Function leftFunction = currentProgram.getFunctionManager()
            .getFunctionContaining(left.getAddress());
        Function rightFunction = currentProgram.getFunctionManager()
            .getFunctionContaining(right.getAddress());
        return leftFunction != null && leftFunction.equals(rightFunction);
    }

    private void selectAddress(String value, String reason) {
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
            line("EXPLICIT_FUNCTION_NOT_FOUND", value);
            return;
        }
        select(function, reason);
    }

    private void select(Function function, String reason) {
        selected.computeIfAbsent(function, ignored -> new LinkedHashSet<>()).add(reason);
    }

    private void exportFunction(Function function, Set<String> reasons) {
        line(
            "FUNCTION",
            function.getName(true),
            function.getEntryPoint().toString(),
            String.join(",", reasons)
        );
        exportCallReferences(function);

        DecompileResults result = decompiler.decompileFunction(function, 120, monitor);
        if (result != null && result.decompileCompleted() &&
            result.getDecompiledFunction() != null) {
            line("DECOMPILE_BEGIN", function.getName(true), function.getEntryPoint().toString());
            out.println(result.getDecompiledFunction().getC());
            line("DECOMPILE_END", function.getName(true), function.getEntryPoint().toString());
        } else {
            line(
                "DECOMPILE_ERROR",
                function.getName(true),
                function.getEntryPoint().toString(),
                result == null ? "null result" : clean(result.getErrorMessage())
            );
        }
    }

    private void exportCallReferences(Function function) {
        int callerCount = 0;
        for (Reference reference : currentProgram.getReferenceManager()
            .getReferencesTo(function.getEntryPoint())) {
            if (!reference.getReferenceType().isCall()) {
                continue;
            }
            Function caller = currentProgram.getFunctionManager()
                .getFunctionContaining(reference.getFromAddress());
            line(
                "DIRECT_CALLER",
                function.getEntryPoint().toString(),
                reference.getFromAddress().toString(),
                describe(caller)
            );
            callerCount++;
        }
        line("DIRECT_CALLER_COUNT", function.getEntryPoint().toString(), Integer.toString(callerCount));
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
