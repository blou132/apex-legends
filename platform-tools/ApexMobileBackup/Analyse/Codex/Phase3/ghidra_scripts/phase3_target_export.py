# Export targeted xrefs and decompiler snippets for Phase3.
# @category ApexMobile

import json
import os
import re

from ghidra.app.decompiler import DecompInterface, DecompileOptions
from ghidra.program.model.pcode import PcodeOp


TARGETS = [
    ("request_avatar_server_list", "RequestAvatarServerList_string", 0x21c409f, "string"),
    ("request_avatar_server_list", "RequestAvatarServerList_metadata_1", 0xa8db6c8, "metadata"),
    ("request_avatar_server_list", "RequestAvatarServerList_metadata_2", 0xa8e7b70, "metadata"),
    ("request_avatar_server_list", "RequestAvatarServerList_metadata_3", 0xa8ecb10, "metadata"),
    ("request_avatar_server_list", "RequestAvatarServerList_code_candidate_1", 0x7941d2c, "code_candidate"),
    ("request_avatar_server_list", "RequestAvatarServerList_code_candidate_2", 0x7941d6c, "code_candidate"),
    ("avatar_server_event", "EVENTID_AVATARSERVERLIST_RETURN_string", 0x217dbf5, "string"),
    ("loginmgr", "PureClient_Login_LoginMgr_cpp", 0x21cab3d, "source_path"),
    ("loginmgr", "ULoginMgrWrapper", 0x221434f, "string"),
    ("loginmgr", "LoginMgrWrapper_cpp", 0x22d375d, "source_path"),
    ("loginmgr", "OpenServerList_string", 0x226d0b2, "string"),
    ("loginmgr", "OpenServerList_metadata", 0xae66080, "metadata"),
    ("loginmgr", "ServerListName_string", 0x2130708, "string"),
    ("loginmgr", "ServerListName_metadata", 0xaa44588, "metadata"),
    ("gameserver_backup_ip", "GameServerBackupIpList_string", 0x2180ba5, "string"),
    ("gameserver_backup_ip", "GameServerBackupIpList_metadata_1", 0xae65f20, "metadata"),
    ("gameserver_backup_ip", "GameServerBackupIpList_metadata_2", 0xae65f70, "metadata"),
    ("sync_payload", "SyncPayloadToGameServer_string", 0x221f64a, "string"),
    ("sync_payload", "SyncPayloadToGameServer_metadata_1", 0xab489d0, "metadata"),
    ("sync_payload", "SyncPayloadToGameServer_metadata_2", 0xab48ac8, "metadata"),
    ("sync_payload", "SyncPayloadToGameServer_metadata_3", 0xab48c20, "metadata"),
    ("sync_payload", "SyncPayloadToGameServer_metadata_4", 0xad87bf8, "metadata"),
    ("sync_payload", "SyncPayloadToGameServer_metadata_5", 0xad87c18, "metadata"),
    ("sync_payload", "SyncPayloadToGameServer_metadata_6", 0xad87c68, "metadata"),
    ("sync_payload", "SyncPayloadToGameServer_code_1", 0x7c1472c, "code_candidate"),
    ("sync_payload", "SyncPayloadToGameServer_code_2", 0x7c14920, "code_candidate"),
    ("sync_payload", "SyncPayloadToGameServer_code_3", 0x7c14cec, "code_candidate"),
    ("sync_payload", "SyncPayloadToGameServer_code_4", 0x7c14e28, "code_candidate"),
    ("sync_payload", "SyncPayloadToGameServer_code_5", 0x7c14ea8, "code_candidate"),
    ("sync_payload", "SyncPayloadToGameServer_code_6", 0x7eb815c, "code_candidate"),
    ("sync_payload", "SyncPayloadToGameServer_code_7", 0x7eb8284, "code_candidate"),
    ("sync_payload", "SyncPayloadToGameServer_code_8", 0x7eb83a4, "code_candidate"),
    ("uedstoolkit", "Script_UEDSToolkit", 0x226211a, "string"),
    ("uedstoolkit", "DSControllerComponent_cpp", 0x21c0147, "source_path"),
    ("uedstoolkit", "socket_http_cpp", 0x2235be7, "source_path"),
    ("uedstoolkit", "RegisterDSControllerComponent_string", 0x2120940, "string"),
    ("uedstoolkit", "RegisterDSControllerComponent_metadata", 0xa98c0b8, "metadata"),
    ("reconnect", "ReconnectSyncData_string", 0x2141b07, "string"),
    ("reconnect", "ReconnectSyncData_code_1", 0x79c3b84, "code_candidate"),
    ("reconnect", "ReconnectSyncData_code_2", 0x79c3c44, "code_candidate"),
    ("reconnect", "OnServerAboutToReconnect_string", 0x21f5446, "string"),
    ("reconnect", "OnServerAboutToReconnect_metadata_1", 0xad72b80, "metadata"),
    ("reconnect", "OnServerAboutToReconnect_metadata_2", 0xad72c18, "metadata"),
    ("reconnect", "OnServerAboutToReconnect_metadata_3", 0xad72e40, "metadata"),
    ("reconnect", "OnServerAboutToReconnect_code_1", 0x7ea006c, "code_candidate"),
    ("reconnect", "OnServerAboutToReconnect_code_2", 0x7ea0418, "code_candidate"),
    ("reconnect", "OnServerAboutToReconnect_code_3", 0x7ea05c4, "code_candidate"),
    ("reconnect", "OnServerAboutToReconnect_code_4", 0x7ea0604, "code_candidate"),
    ("reconnect", "OnServerAboutToReconnect_code_5", 0x7ea06c4, "code_candidate"),
    ("reconnect", "OnPreReconnectOnServer_string", 0x221d27a, "string"),
    ("reconnect", "OnPreReconnectOnServer_metadata_1", 0xa966210, "metadata"),
    ("reconnect", "OnPreReconnectOnServer_metadata_2", 0xa967210, "metadata"),
    ("reconnect", "OnPreReconnectOnServer_metadata_3", 0xa969838, "metadata"),
    ("reconnect", "OnPreReconnectOnServer_code_1", 0x79c9308, "code_candidate"),
    ("reconnect", "OnPreReconnectOnServer_code_2", 0x79c9388, "code_candidate"),
    ("reconnect", "OnPreReconnectOnServer_code_3", 0x79c9408, "code_candidate"),
    ("reconnect", "ClientNotifyReconnectedSuccessfully_string", 0x218a330, "string"),
    ("reconnect", "ClientNotifyReconnectedSuccessfully_metadata_1", 0xa9ae008, "metadata"),
    ("reconnect", "ClientNotifyReconnectedSuccessfully_metadata_2", 0xa9b0698, "metadata"),
    ("reconnect", "ClientNotifyReconnectedSuccessfully_metadata_3", 0xa9ba988, "metadata"),
    ("reconnect", "ClientNotifyReconnectedSuccessfully_code_1", 0x7a1acd8, "code_candidate"),
]

SEARCH_TERMS = [
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
    "sockaddr", "send", "recv", "sendto", "recvfrom", "curl_easy_", "HTTP",
]

REDACT_PATTERNS = [
    (re.compile(r"(?i)(access_token|id_token|cookie|openid|session|ticket|token)([A-Za-z0-9_./=+:-]{8,})"), r"\1<REDACTED>"),
    (re.compile(r"(?i)(m_strGcloudGameKey|m_strMidasSDKOfferId|m_strGCloudSDKOpenId)([A-Za-z0-9_./=+:-]{4,})?"), r"\1<REDACTED>"),
]


def redact(s):
    if s is None:
        return None
    s = str(s)
    for pat, repl in REDACT_PATTERNS:
        s = pat.sub(repl, s)
    return s


def limit_lines(s, max_lines):
    if s is None:
        return None
    lines = str(s).splitlines()
    if len(lines) <= max_lines:
        return str(s)
    return "\n".join(lines[:max_lines]) + "\n/* TRUNCATED: %d additional lines */" % (len(lines) - max_lines)


def limit_chars(s, max_chars):
    if s is None:
        return None
    s = str(s)
    if len(s) <= max_chars:
        return s
    return s[:max_chars] + "...<TRUNCATED>"


def addr(value):
    return currentProgram.getAddressFactory().getDefaultAddressSpace().getAddress(value)


def addr_s(a):
    if a is None:
        return None
    return "0x%x" % a.getOffset()


def function_label(f):
    if f is None:
        return None
    return "%s@%s" % (f.getName(), addr_s(f.getEntryPoint()))


def ref_obj(r):
    ff = currentProgram.getFunctionManager().getFunctionContaining(r.getFromAddress())
    return {
        "from": addr_s(r.getFromAddress()),
        "to": addr_s(r.getToAddress()),
        "type": str(r.getReferenceType()),
        "operand_index": r.getOperandIndex(),
        "from_function": function_label(ff),
    }


def refs_to_list(a, max_count=80):
    refs = []
    it = currentProgram.getReferenceManager().getReferencesTo(a)
    while it.hasNext() and len(refs) < max_count:
        refs.append(ref_obj(it.next()))
    if it.hasNext():
        refs.append({"truncated": True})
    return refs


def refs_from_list(a, max_count=80):
    refs = []
    for r in currentProgram.getReferenceManager().getReferencesFrom(a):
        if len(refs) >= max_count:
            refs.append({"truncated": True})
            break
        refs.append(ref_obj(r))
    return refs


def data_info(a):
    d = currentProgram.getListing().getDefinedDataContaining(a)
    if d is None:
        return {"defined": False}
    value = d.getValue()
    if value is None:
        value = d.getDefaultValueRepresentation()
    return {
        "defined": True,
        "address": addr_s(d.getAddress()),
        "datatype": d.getDataType().getName() if d.getDataType() is not None else None,
        "value": limit_chars(redact(value), 500),
    }


def nearby_data(a, radius=0x80, max_count=64):
    listing = currentProgram.getListing()
    start_off = max(0, a.getOffset() - radius)
    start = addr(start_off)
    end = addr(a.getOffset() + radius)
    d = listing.getDefinedDataAt(start)
    if d is None:
        d = listing.getDefinedDataAfter(start)
    items = []
    while d is not None and d.getAddress().compareTo(end) <= 0 and len(items) < max_count:
        value = d.getValue()
        if value is None:
            value = d.getDefaultValueRepresentation()
        items.append({
            "address": addr_s(d.getAddress()),
            "datatype": d.getDataType().getName() if d.getDataType() is not None else None,
            "value": limit_chars(redact(value), 500),
        })
        d = listing.getDefinedDataAfter(d.getAddress())
    return items


def nearby_instructions(a, max_each_side=8):
    listing = currentProgram.getListing()
    f = currentProgram.getFunctionManager().getFunctionContaining(a)
    if f is None:
        return []
    body = f.getBody()
    ins = listing.getInstructionContaining(a)
    if ins is None:
        ins = listing.getInstructionBefore(a)
    if ins is None:
        return []
    items = []
    before = []
    cur = ins
    for _ in range(max_each_side):
        if cur is None or not body.contains(cur.getAddress()):
            break
        before.insert(0, cur)
        cur = listing.getInstructionBefore(cur.getAddress())
    cur = listing.getInstructionAfter(ins.getAddress())
    after = []
    for _ in range(max_each_side):
        if cur is None or not body.contains(cur.getAddress()):
            break
        after.append(cur)
        cur = listing.getInstructionAfter(cur.getAddress())
    for ii in before + after:
        items.append({"address": addr_s(ii.getAddress()), "text": str(ii)})
    return items


def function_set(funcs, max_count=80):
    out = []
    count = 0
    for f in funcs:
        if count >= max_count:
            out.append({"truncated": True})
            break
        out.append({"name": f.getName(), "entry": addr_s(f.getEntryPoint())})
        count += 1
    return out


def external_call_refs(f, max_count=100):
    listing = currentProgram.getListing()
    rm = currentProgram.getReferenceManager()
    st = currentProgram.getSymbolTable()
    out = []
    it = f.getBody().getAddresses(True)
    while it.hasNext() and len(out) < max_count:
        a = it.next()
        ins = listing.getInstructionAt(a)
        if ins is None:
            continue
        for r in rm.getReferencesFrom(a):
            rt = str(r.getReferenceType())
            to = str(r.getToAddress())
            if "call" in rt.lower() or "external" in to.lower():
                sym = st.getPrimarySymbol(r.getToAddress())
                out.append({
                    "from": addr_s(a),
                    "to": to,
                    "type": rt,
                    "symbol": sym.getName(True) if sym is not None else None,
                })
                if len(out) >= max_count:
                    break
    if len(out) >= max_count:
        out.append({"truncated": True})
    return out


def decompile_function(decomp, f, timeout=35, max_lines=220):
    try:
        res = decomp.decompileFunction(f, timeout, monitor)
        result = {
            "completed": bool(res.decompileCompleted()),
            "error_message": res.getErrorMessage(),
            "c_limited": None,
            "pcode_calls": [],
        }
        if res.decompileCompleted() and res.getDecompiledFunction() is not None:
            result["c_limited"] = limit_lines(redact(res.getDecompiledFunction().getC()), max_lines)
        hf = res.getHighFunction()
        if hf is not None:
            ops = hf.getPcodeOps()
            while ops.hasNext() and len(result["pcode_calls"]) < 120:
                op = ops.next()
                if op.getOpcode() in (PcodeOp.CALL, PcodeOp.CALLIND):
                    inp = op.getInput(0)
                    result["pcode_calls"].append({
                        "seq": str(op.getSeqnum().getTarget()),
                        "op": op.getMnemonic(),
                        "target": str(inp) if inp is not None else None,
                    })
        return result
    except Exception as e:
        return {"completed": False, "error_message": str(e), "c_limited": None, "pcode_calls": []}


def function_obj(decomp, f):
    return {
        "name": f.getName(),
        "entry": addr_s(f.getEntryPoint()),
        "body_min": addr_s(f.getBody().getMinAddress()),
        "body_max": addr_s(f.getBody().getMaxAddress()),
        "signature": str(f.getSignature()),
        "callers": function_set(f.getCallingFunctions(monitor)),
        "callees": function_set(f.getCalledFunctions(monitor)),
        "external_call_refs": external_call_refs(f),
        "decompile": decompile_function(decomp, f),
    }


def target_obj(t):
    group, name, value, kind = t
    a = addr(value)
    block = currentProgram.getMemory().getBlock(a)
    f = currentProgram.getFunctionManager().getFunctionContaining(a)
    return {
        "group": group,
        "name": name,
        "kind": kind,
        "address": addr_s(a),
        "memory_block": block.getName() if block is not None else None,
        "function": function_label(f),
        "data": data_info(a),
        "refs_to": refs_to_list(a),
        "refs_from": refs_from_list(a),
        "nearby_data": nearby_data(a),
        "nearby_instructions": nearby_instructions(a),
    }


def search_summary():
    st = currentProgram.getSymbolTable()
    out = []
    for term in SEARCH_TERMS:
        symbols = []
        it = st.getSymbolIterator(term, True)
        while it.hasNext() and len(symbols) < 40:
            s = it.next()
            symbols.append({
                "name": s.getName(True),
                "address": addr_s(s.getAddress()),
                "type": str(s.getSymbolType()),
            })
        out.append({"term": term, "symbols": symbols})
    return out


def main():
    args = getScriptArgs()
    if args and len(args) > 0:
        output_path = args[0]
    else:
        output_path = os.path.join(os.getcwd(), "phase3_target_export.json")

    decomp = DecompInterface()
    decomp.setOptions(DecompileOptions())
    decomp.openProgram(currentProgram)

    try:
        funcs = []
        seen = set()
        target_items = []
        for t in TARGETS:
            item = target_obj(t)
            target_items.append(item)
            f = currentProgram.getFunctionManager().getFunctionContaining(addr(t[2]))
            if f is not None:
                key = addr_s(f.getEntryPoint())
                if key not in seen:
                    seen.add(key)
                    funcs.append(f)

        function_items = [function_obj(decomp, f) for f in funcs]

        fm = currentProgram.getFunctionManager()
        count = 0
        fit = fm.getFunctions(True)
        while fit.hasNext():
            fit.next()
            count += 1

        lang = currentProgram.getLanguage()
        result = {
            "program": {
                "name": currentProgram.getName(),
                "executable_format": currentProgram.getExecutableFormat(),
                "language_id": lang.getLanguageID().getIdAsString(),
                "compiler_spec": currentProgram.getCompilerSpec().getCompilerSpecID().getIdAsString(),
                "endian": "big" if lang.isBigEndian() else "little",
                "image_base": addr_s(currentProgram.getImageBase()),
                "function_count": count,
            },
            "targets": target_items,
            "functions": function_items,
            "search_terms": search_summary(),
        }

        parent = os.path.dirname(output_path)
        if parent and not os.path.isdir(parent):
            os.makedirs(parent)
        with open(output_path, "w") as f:
            json.dump(result, f, indent=2, sort_keys=False)
        print("Phase3 target export written: %s" % output_path)
    finally:
        decomp.dispose()


main()
