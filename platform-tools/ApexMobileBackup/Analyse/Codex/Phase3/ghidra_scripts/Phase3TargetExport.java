// Exports targeted xrefs and decompiler snippets for Phase3.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressIterator;
import ghidra.program.model.address.AddressSpace;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.data.DataType;
import ghidra.program.model.lang.Language;
import ghidra.program.model.listing.CodeUnit;
import ghidra.program.model.listing.Data;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.Listing;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Phase3TargetExport extends GhidraScript {
    private PrintWriter out;
    private DecompInterface decompiler;
    private AddressSpace space;
    private final Set<String> emittedFunctions = new LinkedHashSet<String>();

    private static class Target {
        final String group;
        final String name;
        final long addr;
        final String kind;

        Target(String group, String name, long addr, String kind) {
            this.group = group;
            this.name = name;
            this.addr = addr;
            this.kind = kind;
        }
    }

    private final List<Target> targets = Arrays.asList(
        new Target("request_avatar_server_list", "RequestAvatarServerList_string", 0x21c409fL, "string"),
        new Target("request_avatar_server_list", "RequestAvatarServerList_metadata_1", 0xa8db6c8L, "metadata"),
        new Target("request_avatar_server_list", "RequestAvatarServerList_metadata_2", 0xa8e7b70L, "metadata"),
        new Target("request_avatar_server_list", "RequestAvatarServerList_metadata_3", 0xa8ecb10L, "metadata"),
        new Target("request_avatar_server_list", "RequestAvatarServerList_code_candidate_1", 0x7941d2cL, "code_candidate"),
        new Target("request_avatar_server_list", "RequestAvatarServerList_code_candidate_2", 0x7941d6cL, "code_candidate"),

        new Target("avatar_server_event", "EVENTID_AVATARSERVERLIST_RETURN_string", 0x217dbf5L, "string"),

        new Target("loginmgr", "PureClient_Login_LoginMgr_cpp", 0x21cab3dL, "source_path"),
        new Target("loginmgr", "ULoginMgrWrapper", 0x221434fL, "string"),
        new Target("loginmgr", "LoginMgrWrapper_cpp", 0x22d375dL, "source_path"),
        new Target("loginmgr", "OpenServerList_string", 0x226d0b2L, "string"),
        new Target("loginmgr", "OpenServerList_metadata", 0xae66080L, "metadata"),
        new Target("loginmgr", "ServerListName_string", 0x2130708L, "string"),
        new Target("loginmgr", "ServerListName_metadata", 0xaa44588L, "metadata"),

        new Target("gameserver_backup_ip", "GameServerBackupIpList_string", 0x2180ba5L, "string"),
        new Target("gameserver_backup_ip", "GameServerBackupIpList_metadata_1", 0xae65f20L, "metadata"),
        new Target("gameserver_backup_ip", "GameServerBackupIpList_metadata_2", 0xae65f70L, "metadata"),

        new Target("sync_payload", "SyncPayloadToGameServer_string", 0x221f64aL, "string"),
        new Target("sync_payload", "SyncPayloadToGameServer_metadata_1", 0xab489d0L, "metadata"),
        new Target("sync_payload", "SyncPayloadToGameServer_metadata_2", 0xab48ac8L, "metadata"),
        new Target("sync_payload", "SyncPayloadToGameServer_metadata_3", 0xab48c20L, "metadata"),
        new Target("sync_payload", "SyncPayloadToGameServer_metadata_4", 0xad87bf8L, "metadata"),
        new Target("sync_payload", "SyncPayloadToGameServer_metadata_5", 0xad87c18L, "metadata"),
        new Target("sync_payload", "SyncPayloadToGameServer_metadata_6", 0xad87c68L, "metadata"),
        new Target("sync_payload", "SyncPayloadToGameServer_code_1", 0x7c1472cL, "code_candidate"),
        new Target("sync_payload", "SyncPayloadToGameServer_code_2", 0x7c14920L, "code_candidate"),
        new Target("sync_payload", "SyncPayloadToGameServer_code_3", 0x7c14cecL, "code_candidate"),
        new Target("sync_payload", "SyncPayloadToGameServer_code_4", 0x7c14e28L, "code_candidate"),
        new Target("sync_payload", "SyncPayloadToGameServer_code_5", 0x7c14ea8L, "code_candidate"),
        new Target("sync_payload", "SyncPayloadToGameServer_code_6", 0x7eb815cL, "code_candidate"),
        new Target("sync_payload", "SyncPayloadToGameServer_code_7", 0x7eb8284L, "code_candidate"),
        new Target("sync_payload", "SyncPayloadToGameServer_code_8", 0x7eb83a4L, "code_candidate"),

        new Target("uedstoolkit", "Script_UEDSToolkit", 0x226211aL, "string"),
        new Target("uedstoolkit", "DSControllerComponent_cpp", 0x21c0147L, "source_path"),
        new Target("uedstoolkit", "socket_http_cpp", 0x2235be7L, "source_path"),
        new Target("uedstoolkit", "RegisterDSControllerComponent_string", 0x2120940L, "string"),
        new Target("uedstoolkit", "RegisterDSControllerComponent_metadata", 0xa98c0b8L, "metadata"),

        new Target("reconnect", "ReconnectSyncData_string", 0x2141b07L, "string"),
        new Target("reconnect", "ReconnectSyncData_code_1", 0x79c3b84L, "code_candidate"),
        new Target("reconnect", "ReconnectSyncData_code_2", 0x79c3c44L, "code_candidate"),
        new Target("reconnect", "OnServerAboutToReconnect_string", 0x21f5446L, "string"),
        new Target("reconnect", "OnServerAboutToReconnect_metadata_1", 0xad72b80L, "metadata"),
        new Target("reconnect", "OnServerAboutToReconnect_metadata_2", 0xad72c18L, "metadata"),
        new Target("reconnect", "OnServerAboutToReconnect_metadata_3", 0xad72e40L, "metadata"),
        new Target("reconnect", "OnServerAboutToReconnect_code_1", 0x7ea006cL, "code_candidate"),
        new Target("reconnect", "OnServerAboutToReconnect_code_2", 0x7ea0418L, "code_candidate"),
        new Target("reconnect", "OnServerAboutToReconnect_code_3", 0x7ea05c4L, "code_candidate"),
        new Target("reconnect", "OnServerAboutToReconnect_code_4", 0x7ea0604L, "code_candidate"),
        new Target("reconnect", "OnServerAboutToReconnect_code_5", 0x7ea06c4L, "code_candidate"),
        new Target("reconnect", "OnPreReconnectOnServer_string", 0x221d27aL, "string"),
        new Target("reconnect", "OnPreReconnectOnServer_metadata_1", 0xa966210L, "metadata"),
        new Target("reconnect", "OnPreReconnectOnServer_metadata_2", 0xa967210L, "metadata"),
        new Target("reconnect", "OnPreReconnectOnServer_metadata_3", 0xa969838L, "metadata"),
        new Target("reconnect", "OnPreReconnectOnServer_code_1", 0x79c9308L, "code_candidate"),
        new Target("reconnect", "OnPreReconnectOnServer_code_2", 0x79c9388L, "code_candidate"),
        new Target("reconnect", "OnPreReconnectOnServer_code_3", 0x79c9408L, "code_candidate"),
        new Target("reconnect", "ClientNotifyReconnectedSuccessfully_string", 0x218a330L, "string"),
        new Target("reconnect", "ClientNotifyReconnectedSuccessfully_metadata_1", 0xa9ae008L, "metadata"),
        new Target("reconnect", "ClientNotifyReconnectedSuccessfully_metadata_2", 0xa9b0698L, "metadata"),
        new Target("reconnect", "ClientNotifyReconnectedSuccessfully_metadata_3", 0xa9ba988L, "metadata"),
        new Target("reconnect", "ClientNotifyReconnectedSuccessfully_code_1", 0x7a1acd8L, "code_candidate")
    );

    private final String[] searchTerms = new String[] {
        "RequestAvatarServerList", "EVENTID_AVATARSERVERLIST_RETURN", "OpenServerList",
        "ServerListName", "GameServerBackupIpList", "SyncPayloadToGameServer",
        "PureClient/Login/LoginMgr.cpp", "LoginMgrWrapper.cpp", "ULoginMgrWrapper",
        "ELuaCppEventType", "LuaCppEvent", "DispatchEvent", "SendEvent", "OnEvent",
        "TriggerEvent", "ProcessEvent", "NativeFunc", "FNativeFuncPtr", "exec",
        "StaticRegisterNatives", "RegisterNatives", "FNativeFunctionRegistrar",
        "/Script/UEDSToolkit", "DSControllerComponent.cpp", "socket_http.cpp",
        "RegisterDSControllerComponent", "OnServerAboutToReconnect",
        "OnPreReconnectOnServer", "ClientNotifyReconnectedSuccessfully", "ReconnectSyncData",
        "UNetDriver", "UNetConnection", "UChannel", "ActorChannel", "RPC",
        "Replicate", "socket", "connect", "getaddrinfo", "inet_addr", "inet_pton",
        "sockaddr", "send", "recv", "sendto", "recvfrom", "curl_easy_", "HTTP"
    };

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputPath;
        if (args.length > 0) {
            outputPath = args[0];
        } else {
            outputPath = new File(getSourceFile().getParentFile().getFile(false), "..\\output\\phase3_target_export.json").getCanonicalPath();
        }

        space = currentProgram.getAddressFactory().getDefaultAddressSpace();
        decompiler = new DecompInterface();
        DecompileOptions options = new DecompileOptions();
        decompiler.setOptions(options);
        decompiler.openProgram(currentProgram);

        File outFile = new File(outputPath);
        outFile.getParentFile().mkdirs();
        out = new PrintWriter(outFile, StandardCharsets.UTF_8.name());
        try {
            writeRoot();
        } finally {
            out.close();
            decompiler.dispose();
        }
        println("Phase3 target export written: " + outFile.getAbsolutePath());
    }

    private void writeRoot() throws Exception {
        out.println("{");
        writeProgramInfo();
        out.println(",");
        writeTargets();
        out.println(",");
        writeFunctions();
        out.println(",");
        writeSearchTermSummary();
        out.println("\n}");
    }

    private void writeProgramInfo() {
        Language lang = currentProgram.getLanguage();
        FunctionIterator it = currentProgram.getFunctionManager().getFunctions(true);
        int functionCount = 0;
        while (it.hasNext()) {
            it.next();
            functionCount++;
        }

        out.println("  \"program\": {");
        kv("name", currentProgram.getName(), true, 4);
        kv("executable_format", currentProgram.getExecutableFormat(), true, 4);
        kv("language_id", lang.getLanguageID().getIdAsString(), true, 4);
        kv("compiler_spec", currentProgram.getCompilerSpec().getCompilerSpecID().getIdAsString(), true, 4);
        kv("endian", lang.isBigEndian() ? "big" : "little", true, 4);
        kv("image_base", addrString(currentProgram.getImageBase()), true, 4);
        kvNum("function_count", functionCount, false, 4);
        out.println("  }");
    }

    private void writeTargets() throws Exception {
        out.println("  \"targets\": [");
        for (int i = 0; i < targets.size(); i++) {
            Target t = targets.get(i);
            writeTarget(t, i != targets.size() - 1);
        }
        out.print("  ]");
    }

    private void writeTarget(Target t, boolean comma) throws Exception {
        Address a = addr(t.addr);
        MemoryBlock block = currentProgram.getMemory().getBlock(a);
        Function f = currentProgram.getFunctionManager().getFunctionContaining(a);
        out.println("    {");
        kv("group", t.group, true, 6);
        kv("name", t.name, true, 6);
        kv("kind", t.kind, true, 6);
        kv("address", addrString(a), true, 6);
        kv("memory_block", block == null ? null : block.getName(), true, 6);
        kv("function", f == null ? null : functionLabel(f), true, 6);
        writeDataInfo(a, 6, true);
        writeRefs("refs_to", currentProgram.getReferenceManager().getReferencesTo(a), 6, true);
        writeRefs("refs_from", currentProgram.getReferenceManager().getReferencesFrom(a), 6, true);
        writeNearbyData(a, 0x80, 6, true);
        writeNearbyInstructions(a, 6, false);
        out.print("    }");
        if (comma) {
            out.println(",");
        } else {
            out.println();
        }
        if (f != null) {
            emittedFunctions.add(addrString(f.getEntryPoint()));
        }
    }

    private void writeFunctions() throws Exception {
        out.println("  \"functions\": [");
        List<Function> funcs = new ArrayList<Function>();
        Set<String> seen = new HashSet<String>();
        for (Target t : targets) {
            Function f = currentProgram.getFunctionManager().getFunctionContaining(addr(t.addr));
            if (f != null && seen.add(addrString(f.getEntryPoint()))) {
                funcs.add(f);
            }
        }
        for (int i = 0; i < funcs.size(); i++) {
            writeFunction(funcs.get(i), i != funcs.size() - 1);
        }
        out.print("  ]");
    }

    private void writeFunction(Function f, boolean comma) throws Exception {
        out.println("    {");
        kv("name", f.getName(), true, 6);
        kv("entry", addrString(f.getEntryPoint()), true, 6);
        kv("body_min", addrString(f.getBody().getMinAddress()), true, 6);
        kv("body_max", addrString(f.getBody().getMaxAddress()), true, 6);
        kv("signature", f.getSignature().toString(), true, 6);
        writeFunctionSet("callers", f.getCallingFunctions(monitor), 6, true);
        writeFunctionSet("callees", f.getCalledFunctions(monitor), 6, true);
        writeFunctionRefs(f, 6, true);
        writeFunctionPcodeCalls(f, 6, true);
        writeDecompilation(f, 6, false);
        out.print("    }");
        if (comma) {
            out.println(",");
        } else {
            out.println();
        }
    }

    private void writeDataInfo(Address a, int indent, boolean comma) {
        Data d = currentProgram.getListing().getDefinedDataContaining(a);
        ind(indent);
        out.println("\"data\": {");
        if (d == null) {
            kv("defined", "false", true, indent + 2);
        } else {
            kv("defined", "true", true, indent + 2);
            kv("address", addrString(d.getAddress()), true, indent + 2);
            DataType dt = d.getDataType();
            kv("datatype", dt == null ? null : dt.getName(), true, indent + 2);
            kv("value", safeValue(d), false, indent + 2);
        }
        ind(indent);
        out.print("}");
        out.println(comma ? "," : "");
    }

    private void writeRefs(String key, ReferenceIterator refs, int indent, boolean comma) {
        ind(indent);
        out.println("\"" + key + "\": [");
        int count = 0;
        while (refs.hasNext() && count < 80) {
            Reference r = refs.next();
            if (count > 0) {
                out.println(",");
            }
            ind(indent + 2);
            out.print("{");
            out.print("\"from\":\"" + esc(addrString(r.getFromAddress())) + "\",");
            out.print("\"to\":\"" + esc(addrString(r.getToAddress())) + "\",");
            out.print("\"type\":\"" + esc(r.getReferenceType().toString()) + "\",");
            out.print("\"operand_index\":" + r.getOperandIndex() + ",");
            Function ff = currentProgram.getFunctionManager().getFunctionContaining(r.getFromAddress());
            out.print("\"from_function\":" + q(ff == null ? null : functionLabel(ff)));
            out.print("}");
            count++;
        }
        if (refs.hasNext()) {
            if (count > 0) {
                out.println(",");
            }
            ind(indent + 2);
            out.print("{\"truncated\":true}");
        }
        out.println();
        ind(indent);
        out.print("]");
        out.println(comma ? "," : "");
    }

    private void writeRefs(String key, Reference[] refs, int indent, boolean comma) {
        ind(indent);
        out.println("\"" + key + "\": [");
        int count = 0;
        for (Reference r : refs) {
            if (count >= 80) {
                break;
            }
            if (count > 0) {
                out.println(",");
            }
            ind(indent + 2);
            out.print("{");
            out.print("\"from\":\"" + esc(addrString(r.getFromAddress())) + "\",");
            out.print("\"to\":\"" + esc(addrString(r.getToAddress())) + "\",");
            out.print("\"type\":\"" + esc(r.getReferenceType().toString()) + "\",");
            out.print("\"operand_index\":" + r.getOperandIndex() + ",");
            Function ff = currentProgram.getFunctionManager().getFunctionContaining(r.getFromAddress());
            out.print("\"from_function\":" + q(ff == null ? null : functionLabel(ff)));
            out.print("}");
            count++;
        }
        if (refs.length > 80) {
            if (count > 0) {
                out.println(",");
            }
            ind(indent + 2);
            out.print("{\"truncated\":true}");
        }
        out.println();
        ind(indent);
        out.print("]");
        out.println(comma ? "," : "");
    }

    private void writeNearbyData(Address a, long radius, int indent, boolean comma) {
        Listing listing = currentProgram.getListing();
        Address start = a;
        Address end = a;
        try {
            start = a.subtractNoWrap(Math.min(radius, a.getOffset()));
            end = a.addNoWrap(radius);
        } catch (Exception e) {
            // Keep the export alive for addresses close to a space boundary.
        }
        ind(indent);
        out.println("\"nearby_data\": [");
        int count = 0;
        Data d = listing.getDefinedDataAt(start);
        if (d == null) {
            d = listing.getDefinedDataAfter(start);
        }
        while (d != null && d.getAddress().compareTo(end) <= 0 && count < 64) {
            if (count > 0) {
                out.println(",");
            }
            ind(indent + 2);
            out.print("{");
            out.print("\"address\":\"" + esc(addrString(d.getAddress())) + "\",");
            DataType dt = d.getDataType();
            out.print("\"datatype\":" + q(dt == null ? null : dt.getName()) + ",");
            out.print("\"value\":" + q(safeValue(d)));
            out.print("}");
            count++;
            d = listing.getDefinedDataAfter(d.getAddress());
        }
        out.println();
        ind(indent);
        out.print("]");
        out.println(comma ? "," : "");
    }

    private void writeNearbyInstructions(Address a, int indent, boolean comma) {
        Listing listing = currentProgram.getListing();
        Function f = currentProgram.getFunctionManager().getFunctionContaining(a);
        ind(indent);
        out.println("\"nearby_instructions\": [");
        if (f != null) {
            AddressSetView body = f.getBody();
            Instruction ins = listing.getInstructionContaining(a);
            if (ins == null) {
                ins = listing.getInstructionBefore(a);
            }
            List<Instruction> before = new ArrayList<Instruction>();
            Instruction cur = ins;
            for (int i = 0; i < 8 && cur != null && body.contains(cur.getAddress()); i++) {
                before.add(0, cur);
                cur = listing.getInstructionBefore(cur.getAddress());
            }
            cur = ins == null ? null : listing.getInstructionAfter(ins.getAddress());
            for (int i = 0; i < 8 && cur != null && body.contains(cur.getAddress()); i++) {
                before.add(cur);
                cur = listing.getInstructionAfter(cur.getAddress());
            }
            for (int i = 0; i < before.size(); i++) {
                if (i > 0) {
                    out.println(",");
                }
                Instruction ii = before.get(i);
                ind(indent + 2);
                out.print("{\"address\":\"" + esc(addrString(ii.getAddress())) + "\",\"text\":" + q(ii.toString()) + "}");
            }
        }
        out.println();
        ind(indent);
        out.print("]");
        out.println(comma ? "," : "");
    }

    private void writeFunctionSet(String key, Set<Function> funcs, int indent, boolean comma) {
        ind(indent);
        out.println("\"" + key + "\": [");
        int count = 0;
        for (Function f : funcs) {
            if (count >= 80) {
                if (count > 0) {
                    out.println(",");
                }
                ind(indent + 2);
                out.print("{\"truncated\":true}");
                break;
            }
            if (count > 0) {
                out.println(",");
            }
            ind(indent + 2);
            out.print("{\"name\":" + q(f.getName()) + ",\"entry\":\"" + esc(addrString(f.getEntryPoint())) + "\"}");
            count++;
        }
        out.println();
        ind(indent);
        out.print("]");
        out.println(comma ? "," : "");
    }

    private void writeFunctionRefs(Function f, int indent, boolean comma) {
        ind(indent);
        out.println("\"external_call_refs\": [");
        int count = 0;
        Listing listing = currentProgram.getListing();
        ReferenceManager rm = currentProgram.getReferenceManager();
        AddressIterator ait = f.getBody().getAddresses(true);
        while (ait.hasNext()) {
            Address a = ait.next();
            if (monitor.isCancelled()) {
                break;
            }
            Instruction ins = listing.getInstructionAt(a);
            if (ins == null) {
                continue;
            }
            Reference[] refs = rm.getReferencesFrom(a);
            for (Reference r : refs) {
                String to = r.getToAddress().toString();
                String rt = r.getReferenceType().toString();
                if (rt.toLowerCase().contains("call") || to.toLowerCase().contains("external")) {
                    if (count >= 80) {
                        break;
                    }
                    if (count > 0) {
                        out.println(",");
                    }
                    ind(indent + 2);
                    Symbol sym = currentProgram.getSymbolTable().getPrimarySymbol(r.getToAddress());
                    out.print("{\"from\":\"" + esc(addrString(a)) + "\",\"to\":\"" + esc(to) + "\",\"type\":\"" + esc(rt) + "\",\"symbol\":" + q(sym == null ? null : sym.getName(true)) + "}");
                    count++;
                }
            }
        }
        out.println();
        ind(indent);
        out.print("]");
        out.println(comma ? "," : "");
    }

    private void writeFunctionPcodeCalls(Function f, int indent, boolean comma) {
        ind(indent);
        out.println("\"pcode_call_targets\": [");
        int count = 0;
        try {
            DecompileResults res = decompiler.decompileFunction(f, 45, monitor);
            if (res.decompileCompleted()) {
                HighFunction hf = res.getHighFunction();
                if (hf != null) {
                    Iterator<PcodeOpAST> ops = hf.getPcodeOps();
                    while (ops.hasNext() && count < 120) {
                        PcodeOpAST op = ops.next();
                        int opcode = op.getOpcode();
                        if (opcode == ghidra.program.model.pcode.PcodeOp.CALL ||
                            opcode == ghidra.program.model.pcode.PcodeOp.CALLIND) {
                            if (count > 0) {
                                out.println(",");
                            }
                            Varnode in = op.getInput(0);
                            ind(indent + 2);
                            out.print("{\"seq\":\"" + esc(op.getSeqnum().getTarget().toString()) + "\",\"op\":\"" + esc(op.getMnemonic()) + "\",\"target\":" + q(in == null ? null : in.toString()) + "}");
                            count++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            ind(indent + 2);
            out.print("{\"error\":" + q(e.toString()) + "}");
        }
        out.println();
        ind(indent);
        out.print("]");
        out.println(comma ? "," : "");
    }

    private void writeDecompilation(Function f, int indent, boolean comma) {
        ind(indent);
        out.println("\"decompile\": {");
        try {
            DecompileResults res = decompiler.decompileFunction(f, 60, monitor);
            kv("completed", Boolean.toString(res.decompileCompleted()), true, indent + 2);
            kv("error_message", res.getErrorMessage(), true, indent + 2);
            String c = null;
            if (res.decompileCompleted() && res.getDecompiledFunction() != null) {
                c = limitLines(redact(res.getDecompiledFunction().getC()), 260);
            }
            kv("c_limited", c, false, indent + 2);
        } catch (Exception e) {
            kv("completed", "false", true, indent + 2);
            kv("error_message", e.toString(), true, indent + 2);
            kv("c_limited", null, false, indent + 2);
        }
        ind(indent);
        out.print("}");
        out.println(comma ? "," : "");
    }

    private void writeSearchTermSummary() {
        out.println("  \"search_terms\": [");
        SymbolTable st = currentProgram.getSymbolTable();
        for (int i = 0; i < searchTerms.length; i++) {
            String term = searchTerms[i];
            ind(4);
            out.println("{");
            kv("term", term, true, 6);
            ind(6);
            out.println("\"symbols\": [");
            int count = 0;
            SymbolIterator syms = st.getSymbolIterator(term, true);
            while (syms.hasNext() && count < 40) {
                Symbol s = syms.next();
                if (count > 0) {
                    out.println(",");
                }
                ind(8);
                out.print("{\"name\":" + q(s.getName(true)) + ",\"address\":\"" + esc(addrString(s.getAddress())) + "\",\"type\":" + q(s.getSymbolType().toString()) + "}");
                count++;
            }
            out.println();
            ind(6);
            out.print("]");
            out.println();
            ind(4);
            out.print("}");
            if (i != searchTerms.length - 1) {
                out.println(",");
            } else {
                out.println();
            }
        }
        out.print("  ]");
    }

    private Address addr(long value) {
        return space.getAddress(value);
    }

    private String addrString(Address a) {
        if (a == null) {
            return null;
        }
        return "0x" + Long.toHexString(a.getOffset());
    }

    private String functionLabel(Function f) {
        return f.getName() + "@" + addrString(f.getEntryPoint());
    }

    private String safeValue(Data d) {
        if (d == null) {
            return null;
        }
        Object value = d.getValue();
        String s = value == null ? d.getDefaultValueRepresentation() : value.toString();
        return limitChars(redact(s), 500);
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
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < maxLines; i++) {
            b.append(lines[i]).append('\n');
        }
        b.append("/* TRUNCATED: ").append(lines.length - maxLines).append(" additional lines */");
        return b.toString();
    }

    private String limitChars(String s, int maxChars) {
        if (s == null || s.length() <= maxChars) {
            return s;
        }
        return s.substring(0, maxChars) + "...<TRUNCATED>";
    }

    private void kv(String key, String value, boolean comma, int indent) {
        ind(indent);
        out.print("\"" + esc(key) + "\": " + q(value));
        out.println(comma ? "," : "");
    }

    private void kvNum(String key, long value, boolean comma, int indent) {
        ind(indent);
        out.print("\"" + esc(key) + "\": " + value);
        out.println(comma ? "," : "");
    }

    private String q(String value) {
        if (value == null) {
            return "null";
        }
        return "\"" + esc(value) + "\"";
    }

    private String esc(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\':
                    b.append("\\\\");
                    break;
                case '"':
                    b.append("\\\"");
                    break;
                case '\b':
                    b.append("\\b");
                    break;
                case '\f':
                    b.append("\\f");
                    break;
                case '\n':
                    b.append("\\n");
                    break;
                case '\r':
                    b.append("\\r");
                    break;
                case '\t':
                    b.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        b.append(String.format("\\u%04x", (int)c));
                    } else {
                        b.append(c);
                    }
            }
        }
        return b.toString();
    }

    private void ind(int n) {
        for (int i = 0; i < n; i++) {
            out.print(' ');
        }
    }
}
