// Targeted Phase5 callback and server-list export for Apex Mobile libUE4.so.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.lang.Register;
import ghidra.program.model.listing.Function;
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

public class ApexPhase5Export extends GhidraScript {
    private static final long IMAGE_BASE = 0x100000L;
    private static final long REQUEST_BUILD = 0x6bc68e8L;
    private static final long CALLBACK_BINDER = 0x6bc6ca0L;
    private static final long REQUEST_NATIVE_THUNK = 0x7a31858L;
    private static final long RESPONSE_ADAPTER = 0x6be413cL;
    private static final long RESPONSE_HANDLER = 0x6be3bdcL;
    private static final long EVENT_EMITTER = 0x6be3f4cL;
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
            throw new IllegalArgumentException("Phase5 output directory argument is required");
        }

        File phase5Dir = new File(args[0]).getCanonicalFile();
        outputDir = new File(phase5Dir, "output");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase5 output directory");
        }

        memory = currentProgram.getMemory();
        listing = currentProgram.getListing();
        DecompileOptions options = new DecompileOptions();
        decompiler = new DecompInterface();
        decompiler.setOptions(options);
        decompiler.openProgram(currentProgram);

        try {
            validateProgram();
            writeRequestBuild();
            writeCallbackVtables();
            writeTargetedFlowSkeleton();
            println("PHASE5_TARGETED_EXPORT_OK");
            println(outputDir.getAbsolutePath());
        } finally {
            decompiler.dispose();
        }
    }

    private void validateProgram() {
        if (currentProgram.getImageBase().getOffset() != IMAGE_BASE) {
            throw new IllegalStateException("Unexpected image base: " + fmt(currentProgram.getImageBase()));
        }
        for (long target : new long[] { REQUEST_BUILD, CALLBACK_BINDER, REQUEST_NATIVE_THUNK,
                RESPONSE_ADAPTER, RESPONSE_HANDLER, EVENT_EMITTER, SYNC_PAYLOAD_THUNK }) {
            if (functionAt(target) == null) {
                throw new IllegalStateException("Missing target function at " + hx(target));
            }
        }
        for (long vtable : CALLBACK_VTABLES) {
            MemoryBlock block = memory.getBlock(addr(vtable));
            if (block == null) {
                throw new IllegalStateException("Unmapped callback vtable candidate " + hx(vtable));
            }
        }
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
        j.prop("phase", "Phase5", true);
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
