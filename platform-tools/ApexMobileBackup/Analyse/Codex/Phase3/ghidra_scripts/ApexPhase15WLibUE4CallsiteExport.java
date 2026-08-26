// Exact Phase15W libUE4 CreatePuffer call-site export.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ApexPhase15WLibUE4CallsiteExport extends GhidraScript {
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
        File outputFile = new File(outputDir, "libUE4_create_puffer_callsite.txt");

        try (PrintWriter out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name())) {
            Function function = currentProgram.getFunctionManager().getFunctionAt(toAddr(0x080d1ac8L));
            if (function == null) {
                throw new IllegalStateException("Expected CreatePuffer owner is missing");
            }
            out.println("PROGRAM\t" + currentProgram.getName());
            out.println("FUNCTION\t" + function.getName(true) + "\t" + function.getEntryPoint());
            out.println("DISASSEMBLY_BEGIN");
            InstructionIterator instructions = currentProgram.getListing()
                .getInstructions(function.getBody(), true);
            while (instructions.hasNext()) {
                Instruction instruction = instructions.next();
                out.println("INSTRUCTION\t" + instruction.getAddress() + "\t" + instruction);
            }
            out.println("DISASSEMBLY_END");

            DecompInterface decompiler = new DecompInterface();
            decompiler.setOptions(new DecompileOptions());
            decompiler.openProgram(currentProgram);
            DecompileResults results = decompiler.decompileFunction(function, 120, TaskMonitor.DUMMY);
            if (results.decompileCompleted()) {
                out.println("DECOMPILE_BEGIN");
                out.println(results.getDecompiledFunction().getC());
                out.println("DECOMPILE_END");
            } else {
                out.println("DECOMPILE_ERROR\t" + results.getErrorMessage());
            }
            decompiler.dispose();
        }

        println("PHASE15W_LIBUE4_CALLSITE_EXPORT_OK " + currentProgram.getName());
        println(outputFile.getAbsolutePath());
    }
}
