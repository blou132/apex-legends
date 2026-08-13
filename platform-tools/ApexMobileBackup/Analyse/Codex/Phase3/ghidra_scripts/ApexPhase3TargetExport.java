// Targeted Phase3B export for Apex Legends Mobile libUE4.so.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressOverflowException;
import ghidra.program.model.address.AddressSet;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.address.AddressSpace;
import ghidra.program.model.data.DataType;
import ghidra.program.model.listing.Data;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.Listing;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryAccessException;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.pcode.HighFunction;
import ghidra.program.model.pcode.PcodeOpAST;
import ghidra.program.model.pcode.Varnode;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.program.model.symbol.ReferenceManager;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;
import ghidra.program.model.symbol.SymbolTable;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ApexPhase3TargetExport extends GhidraScript {
    private AddressSpace space;
    private ReferenceManager refs;
    private Listing listing;
    private Memory memory;
    private DecompInterface decompiler;
    private File outputDir;

    private static class Target {
        final String name;
        final long address;
        final String kind;

        Target(String name, long address, String kind) {
            this.name = name;
            this.address = address;
            this.kind = kind;
        }
    }

    private static class Group {
        final String id;
        final String outputFile;
        final List<Target> targets;
        final long[] functions;
        final String[] searchTerms;
        final String[] focusTerms;
        final boolean dumpMetadata;

        Group(String id, String outputFile, List<Target> targets, long[] functions, String[] searchTerms,
                String[] focusTerms, boolean dumpMetadata) {
            this.id = id;
            this.outputFile = outputFile;
            this.targets = targets;
            this.functions = functions;
            this.searchTerms = searchTerms;
            this.focusTerms = focusTerms;
            this.dumpMetadata = dumpMetadata;
        }
    }

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length > 0) {
            outputDir = new File(args[0]);
        } else {
            outputDir = new File(getSourceFile().getParentFile().getFile(false), "..\\Phase3B\\output").getCanonicalFile();
        }
        outputDir.mkdirs();

        space = currentProgram.getAddressFactory().getDefaultAddressSpace();
        refs = currentProgram.getReferenceManager();
        listing = currentProgram.getListing();
        memory = currentProgram.getMemory();

        decompiler = new DecompInterface();
        DecompileOptions options = new DecompileOptions();
        decompiler.setOptions(options);
        decompiler.openProgram(currentProgram);
        Set<String> onlyGroups = new HashSet<String>();
        for (int i = 1; i < args.length; i++) {
            onlyGroups.add(args[i].toLowerCase());
        }

        try {
            for (Group g : buildGroups()) {
                if (!onlyGroups.isEmpty() && !onlyGroups.contains(g.id.toLowerCase())) {
                    continue;
                }
                exportGroup(g);
            }
        } finally {
            decompiler.dispose();
        }
        println("PHASE3B_EXPORT_OK");
        println(outputDir.getAbsolutePath());
    }

    private List<Group> buildGroups() {
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group(
            "request_avatar",
            "request_avatar_xrefs.json",
            Arrays.asList(
                new Target("RequestAvatarServerList_string", 0x21c409fL, "string"),
                new Target("RequestAvatarServerList_metadata_1", 0xa8db6c8L, "metadata"),
                new Target("RequestAvatarServerList_metadata_2", 0xa8e7b70L, "metadata"),
                new Target("RequestAvatarServerList_metadata_3", 0xa8ecb10L, "metadata"),
                new Target("RequestAvatarServerList_code_1", 0x7941d2cL, "code"),
                new Target("RequestAvatarServerList_code_2", 0x7941d6cL, "code")
            ),
            new long[] { 0x7941d10L },
            new String[] {
                "RequestAvatarServerList", "FNativeFunctionRegistrar", "RegisterFunction", "NativeFunc",
                "ProcessEvent", "UFunction", "StaticRegisterNatives", "exec"
            },
            new String[] {
                "RequestAvatarServerList", "NativeFunc", "FNativeFunctionRegistrar", "RegisterFunction",
                "ProcessEvent", "UFunction", "exec", "StaticRegisterNatives"
            },
            true));
        groups.add(new Group(
            "request_avatar_metadata",
            "request_avatar_metadata.json",
            Arrays.asList(
                new Target("RequestAvatarServerList_metadata_1", 0xa8db6c8L, "metadata"),
                new Target("RequestAvatarServerList_metadata_2", 0xa8e7b70L, "metadata"),
                new Target("RequestAvatarServerList_metadata_3", 0xa8ecb10L, "metadata")
            ),
            new long[] { 0x7941d10L },
            new String[] { "RequestAvatarServerList", "NativeFunc", "UFunction" },
            new String[] { "NativeFunc", "UFunction", "RequestAvatarServerList" },
            true));
        groups.add(new Group(
            "avatar_event",
            "avatar_event_xrefs.json",
            Arrays.asList(
                new Target("EVENTID_AVATARSERVERLIST_RETURN_string", 0x217dbf5L, "string")
            ),
            new long[] {},
            new String[] {
                "EVENTID_AVATARSERVERLIST_RETURN", "ELuaCppEventType::", "LuaCppEvent", "DispatchEvent",
                "SendEvent", "OnEvent", "TriggerEvent"
            },
            new String[] {
                "EVENTID_AVATARSERVERLIST_RETURN", "ELuaCppEventType", "DispatchEvent", "SendEvent",
                "TriggerEvent", "switch", "case"
            },
            false));
        groups.add(new Group(
            "loginmgr",
            "loginmgr_callgraph.json",
            Arrays.asList(
                new Target("PureClient_Login_LoginMgr_cpp", 0x21cab3dL, "source_path"),
                new Target("ULoginMgrWrapper", 0x221434fL, "string"),
                new Target("LoginMgrWrapper_cpp", 0x22d375dL, "source_path"),
                new Target("OpenServerList_string", 0x226d0b2L, "string"),
                new Target("OpenServerList_metadata", 0xae66080L, "metadata"),
                new Target("ServerListName_string", 0x2130708L, "string"),
                new Target("ServerListName_metadata", 0xaa44588L, "metadata"),
                new Target("RequestAvatarServerList_string", 0x21c409fL, "string")
            ),
            new long[] { 0x7941d10L },
            new String[] {
                "PureClient/Login/LoginMgr.cpp", "ULoginMgrWrapper", "LoginMgrWrapper.cpp",
                "OpenServerList", "ServerListName", "RequestAvatarServerList"
            },
            new String[] { "LoginMgr", "OpenServerList", "ServerListName", "RequestAvatarServerList", "ProcessEvent" },
            false));
        groups.add(new Group(
            "gameserver_backup",
            "gameserver_backup_xrefs.json",
            Arrays.asList(
                new Target("GameServerBackupIpList_string", 0x2180ba5L, "string"),
                new Target("GameServerBackupIpList_metadata_1", 0xae65f20L, "metadata"),
                new Target("GameServerBackupIpList_metadata_2", 0xae65f70L, "metadata")
            ),
            new long[] {},
            new String[] {
                "GameServerBackupIpList", "ArrayProperty", "StrProperty", "NameProperty",
                "StructProperty", "MapProperty", "inet_addr", "inet_pton", "getaddrinfo", "sockaddr"
            },
            new String[] {
                "GameServerBackupIpList", "ArrayProperty", "StrProperty", "NameProperty",
                "StructProperty", "MapProperty", "inet_addr", "inet_pton", "getaddrinfo", "sockaddr"
            },
            true));
        groups.add(new Group(
            "syncpayload",
            "syncpayload_callgraph.json",
            Arrays.asList(
                new Target("SyncPayloadToGameServer_string", 0x221f64aL, "string"),
                new Target("SyncPayloadToGameServer_metadata_1", 0xab489d0L, "metadata"),
                new Target("SyncPayloadToGameServer_metadata_2", 0xab48ac8L, "metadata"),
                new Target("SyncPayloadToGameServer_metadata_3", 0xab48c20L, "metadata"),
                new Target("SyncPayloadToGameServer_metadata_4", 0xad87bf8L, "metadata"),
                new Target("SyncPayloadToGameServer_metadata_5", 0xad87c18L, "metadata"),
                new Target("SyncPayloadToGameServer_metadata_6", 0xad87c68L, "metadata")
            ),
            new long[] { 0x7c14710L, 0x7c148b0L, 0x7c14cacL, 0x7c14e44L, 0x7eb80dcL, 0x7eb8224L, 0x7eb8364L },
            new String[] {
                "SyncPayloadToGameServer", "ProcessEvent", "UNetDriver", "UNetConnection", "UChannel",
                "ActorChannel", "CallRemoteFunction", "InternalProcessRemoteFunction", "ProcessRemoteFunction",
                "ReplicateActor", "SendBunch"
            },
            new String[] {
                "SyncPayloadToGameServer", "ProcessEvent", "UNetDriver", "UNetConnection", "UChannel",
                "ActorChannel", "CallRemoteFunction", "InternalProcessRemoteFunction", "ProcessRemoteFunction",
                "ReplicateActor", "SendBunch"
            },
            true));
        groups.add(new Group(
            "reconnect",
            "reconnect_callgraph.json",
            Arrays.asList(
                new Target("ReconnectSyncData_string", 0x2141b07L, "string"),
                new Target("OnServerAboutToReconnect_string", 0x21f5446L, "string"),
                new Target("OnServerAboutToReconnect_metadata_1", 0xad72b80L, "metadata"),
                new Target("OnServerAboutToReconnect_metadata_2", 0xad72c18L, "metadata"),
                new Target("OnServerAboutToReconnect_metadata_3", 0xad72e40L, "metadata"),
                new Target("OnPreReconnectOnServer_string", 0x221d27aL, "string"),
                new Target("OnPreReconnectOnServer_metadata_1", 0xa966210L, "metadata"),
                new Target("OnPreReconnectOnServer_metadata_2", 0xa967210L, "metadata"),
                new Target("OnPreReconnectOnServer_metadata_3", 0xa969838L, "metadata"),
                new Target("ClientNotifyReconnectedSuccessfully_string", 0x218a330L, "string"),
                new Target("ClientNotifyReconnectedSuccessfully_metadata_1", 0xa9ae008L, "metadata"),
                new Target("ClientNotifyReconnectedSuccessfully_metadata_2", 0xa9b0698L, "metadata"),
                new Target("ClientNotifyReconnectedSuccessfully_metadata_3", 0xa9ba988L, "metadata")
            ),
            new long[] { 0x79c3420L, 0x79c90bcL, 0x7e9ffe4L, 0x7ea03fcL, 0x7ea059cL, 0x7ea05dcL, 0x7ea0694L, 0x7a1a7c8L },
            new String[] {
                "ReconnectSyncData", "OnServerAboutToReconnect", "OnPreReconnectOnServer",
                "ClientNotifyReconnectedSuccessfully", "UNetConnection", "URL", "FURL", "Host", "Port",
                "RemoteAddr", "IpAddr", "InternetAddr", "GetRemoteAddr", "ServerConnection",
                "WorldContext", "PendingNetGame", "getaddrinfo", "socket"
            },
            new String[] {
                "ReconnectSyncData", "OnServerAboutToReconnect", "OnPreReconnectOnServer",
                "ClientNotifyReconnectedSuccessfully", "UNetConnection", "FURL", "Host", "Port",
                "RemoteAddr", "IpAddr", "InternetAddr", "GetRemoteAddr", "ServerConnection",
                "PendingNetGame", "getaddrinfo", "socket"
            },
            true));
        groups.add(new Group(
            "uedstoolkit",
            "uedstoolkit_callgraph.json",
            Arrays.asList(
                new Target("socket_http_cpp", 0x2235be7L, "source_path"),
                new Target("DSControllerComponent_cpp", 0x21c0147L, "source_path"),
                new Target("RegisterDSControllerComponent_string", 0x2120940L, "string"),
                new Target("RegisterDSControllerComponent_metadata", 0xa98c0b8L, "metadata"),
                new Target("Script_UEDSToolkit", 0x226211aL, "string")
            ),
            new long[] {},
            new String[] {
                "socket_http.cpp", "DSControllerComponent.cpp", "RegisterDSControllerComponent",
                "/Script/UEDSToolkit", "socket", "connect", "send", "recv", "getaddrinfo", "curl_easy_",
                "HTTP"
            },
            new String[] {
                "socket_http.cpp", "DSControllerComponent", "RegisterDSControllerComponent",
                "socket", "connect", "send", "recv", "getaddrinfo", "curl_easy_", "HTTP"
            },
            true));
        return groups;
    }

    private void exportGroup(Group g) throws Exception {
        File file = new File(outputDir, g.outputFile);
        PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8.name());
        try {
            Json j = new Json(out);
            j.objStart();
            j.prop("group", g.id, true);
            writeProgram(j, true);
            writeTargets(j, g.targets, true);
            writeSearchTerms(j, g.searchTerms, true);
            if (g.dumpMetadata) {
                writeMetadataWindows(j, g.targets, true);
            } else {
                j.propName("metadata_windows", true);
                j.arrayStart();
                j.arrayEnd();
            }
            writeFunctions(j, g, false);
            j.objEnd();
        } finally {
            out.close();
        }
        println("WROTE " + file.getAbsolutePath());
    }

    private void writeProgram(Json j, boolean comma) {
        j.propName("program", comma);
        j.objStart();
        j.prop("name", currentProgram.getName(), true);
        j.prop("language_id", currentProgram.getLanguageID().toString(), true);
        j.prop("image_base", fmt(currentProgram.getImageBase()), true);
        j.prop("executable_format", currentProgram.getExecutableFormat(), false);
        j.objEnd();
    }

    private void writeTargets(Json j, List<Target> targets, boolean comma) {
        j.propName("targets", comma);
        j.arrayStart();
        boolean first = true;
        for (Target t : targets) {
            if (!first) {
                j.comma();
            }
            first = false;
            writeTarget(j, t);
        }
        j.arrayEnd();
    }

    private void writeTarget(Json j, Target t) {
        Address a = addr(t.address);
        Function containing = currentProgram.getFunctionManager().getFunctionContaining(a);
        Function at = currentProgram.getFunctionManager().getFunctionAt(a);
        MemoryBlock block = memory.getBlock(a);
        Data data = listing.getDefinedDataContaining(a);
        Instruction ins = listing.getInstructionContaining(a);
        j.objStart();
        j.prop("name", t.name, true);
        j.prop("kind", t.kind, true);
        j.prop("address", fmt(a), true);
        j.prop("memory_block", block == null ? null : block.getName(), true);
        j.propName("function_containing", true);
        writeFunctionSummary(j, containing);
        j.propName("function_at", true);
        writeFunctionSummary(j, at);
        j.propName("defined_data", true);
        writeDataSummary(j, data);
        j.prop("instruction", ins == null ? null : ins.toString(), true);
        j.propName("xrefs_to", true);
        writeReferences(j, refs.getReferencesTo(a), 120);
        j.propName("xrefs_from", false);
        writeReferences(j, refs.getReferencesFrom(a), 120);
        j.objEnd();
    }

    private void writeMetadataWindows(Json j, List<Target> targets, boolean comma) {
        j.propName("metadata_windows", comma);
        j.arrayStart();
        boolean first = true;
        for (Target t : targets) {
            if (!"metadata".equals(t.kind)) {
                continue;
            }
            if (!first) {
                j.comma();
            }
            first = false;
            writeMetadataWindow(j, t);
        }
        j.arrayEnd();
    }

    private void writeMetadataWindow(Json j, Target t) {
        Address center = addr(t.address);
        Address start = safeAdd(center, -0x100);
        Address end = safeAdd(center, 0x100);
        j.objStart();
        j.prop("target", t.name, true);
        j.prop("center", fmt(center), true);
        j.prop("start", fmt(start), true);
        j.prop("end", fmt(end), true);
        j.propName("pointers", false);
        j.arrayStart();
        boolean first = true;
        for (long off = -0x100; off <= 0x100; off += 8) {
            Address slot = safeAdd(center, off);
            if (slot == null || memory.getBlock(slot) == null) {
                continue;
            }
            Long value = readPointer(slot);
            if (value == null || value.longValue() == 0L) {
                continue;
            }
            Address dst = addr(value.longValue());
            MemoryBlock dstBlock = memory.getBlock(dst);
            if (dstBlock == null) {
                continue;
            }
            if (!first) {
                j.comma();
            }
            first = false;
            writePointer(j, off, slot, dst);
        }
        j.arrayEnd();
        j.objEnd();
    }

    private void writePointer(Json j, long offset, Address slot, Address dst) {
        Function at = currentProgram.getFunctionManager().getFunctionAt(dst);
        Function containing = currentProgram.getFunctionManager().getFunctionContaining(dst);
        Data data = listing.getDefinedDataContaining(dst);
        Instruction ins = listing.getInstructionAt(dst);
        MemoryBlock block = memory.getBlock(dst);
        j.objStart();
        j.prop("offset", fmtSigned(offset), true);
        j.prop("slot", fmt(slot), true);
        j.prop("value", fmt(dst), true);
        j.prop("target_block", block == null ? null : block.getName(), true);
        j.propName("function_at", true);
        writeFunctionSummary(j, at);
        j.propName("function_containing", true);
        writeFunctionSummary(j, containing);
        j.propName("defined_data", true);
        writeDataSummary(j, data);
        j.prop("instruction_at", ins == null ? null : ins.toString(), true);
        j.prop("ascii_preview", readAscii(dst, 160), false);
        j.objEnd();
    }

    private void writeSearchTerms(Json j, String[] terms, boolean comma) {
        j.propName("search_terms", comma);
        j.arrayStart();
        boolean first = true;
        Set<String> seen = new HashSet<String>();
        for (String term : terms) {
            if (!seen.add(term)) {
                continue;
            }
            if (!first) {
                j.comma();
            }
            first = false;
            writeSearchTerm(j, term);
        }
        j.arrayEnd();
    }

    private void writeSearchTerm(Json j, String term) {
        j.objStart();
        j.prop("term", term, true);
        j.propName("symbols", true);
        j.arrayStart();
        int count = 0;
        SymbolTable st = currentProgram.getSymbolTable();
        SymbolIterator syms = st.getSymbolIterator(term, true);
        while (syms.hasNext() && count < 80) {
            Symbol s = syms.next();
            if (count > 0) {
                j.comma();
            }
            j.objStart();
            j.prop("name", s.getName(true), true);
            j.prop("address", fmt(s.getAddress()), true);
            j.prop("type", s.getSymbolType().toString(), true);
            Function f = currentProgram.getFunctionManager().getFunctionContaining(s.getAddress());
            j.propName("function_containing", false);
            writeFunctionSummary(j, f);
            j.objEnd();
            count++;
        }
        j.arrayEnd();
        j.prop("truncated", syms.hasNext(), true);
        int byteSearchLimit = byteSearchLimit(term);
        List<Address> occurrences = byteSearchLimit > 0 ? findBytes(term, byteSearchLimit) : new ArrayList<Address>();
        j.propName("byte_occurrences", false);
        j.arrayStart();
        boolean first = true;
        for (Address a : occurrences) {
            if (!first) {
                j.comma();
            }
            first = false;
            j.objStart();
            j.prop("address", fmt(a), true);
            j.prop("preview", readAscii(a, 160), true);
            j.propName("xrefs_to", false);
            writeReferences(j, refs.getReferencesTo(a), 30);
            j.objEnd();
        }
        j.arrayEnd();
        j.objEnd();
    }

    private int byteSearchLimit(String term) {
        if (term == null || term.length() < 12) {
            return 0;
        }
        if (term.equals("ELuaCppEventType::")) {
            return 80;
        }
        if (term.endsWith(".cpp") || term.startsWith("/Script/") || term.startsWith("EVENTID_") ||
                term.contains("Reconnect") || term.contains("RequestAvatar") ||
                term.contains("SyncPayload") || term.contains("GameServerBackup") ||
                term.contains("RegisterDSControllerComponent")) {
            return 12;
        }
        String lower = term.toLowerCase();
        String[] broad = new String[] {
            "url", "furl", "host", "port", "socket", "connect", "send", "recv", "http",
            "process", "event", "nameproperty", "strproperty", "arrayproperty", "structproperty",
            "mapproperty", "unet", "uchannel"
        };
        for (String b : broad) {
            if (lower.equals(b) || lower.contains(b) && term.length() < 24) {
                return 0;
            }
        }
        return 8;
    }

    private void writeFunctions(Json j, Group g, boolean comma) {
        j.propName("functions", comma);
        j.arrayStart();
        LinkedHashMap<String, Function> selected = new LinkedHashMap<String, Function>();
        for (long entry : g.functions) {
            addFunction(selected, currentProgram.getFunctionManager().getFunctionAt(addr(entry)));
        }
        for (Target t : g.targets) {
            Address a = addr(t.address);
            addFunction(selected, currentProgram.getFunctionManager().getFunctionContaining(a));
            addFunctionsFromReferences(selected, refs.getReferencesTo(a));
            addFunctionsFromReferences(selected, refs.getReferencesFrom(a));
        }

        Set<String> roots = new LinkedHashSet<String>(selected.keySet());
        LinkedHashMap<String, Function> graph = collectGraph(selected.values(), 3, 90);
        selected.putAll(graph);

        boolean first = true;
        int decompileCount = 0;
        for (Map.Entry<String, Function> e : selected.entrySet()) {
            Function f = e.getValue();
            if (f == null) {
                continue;
            }
            if (!first) {
                j.comma();
            }
            first = false;
            boolean root = roots.contains(e.getKey());
            boolean decompileCode = root && decompileCount < 36;
            if (decompileCode) {
                decompileCount++;
            }
            writeFunctionDetail(j, f, root, decompileCode, g.focusTerms);
        }
        j.arrayEnd();
    }

    private void writeFunctionDetail(Json j, Function f, boolean root, boolean decompileCode, String[] focusTerms) {
        j.objStart();
        j.prop("name", f.getName(), true);
        j.prop("entry", fmt(f.getEntryPoint()), true);
        j.prop("is_root", root, true);
        AddressSetView body = f.getBody();
        j.prop("body_min", fmt(body.getMinAddress()), true);
        j.prop("body_max", fmt(body.getMaxAddress()), true);
        j.prop("signature", f.getSignature().toString(), true);
        j.propName("callers", true);
        writeFunctionSet(j, f.getCallingFunctions(monitor), 80);
        j.propName("callees", true);
        writeFunctionSet(j, f.getCalledFunctions(monitor), 80);
        j.propName("body_data_refs", true);
        writeBodyDataRefs(j, f, focusTerms, root ? 100 : 20);
        j.propName("decompile", false);
        j.objStart();
        if (decompileCode) {
            DecompileResult dc = decompile(f, 20);
            j.prop("completed", dc.completed, true);
            j.prop("error", dc.error, true);
            j.prop("focus_hits", join(dc.focusHits), true);
            j.prop("c_limited", dc.c, false);
        } else {
            j.prop("completed", false, true);
            j.prop("error", root ? "decompile_limit_reached" : "not_decompiled_graph_node", true);
            j.prop("focus_hits", "", true);
            j.prop("c_limited", null, false);
        }
        j.objEnd();
        j.objEnd();
    }

    private void writeBodyDataRefs(Json j, Function f, String[] focusTerms, int max) {
        j.arrayStart();
        int count = 0;
        Set<String> seen = new HashSet<String>();
        Listing l = currentProgram.getListing();
        AddressSet body = new AddressSet(f.getBody());
        int scanned = 0;
        int maxInstructions = max > 20 ? 2500 : 400;
        for (Instruction ins = l.getInstructionAt(body.getMinAddress());
                ins != null && body.contains(ins.getAddress()) && count < max && scanned < maxInstructions;
                ins = l.getInstructionAfter(ins.getAddress())) {
            scanned++;
            Reference[] from = refs.getReferencesFrom(ins.getAddress());
            for (Reference r : from) {
                if (count >= max) {
                    break;
                }
                if (!r.getReferenceType().isData()) {
                    continue;
                }
                String key = fmt(r.getFromAddress()) + ">" + fmt(r.getToAddress());
                if (!seen.add(key)) {
                    continue;
                }
                String preview = readAscii(r.getToAddress(), 120);
                boolean focus = containsAny(preview, focusTerms);
                if (count > 0) {
                    j.comma();
                }
                j.objStart();
                j.prop("from", fmt(r.getFromAddress()), true);
                j.prop("to", fmt(r.getToAddress()), true);
                j.prop("type", r.getReferenceType().toString(), true);
                j.prop("instruction", ins.toString(), true);
                j.prop("target_preview", preview, true);
                j.prop("focus_hit", focus, false);
                j.objEnd();
                count++;
            }
        }
        if (count >= max) {
            j.comma();
            j.objStart();
            j.prop("truncated", true, false);
            j.objEnd();
        }
        if (scanned >= maxInstructions) {
            if (count > 0) {
                j.comma();
            }
            j.objStart();
            j.prop("truncated_by_instruction_scan", true, true);
            j.prop("instructions_scanned", scanned, false);
            j.objEnd();
        }
        j.arrayEnd();
    }

    private void addFunctionsFromReferences(LinkedHashMap<String, Function> selected, ReferenceIterator it) {
        while (it.hasNext() && selected.size() < 80) {
            Reference r = it.next();
            addFunction(selected, currentProgram.getFunctionManager().getFunctionContaining(r.getFromAddress()));
            addFunction(selected, currentProgram.getFunctionManager().getFunctionContaining(r.getToAddress()));
        }
    }

    private void addFunctionsFromReferences(LinkedHashMap<String, Function> selected, Reference[] arr) {
        for (Reference r : arr) {
            if (selected.size() >= 80) {
                return;
            }
            addFunction(selected, currentProgram.getFunctionManager().getFunctionContaining(r.getFromAddress()));
            addFunction(selected, currentProgram.getFunctionManager().getFunctionContaining(r.getToAddress()));
        }
    }

    private LinkedHashMap<String, Function> collectGraph(Collection<Function> roots, int depthLimit, int max) {
        LinkedHashMap<String, Function> out = new LinkedHashMap<String, Function>();
        Queue<Function> fq = new ArrayDeque<Function>();
        Queue<Integer> dq = new ArrayDeque<Integer>();
        for (Function f : roots) {
            if (f == null) {
                continue;
            }
            addFunction(out, f);
            fq.add(f);
            dq.add(Integer.valueOf(0));
        }
        while (!fq.isEmpty() && out.size() < max) {
            Function f = fq.remove();
            int depth = dq.remove().intValue();
            if (depth >= depthLimit) {
                continue;
            }
            for (Function next : f.getCalledFunctions(monitor)) {
                if (out.size() >= max) {
                    break;
                }
                if (addFunction(out, next)) {
                    fq.add(next);
                    dq.add(Integer.valueOf(depth + 1));
                }
            }
            for (Function next : f.getCallingFunctions(monitor)) {
                if (out.size() >= max) {
                    break;
                }
                if (addFunction(out, next)) {
                    fq.add(next);
                    dq.add(Integer.valueOf(depth + 1));
                }
            }
        }
        return out;
    }

    private boolean addFunction(LinkedHashMap<String, Function> map, Function f) {
        if (f == null) {
            return false;
        }
        String key = fmt(f.getEntryPoint());
        if (map.containsKey(key)) {
            return false;
        }
        map.put(key, f);
        return true;
    }

    private void writeFunctionSet(Json j, Set<Function> funcs, int max) {
        j.arrayStart();
        int count = 0;
        for (Function f : funcs) {
            if (count >= max) {
                break;
            }
            if (count > 0) {
                j.comma();
            }
            writeFunctionSummary(j, f);
            count++;
        }
        if (funcs.size() > max) {
            j.comma();
            j.objStart();
            j.prop("truncated", true, false);
            j.objEnd();
        }
        j.arrayEnd();
    }

    private void writeFunctionSummary(Json j, Function f) {
        if (f == null) {
            j.nullValue();
            return;
        }
        j.objStart();
        j.prop("name", f.getName(), true);
        j.prop("entry", fmt(f.getEntryPoint()), true);
        try {
            j.prop("body_min", fmt(f.getBody().getMinAddress()), true);
            j.prop("body_max", fmt(f.getBody().getMaxAddress()), false);
        } catch (Exception e) {
            j.prop("body_min", null, true);
            j.prop("body_max", null, false);
        }
        j.objEnd();
    }

    private void writeDataSummary(Json j, Data data) {
        if (data == null) {
            j.nullValue();
            return;
        }
        DataType dt = data.getDataType();
        j.objStart();
        j.prop("address", fmt(data.getAddress()), true);
        j.prop("datatype", dt == null ? null : dt.getName(), true);
        j.prop("value", safeDataValue(data), false);
        j.objEnd();
    }

    private void writeReferences(Json j, ReferenceIterator it, int max) {
        j.arrayStart();
        int count = 0;
        while (it.hasNext() && count < max) {
            Reference r = it.next();
            if (count > 0) {
                j.comma();
            }
            writeReference(j, r);
            count++;
        }
        if (it.hasNext()) {
            j.comma();
            j.objStart();
            j.prop("truncated", true, false);
            j.objEnd();
        }
        j.arrayEnd();
    }

    private void writeReferences(Json j, Reference[] arr, int max) {
        j.arrayStart();
        int count = 0;
        for (Reference r : arr) {
            if (count >= max) {
                break;
            }
            if (count > 0) {
                j.comma();
            }
            writeReference(j, r);
            count++;
        }
        if (arr.length > max) {
            j.comma();
            j.objStart();
            j.prop("truncated", true, false);
            j.objEnd();
        }
        j.arrayEnd();
    }

    private void writeReference(Json j, Reference r) {
        Function from = currentProgram.getFunctionManager().getFunctionContaining(r.getFromAddress());
        Function to = currentProgram.getFunctionManager().getFunctionContaining(r.getToAddress());
        j.objStart();
        j.prop("from", fmt(r.getFromAddress()), true);
        j.prop("to", fmt(r.getToAddress()), true);
        j.prop("type", r.getReferenceType().toString(), true);
        j.prop("operand_index", r.getOperandIndex(), true);
        j.propName("from_function", true);
        writeFunctionSummary(j, from);
        j.propName("to_function", false);
        writeFunctionSummary(j, to);
        j.objEnd();
    }

    private List<Address> findBytes(String text, int max) {
        List<Address> out = new ArrayList<Address>();
        byte[] needle = (text + "\0").getBytes(StandardCharsets.UTF_8);
        Address start = memory.getMinAddress();
        while (start != null && out.size() < max && !monitor.isCancelled()) {
            Address found = memory.findBytes(start, memory.getMaxAddress(), needle, null, true, TaskMonitor.DUMMY);
            if (found == null) {
                break;
            }
            out.add(found);
            start = safeAdd(found, 1);
        }
        if (out.isEmpty()) {
            needle = text.getBytes(StandardCharsets.UTF_8);
            start = memory.getMinAddress();
            while (start != null && out.size() < max && !monitor.isCancelled()) {
                Address found = memory.findBytes(start, memory.getMaxAddress(), needle, null, true, TaskMonitor.DUMMY);
                if (found == null) {
                    break;
                }
                out.add(found);
                start = safeAdd(found, 1);
            }
        }
        return out;
    }

    private static class DecompileResult {
        boolean completed;
        String error;
        String c;
        List<String> focusHits = new ArrayList<String>();
    }

    private DecompileResult decompile(Function f, int timeoutSecs) {
        DecompileResult out = new DecompileResult();
        try {
            DecompileResults res = decompiler.decompileFunction(f, timeoutSecs, monitor);
            out.completed = res.decompileCompleted();
            out.error = res.getErrorMessage();
            if (res.decompileCompleted() && res.getDecompiledFunction() != null) {
                out.c = limitLines(redact(res.getDecompiledFunction().getC()), 220);
            }
            HighFunction hf = res.getHighFunction();
            if (hf != null) {
                Set<String> calls = new LinkedHashSet<String>();
                java.util.Iterator<PcodeOpAST> ops = hf.getPcodeOps();
                while (ops.hasNext()) {
                    PcodeOpAST op = ops.next();
                    if (op.getOpcode() == PcodeOpAST.CALL && op.getInput(0) != null) {
                        Varnode vn = op.getInput(0);
                        if (vn.isAddress()) {
                            Function cf = currentProgram.getFunctionManager().getFunctionAt(vn.getAddress());
                            if (cf != null) {
                                calls.add(cf.getName() + "@" + fmt(cf.getEntryPoint()));
                            }
                        }
                    }
                }
                out.focusHits.addAll(calls);
            }
        } catch (Exception e) {
            out.completed = false;
            out.error = e.toString();
        }
        return out;
    }

    private Long readPointer(Address a) {
        try {
            return Long.valueOf(memory.getLong(a, false));
        } catch (MemoryAccessException e) {
            return null;
        }
    }

    private Address safeAdd(Address a, long delta) {
        if (a == null) {
            return null;
        }
        try {
            if (delta >= 0) {
                return a.addNoWrap(delta);
            }
            return a.subtractNoWrap(-delta);
        } catch (AddressOverflowException e) {
            return a;
        }
    }

    private Address addr(long value) {
        return space.getAddress(value);
    }

    private String fmt(Address a) {
        if (a == null) {
            return null;
        }
        return "0x" + Long.toHexString(a.getOffset());
    }

    private String fmtSigned(long value) {
        if (value < 0) {
            return "-0x" + Long.toHexString(-value);
        }
        return "0x" + Long.toHexString(value);
    }

    private String readAscii(Address a, int max) {
        if (a == null || memory.getBlock(a) == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Address cur = a;
        for (int i = 0; i < max; i++) {
            try {
                byte b = memory.getByte(cur);
                int c = b & 0xff;
                if (c == 0) {
                    break;
                }
                if (c < 0x20 || c > 0x7e) {
                    if (sb.length() == 0) {
                        return null;
                    }
                    break;
                }
                sb.append((char)c);
                cur = cur.addNoWrap(1);
            } catch (Exception e) {
                break;
            }
        }
        return sb.length() == 0 ? null : redact(sb.toString());
    }

    private String safeDataValue(Data d) {
        if (d == null) {
            return null;
        }
        try {
            Object value = d.getValue();
            String s = value == null ? d.getDefaultValueRepresentation() : value.toString();
            return limitChars(redact(s), 500);
        } catch (Exception e) {
            return e.toString();
        }
    }

    private boolean containsAny(String text, String[] terms) {
        if (text == null || terms == null) {
            return false;
        }
        String lower = text.toLowerCase();
        for (String term : terms) {
            if (term != null && lower.contains(term.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String join(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String s : values) {
            if (count > 0) {
                sb.append("; ");
            }
            sb.append(s);
            count++;
            if (count >= 40) {
                sb.append("; <TRUNCATED>");
                break;
            }
        }
        return sb.toString();
    }

    private String redact(String s) {
        if (s == null) {
            return null;
        }
        String r = s;
        r = r.replaceAll("(?i)(access_token|id_token|cookie|openid|session|ticket|token)([A-Za-z0-9_./=+:-]{8,})", "$1<REDACTED>");
        r = r.replaceAll("(?i)(m_strGcloudGameKey|m_strMidasSDKOfferId|m_strGCloudSDKOpenId)([A-Za-z0-9_./=+:-]{4,})?", "$1<REDACTED>");
        return r;
    }

    private String limitLines(String s, int maxLines) {
        if (s == null) {
            return null;
        }
        String[] lines = s.split("\\R", -1);
        if (lines.length <= maxLines) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxLines; i++) {
            sb.append(lines[i]).append('\n');
        }
        sb.append("/* TRUNCATED: ").append(lines.length - maxLines).append(" additional lines */");
        return sb.toString();
    }

    private String limitChars(String s, int maxChars) {
        if (s == null || s.length() <= maxChars) {
            return s;
        }
        return s.substring(0, maxChars) + "...<TRUNCATED>";
    }

    private static class Json {
        private final PrintWriter out;
        private final ArrayDeque<Boolean> pendingComplexCommas = new ArrayDeque<Boolean>();
        private final ArrayDeque<Integer> pendingComplexDepths = new ArrayDeque<Integer>();
        private int depth = 0;

        Json(PrintWriter out) {
            this.out = out;
        }

        void objStart() {
            out.print("{");
            depth++;
        }

        void objEnd() {
            out.print("}");
            depth--;
            finishComplexValue();
        }

        void arrayStart() {
            out.print("[");
            depth++;
        }

        void arrayEnd() {
            out.print("]");
            depth--;
            finishComplexValue();
        }

        void comma() {
            out.print(",");
        }

        void nullValue() {
            out.print("null");
            finishComplexValue();
        }

        void propName(String key, boolean comma) {
            rawPropName(key);
            pendingComplexCommas.push(Boolean.valueOf(comma));
            pendingComplexDepths.push(Integer.valueOf(depth));
        }

        private void rawPropName(String key) {
            out.print(q(key));
            out.print(":");
        }

        void prop(String key, String value, boolean comma) {
            rawPropName(key);
            out.print(q(value));
            if (comma) {
                comma();
            }
        }

        void prop(String key, boolean value, boolean comma) {
            rawPropName(key);
            out.print(value ? "true" : "false");
            if (comma) {
                comma();
            }
        }

        void prop(String key, int value, boolean comma) {
            rawPropName(key);
            out.print(value);
            if (comma) {
                comma();
            }
        }

        private void finishComplexValue() {
            if (!pendingComplexCommas.isEmpty() && pendingComplexDepths.peek().intValue() == depth) {
                pendingComplexDepths.pop();
                boolean comma = pendingComplexCommas.pop().booleanValue();
                if (comma) {
                    comma();
                }
            }
        }

        private static String q(String value) {
            if (value == null) {
                return "null";
            }
            return "\"" + esc(value) + "\"";
        }

        private static String esc(String s) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    default:
                        if (c < 0x20) {
                            sb.append(String.format("\\u%04x", (int)c));
                        } else {
                            sb.append(c);
                        }
                }
            }
            return sb.toString();
        }
    }
}
