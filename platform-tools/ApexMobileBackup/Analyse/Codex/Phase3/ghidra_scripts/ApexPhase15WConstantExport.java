// Exact-only Phase15W libUE4 scalar and defined-data search.
// @category ApexMobile

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

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApexPhase15WConstantExport extends GhidraScript {
    private static final long[] VALUES = {
        0x0430002eL,
        0x0430002fL,
        0x04300030L,
        0x04300031L,
        0x04300032L
    };

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

        File outputFile = new File(outputDir, "libUE4_exact_constants.txt");
        try (PrintWriter out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name())) {
            Map<Long, Integer> instructionCounts = initCounts();
            Map<Long, Integer> dataCounts = initCounts();

            line(out, "PROGRAM", currentProgram.getName());
            line(out, "LANGUAGE", currentProgram.getLanguageID().toString());
            line(out, "IMAGE_BASE", currentProgram.getImageBase().toString());

            InstructionIterator instructions = currentProgram.getListing().getInstructions(true);
            while (instructions.hasNext() && !monitor.isCancelled()) {
                Instruction instruction = instructions.next();
                for (int operand = 0; operand < instruction.getNumOperands(); operand++) {
                    for (Object object : instruction.getOpObjects(operand)) {
                        if (!(object instanceof Scalar)) {
                            continue;
                        }
                        long value = ((Scalar) object).getUnsignedValue();
                        if (!instructionCounts.containsKey(value)) {
                            continue;
                        }
                        instructionCounts.put(value, instructionCounts.get(value) + 1);
                        Function owner = currentProgram.getFunctionManager()
                            .getFunctionContaining(instruction.getAddress());
                        String ownerText = owner == null
                            ? ""
                            : owner.getName(true) + "@" + owner.getEntryPoint();
                        line(out, "INSTRUCTION_HIT", hex(value), instruction.getAddress().toString(),
                            instruction.toString(), ownerText);
                    }
                }
            }

            DataIterator dataIterator = currentProgram.getListing().getDefinedData(true);
            while (dataIterator.hasNext() && !monitor.isCancelled()) {
                Data data = dataIterator.next();
                Object valueObject = data.getValue();
                if (!(valueObject instanceof Scalar)) {
                    continue;
                }
                long value = ((Scalar) valueObject).getUnsignedValue();
                if (!dataCounts.containsKey(value)) {
                    continue;
                }
                dataCounts.put(value, dataCounts.get(value) + 1);
                line(out, "DATA_HIT", hex(value), data.getAddress().toString(),
                    clean(data.getDefaultValueRepresentation()), references(data.getAddress()));
            }

            for (long value : VALUES) {
                line(out, "SUMMARY", hex(value),
                    "instruction_hits=" + instructionCounts.get(value),
                    "defined_data_hits=" + dataCounts.get(value));
            }
        }

        println("PHASE15W_CONSTANT_EXPORT_OK " + currentProgram.getName());
        println(outputFile.getAbsolutePath());
    }

    private Map<Long, Integer> initCounts() {
        Map<Long, Integer> result = new LinkedHashMap<>();
        for (long value : VALUES) {
            result.put(value, 0);
        }
        return result;
    }

    private String references(Address address) {
        StringBuilder result = new StringBuilder();
        ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(address);
        int count = 0;
        while (refs.hasNext() && count++ < 32) {
            Reference ref = refs.next();
            if (result.length() > 0) {
                result.append(',');
            }
            Function owner = currentProgram.getFunctionManager().getFunctionContaining(ref.getFromAddress());
            result.append(ref.getFromAddress()).append(':').append(ref.getReferenceType());
            if (owner != null) {
                result.append(':').append(owner.getName(true)).append('@').append(owner.getEntryPoint());
            }
        }
        return result.toString();
    }

    private static String hex(long value) {
        return "0x" + Long.toHexString(value);
    }

    private static void line(PrintWriter out, String... values) {
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
