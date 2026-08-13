// Targeted Phase13 JNI_OnLoad validation for one APK native library.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Function;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ApexPhase13JNIInventoryExport extends GhidraScript {
    private static final String JNI_ONLOAD = "JNI_OnLoad";
    private static final String[] TARGET_STRINGS = {
        "Java_com_epicgames_ue4_GameActivity_nativeResumeMainInit",
        "nativeResumeMainInit",
        "com/epicgames/ue4/GameActivity",
        "com.epicgames.ue4.GameActivity",
        "()V",
        "RegisterNatives",
        "JNI_OnLoad"
    };

    private PrintWriter out;
    private Memory memory;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length != 1) {
            throw new IllegalArgumentException("Phase13 local output directory is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase13 output directory");
        }
        String programName = currentProgram.getName().replaceAll("[^A-Za-z0-9._-]", "_");
        File outputFile = new File(outputDir, programName + "_phase13_jni_onload.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        memory = currentProgram.getMemory();

        DecompInterface decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);
        try {
            line("PROGRAM", currentProgram.getName());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());
            line("LANGUAGE", currentProgram.getLanguageID().toString());
            scanTargetStrings();

            List<Function> roots = findOnLoadFunctions();
            line("JNI_ONLOAD_FUNCTION_COUNT", Integer.toString(roots.size()));
            if (roots.size() != 1) {
                line("JNI_ONLOAD_RESOLUTION", "NOT_UNIQUE");
                return;
            }

            Function root = roots.get(0);
            line("JNI_ONLOAD_RESOLUTION", "UNIQUE");
            line("JNI_ONLOAD_FUNCTION", root.getName(true), root.getEntryPoint().toString(),
                root.getSignature().getPrototypeString());

            DecompileResults results = decompiler.decompileFunction(root, 90, TaskMonitor.DUMMY);
            if (!results.decompileCompleted()) {
                line("DECOMPILE_STATUS", "FAILED", clean(results.getErrorMessage()));
                return;
            }

            String code = results.getDecompiledFunction().getC();
            line("DECOMPILE_STATUS", "OK");
            line("GETENV_SLOT_0X30", yesNo(hasOffset(code, "0x30")));
            line("FINDCLASS_SLOT_0X30", yesNo(count(code, "+ 0x30") >= 2));
            line("REGISTER_NATIVES_SLOT_0X6B8", yesNo(hasOffset(code, "0x6b8")));
            line("EXCEPTION_CHECK_SLOT_0X720", yesNo(hasOffset(code, "0x720")));

            int edgeCount = 0;
            for (Function callee : root.getCalledFunctions(TaskMonitor.DUMMY)) {
                line("DIRECT_CALLEE", callee.getName(true), callee.getEntryPoint().toString());
                edgeCount++;
            }
            line("DIRECT_CALLEE_COUNT", Integer.toString(edgeCount));
            line("PHASE13_TARGETED_EXPORT", "OK");
        } finally {
            decompiler.dispose();
            out.close();
        }
    }

    private List<Function> findOnLoadFunctions() {
        List<Function> roots = new ArrayList<>();
        SymbolIterator symbols = currentProgram.getSymbolTable().getSymbols(JNI_ONLOAD);
        while (symbols.hasNext()) {
            Symbol symbol = symbols.next();
            if (!JNI_ONLOAD.equals(symbol.getName())) {
                continue;
            }
            Function function = currentProgram.getFunctionManager().getFunctionAt(symbol.getAddress());
            line("JNI_ONLOAD_SYMBOL", symbol.getAddress().toString(),
                symbol.getSymbolType().toString(), symbol.getSource().toString(),
                function == null ? "NO_FUNCTION" : function.getName(true));
            if (function != null && !roots.contains(function)) {
                roots.add(function);
            }
        }
        return roots;
    }

    private void scanTargetStrings() throws Exception {
        for (String target : TARGET_STRINGS) {
            byte[] needle = (target + "\0").getBytes(StandardCharsets.US_ASCII);
            int count = 0;
            for (MemoryBlock block : memory.getBlocks()) {
                if (!block.isInitialized()) {
                    continue;
                }
                Address cursor = block.getStart();
                while (cursor.compareTo(block.getEnd()) <= 0) {
                    Address found = memory.findBytes(cursor, block.getEnd(), needle, null, true,
                        TaskMonitor.DUMMY);
                    if (found == null) {
                        break;
                    }
                    line("TARGET_STRING", target, found.toString(), block.getName(),
                        "NULL_TERMINATED_EXACT_STRING");
                    count++;
                    cursor = found.add(needle.length);
                }
            }
            line("TARGET_STRING_COUNT", target, Integer.toString(count));
        }
    }

    private boolean hasOffset(String code, String offset) {
        return code.toLowerCase().contains("+ " + offset.toLowerCase());
    }

    private int count(String value, String needle) {
        int result = 0;
        int offset = 0;
        while ((offset = value.indexOf(needle, offset)) >= 0) {
            result++;
            offset += needle.length();
        }
        return result;
    }

    private String yesNo(boolean value) {
        return value ? "YES" : "NO";
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
