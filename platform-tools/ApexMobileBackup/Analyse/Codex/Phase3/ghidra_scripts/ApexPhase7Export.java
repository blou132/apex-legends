// Targeted Phase7 Lua loader and asset-mapping export for Apex Mobile libUE4.so.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.lang.Register;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.listing.Listing;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.scalar.Scalar;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApexPhase7Export extends GhidraScript {
    private static final long IMAGE_BASE = 0x100000L;
    private static final long REQUEST_BUILD = 0x6bc68e8L;
    private static final long CALLBACK_BINDER = 0x6bc6ca0L;
    private static final long REQUEST_NATIVE_THUNK = 0x7a31858L;
    private static final long RESPONSE_ADAPTER = 0x6be413cL;
    private static final long RESPONSE_HANDLER = 0x6be3bdcL;
    private static final long EVENT_EMITTER = 0x6be3f4cL;
    private static final long DYNAMIC_DISPATCH = 0x6be427cL;
    private static final long DYNAMIC_DISPATCH_CORE = 0x6be4d1cL;
    private static final long LUA_MODULE_CLOSURE = 0x66365bcL;
    private static final long LUA_LOOKUP_HELPER = 0x42bf01cL;
    private static final long LUA_INVOKE_HELPER = 0x4440f2cL;
    private static final long LUA_CLOSURE_HELPER = 0x4f0f238L;
    private static final long LUA_OPTIONAL_HOOK = 0x6636b54L;
    private static final long LUA_CLOSURE_CALLER_A = 0x663a4f4L;
    private static final long LUA_CLOSURE_CALLER_B = 0x4929540L;
    private static final long LUA_CLOSURE_CALLER_C = 0x49a8b54L;
    private static final long LUA_FILE_READ_FALLBACK = 0x49a9694L;
    private static final long LUA_LOAD_CHUNK = 0x48ab4d0L;
    private static final long LUA_BUFFER_READER = 0xa02e2a4L;
    private static final long LUA_PATH_RESOLVER_VTABLE = 0xaf721e8L;
    private static final long FILE_MANAGER_FACTORY = 0x46355e8L;
    private static final long FILE_BACKEND_VTABLE = 0xaf710e0L;
    private static final long UTF16_TO_NARROW = 0x427a4acL;
    private static final long NARROW_COPY = 0x427a69cL;
    private static final long SYNC_PAYLOAD_THUNK = 0xa220f70L;
    private static final long EVENT_VALUE = 0x138L;
    private static final long LOGIN_BACKUP_LIST_OFFSET = 0x150L;
    private static final long[] CALLBACK_VTABLES = new long[] { 0xa732320L, 0xa732390L };
    private static final int VTABLE_BEFORE = 0x20;
    private static final int VTABLE_AFTER = 0x200;

    private Memory memory;
    private Listing listing;
    private DecompInterface decompiler;
    private File outputDir;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase7 output directory argument is required");
        }

        File phase7Dir = new File(args[0]).getCanonicalFile();
        outputDir = new File(phase7Dir, "output");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase7 output directory");
        }

        memory = currentProgram.getMemory();
        listing = currentProgram.getListing();
        DecompileOptions options = new DecompileOptions();
        decompiler = new DecompInterface();
        decompiler.setOptions(options);
        decompiler.openProgram(currentProgram);

        try {
            validateProgram();
            writeEncodedNamesPhase7();
            writeLuaLoaderPhase7();
            writeLuaAssetMappingPhase7();
            writeEventSystemLocationPhase7();
            println("PHASE7_TARGETED_EXPORT_OK");
            println(outputDir.getAbsolutePath());
        } finally {
            decompiler.dispose();
        }
    }

    private void validateProgram() {
        if (currentProgram.getImageBase().getOffset() != IMAGE_BASE) {
            throw new IllegalStateException("Unexpected image base: " + fmt(currentProgram.getImageBase()));
        }
        for (long target : new long[] { REQUEST_NATIVE_THUNK, RESPONSE_HANDLER, EVENT_EMITTER,
                DYNAMIC_DISPATCH, DYNAMIC_DISPATCH_CORE, LUA_MODULE_CLOSURE,
                LUA_LOOKUP_HELPER, LUA_INVOKE_HELPER, LUA_CLOSURE_HELPER,
                LUA_OPTIONAL_HOOK, LUA_CLOSURE_CALLER_A, LUA_CLOSURE_CALLER_B,
                LUA_CLOSURE_CALLER_C, LUA_FILE_READ_FALLBACK, LUA_LOAD_CHUNK,
                LUA_BUFFER_READER, FILE_MANAGER_FACTORY, UTF16_TO_NARROW,
                NARROW_COPY, SYNC_PAYLOAD_THUNK }) {
            if (functionAt(target) == null) {
                throw new IllegalStateException("Missing target function at " + hx(target));
            }
        }
    }

    private void writeEncodedNamesPhase7() throws Exception {
        PrintWriter out = openJson("encoded_names.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "EventSystem encoded UTF-16 names", true);
        j.prop("status", "PROBABLE", true);
        j.prop("script_confirmed_fragment", "[8 encoded code units]ools/EventSystem/EventSystem.lua", true);
        j.prop("script_probable_plaintext", "Script/Tools/EventSystem/EventSystem.lua", true);
        j.prop("script_encoded_data_ghidra", "0x23e6d50", true);
        j.prop("script_encoded_data_elf", "0x22e6d50", true);
        j.prop("name_confirmed_fragment", "EventSystem.Post[8 encoded code units]", true);
        j.prop("name_probable_plaintext", "EventSystem.PostCppEvent", true);
        j.prop("name_encoded_data_ghidra", "0x23e76e0", true);
        j.prop("name_encoded_data_elf", "0x22e76e0", true);
        j.prop("decoder", "UNKNOWN: no transform of the encoded code units is present in the emitter or bridge before lookup", true);
        j.prop("promotion_to_confirmed", "NO", true);
        j.propName("emitter", true);
        writeFunction(j, functionAt(EVENT_EMITTER), true);
        j.propName("bridge", false);
        writeFunction(j, functionAt(DYNAMIC_DISPATCH), true);
        j.objEnd();
        closeJson(out);
    }

    private void writeLuaLoaderPhase7() throws Exception {
        PrintWriter out = openJson("lua_loader.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "Lua module loader reachable from FUN_06be427c", true);
        j.prop("status", "PARTIAL", true);
        j.prop("boundary", "generic virtual file lookup/read and Lua chunk handoff confirmed; physical container backend not proven", true);
        j.propName("module_closure", true);
        writeFunction(j, functionAt(LUA_MODULE_CLOSURE), true);
        j.propName("lookup_helper", true);
        writeFunction(j, functionAt(LUA_LOOKUP_HELPER), true);
        j.propName("invoke_helper", true);
        writeFunction(j, functionAt(LUA_INVOKE_HELPER), true);
        j.propName("closure_helper", true);
        writeFunction(j, functionAt(LUA_CLOSURE_HELPER), true);
        j.propName("optional_hook", true);
        writeFunction(j, functionAt(LUA_OPTIONAL_HOOK), true);
        j.propName("closure_registration_caller_a", true);
        writeFunction(j, functionAt(LUA_CLOSURE_CALLER_A), true);
        j.propName("closure_registration_caller_b", true);
        writeFunction(j, functionAt(LUA_CLOSURE_CALLER_B), true);
        j.propName("closure_registration_caller_c", true);
        writeFunction(j, functionAt(LUA_CLOSURE_CALLER_C), true);
        j.propName("file_read_fallback", true);
        writeFunction(j, functionAt(LUA_FILE_READ_FALLBACK), true);
        j.propName("load_chunk", true);
        writeFunction(j, functionAt(LUA_LOAD_CHUNK), true);
        j.propName("buffer_reader", true);
        writeFunction(j, functionAt(LUA_BUFFER_READER), true);
        long resolverTarget = memory.getLong(addr(LUA_PATH_RESOLVER_VTABLE + 0x48));
        long openTarget = memory.getLong(addr(LUA_PATH_RESOLVER_VTABLE + 0x18));
        long backendOpenTarget = memory.getLong(addr(FILE_BACKEND_VTABLE + 0xc0));
        j.prop("path_resolver_vtable", hx(LUA_PATH_RESOLVER_VTABLE), true);
        j.prop("file_open_slot", "+0x18", true);
        j.prop("file_open_target", hx(openTarget), true);
        j.propName("file_open_function", true);
        writeFunction(j, functionAt(openTarget), true);
        j.prop("path_resolver_slot", "+0x48", true);
        j.prop("path_resolver_target", hx(resolverTarget), true);
        j.propName("path_resolver_function", true);
        writeFunction(j, functionAt(resolverTarget), true);
        j.propName("file_manager_factory", true);
        writeFunction(j, functionAt(FILE_MANAGER_FACTORY), true);
        j.prop("file_backend_vtable", hx(FILE_BACKEND_VTABLE), true);
        j.prop("file_backend_open_slot", "+0xc0", true);
        j.prop("file_backend_open_target", hx(backendOpenTarget), true);
        j.propName("file_backend_open_function", true);
        writeFunction(j, functionAt(backendOpenTarget), true);
        j.propName("closure_callgraph_depth_3", true);
        writeCallGraph(j, functionAt(LUA_MODULE_CLOSURE), 3, 250);
        j.propName("named_lua_functions", false);
        writeNamedLuaFunctions(j, 200);
        j.objEnd();
        closeJson(out);
    }

    private void writeLuaAssetMappingPhase7() throws Exception {
        PrintWriter out = openJson("lua_asset_mapping.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "module path to asset lookup", true);
        j.prop("status", "UNKNOWN", true);
        j.prop("confirmed_transform", "case-insensitive .LUA suffix check and removal", true);
        j.prop("virtual_asset_name", "encoded-prefix module path without .lua; exact final lookup name not proven", true);
        j.prop("mount_prefix", "UNKNOWN", true);
        j.prop("extension_rewrite", "UNKNOWN beyond removal of .lua", true);
        j.prop("archive_reader", "CONFIRMED generic virtual/platform file manager; concrete PAK/custom/physical backend UNKNOWN", true);
        j.propName("bridge", true);
        writeFunction(j, functionAt(DYNAMIC_DISPATCH), true);
        j.propName("dispatch_core", true);
        writeFunction(j, functionAt(DYNAMIC_DISPATCH_CORE), true);
        j.propName("utf16_to_narrow_helper", true);
        writeFunction(j, functionAt(UTF16_TO_NARROW), true);
        j.propName("narrow_copy_helper", false);
        writeFunction(j, functionAt(NARROW_COPY), true);
        j.objEnd();
        closeJson(out);
    }

    private void writeEventSystemLocationPhase7() throws Exception {
        PrintWriter out = openJson("eventsystem_location.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "EventSystem module container", true);
        j.prop("status", "UNKNOWN_CONTAINER", true);
        j.prop("reason", "The native loader boundary does not identify a physical container and the local PAK inputs are absent", true);
        j.prop("decision_gate", "D", true);
        j.prop("eventsystem_source_recovered", "NO", true);
        j.prop("eventsystem_bytecode_recovered", "NO", true);
        j.prop("container_identified", "NO", false);
        j.objEnd();
        closeJson(out);
    }

    private void writeNamedLuaFunctions(Json j, int max) {
        j.arrayStart();
        FunctionIterator functions = currentProgram.getFunctionManager().getFunctions(true);
        int count = 0;
        while (functions.hasNext() && count < max) {
            Function function = functions.next();
            String name = function.getName();
            String lower = name.toLowerCase();
            if (!lower.contains("lua") && !lower.contains("pcall") && !lower.contains("loadbuffer")) {
                continue;
            }
            if (count++ > 0) {
                j.comma();
            }
            writeFunctionSummary(j, function);
        }
        j.arrayEnd();
    }

    private void writeEventEmitterPhase6() throws Exception {
        PrintWriter out = openJson("event_emitter.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "FUN_06be3f4c event wrapper", true);
        j.prop("status", "CONFIRMED", true);
        j.prop("emitter_ghidra", hx(EVENT_EMITTER), true);
        j.prop("emitter_elf_virtual_address", hx(EVENT_EMITTER - IMAGE_BASE), true);
        j.prop("dispatcher_call_site", "0x6be4054", true);
        j.prop("event_value_source", "uint16 param_2; RequestAvatar caller passes 0x138", true);
        j.prop("target_context", "param_1 forwarded to the Lua bridge", true);
        j.prop("success_argument", "param_3 forwarded unchanged", true);
        j.prop("response_body_argument", "param_4 forwarded unchanged as the callback FString", true);
        j.prop("script_path_confirmed_fragment", "[8 encoded UTF-16 code units]ools/EventSystem/EventSystem.lua", true);
        j.prop("script_path_probable_plaintext", "Script/Tools/EventSystem/EventSystem.lua", true);
        j.prop("script_path_confidence", "PROBABLE: exact length and clear suffix match; encoded prefix was not decoded", true);
        j.prop("dynamic_name_confirmed_fragment", "EventSystem.Post[8 encoded UTF-16 code units]", true);
        j.prop("dynamic_name_probable_plaintext", "EventSystem.PostCppEvent", true);
        j.prop("dynamic_name_confidence", "PROBABLE: exact length and ELuaCppEventType context match; encoded suffix was not decoded", true);
        j.prop("script_path_length_with_terminator", 41, true);
        j.prop("dynamic_name_length_with_terminator", 25, true);
        j.prop("encoded_script_prefix_rodata", "0x23e6d50", true);
        j.prop("encoded_dynamic_suffix_rodata", "0x23e76e0", true);
        j.propName("response_handler", true);
        writeFunction(j, functionAt(RESPONSE_HANDLER), true);
        j.propName("event_emitter", false);
        writeFunction(j, functionAt(EVENT_EMITTER), true);
        j.objEnd();
        closeJson(out);
    }

    private void writeDynamicDispatchPhase6() throws Exception {
        Function dispatcher = functionAt(DYNAMIC_DISPATCH);
        Set<Function> callers = safeCallingFunctions(dispatcher);
        PrintWriter out = openJson("dynamic_dispatch.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "FUN_06be427c dynamic dispatch", true);
        j.prop("status", "CONFIRMED", true);
        j.prop("classification", "native-to-Lua bridge", true);
        j.prop("process_event_like", "INVALIDATED for this function", true);
        j.prop("blueprint_dispatcher", "INVALIDATED for this function", true);
        j.prop("evidence", "checks a Lua runtime, recognizes .LUA, resolves a module/function path, pushes typed values, and invokes through Lua helpers", true);
        j.prop("lua_runtime_global", "0xb696eb8; bridge reads runtime+0x10", true);
        j.prop("typed_stack_tag", "0x16 associated with FUN_066365bc", true);
        j.prop("lookup_helper", "FUN_042bf01c at 0x42bf01c", true);
        j.prop("invoke_helper", "FUN_04440f2c at 0x4440f2c", true);
        j.prop("dotted_dispatch_core", "FUN_06be4d1c at 0x6be4d1c", true);
        j.prop("direct_caller_count", callers.size(), true);
        j.prop("caller_sample_limit", "Only one direct caller exists in the Ghidra database; a 10-20 caller comparison is unavailable", true);
        j.propName("dispatcher", true);
        writeFunction(j, dispatcher, true);
        j.propName("dispatch_core", true);
        writeFunction(j, functionAt(DYNAMIC_DISPATCH_CORE), true);
        j.propName("all_direct_callers", true);
        writeFunctionSet(j, callers, 1000);
        j.propName("caller_samples", false);
        writeCallerSamples(j, callers, 20);
        j.objEnd();
        closeJson(out);
    }

    private void writeEventConsumerPhase6() throws Exception {
        PrintWriter out = openJson("event_consumer.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "event 0x138 consumer", true);
        j.prop("status", "UNKNOWN", true);
        j.prop("search_boundary", "dispatcher-related functions and existing text assets only", true);
        j.prop("event_value", hx(EVENT_VALUE), true);
        j.prop("registration_mechanism", "UNKNOWN beyond the Lua EventSystem boundary", true);
        j.prop("consumer_kind", "PROBABLE Lua script", true);
        j.prop("consumer_location_probable", "PAK/Lua", true);
        j.prop("evidence", "The native emitter calls the Lua bridge; no accessible extracted Lua contains the event name or its variants", true);
        j.prop("static_native_limit_reached", "YES", true);
        j.prop("next_required_source", "Script/Tools/EventSystem/EventSystem.lua and the Lua subscriber for EVENTID_AVATARSERVERLIST_RETURN", true);
        j.propName("dispatcher", true);
        writeFunction(j, functionAt(DYNAMIC_DISPATCH), true);
        j.propName("dispatcher_callgraph_depth_2", false);
        writeCallGraph(j, functionAt(DYNAMIC_DISPATCH), 2, 200);
        j.objEnd();
        closeJson(out);
    }

    private void writeServerListParserPhase6() throws Exception {
        PrintWriter out = openJson("serverlist_parser.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "server-list parser from event consumer", true);
        j.prop("status", "UNKNOWN", true);
        j.prop("finding", "No parser can be assigned before resolving the registered consumer", true);
        j.prop("response_representation", "CONFIRMED FString at the native-to-Lua boundary", true);
        j.prop("wire_format", "UNKNOWN", true);
        j.prop("parser", "UNKNOWN", true);
        j.prop("observed_server_list_fields", "none", true);
        j.prop("host_ip_field", "UNKNOWN", true);
        j.prop("port_field", "UNKNOWN", true);
        j.propName("event_emitter", false);
        writeFunction(j, functionAt(EVENT_EMITTER), true);
        j.objEnd();
        closeJson(out);
    }

    private void writeLoginStoragePhase6() throws Exception {
        PrintWriter out = openJson("login_storage.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "Login server-list storage", true);
        j.prop("status", "UNKNOWN", true);
        j.prop("property_offset", hx(LOGIN_BACKUP_LIST_OFFSET), true);
        j.prop("property_name", "GameServerBackupIpList", true);
        j.prop("owner", "PROBABLE Login from Phase4 metadata", true);
        j.prop("property_type", "PROBABLE TArray<FName> from Phase4 metadata", true);
        j.prop("login_instance", "UNKNOWN", true);
        j.prop("writer", "UNKNOWN", true);
        j.prop("element_meaning", "UNKNOWN", true);
        j.prop("open_server_list_trigger", "UNKNOWN", true);
        j.prop("finding", "No consumer-derived Login instance or access to Login+0x150 is available", false);
        j.objEnd();
        closeJson(out);
    }

    private void writeRequestUrlSourcePhase6() throws Exception {
        Function thunk = functionAt(REQUEST_NATIVE_THUNK);
        Set<Function> callers = safeCallingFunctions(thunk);
        PrintWriter out = openJson("request_url_source.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "RequestAvatarServerList URL source", true);
        j.prop("status", "UNKNOWN", true);
        j.prop("finding", "URL is a decoded Unreal-frame FString; concrete producer not resolved", true);
        j.prop("direct_caller_count", callers.size(), true);
        j.prop("entry_references", "0x2a46298 INDIRECTION; 0x315ea38 DATA; no direct code caller", true);
        j.prop("dispatch_source", "PROBABLE Unreal reflection/script invocation", true);
        j.prop("exact_url_reconstructible", "NO from currently accessible static evidence", true);
        j.propName("native_thunk", true);
        writeFunction(j, thunk, true);
        j.propName("direct_callers", false);
        writeFunctionSet(j, callers, 1000);
        j.objEnd();
        closeJson(out);
    }

    private void writeSyncPayloadReceiverPhase6() throws Exception {
        Function thunk = functionAt(SYNC_PAYLOAD_THUNK);
        Set<Function> callers = safeCallingFunctions(thunk);
        PrintWriter out = openJson("syncpayload_receiver.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "SyncPayloadToGameServer receiver", true);
        j.prop("status", "UNKNOWN", true);
        j.prop("virtual_offset", "+0xa58", true);
        j.prop("direct_caller_count", callers.size(), true);
        j.prop("entry_references", "0x2bdaf30 INDIRECTION; 0x32b0638 DATA; no direct code caller", true);
        j.prop("concrete_receiver", "UNKNOWN", true);
        j.prop("concrete_slot_target", "UNKNOWN", true);
        j.prop("rpc_classification", "UNKNOWN", true);
        j.propName("native_thunk", true);
        writeFunction(j, thunk, true);
        j.propName("direct_callers", false);
        writeFunctionSet(j, callers, 1000);
        j.objEnd();
        closeJson(out);
    }

    private void writeCallerSamples(Json j, Set<Function> callers, int max) {
        j.arrayStart();
        int count = 0;
        for (Function caller : callers) {
            if (count >= max) {
                break;
            }
            if (count++ > 0) {
                j.comma();
            }
            writeFunction(j, caller, true);
        }
        j.arrayEnd();
    }

    private void writeRequestBuild() throws Exception {
        PrintWriter out = openJson("request_build.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("scope", "FUN_06bc68e8 plus direct callback binder FUN_06bc6ca0", true);
        j.propName("native_thunk", true);
        writeFunction(j, functionAt(REQUEST_NATIVE_THUNK), true);
        j.propName("request_build", true);
        writeFunction(j, functionAt(REQUEST_BUILD), true);
        j.propName("callback_binder", false);
        writeFunction(j, functionAt(CALLBACK_BINDER), true);
        j.objEnd();
        closeJson(out);
    }

    private void writeCallbackVtables() throws Exception {
        PrintWriter out = openJson("callback_vtables.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("range", "-0x20..+0x200 from each candidate address point", true);
        j.propName("vtables", true);
        j.arrayStart();
        Set<Long> exportedFunctions = new LinkedHashSet<Long>();
        for (int i = 0; i < CALLBACK_VTABLES.length; i++) {
            if (i > 0) {
                j.comma();
            }
            writeVtable(j, CALLBACK_VTABLES[i], exportedFunctions);
        }
        j.arrayEnd();
        j.propName("executable_entry_functions", false);
        j.arrayStart();
        int index = 0;
        for (Long entry : exportedFunctions) {
            Function function = functionAt(entry.longValue());
            if (function == null) {
                function = functionContaining(entry.longValue());
            }
            if (function == null) {
                continue;
            }
            if (index++ > 0) {
                j.comma();
            }
            writeFunction(j, function, true);
        }
        j.arrayEnd();
        j.objEnd();
        closeJson(out);
    }

    private void writeTargetedFlowSkeleton() throws Exception {
        writeResponseEvent();
        writeServerListResponse();
        writeGameServerBackupWriter();
        writeSyncPayload();
    }

    private void writeResponseEvent() throws Exception {
        PrintWriter out = openJson("response_event.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "response_event", true);
        j.prop("status", "CONFIRMED", true);
        j.prop("event_value", hx(EVENT_VALUE), true);
        j.prop("path", "FUN_06be413c -> FUN_06be3bdc -> FUN_06be3f4c(event 0x138)", true);
        j.propName("response_adapter", true);
        writeFunction(j, functionAt(RESPONSE_ADAPTER), true);
        j.propName("response_handler", true);
        writeFunction(j, functionAt(RESPONSE_HANDLER), true);
        j.propName("event_emitter", true);
        writeFunction(j, functionAt(EVENT_EMITTER), true);
        j.propName("callgraph_depth_5", false);
        writeCallGraph(j, functionAt(RESPONSE_HANDLER), 5, 250);
        j.objEnd();
        closeJson(out);
    }

    private void writeServerListResponse() throws Exception {
        PrintWriter out = openJson("serverlist_response.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "serverlist_response", true);
        j.prop("status", "PROBABLE", true);
        j.prop("format", "opaque response-body FString passed to event 0x138", true);
        j.prop("parser_status", "UNKNOWN: no JSON/protobuf/custom parser is called by the callback", true);
        j.prop("observed_fields", "none in the callback callgraph", true);
        j.propName("response_handler", false);
        writeFunction(j, functionAt(RESPONSE_HANDLER), true);
        j.objEnd();
        closeJson(out);
    }

    private void writeGameServerBackupWriter() throws Exception {
        PrintWriter out = openJson("gameserverbackup_writer.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "gameserverbackup_writer", true);
        j.prop("status", "UNKNOWN", true);
        j.prop("login_backup_list_offset", hx(LOGIN_BACKUP_LIST_OFFSET), true);
        j.prop("finding", "No proven Login instance or Login+0x150 access in the response callback", true);
        j.prop("callback_capture_offset", "+0x8", true);
        j.prop("capture_status", "object used through vtable+0x158; concrete Login type not proven", true);
        j.propName("response_handler", false);
        writeFunction(j, functionAt(RESPONSE_HANDLER), true);
        j.objEnd();
        closeJson(out);
    }

    private void writeSyncPayload() throws Exception {
        PrintWriter out = openJson("syncpayload_virtual.json");
        Json j = new Json(out);
        j.objStart();
        writeHeader(j);
        j.prop("topic", "syncpayload_virtual", true);
        j.prop("status", "UNKNOWN", true);
        j.prop("virtual_offset", "+0xa58", true);
        j.prop("finding", "The receiver is decoded from the Unreal frame; no unique concrete vtable target is proven", true);
        j.propName("native_thunk", false);
        writeFunction(j, functionAt(SYNC_PAYLOAD_THUNK), true);
        j.objEnd();
        closeJson(out);
    }

    private void writeCallGraph(Json j, Function root, int maxDepth, int maxNodes) {
        j.objStart();
        j.prop("root", root == null ? null : fmt(root.getEntryPoint()), true);
        j.prop("max_depth", maxDepth, true);
        j.prop("max_nodes", maxNodes, true);
        j.propName("nodes", false);
        j.arrayStart();
        if (root != null) {
            ArrayDeque<GraphNode> queue = new ArrayDeque<GraphNode>();
            Set<Long> visited = new LinkedHashSet<Long>();
            queue.add(new GraphNode(root, 0));
            int count = 0;
            while (!queue.isEmpty() && count < maxNodes) {
                GraphNode node = queue.removeFirst();
                long entry = node.function.getEntryPoint().getOffset();
                if (!visited.add(Long.valueOf(entry))) {
                    continue;
                }
                if (count++ > 0) {
                    j.comma();
                }
                j.objStart();
                j.prop("depth", node.depth, true);
                j.propName("function", true);
                writeFunctionSummary(j, node.function);
                j.propName("callees", false);
                Set<Function> callees = safeCalledFunctions(node.function);
                writeFunctionSet(j, callees, 200);
                j.objEnd();
                if (node.depth < maxDepth) {
                    for (Function callee : callees) {
                        if (!visited.contains(Long.valueOf(callee.getEntryPoint().getOffset()))) {
                            queue.addLast(new GraphNode(callee, node.depth + 1));
                        }
                    }
                }
            }
        }
        j.arrayEnd();
        j.objEnd();
    }

    private void writeVtable(Json j, long center, Set<Long> exportedFunctions) {
        j.objStart();
        j.prop("address_point", hx(center), true);
        j.prop("address_point_elf_virtual_address", hx(center - IMAGE_BASE), true);
        j.prop("block", blockName(addr(center)), true);
        j.propName("references_to_address_point", true);
        writeReferences(j, currentProgram.getReferenceManager().getReferencesTo(addr(center)), 100);
        j.propName("slots", false);
        j.arrayStart();
        int index = 0;
        for (long slot = center - VTABLE_BEFORE; slot <= center + VTABLE_AFTER; slot += 8L) {
            if (index++ > 0) {
                j.comma();
            }
            writeVtableSlot(j, center, slot, exportedFunctions);
        }
        j.arrayEnd();
        j.objEnd();
    }

    private void writeVtableSlot(Json j, long center, long slot, Set<Long> exportedFunctions) {
        Long value = null;
        String error = null;
        try {
            value = Long.valueOf(memory.getLong(addr(slot)));
        } catch (Exception e) {
            error = e.toString();
        }
        Address target = value == null ? null : addr(value.longValue());
        MemoryBlock targetBlock = target == null ? null : memory.getBlock(target);
        Function exact = target == null ? null : currentProgram.getFunctionManager().getFunctionAt(target);
        Function containing = target == null ? null : currentProgram.getFunctionManager().getFunctionContaining(target);
        boolean executable = targetBlock != null && targetBlock.isExecute();
        if (executable && value != null) {
            exportedFunctions.add(value);
        }

        j.objStart();
        j.prop("relative_offset", signedHex(slot - center), true);
        j.prop("slot", hx(slot), true);
        j.prop("slot_elf_virtual_address", hx(slot - IMAGE_BASE), true);
        j.prop("value", value == null ? null : hx(value.longValue()), true);
        j.prop("read_error", error, true);
        j.prop("classification", classifyValue(value, targetBlock), true);
        j.prop("target_block", targetBlock == null ? null : targetBlock.getName(), true);
        j.prop("target_ascii", targetBlock != null && !executable ? readAscii(target, 160) : null, true);
        j.propName("function_at_target", true);
        writeFunctionSummary(j, exact);
        j.propName("function_containing_target", false);
        writeFunctionSummary(j, containing);
        j.objEnd();
    }

    private void writeFunction(Json j, Function function, boolean includeDecompile) {
        if (function == null) {
            j.nullValue();
            return;
        }
        long entry = function.getEntryPoint().getOffset();
        j.objStart();
        j.prop("name", function.getName(), true);
        j.prop("entry", hx(entry), true);
        j.prop("entry_elf_virtual_address", hx(entry - IMAGE_BASE), true);
        j.prop("body_min", fmt(function.getBody().getMinAddress()), true);
        j.prop("body_max", fmt(function.getBody().getMaxAddress()), true);
        j.prop("size", function.getBody().getNumAddresses(), true);
        j.prop("signature", function.getSignature().toString(), true);
        j.propName("references_to_entry", true);
        writeReferences(j, currentProgram.getReferenceManager().getReferencesTo(function.getEntryPoint()), 200);
        j.propName("callers", true);
        writeFunctionSet(j, safeCallingFunctions(function), 200);
        j.propName("callees", true);
        writeFunctionSet(j, safeCalledFunctions(function), 200);
        j.propName("call_instructions", true);
        writeCallInstructions(j, function);
        j.propName("disassembly", true);
        writeDisassembly(j, function);
        j.propName("decompile", false);
        if (includeDecompile) {
            writeDecompile(j, function);
        } else {
            j.nullValue();
        }
        j.objEnd();
    }

    private void writeCallInstructions(Json j, Function function) {
        j.arrayStart();
        InstructionIterator instructions = listing.getInstructions(function.getBody(), true);
        int count = 0;
        while (instructions.hasNext() && count < 2000) {
            Instruction instruction = instructions.next();
            String mnemonic = instruction.getMnemonicString().toUpperCase();
            if (!(mnemonic.equals("BL") || mnemonic.equals("BLR") || mnemonic.equals("BR"))) {
                continue;
            }
            if (count++ > 0) {
                j.comma();
            }
            j.objStart();
            j.prop("address", fmt(instruction.getAddress()), true);
            j.prop("elf_virtual_address", hx(instruction.getAddress().getOffset() - IMAGE_BASE), true);
            j.prop("mnemonic", mnemonic, true);
            j.prop("instruction", instruction.toString(), true);
            j.propName("flows", false);
            writeAddressArray(j, instruction.getFlows());
            j.objEnd();
        }
        j.arrayEnd();
    }

    private void writeDisassembly(Json j, Function function) {
        j.arrayStart();
        InstructionIterator instructions = listing.getInstructions(function.getBody(), true);
        Map<String, Long> registers = new HashMap<String, Long>();
        int count = 0;
        while (instructions.hasNext() && count < 4000) {
            Instruction instruction = instructions.next();
            if (count++ > 0) {
                j.comma();
            }
            ResolvedInstruction resolved = resolveInstruction(instruction, registers);
            j.objStart();
            j.prop("address", fmt(instruction.getAddress()), true);
            j.prop("text", instruction.toString(), true);
            j.prop("mnemonic", instruction.getMnemonicString(), true);
            j.prop("resolved_address", resolved.address == null ? null : hx(resolved.address.longValue()), true);
            j.prop("resolved_block", resolved.address == null ? null : blockName(addr(resolved.address.longValue())), true);
            j.prop("loaded_value", resolved.loadedValue == null ? null : hx(resolved.loadedValue.longValue()), true);
            j.prop("loaded_ascii", resolved.loadedValue == null ? null : readAscii(addr(resolved.loadedValue.longValue()), 160), true);
            j.propName("flows", false);
            writeAddressArray(j, instruction.getFlows());
            j.objEnd();
        }
        j.arrayEnd();
    }

    private ResolvedInstruction resolveInstruction(Instruction instruction, Map<String, Long> registers) {
        ResolvedInstruction result = new ResolvedInstruction();
        String mnemonic = instruction.getMnemonicString().toUpperCase();
        String destination = registerName(firstRegister(instruction, 0));
        if ("ADRP".equals(mnemonic) || "ADR".equals(mnemonic)) {
            Long immediate = operandValue(instruction, 1);
            if (destination != null && immediate != null) {
                registers.put(destination, immediate);
                result.address = immediate;
            }
            return result;
        }
        if ("ADD".equals(mnemonic)) {
            String source = registerName(firstRegister(instruction, 1));
            Long base = source == null ? null : registers.get(source);
            Long immediate = operandValue(instruction, 2);
            if (destination != null && base != null && immediate != null) {
                long resolved = base.longValue() + immediate.longValue();
                registers.put(destination, Long.valueOf(resolved));
                result.address = Long.valueOf(resolved);
            } else if (destination != null) {
                registers.remove(destination);
            }
            return result;
        }
        if ("LDR".equals(mnemonic) || "LDUR".equals(mnemonic)) {
            String source = registerName(firstRegister(instruction, 1));
            Long base = source == null ? null : registers.get(source);
            Long immediate = operandValue(instruction, 1);
            if (base != null) {
                long resolved = base.longValue() + (immediate == null ? 0L : immediate.longValue());
                result.address = Long.valueOf(resolved);
                try {
                    long loaded = memory.getLong(addr(resolved));
                    result.loadedValue = Long.valueOf(loaded);
                    if (destination != null) {
                        registers.put(destination, Long.valueOf(loaded));
                    }
                } catch (Exception e) {
                    if (destination != null) {
                        registers.remove(destination);
                    }
                }
            }
            return result;
        }
        if (destination != null && writesFirstOperand(mnemonic)) {
            registers.remove(destination);
        }
        return result;
    }

    private void writeDecompile(Json j, Function function) {
        j.objStart();
        try {
            DecompileResults results = decompiler.decompileFunction(function, 45, TaskMonitor.DUMMY);
            boolean completed = results.decompileCompleted();
            j.prop("completed", completed, true);
            j.prop("error", completed ? null : results.getErrorMessage(), true);
            String c = completed ? results.getDecompiledFunction().getC() : null;
            j.prop("c", c == null ? null : limitLines(redact(c), 700), false);
        } catch (Exception e) {
            j.prop("completed", false, true);
            j.prop("error", e.toString(), true);
            j.prop("c", (String)null, false);
        }
        j.objEnd();
    }

    private Set<Function> safeCalledFunctions(Function function) {
        Set<Function> result = new LinkedHashSet<Function>();
        try {
            result.addAll(function.getCalledFunctions(TaskMonitor.DUMMY));
        } catch (Exception e) {
            // Best-effort export from the existing analysis database.
        }
        return result;
    }

    private Set<Function> safeCallingFunctions(Function function) {
        Set<Function> result = new LinkedHashSet<Function>();
        try {
            result.addAll(function.getCallingFunctions(TaskMonitor.DUMMY));
        } catch (Exception e) {
            // Best-effort export from the existing analysis database.
        }
        return result;
    }

    private void writeFunctionSet(Json j, Set<Function> functions, int max) {
        j.arrayStart();
        int index = 0;
        for (Function function : functions) {
            if (index >= max) {
                break;
            }
            if (index++ > 0) {
                j.comma();
            }
            writeFunctionSummary(j, function);
        }
        j.arrayEnd();
    }

    private void writeFunctionSummary(Json j, Function function) {
        if (function == null) {
            j.nullValue();
            return;
        }
        j.objStart();
        j.prop("name", function.getName(), true);
        j.prop("entry", fmt(function.getEntryPoint()), true);
        j.prop("entry_elf_virtual_address", hx(function.getEntryPoint().getOffset() - IMAGE_BASE), true);
        j.prop("body_min", fmt(function.getBody().getMinAddress()), true);
        j.prop("body_max", fmt(function.getBody().getMaxAddress()), false);
        j.objEnd();
    }

    private void writeReferences(Json j, ReferenceIterator references, int max) {
        j.arrayStart();
        int count = 0;
        while (references.hasNext() && count < max) {
            Reference reference = references.next();
            if (count++ > 0) {
                j.comma();
            }
            j.objStart();
            j.prop("from", fmt(reference.getFromAddress()), true);
            j.prop("to", fmt(reference.getToAddress()), true);
            j.prop("type", reference.getReferenceType().toString(), true);
            j.propName("function_containing", false);
            writeFunctionSummary(j, currentProgram.getFunctionManager().getFunctionContaining(reference.getFromAddress()));
            j.objEnd();
        }
        j.arrayEnd();
    }

    private void writeAddressArray(Json j, Address[] addresses) {
        j.arrayStart();
        for (int i = 0; i < addresses.length; i++) {
            if (i > 0) {
                j.comma();
            }
            j.stringValue(fmt(addresses[i]));
        }
        j.arrayEnd();
    }

    private String classifyValue(Long value, MemoryBlock block) {
        if (value == null) {
            return "UNREADABLE";
        }
        if (value.longValue() == 0L) {
            return "NULL";
        }
        if (block == null) {
            return value.longValue() < 0x100000L ? "INTEGER_OR_OFFSET" : "UNMAPPED_VALUE";
        }
        if (block.isExecute()) {
            return "EXECUTABLE_POINTER";
        }
        String text = readAscii(addr(value.longValue()), 160);
        if (text != null) {
            return "STRING_POINTER";
        }
        return "POINTER_" + block.getName();
    }

    private Register firstRegister(Instruction instruction, int operandIndex) {
        Object[] objects = instruction.getOpObjects(operandIndex);
        for (Object object : objects) {
            if (object instanceof Register) {
                return (Register)object;
            }
        }
        return null;
    }

    private Long operandValue(Instruction instruction, int operandIndex) {
        Object[] objects = instruction.getOpObjects(operandIndex);
        Long scalar = null;
        for (Object object : objects) {
            if (object instanceof Address) {
                return Long.valueOf(((Address)object).getOffset());
            }
            if (object instanceof Scalar) {
                scalar = Long.valueOf(((Scalar)object).getSignedValue());
            }
        }
        return scalar;
    }

    private boolean writesFirstOperand(String mnemonic) {
        return !("CMP".equals(mnemonic) || "CMN".equals(mnemonic) || "TST".equals(mnemonic) ||
            mnemonic.startsWith("B") || "STR".equals(mnemonic) || "STP".equals(mnemonic));
    }

    private String registerName(Register register) {
        return register == null ? null : register.getName().toLowerCase();
    }

    private Function functionAt(long value) {
        return currentProgram.getFunctionManager().getFunctionAt(addr(value));
    }

    private Function functionContaining(long value) {
        return currentProgram.getFunctionManager().getFunctionContaining(addr(value));
    }

    private Address addr(long value) {
        return currentProgram.getAddressFactory().getDefaultAddressSpace().getAddress(value);
    }

    private String fmt(Address address) {
        return address == null ? null : hx(address.getOffset());
    }

    private String hx(long value) {
        return "0x" + Long.toHexString(value);
    }

    private String signedHex(long value) {
        return value < 0L ? "-0x" + Long.toHexString(-value) : "+0x" + Long.toHexString(value);
    }

    private String blockName(Address address) {
        MemoryBlock block = address == null ? null : memory.getBlock(address);
        return block == null ? null : block.getName();
    }

    private String readAscii(Address address, int max) {
        if (address == null || memory.getBlock(address) == null) {
            return null;
        }
        StringBuilder text = new StringBuilder();
        Address cursor = address;
        for (int i = 0; i < max; i++) {
            try {
                int value = memory.getByte(cursor) & 0xff;
                if (value == 0) {
                    break;
                }
                if (value < 0x20 || value > 0x7e) {
                    return text.length() >= 3 ? redact(text.toString()) : null;
                }
                text.append((char)value);
                cursor = cursor.addNoWrap(1);
            } catch (Exception e) {
                break;
            }
        }
        return text.length() == 0 ? null : redact(text.toString());
    }

    private String limitLines(String text, int maxLines) {
        String[] lines = text.split("\\R", -1);
        if (lines.length <= maxLines) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxLines; i++) {
            result.append(lines[i]).append('\n');
        }
        return result.append("/* TRUNCATED */").toString();
    }

    private String redact(String text) {
        if (text == null) {
            return null;
        }
        String result = text;
        result = result.replaceAll("(?i)(access_token|id_token|refresh_token|cookie|openid|session|ticket|token)([A-Za-z0-9_./=+:-]{8,})", "$1<REDACTED>");
        result = result.replaceAll("(?i)(m_strGcloudGameKey|m_strMidasSDKOfferId|m_strGCloudSDKOpenId)([A-Za-z0-9_./=+:-]{4,})?", "$1<REDACTED>");
        result = result.replaceAll("(?i)C:\\\\Users\\\\[A-Za-z0-9._-]+", "C:\\\\Users\\\\<REDACTED>");
        return result;
    }

    private void writeHeader(Json j) {
        j.prop("phase", "Phase7", true);
        j.prop("generated", LocalDate.now().toString(), true);
        j.prop("program", currentProgram.getName(), true);
        j.prop("mode", "-process libUE4.so -noanalysis -readOnly", true);
        j.prop("image_base", hx(IMAGE_BASE), true);
        j.prop("address_model", "GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000", true);
    }

    private PrintWriter openJson(String name) throws Exception {
        return new PrintWriter(new File(outputDir, name), StandardCharsets.UTF_8.name());
    }

    private void closeJson(PrintWriter out) {
        out.println();
        out.close();
    }

    private static class GraphNode {
        final Function function;
        final int depth;

        GraphNode(Function function, int depth) {
            this.function = function;
            this.depth = depth;
        }
    }

    private static class ResolvedInstruction {
        Long address;
        Long loadedValue;
    }

    private static class Json {
        private final PrintWriter out;
        private int depth;
        private final ArrayDeque<Boolean> pendingCommas = new ArrayDeque<Boolean>();
        private final ArrayDeque<Integer> pendingDepths = new ArrayDeque<Integer>();

        Json(PrintWriter out) {
            this.out = out;
        }

        void objStart() { out.print("{"); depth++; }
        void objEnd() { out.print("}"); depth--; finishComplex(); }
        void arrayStart() { out.print("["); depth++; }
        void arrayEnd() { out.print("]"); depth--; finishComplex(); }
        void comma() { out.print(","); }

        void propName(String key, boolean comma) {
            out.print(quote(key));
            out.print(":");
            pendingCommas.push(Boolean.valueOf(comma));
            pendingDepths.push(Integer.valueOf(depth));
        }

        void prop(String key, String value, boolean comma) {
            out.print(quote(key)); out.print(":"); out.print(quote(value)); if (comma) comma();
        }

        void prop(String key, long value, boolean comma) {
            out.print(quote(key)); out.print(":"); out.print(value); if (comma) comma();
        }

        void prop(String key, boolean value, boolean comma) {
            out.print(quote(key)); out.print(":"); out.print(value ? "true" : "false"); if (comma) comma();
        }

        void stringValue(String value) { out.print(quote(value)); finishComplex(); }
        void nullValue() { out.print("null"); finishComplex(); }

        private void finishComplex() {
            if (!pendingCommas.isEmpty() && pendingDepths.peek().intValue() == depth) {
                pendingDepths.pop();
                if (pendingCommas.pop().booleanValue()) comma();
            }
        }

        private static String quote(String value) {
            if (value == null) return "null";
            StringBuilder result = new StringBuilder("\"");
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                switch (c) {
                    case '\\': result.append("\\\\"); break;
                    case '"': result.append("\\\""); break;
                    case '\b': result.append("\\b"); break;
                    case '\f': result.append("\\f"); break;
                    case '\n': result.append("\\n"); break;
                    case '\r': result.append("\\r"); break;
                    case '\t': result.append("\\t"); break;
                    default:
                        if (c < 0x20) result.append(String.format("\\u%04x", (int)c)); else result.append(c);
                }
            }
            return result.append('"').toString();
        }
    }
}
