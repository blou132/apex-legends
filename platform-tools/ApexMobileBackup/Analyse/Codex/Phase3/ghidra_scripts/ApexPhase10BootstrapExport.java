// Targeted Phase10 bootstrap dependency export for Apex Mobile native and DEX programs.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.listing.Data;
import ghidra.program.model.listing.DataIterator;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
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

public class ApexPhase10BootstrapExport extends GhidraScript {
    private static final String[] TERMS = {
        "tdm/v1/route",
        "http post from http curl",
        "getinfo result",
        "retcode:",
        "sendclientroutepostrequest",
        "httprouteproc2",
        "tdmhttpclient.cpp",
        "tdatamasterreportmanager.cpp",
        "tdmhttpmanager.cpp",
        "sendclientroute",
        "client route",
        "cfgpush/getconfig",
        "cloudctrl.mgapex.com",
        "gcloudremoteconfig",
        "remoteconfig",
        "defaultvalue is null",
        "rpcaddresssvrportlist",
        "rpcaddresssvrbkiplist",
        "rpcconnectmode",
        "rpcconnecttdrproto",
        "rpcparallelchannels",
        "play/log/timestamp",
        "unknownhostexception",
        "playcommon",
        "clientlaunch",
        "requestavatarserverlist",
        "nativesetglobalactivity"
    };
    private static final String[] FUNCTION_NAME_TERMS = {
        "remoteconfig",
        "configureimpl",
        "getfromcache",
        "getdefaultconfig",
        "clearremoteconfigcache",
        "downloaderactivity",
        "downloaderclient",
        "expansionfile",
        "apkexpansion",
        "gameactivity::onactivityresult",
        "gameactivity::onresumebody",
        "gameactivity::onresume",
        "gameactivity::startactivityforresult"
    };

    private DecompInterface decompiler;
    private PrintWriter out;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase10 local output directory argument is required");
        }

        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase10 output directory");
        }

        String programName = currentProgram.getName().replaceAll("[^A-Za-z0-9._-]", "_");
        File outputFile = new File(outputDir, programName + "_bootstrap_export.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("LANGUAGE", currentProgram.getLanguageID().toString());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());

            Map<Function, Set<String>> owners = new LinkedHashMap<>();
            int hits = scanDefinedStrings(owners);
            line("STRING_HIT_COUNT", Integer.toString(hits));
            line("DIRECT_OWNER_COUNT", Integer.toString(owners.size()));

            Set<Function> exportFunctions = new LinkedHashSet<>(owners.keySet());
            List<Function> directOwners = new ArrayList<>(owners.keySet());
            for (Function owner : directOwners) {
                exportFunctions.addAll(owner.getCallingFunctions(TaskMonitor.DUMMY));
                exportFunctions.addAll(owner.getCalledFunctions(TaskMonitor.DUMMY));
            }
            for (int i = 1; i < args.length; i++) {
                String rawAddress = args[i].replaceFirst("^0[xX]", "");
                long offset = Long.parseUnsignedLong(rawAddress, 16);
                Function requested = currentProgram.getFunctionManager().getFunctionAt(toAddr(offset));
                if (requested == null) {
                    requested = currentProgram.getFunctionManager().getFunctionContaining(toAddr(offset));
                }
                if (requested == null) {
                    line("REQUESTED_FUNCTION_MISSING", args[i]);
                } else {
                    exportFunctions.add(requested);
                }
            }
            FunctionIterator functions = currentProgram.getFunctionManager().getFunctions(true);
            while (functions.hasNext()) {
                Function function = functions.next();
                String lowerName = function.getName(true).toLowerCase(Locale.ROOT);
                for (String term : FUNCTION_NAME_TERMS) {
                    if (lowerName.contains(term)) {
                        exportFunctions.add(function);
                        break;
                    }
                }
            }
            line("FUNCTION_EXPORT_COUNT", Integer.toString(exportFunctions.size()));

            int exported = 0;
            for (Function function : exportFunctions) {
                if (exported++ >= 300) {
                    line("FUNCTION_EXPORT_TRUNCATED", "300");
                    break;
                }
                exportFunction(function, owners.get(function));
            }

            println("PHASE10_BOOTSTRAP_EXPORT_OK " + currentProgram.getName());
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private int scanDefinedStrings(Map<Function, Set<String>> owners) {
        DataIterator dataIterator = currentProgram.getListing().getDefinedData(true);
        int hitCount = 0;
        while (dataIterator.hasNext()) {
            Data data = dataIterator.next();
            String value = data.getValue() instanceof String
                ? (String) data.getValue()
                : data.getDefaultValueRepresentation();
            if (value == null || value.length() < 4) {
                continue;
            }
            String lower = value.toLowerCase(Locale.ROOT);
            String matched = match(lower);
            if (matched == null) {
                continue;
            }

            hitCount++;
            line("STRING", data.getAddress().toString(), matched, clean(value));
            ReferenceIterator refs = currentProgram.getReferenceManager().getReferencesTo(data.getAddress());
            int refCount = 0;
            while (refs.hasNext() && refCount++ < 200) {
                Reference ref = refs.next();
                Function owner = currentProgram.getFunctionManager().getFunctionContaining(ref.getFromAddress());
                String ownerText = owner == null ? "" : owner.getName() + "@" + owner.getEntryPoint();
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

        DecompileResults results = decompiler.decompileFunction(function, 60, TaskMonitor.DUMMY);
        if (!results.decompileCompleted()) {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        } else {
            out.println("DECOMPILE_BEGIN");
            String[] lines = results.getDecompiledFunction().getC().split("\\R", -1);
            int limit = Math.min(lines.length, 700);
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
