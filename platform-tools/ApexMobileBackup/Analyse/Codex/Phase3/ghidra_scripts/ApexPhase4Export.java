// Targeted Phase4 Unreal metadata export for Apex Mobile libUE4.so.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressOverflowException;
import ghidra.program.model.address.AddressSpace;
import ghidra.program.model.address.AddressSet;
import ghidra.program.model.lang.Register;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.listing.Listing;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.reloc.Relocation;
import ghidra.program.model.reloc.RelocationTable;
import ghidra.program.model.scalar.Scalar;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApexPhase4Export extends GhidraScript {
    private static final long IMAGE_BASE = 0x100000L;
    private static final long REQUEST_STRING = 0x22c409fL;
    private static final long[] REQUEST_SLOTS = new long[] {
        0xa9db6c8L, 0xa9e7b70L, 0xa9ecb10L
    };
    private static final long[] REQUEST_FUNCTIONS = new long[] {
        0x7a41d0cL, 0x7a41d4cL,
        0x7a31858L, 0x7a41d8cL,
        0x7a2e860L, 0x7a41dccL,
        0x51dba88L, 0x7a4198cL
    };
    private static final int WINDOW_RADIUS = 0x100;
    private static final long EVENT_STRING = 0x227dbf5L;
    private static final long EVENT_QUALIFIED_STRING = 0x227dbe3L;
    private static final long EVENT_TABLE_SLOT = 0xad25078L;
    private static final long GAMESERVER_STRING = 0x2280ba5L;
    private static final long[] GAMESERVER_SLOTS = new long[] {
        0xaf65f20L, 0xaf65f70L
    };
    private static final long[] GAMESERVER_REFERENCE_SLOTS = new long[] {
        0xaf66130L, 0xaf66140L, 0xaf66080L
    };
    private static final long SYNC_STRING = 0x231f64aL;
    private static final long[] SYNC_SLOTS = new long[] {
        0xac489d0L, 0xac48ac8L, 0xac48c20L,
        0xae87bf8L, 0xae87c18L, 0xae87c68L
    };
    private static final long[] LOGIN_STRINGS = new long[] {
        0x22cab3dL, 0x231434fL, 0x23d375dL,
        REQUEST_STRING, 0x236d0b2L, 0x2230708L
    };
    private static final String[] LOGIN_LABELS = new String[] {
        "PureClient/Login/LoginMgr.cpp", "ULoginMgrWrapper", "LoginMgrWrapper.cpp",
        "RequestAvatarServerList", "OpenServerList", "ServerListName"
    };

    private AddressSpace space;
    private Memory memory;
    private Listing listing;
    private RelocationTable relocationTable;
    private DecompInterface decompiler;
    private File phase4Dir;
    private File outputDir;
    private final Map<Long, List<Relocation>> windowRelocations = new HashMap<Long, List<Relocation>>();
    private final Map<Long, List<Long>> rawPointerIndex = new HashMap<Long, List<Long>>();
    private final Map<Long, List<Relocation>> targetRelocationIndex = new HashMap<Long, List<Relocation>>();
    private final Map<Long, List<CodeRef>> codeReferenceIndex = new HashMap<Long, List<CodeRef>>();
    private final List<CodeRef> eventValueReferences = new ArrayList<CodeRef>();
    private final List<CodeRef> gameServerOffsetReferences = new ArrayList<CodeRef>();

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase4 output directory argument is required");
        }
        phase4Dir = new File(args[0]).getCanonicalFile();
        outputDir = new File(phase4Dir, "output");
        outputDir.mkdirs();

        space = currentProgram.getAddressFactory().getDefaultAddressSpace();
        memory = currentProgram.getMemory();
        listing = currentProgram.getListing();
        relocationTable = currentProgram.getRelocationTable();

        DecompileOptions options = new DecompileOptions();
        decompiler = new DecompInterface();
        decompiler.setOptions(options);
        decompiler.openProgram(currentProgram);

        try {
            validateProgram();
            if (args.length > 1 && "runtime-probe".equals(args[1])) {
                writeRuntimeProbe();
                println("PHASE4_RUNTIME_PROBE_OK");
                return;
            }
            indexWindowRelocations();
            indexTargetReferences();
            writeRequestAvatarExport();
            writeRequestAvatarNativeExport();
            writeStructuredTargetExport("gameserverbackup_structure.json", "GameServerBackupIpList",
                GAMESERVER_STRING, GAMESERVER_SLOTS);
            writeStructuredTargetExport("syncpayload_structure.json", "SyncPayloadToGameServer",
                SYNC_STRING, SYNC_SLOTS);
            writeLoginExport();
            writeEventExport();
            writeHelpersExport();
            println("PHASE4_TARGETED_EXPORT_OK");
            println(outputDir.getAbsolutePath());
        } finally {
            decompiler.dispose();
        }
    }

    private void validateProgram() throws Exception {
        if (currentProgram.getImageBase().getOffset() != IMAGE_BASE) {
            throw new IllegalStateException("Unexpected image base: " + fmt(currentProgram.getImageBase()));
        }
        String request = readAscii(addr(REQUEST_STRING), 64);
        if (!"RequestAvatarServerList".equals(request)) {
            throw new IllegalStateException("RequestAvatarServerList mismatch at " + hx(REQUEST_STRING) + ": " + request);
        }
        for (long slot : REQUEST_SLOTS) {
            long value = memory.getLong(addr(slot));
            if (value != REQUEST_STRING) {
                throw new IllegalStateException("Unexpected request slot value at " + hx(slot) + ": " + hx(value));
            }
        }
    }

    private void indexWindowRelocations() {
        Iterator<Relocation> it = relocationTable.getRelocations();
        while (it.hasNext() && !monitor.isCancelled()) {
            Relocation relocation = it.next();
            Address relocationAddress = relocation.getAddress();
            if (relocationAddress == null) {
                continue;
            }
            long value = relocationAddress.getOffset();
            for (long center : allMetadataSlots()) {
                if (value >= center - WINDOW_RADIUS && value <= center + WINDOW_RADIUS) {
                    List<Relocation> entries = windowRelocations.get(Long.valueOf(value));
                    if (entries == null) {
                        entries = new ArrayList<Relocation>();
                        windowRelocations.put(Long.valueOf(value), entries);
                    }
                    entries.add(relocation);
                }
            }
        }
    }

    private long[] allMetadataSlots() {
        long[] all = new long[REQUEST_SLOTS.length + GAMESERVER_SLOTS.length +
            GAMESERVER_REFERENCE_SLOTS.length + SYNC_SLOTS.length + 1];
        int index = 0;
        for (long value : REQUEST_SLOTS) {
            all[index++] = value;
        }
        for (long value : GAMESERVER_SLOTS) {
            all[index++] = value;
        }
        for (long value : GAMESERVER_REFERENCE_SLOTS) {
            all[index++] = value;
        }
        for (long value : SYNC_SLOTS) {
            all[index++] = value;
        }
        all[index] = EVENT_TABLE_SLOT;
        return all;
    }

    private void indexTargetReferences() {
        LinkedHashSet<Long> targets = new LinkedHashSet<Long>();
        targets.add(Long.valueOf(REQUEST_STRING));
        targets.add(Long.valueOf(EVENT_STRING));
        targets.add(Long.valueOf(EVENT_QUALIFIED_STRING));
        targets.add(Long.valueOf(GAMESERVER_STRING));
        targets.add(Long.valueOf(SYNC_STRING));
        for (long value : LOGIN_STRINGS) {
            targets.add(Long.valueOf(value));
        }
        for (long value : REQUEST_FUNCTIONS) {
            targets.add(Long.valueOf(value));
        }
        for (long value : new long[] {
                0x7a31858L, 0x7a41d8cL, 0x41b178cL, 0x48e8b24L, 0x4763790L,
                0xa9e7ab0L, 0xa9e7b60L, 0xa9e7cd8L }) {
            targets.add(Long.valueOf(value));
        }
        for (long slot : allMetadataSlots()) {
            targets.add(Long.valueOf(slot));
            for (long delta : new long[] { -16L, -8L, 8L, 16L }) {
                try {
                    long value = memory.getLong(addr(slot + delta));
                    MemoryBlock block = memory.getBlock(addr(value));
                    if (block != null && block.isExecute()) {
                        targets.add(Long.valueOf(value));
                    }
                } catch (Exception e) {
                    // Ignore unreadable neighboring data.
                }
            }
        }
        indexRawPointers(targets);
        indexTargetRelocations(targets);
        indexCodeReferences(targets);
    }

    private void indexRawPointers(Set<Long> targets) {
        for (MemoryBlock block : memory.getBlocks()) {
            String name = block.getName();
            if (!(".data.rel.ro".equals(name) || ".data".equals(name) || ".got".equals(name) ||
                    ".got.plt".equals(name))) {
                continue;
            }
            Address current = block.getStart();
            Address end;
            try {
                end = block.getEnd().subtractNoWrap(7);
            } catch (AddressOverflowException e) {
                continue;
            }
            while (current.compareTo(end) <= 0 && !monitor.isCancelled()) {
                try {
                    long value = memory.getLong(current);
                    Long key = Long.valueOf(value);
                    if (targets.contains(key)) {
                        List<Long> slots = rawPointerIndex.get(key);
                        if (slots == null) {
                            slots = new ArrayList<Long>();
                            rawPointerIndex.put(key, slots);
                        }
                        slots.add(Long.valueOf(current.getOffset()));
                    }
                    current = current.addNoWrap(8);
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    private void indexTargetRelocations(Set<Long> targets) {
        Iterator<Relocation> it = relocationTable.getRelocations();
        while (it.hasNext() && !monitor.isCancelled()) {
            Relocation relocation = it.next();
            Address slot = relocation.getAddress();
            if (slot == null) {
                continue;
            }
            try {
                long value = memory.getLong(slot);
                Long key = Long.valueOf(value);
                if (targets.contains(key)) {
                    List<Relocation> entries = targetRelocationIndex.get(key);
                    if (entries == null) {
                        entries = new ArrayList<Relocation>();
                        targetRelocationIndex.put(key, entries);
                    }
                    entries.add(relocation);
                }
            } catch (Exception e) {
                // Ignore relocations whose patched slot cannot be read as uint64.
            }
        }
    }

    private void indexCodeReferences(Set<Long> targets) {
        MemoryBlock text = memory.getBlock(".text");
        if (text == null) {
            return;
        }
        InstructionIterator instructions = listing.getInstructions(
            new AddressSet(text.getStart(), text.getEnd()), true);
        Map<String, Long> registers = new HashMap<String, Long>();
        Function previous = null;
        while (instructions.hasNext() && !monitor.isCancelled()) {
            Instruction instruction = instructions.next();
            Function containing = currentProgram.getFunctionManager().getFunctionContaining(instruction.getAddress());
            if (containing != previous) {
                registers.clear();
                previous = containing;
            }
            String mnemonic = instruction.getMnemonicString().toUpperCase();
            if ("BL".equals(mnemonic) || "B".equals(mnemonic)) {
                for (Address flow : instruction.getFlows()) {
                    if (targets.contains(Long.valueOf(flow.getOffset()))) {
                        addCodeReference(flow.getOffset(), instruction, containing, "DIRECT_BRANCH", flow.getOffset());
                    }
                }
            }
            ResolvedInstruction resolved = resolveInstruction(instruction, registers);
            if (resolved.address != null && targets.contains(resolved.address)) {
                addCodeReference(resolved.address.longValue(), instruction, containing,
                    "DIRECT_ADDRESS_MATERIALIZATION", resolved.address.longValue());
            }
            if (resolved.loadedValue != null && targets.contains(resolved.loadedValue)) {
                addCodeReference(resolved.loadedValue.longValue(), instruction, containing,
                    "INDIRECT_POINTER_LOAD", resolved.address == null ? 0L : resolved.address.longValue());
            }
            if (hasScalar(instruction, 0x138L) &&
                    ("CMP".equals(mnemonic) || "CMN".equals(mnemonic) || mnemonic.startsWith("MOV"))) {
                addStandaloneCodeReference(eventValueReferences, instruction, containing,
                    "EVENT_VALUE_IMMEDIATE_CANDIDATE", 0x138L);
            }
            if (hasScalar(instruction, 0x150L) &&
                    (mnemonic.startsWith("LDR") || mnemonic.startsWith("LDUR") ||
                     mnemonic.startsWith("STR") || mnemonic.startsWith("STUR"))) {
                addStandaloneCodeReference(gameServerOffsetReferences, instruction, containing,
                    mnemonic.startsWith("ST") ? "PROPERTY_OFFSET_WRITE_CANDIDATE" :
                        "PROPERTY_OFFSET_READ_CANDIDATE", 0x150L);
            }
        }
    }

    private boolean hasScalar(Instruction instruction, long expected) {
        for (int operand = 0; operand < instruction.getNumOperands(); operand++) {
            for (Object object : instruction.getOpObjects(operand)) {
                if (object instanceof Scalar && ((Scalar)object).getUnsignedValue() == expected) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addStandaloneCodeReference(List<CodeRef> refs, Instruction instruction,
            Function function, String kind, long resolvedAddress) {
        if (refs.size() >= 1000) {
            return;
        }
        CodeRef ref = new CodeRef();
        ref.address = instruction.getAddress().getOffset();
        ref.kind = kind;
        ref.instruction = instruction.toString();
        ref.resolvedAddress = resolvedAddress;
        ref.function = function;
        refs.add(ref);
    }

    private void addCodeReference(long target, Instruction instruction, Function function, String kind,
            long resolvedAddress) {
        Long key = Long.valueOf(target);
        List<CodeRef> refs = codeReferenceIndex.get(key);
        if (refs == null) {
            refs = new ArrayList<CodeRef>();
            codeReferenceIndex.put(key, refs);
        }
        for (CodeRef ref : refs) {
            if (ref.address == instruction.getAddress().getOffset() && ref.kind.equals(kind)) {
                return;
            }
        }
        CodeRef ref = new CodeRef();
        ref.address = instruction.getAddress().getOffset();
        ref.kind = kind;
        ref.instruction = instruction.toString();
        ref.resolvedAddress = resolvedAddress;
        ref.function = function;
        refs.add(ref);
    }

    private static class CodeRef {
        long address;
        String kind;
        String instruction;
        long resolvedAddress;
        Function function;
    }

    private void writeRequestAvatarExport() throws Exception {
        File file = new File(outputDir, "requestavatar_table_layout.json");
        PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8.name());
        Json j = new Json(out);
        j.objStart();
        j.prop("phase", "Phase4", true);
        j.prop("generated", LocalDate.now().toString(), true);
        j.prop("program", currentProgram.getName(), true);
        j.prop("mode", "-process libUE4.so -noanalysis -readOnly", true);
        j.prop("image_base", hx(IMAGE_BASE), true);
        j.prop("address_model", "GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000", true);
        j.prop("request_string_address", hx(REQUEST_STRING), true);
        j.prop("request_string", readAscii(addr(REQUEST_STRING), 64), true);
        j.propName("metadata_windows", true);
        j.arrayStart();
        for (int i = 0; i < REQUEST_SLOTS.length; i++) {
            if (i > 0) {
                j.comma();
            }
            writeWindow(j, REQUEST_SLOTS[i]);
        }
        j.arrayEnd();
        j.propName("corrected_functions", false);
        j.arrayStart();
        for (int i = 0; i < REQUEST_FUNCTIONS.length; i++) {
            if (i > 0) {
                j.comma();
            }
            Function function = currentProgram.getFunctionManager().getFunctionAt(addr(REQUEST_FUNCTIONS[i]));
            writeFunction(j, function, 0, new HashSet<Long>());
        }
        j.arrayEnd();
        j.objEnd();
        out.println();
        out.close();
    }

    private void writeRequestAvatarNativeExport() throws Exception {
        PrintWriter out = openJson("requestavatar_native.json");
        Json j = new Json(out);
        j.objStart();
        writeCommonHeader(j);
        j.prop("target", "RequestAvatarServerList", true);
        j.propName("native_registration_pair", true);
        j.objStart();
        j.prop("classification", "FNameNativePtrPair-like native registration entry", true);
        j.prop("name_slot", "0xa9db6c8", true);
        j.prop("function_slot", "0xa9db6d0", true);
        j.prop("function_pointer", "0x7a31858", true);
        j.prop("status", "CONFIRMED", false);
        j.objEnd();
        j.propName("generated_function_info", true);
        j.objStart();
        j.prop("classification", "generated UFunction constructor/name entry", true);
        j.prop("constructor_slot", "0xa9ecb08", true);
        j.prop("constructor_pointer", "0x7a41d4c", true);
        j.prop("name_slot", "0xa9ecb10", true);
        j.prop("status", "CONFIRMED", false);
        j.objEnd();
        j.propName("function_params_descriptor", true);
        j.objStart();
        j.prop("descriptor", "0xa9e7b60", true);
        j.prop("name_field", "0xa9e7b70", true);
        j.prop("constructor", "0x7a41d4c", true);
        j.prop("status", "PROBABLE_FFUNCTIONPARAMS", false);
        j.objEnd();
        j.propName("target_evidence", true);
        writeTargetEvidence(j, REQUEST_STRING);
        j.propName("native_function_evidence", true);
        writeTargetEvidence(j, 0x7a31858L);
        j.propName("functions", false);
        j.arrayStart();
        long[] functions = new long[] {
            0x7a31858L, 0x7a41d4cL, 0x7a41d0cL, 0x7a41d8cL,
            0x7a285f0L, 0x7a37dc0L, 0x59478dcL
        };
        for (int i = 0; i < functions.length; i++) {
            if (i > 0) {
                j.comma();
            }
            writeFunction(j, currentProgram.getFunctionManager().getFunctionAt(addr(functions[i])), 0,
                new HashSet<Long>());
        }
        j.arrayEnd();
        j.objEnd();
        closeJson(out);
    }

    private void writeRuntimeProbe() throws Exception {
        PrintWriter out = openJson("requestavatar_runtime_probe.json");
        Json j = new Json(out);
        j.objStart();
        writeCommonHeader(j);
        j.prop("source_thunk", "0x7a31858", true);
        j.prop("direct_runtime_callee", "0x6bc68e8", true);
        j.propName("function", false);
        writeFunction(j, currentProgram.getFunctionManager().getFunctionAt(addr(0x6bc68e8L)), 0,
            new HashSet<Long>());
        j.objEnd();
        closeJson(out);
    }

    private void writeStructuredTargetExport(String fileName, String label, long stringAddress,
            long[] slots) throws Exception {
        PrintWriter out = openJson(fileName);
        Json j = new Json(out);
        j.objStart();
        writeCommonHeader(j);
        j.prop("target", label, true);
        j.prop("string_address", hx(stringAddress), true);
        j.prop("string", readAscii(addr(stringAddress), 180), true);
        j.propName("target_evidence", true);
        writeTargetEvidence(j, stringAddress);
        j.propName("metadata_windows", true);
        j.arrayStart();
        for (int i = 0; i < slots.length; i++) {
            if (i > 0) {
                j.comma();
            }
            writeWindow(j, slots[i]);
        }
        j.arrayEnd();
        j.propName("adjacent_text_candidates", false);
        writeAdjacentTextCandidates(j, slots);
        if ("GameServerBackupIpList".equals(label)) {
            j.comma();
            j.propName("property_offset_access_candidates", true);
            writeCodeReferences(j, gameServerOffsetReferences);
            j.propName("descriptor_reference_windows", false);
            j.arrayStart();
            for (int i = 0; i < GAMESERVER_REFERENCE_SLOTS.length; i++) {
                if (i > 0) {
                    j.comma();
                }
                writeWindow(j, GAMESERVER_REFERENCE_SLOTS[i]);
            }
            j.arrayEnd();
        }
        if ("SyncPayloadToGameServer".equals(label)) {
            j.comma();
            j.propName("candidate_functions", false);
            j.arrayStart();
            long[] candidates = new long[] {
                0xa220f70L, 0x7d14e48L, 0x7fb81ccL, 0x7fb8344L,
                0x7fb8384L, 0x7d14d84L, 0x7d14b34L
            };
            for (int i = 0; i < candidates.length; i++) {
                if (i > 0) {
                    j.comma();
                }
                writeFunction(j, currentProgram.getFunctionManager().getFunctionAt(addr(candidates[i])), 0,
                    new HashSet<Long>());
            }
            j.arrayEnd();
        }
        j.objEnd();
        closeJson(out);
    }

    private void writeLoginExport() throws Exception {
        PrintWriter out = openJson("loginmgr_path.json");
        Json j = new Json(out);
        j.objStart();
        writeCommonHeader(j);
        j.propName("strings", false);
        j.arrayStart();
        for (int i = 0; i < LOGIN_STRINGS.length; i++) {
            if (i > 0) {
                j.comma();
            }
            j.objStart();
            j.prop("label", LOGIN_LABELS[i], true);
            j.prop("address", hx(LOGIN_STRINGS[i]), true);
            j.prop("ascii", readAscii(addr(LOGIN_STRINGS[i]), 180), true);
            j.propName("evidence", false);
            writeTargetEvidence(j, LOGIN_STRINGS[i]);
            j.objEnd();
        }
        j.arrayEnd();
        j.objEnd();
        closeJson(out);
    }

    private void writeEventExport() throws Exception {
        PrintWriter out = openJson("avatar_event_table.json");
        Json j = new Json(out);
        j.objStart();
        writeCommonHeader(j);
        j.prop("target_substring_address", hx(EVENT_STRING), true);
        j.prop("qualified_string_address", hx(EVENT_QUALIFIED_STRING), true);
        j.prop("qualified_string", readAscii(addr(EVENT_QUALIFIED_STRING), 180), true);
        j.propName("target_substring_evidence", true);
        writeTargetEvidence(j, EVENT_STRING);
        j.propName("qualified_string_evidence", true);
        writeTargetEvidence(j, EVENT_QUALIFIED_STRING);
        j.propName("event_table_window", true);
        writeWindow(j, EVENT_TABLE_SLOT);
        j.propName("numeric_value_references", true);
        writeCodeReferences(j, eventValueReferences);
        j.propName("qualified_event_strings", false);
        j.arrayStart();
        List<Address> events = findAsciiOccurrences("ELuaCppEventType::", 2000);
        for (int i = 0; i < events.size(); i++) {
            if (i > 0) {
                j.comma();
            }
            Address event = events.get(i);
            j.objStart();
            j.prop("address", fmt(event), true);
            j.prop("elf_virtual_address", hx(event.getOffset() - IMAGE_BASE), true);
            j.prop("name", readAscii(event, 180), false);
            j.objEnd();
        }
        j.arrayEnd();
        j.objEnd();
        closeJson(out);
    }

    private void writeHelpersExport() throws Exception {
        PrintWriter out = openJson("unreal_helpers.json");
        Json j = new Json(out);
        j.objStart();
        writeCommonHeader(j);
        j.propName("helpers", false);
        j.arrayStart();
        long[] helpers = new long[] { 0x41b178cL, 0x48e8b24L, 0x4763790L };
        for (int i = 0; i < helpers.length; i++) {
            if (i > 0) {
                j.comma();
            }
            writeFunction(j, currentProgram.getFunctionManager().getFunctionAt(addr(helpers[i])), 0,
                new HashSet<Long>());
        }
        j.arrayEnd();
        j.objEnd();
        closeJson(out);
    }

    private void writeCommonHeader(Json j) {
        j.prop("phase", "Phase4", true);
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

    private void writeAdjacentTextCandidates(Json j, long[] slots) {
        LinkedHashSet<Long> candidates = new LinkedHashSet<Long>();
        for (long slot : slots) {
            for (long delta : new long[] { -16L, -8L, 8L, 16L }) {
                try {
                    long value = memory.getLong(addr(slot + delta));
                    MemoryBlock block = memory.getBlock(addr(value));
                    if (block != null && block.isExecute()) {
                        candidates.add(Long.valueOf(value));
                    }
                } catch (Exception e) {
                    // Best-effort classification of neighboring fields.
                }
            }
        }
        j.arrayStart();
        int index = 0;
        for (Long candidate : candidates) {
            if (index > 0) {
                j.comma();
            }
            j.objStart();
            j.prop("address", hx(candidate.longValue()), true);
            j.propName("function", true);
            writeFunctionSummary(j, currentProgram.getFunctionManager().getFunctionAt(addr(candidate.longValue())));
            j.propName("evidence", false);
            writeTargetEvidence(j, candidate.longValue());
            j.objEnd();
            index++;
        }
        j.arrayEnd();
    }

    private void writeTargetEvidence(Json j, long target) {
        j.objStart();
        j.prop("target", hx(target), true);
        j.prop("block", blockName(addr(target)), true);
        j.prop("ascii", readAscii(addr(target), 180), true);
        j.propName("ghidra_xrefs", true);
        writeReferences(j, currentProgram.getReferenceManager().getReferencesTo(addr(target)), 100);
        j.propName("direct_code_references", true);
        writeCodeReferences(j, codeReferenceIndex.get(Long.valueOf(target)));
        j.propName("raw_function_or_data_pointers", true);
        writeRawPointerLocations(j, target);
        j.propName("relocation_function_or_data_pointers", false);
        writeRelocations(j, targetRelocationIndex.get(Long.valueOf(target)));
        j.objEnd();
    }

    private void writeWindow(Json j, long center) {
        j.objStart();
        j.prop("center", hx(center), true);
        j.prop("center_elf_virtual_address", hx(center - IMAGE_BASE), true);
        j.prop("range_start", hx(center - WINDOW_RADIUS), true);
        j.prop("range_end", hx(center + WINDOW_RADIUS), true);
        j.prop("stride", 8, true);
        j.propName("slots", false);
        j.arrayStart();
        int item = 0;
        for (long slot = center - WINDOW_RADIUS; slot <= center + WINDOW_RADIUS; slot += 8) {
            if (item > 0) {
                j.comma();
            }
            writeSlot(j, center, slot);
            item++;
        }
        j.arrayEnd();
        j.objEnd();
    }

    private void writeSlot(Json j, long center, long slotOffset) {
        Address slot = addr(slotOffset);
        long value = 0L;
        String error = null;
        try {
            value = memory.getLong(slot);
        } catch (Exception e) {
            error = e.toString();
        }
        Address target = error == null ? addr(value) : null;
        MemoryBlock targetBlock = target == null ? null : memory.getBlock(target);

        j.objStart();
        j.prop("relative_offset", signedHex(slotOffset - center), true);
        j.prop("slot", hx(slotOffset), true);
        j.prop("slot_elf_virtual_address", hx(slotOffset - IMAGE_BASE), true);
        j.prop("slot_block", blockName(slot), true);
        j.prop("value", error == null ? hx(value) : null, true);
        j.prop("read_error", error, true);
        j.prop("classification", classifyValue(value, targetBlock, error), true);
        j.prop("target_block", targetBlock == null ? null : targetBlock.getName(), true);
        j.prop("target_ascii", targetBlock != null && ".rodata".equals(targetBlock.getName()) ? readAscii(target, 180) : null, true);
        j.prop("target_instruction", instructionText(target), true);
        j.propName("function_at_target", true);
        writeFunctionSummary(j, target == null ? null : currentProgram.getFunctionManager().getFunctionAt(target));
        j.propName("function_containing_target", true);
        writeFunctionSummary(j, target == null ? null : currentProgram.getFunctionManager().getFunctionContaining(target));
        j.prop("recursive_value", recursiveValue(target, targetBlock), true);
        j.propName("relocations", true);
        writeRelocations(j, windowRelocations.get(Long.valueOf(slotOffset)));
        j.prop("ghidra_xrefs_to_slot", countReferences(currentProgram.getReferenceManager().getReferencesTo(slot), 200), false);
        j.objEnd();
    }

    private String classifyValue(long value, MemoryBlock block, String error) {
        if (error != null) {
            return "UNREADABLE";
        }
        if (value == 0L) {
            return "NULL";
        }
        if (block == null) {
            return value < 0x100000L ? "INTEGER_OR_FLAGS" : "UNMAPPED_VALUE";
        }
        String name = block.getName();
        if (".rodata".equals(name) || ".dynstr".equals(name)) {
            return readAscii(addr(value), 180) == null ? "RODATA_POINTER" : "STRING_POINTER";
        }
        if (block.isExecute()) {
            return "TEXT_POINTER";
        }
        if (".data.rel.ro".equals(name)) {
            return "DATA_REL_RO_POINTER";
        }
        if (".data".equals(name) || ".bss".equals(name)) {
            return "DATA_POINTER";
        }
        return "POINTER_" + name;
    }

    private String recursiveValue(Address target, MemoryBlock block) {
        if (target == null || block == null || !".data.rel.ro".equals(block.getName())) {
            return null;
        }
        try {
            long nested = memory.getLong(target);
            Address nestedAddress = addr(nested);
            MemoryBlock nestedBlock = memory.getBlock(nestedAddress);
            String text = nestedBlock == null ? null : readAscii(nestedAddress, 120);
            return hx(nested) + (nestedBlock == null ? "" : " -> " + nestedBlock.getName()) +
                (text == null ? "" : " ascii:" + text);
        } catch (Exception e) {
            return null;
        }
    }

    private void writeRelocations(Json j, List<Relocation> relocations) {
        j.arrayStart();
        if (relocations != null) {
            for (int i = 0; i < relocations.size(); i++) {
                if (i > 0) {
                    j.comma();
                }
                Relocation relocation = relocations.get(i);
                j.objStart();
                j.prop("type", relocation.getType(), true);
                j.prop("type_name", relocationTypeName(relocation.getType()), true);
                j.prop("status", relocation.getStatus() == null ? null : relocation.getStatus().toString(), true);
                j.prop("symbol", relocation.getSymbolName(), true);
                j.propName("values", false);
                j.arrayStart();
                long[] values = relocation.getValues();
                if (values != null) {
                    for (int k = 0; k < values.length; k++) {
                        if (k > 0) {
                            j.comma();
                        }
                        j.stringValue(hx(values[k]));
                    }
                }
                j.arrayEnd();
                j.objEnd();
            }
        }
        j.arrayEnd();
    }

    private void writeRawPointerLocations(Json j, long target) {
        j.arrayStart();
        List<Long> slots = rawPointerIndex.get(Long.valueOf(target));
        if (slots != null) {
            int limit = Math.min(slots.size(), 200);
            for (int i = 0; i < limit; i++) {
                if (i > 0) {
                    j.comma();
                }
                long slot = slots.get(i).longValue();
                j.objStart();
                j.prop("kind", "RAW_POINTER_REFERENCE", true);
                j.prop("slot", hx(slot), true);
                j.prop("slot_block", blockName(addr(slot)), true);
                j.prop("value", hx(target), false);
                j.objEnd();
            }
        }
        j.arrayEnd();
    }

    private void writeCodeReferences(Json j, List<CodeRef> references) {
        j.arrayStart();
        if (references != null) {
            int limit = Math.min(references.size(), 200);
            for (int i = 0; i < limit; i++) {
                if (i > 0) {
                    j.comma();
                }
                CodeRef ref = references.get(i);
                j.objStart();
                j.prop("kind", ref.kind, true);
                j.prop("address", hx(ref.address), true);
                j.prop("elf_virtual_address", hx(ref.address - IMAGE_BASE), true);
                j.prop("instruction", ref.instruction, true);
                j.prop("resolved_address", ref.resolvedAddress == 0L ? null : hx(ref.resolvedAddress), true);
                j.propName("function_containing", false);
                writeFunctionSummary(j, ref.function);
                j.objEnd();
            }
        }
        j.arrayEnd();
    }

    private void writeReferences(Json j, ReferenceIterator references, int max) {
        j.arrayStart();
        int count = 0;
        while (references.hasNext() && count < max) {
            Reference reference = references.next();
            if (count > 0) {
                j.comma();
            }
            j.objStart();
            j.prop("kind", "GHIDRA_XREF", true);
            j.prop("from", fmt(reference.getFromAddress()), true);
            j.prop("to", fmt(reference.getToAddress()), true);
            j.prop("type", reference.getReferenceType().toString(), true);
            j.propName("function_containing", false);
            writeFunctionSummary(j, currentProgram.getFunctionManager().getFunctionContaining(reference.getFromAddress()));
            j.objEnd();
            count++;
        }
        j.arrayEnd();
    }

    private List<Address> findAsciiOccurrences(String text, int max) {
        List<Address> result = new ArrayList<Address>();
        byte[] needle = text.getBytes(StandardCharsets.UTF_8);
        Address start = memory.getMinAddress();
        while (start != null && result.size() < max && !monitor.isCancelled()) {
            Address found = memory.findBytes(start, memory.getMaxAddress(), needle, null, true,
                TaskMonitor.DUMMY);
            if (found == null) {
                break;
            }
            result.add(found);
            try {
                start = found.addNoWrap(1);
            } catch (AddressOverflowException e) {
                break;
            }
        }
        return result;
    }

    private void writeFunction(Json j, Function function, int depth, Set<Long> visited) {
        if (function == null) {
            j.nullValue();
            return;
        }
        long entry = function.getEntryPoint().getOffset();
        boolean repeated = visited.contains(Long.valueOf(entry));
        visited.add(Long.valueOf(entry));

        j.objStart();
        j.prop("name", function.getName(), true);
        j.prop("entry", hx(entry), true);
        j.prop("entry_elf_virtual_address", hx(entry - IMAGE_BASE), true);
        j.prop("body_min", fmt(function.getBody().getMinAddress()), true);
        j.prop("body_max", fmt(function.getBody().getMaxAddress()), true);
        j.prop("signature", function.getSignature().toString(), true);
        j.prop("repeated", repeated, true);
        j.propName("disassembly", true);
        writeDisassembly(j, function);
        j.propName("decompile", true);
        writeDecompile(j, function);
        j.propName("ghidra_callers", true);
        writeFunctionSet(j, safeCallingFunctions(function));
        j.propName("ghidra_callees", true);
        writeFunctionSet(j, safeCalledFunctions(function));
        j.propName("callgraph", false);
        j.arrayStart();
        if (!repeated && depth > 0) {
            int count = 0;
            for (Function callee : safeCalledFunctions(function)) {
                if (count > 0) {
                    j.comma();
                }
                writeFunction(j, callee, depth - 1, visited);
                count++;
                if (count >= 16) {
                    break;
                }
            }
        }
        j.arrayEnd();
        j.objEnd();
    }

    private void writeDisassembly(Json j, Function function) {
        j.arrayStart();
        Instruction instruction = listing.getInstructionAt(function.getBody().getMinAddress());
        Address end = function.getBody().getMaxAddress();
        Map<String, Long> registers = new HashMap<String, Long>();
        int count = 0;
        while (instruction != null && instruction.getAddress().compareTo(end) <= 0 && count < 1000) {
            if (count > 0) {
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
            j.propName("flows", false);
            j.arrayStart();
            Address[] flows = instruction.getFlows();
            for (int i = 0; i < flows.length; i++) {
                if (i > 0) {
                    j.comma();
                }
                j.stringValue(fmt(flows[i]));
            }
            j.arrayEnd();
            j.objEnd();
            instruction = instruction.getNext();
            count++;
        }
        j.arrayEnd();
    }

    private static class ResolvedInstruction {
        Long address;
        Long loadedValue;
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

    private void writeDecompile(Json j, Function function) {
        j.objStart();
        try {
            DecompileResults results = decompiler.decompileFunction(function, 30, TaskMonitor.DUMMY);
            boolean completed = results.decompileCompleted();
            j.prop("completed", completed, true);
            j.prop("error", completed ? null : results.getErrorMessage(), true);
            String c = completed ? results.getDecompiledFunction().getC() : null;
            j.prop("c", c == null ? null : limitLines(redact(c), 300), false);
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
            // Best-effort export.
        }
        return result;
    }

    private Set<Function> safeCallingFunctions(Function function) {
        Set<Function> result = new LinkedHashSet<Function>();
        try {
            result.addAll(function.getCallingFunctions(TaskMonitor.DUMMY));
        } catch (Exception e) {
            // Best-effort export.
        }
        return result;
    }

    private void writeFunctionSet(Json j, Set<Function> functions) {
        j.arrayStart();
        int count = 0;
        for (Function function : functions) {
            if (count > 0) {
                j.comma();
            }
            writeFunctionSummary(j, function);
            count++;
            if (count >= 100) {
                break;
            }
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
        j.prop("body_min", fmt(function.getBody().getMinAddress()), true);
        j.prop("body_max", fmt(function.getBody().getMaxAddress()), false);
        j.objEnd();
    }

    private int countReferences(ReferenceIterator references, int max) {
        int count = 0;
        while (references.hasNext() && count < max) {
            references.next();
            count++;
        }
        return count;
    }

    private String instructionText(Address address) {
        if (address == null) {
            return null;
        }
        Instruction instruction = listing.getInstructionAt(address);
        return instruction == null ? null : instruction.toString();
    }

    private String readAscii(Address address, int max) {
        if (address == null || memory.getBlock(address) == null) {
            return null;
        }
        StringBuilder text = new StringBuilder();
        Address current = address;
        for (int i = 0; i < max; i++) {
            try {
                int value = memory.getByte(current) & 0xff;
                if (value == 0) {
                    break;
                }
                if (value < 0x20 || value > 0x7e) {
                    return text.length() >= 3 ? redact(text.toString()) : null;
                }
                text.append((char)value);
                current = current.addNoWrap(1);
            } catch (Exception e) {
                break;
            }
        }
        return text.length() == 0 ? null : redact(text.toString());
    }

    private String relocationTypeName(int type) {
        switch (type) {
            case 257:
                return "R_AARCH64_ABS64";
            case 1025:
                return "R_AARCH64_GLOB_DAT";
            case 1026:
                return "R_AARCH64_JUMP_SLOT";
            case 1027:
                return "R_AARCH64_RELATIVE";
            case 1032:
                return "R_AARCH64_IRELATIVE";
            default:
                return "AARCH64_TYPE_" + type;
        }
    }

    private Address addr(long value) {
        return space.getAddress(value);
    }

    private String blockName(Address address) {
        MemoryBlock block = address == null ? null : memory.getBlock(address);
        return block == null ? null : block.getName();
    }

    private String fmt(Address address) {
        return address == null ? null : hx(address.getOffset());
    }

    private String hx(long value) {
        return "0x" + Long.toHexString(value);
    }

    private String signedHex(long value) {
        return value < 0 ? "-0x" + Long.toHexString(-value) : "+0x" + Long.toHexString(value);
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
        result.append("/* TRUNCATED: ").append(lines.length - maxLines).append(" lines */");
        return result.toString();
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

    private static class Json {
        private final PrintWriter out;
        private int depth;
        private final ArrayDeque<Boolean> pendingCommas = new ArrayDeque<Boolean>();
        private final ArrayDeque<Integer> pendingDepths = new ArrayDeque<Integer>();

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
            finishComplex();
        }

        void arrayStart() {
            out.print("[");
            depth++;
        }

        void arrayEnd() {
            out.print("]");
            depth--;
            finishComplex();
        }

        void comma() {
            out.print(",");
        }

        void propName(String key, boolean comma) {
            out.print(quote(key));
            out.print(":");
            pendingCommas.push(Boolean.valueOf(comma));
            pendingDepths.push(Integer.valueOf(depth));
        }

        void prop(String key, String value, boolean comma) {
            out.print(quote(key));
            out.print(":");
            out.print(quote(value));
            if (comma) {
                comma();
            }
        }

        void prop(String key, int value, boolean comma) {
            out.print(quote(key));
            out.print(":");
            out.print(value);
            if (comma) {
                comma();
            }
        }

        void prop(String key, boolean value, boolean comma) {
            out.print(quote(key));
            out.print(":");
            out.print(value ? "true" : "false");
            if (comma) {
                comma();
            }
        }

        void stringValue(String value) {
            out.print(quote(value));
            finishComplex();
        }

        void nullValue() {
            out.print("null");
            finishComplex();
        }

        private void finishComplex() {
            if (!pendingCommas.isEmpty() && pendingDepths.peek().intValue() == depth) {
                pendingDepths.pop();
                boolean comma = pendingCommas.pop().booleanValue();
                if (comma) {
                    comma();
                }
            }
        }

        private static String quote(String value) {
            if (value == null) {
                return "null";
            }
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
                        if (c < 0x20) {
                            result.append(String.format("\\u%04x", (int)c));
                        } else {
                            result.append(c);
                        }
                }
            }
            return result.append('"').toString();
        }
    }
}
