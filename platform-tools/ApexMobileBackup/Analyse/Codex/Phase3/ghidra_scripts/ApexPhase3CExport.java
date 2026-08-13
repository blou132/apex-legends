// Phase3C rebased address model and targeted export for Apex Mobile libUE4.so.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressOverflowException;
import ghidra.program.model.address.AddressSpace;
import ghidra.program.model.listing.Data;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.Listing;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryAccessException;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.reloc.Relocation;
import ghidra.program.model.reloc.RelocationTable;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApexPhase3CExport extends GhidraScript {
    private static final long EXPECTED_IMAGE_BASE = 0x100000L;

    private AddressSpace space;
    private Memory memory;
    private Listing listing;
    private RelocationTable relocationTable;
    private DecompInterface decompiler;
    private File phase3cDir;
    private File outputDir;
    private long imageBase;
    private List<MapCheck> addressChecks;
    private boolean mappingConfirmed;
    private final Map<String, List<RawPointerRef>> rawPointerCache = new LinkedHashMap<String, List<RawPointerRef>>();
    private final Map<String, List<RelocInfo>> relocationCache = new LinkedHashMap<String, List<RelocInfo>>();
    private final Map<String, List<Address>> byteSearchCache = new LinkedHashMap<String, List<Address>>();

    private static class LoadSegment {
        final long offset;
        final long vaddr;
        final long filesz;

        LoadSegment(long offset, long vaddr, long filesz) {
            this.offset = offset;
            this.vaddr = vaddr;
            this.filesz = filesz;
        }
    }

    // From Phase3/output/elf_libUE4_summary.json. Used only for explicit VA/file offset reporting.
    private static final LoadSegment[] LOADS = new LoadSegment[] {
        new LoadSegment(0x0L, 0x0L, 0x39593dcL),
        new LoadSegment(0x39593e0L, 0x395a3e0L, 0x6a68040L),
        new LoadSegment(0xa3c1420L, 0xa3c3420L, 0xfbba40L),
        new LoadSegment(0xb37ce60L, 0xb37fe60L, 0x1db167L),
        new LoadSegment(0xb559000L, 0xb8c4000L, 0x2700L),
        new LoadSegment(0xb55b000L, 0xb8c7000L, 0x15b8L),
        new LoadSegment(0xb55c000L, 0xb8cc000L, 0x56f0L)
    };

    private static class MapCheck {
        final String label;
        final String kind;
        final long elfVa;
        final String expectedSection;
        final String expectedText;
        long ghidraAddress;
        String fileOffset;
        String directGhidraBlock;
        String ghidraBlock;
        String content;
        boolean match;

        MapCheck(String label, String kind, long elfVa, String expectedSection, String expectedText) {
            this.label = label;
            this.kind = kind;
            this.elfVa = elfVa;
            this.expectedSection = expectedSection;
            this.expectedText = expectedText;
        }
    }

    private static class Target {
        final String label;
        final String kind;
        final long phase2Va;
        final long phase3bAddress;
        final long ghidraAddress;
        final String expectedText;

        Target(String label, String kind, long phase2Va, long phase3bAddress, long ghidraAddress, String expectedText) {
            this.label = label;
            this.kind = kind;
            this.phase2Va = phase2Va;
            this.phase3bAddress = phase3bAddress;
            this.ghidraAddress = ghidraAddress;
            this.expectedText = expectedText;
        }
    }

    private static class Group {
        final String id;
        final String title;
        final String mdName;
        final String jsonName;
        final List<Target> targets;
        final long[] oldFunctionEntries;
        final long[] probeAddresses;
        final String[] searchTerms;
        final String[] focusTerms;

        Group(String id, String title, String mdName, String jsonName, List<Target> targets,
                long[] oldFunctionEntries, long[] probeAddresses, String[] searchTerms, String[] focusTerms) {
            this.id = id;
            this.title = title;
            this.mdName = mdName;
            this.jsonName = jsonName;
            this.targets = targets;
            this.oldFunctionEntries = oldFunctionEntries;
            this.probeAddresses = probeAddresses;
            this.searchTerms = searchTerms;
            this.focusTerms = focusTerms;
        }
    }

    private static class RawPointerRef {
        Address slot;
        long value;
        String block;
        Function function;
        int referencesToSlot;
    }

    private static class RelocInfo {
        Address address;
        int type;
        String typeName;
        String status;
        String symbolName;
        long[] values;
        String bytes;
        String patchedLong;
        String block;
        String reason;
    }

    private static class DecompileInfo {
        boolean completed;
        String error;
        String focusHits;
        String cLimited;
    }

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length > 0) {
            phase3cDir = new File(args[0]).getCanonicalFile();
        } else {
            phase3cDir = new File(getSourceFile().getParentFile().getFile(false), "..\\Phase3C").getCanonicalFile();
        }
        outputDir = new File(phase3cDir, "output");
        outputDir.mkdirs();

        space = currentProgram.getAddressFactory().getDefaultAddressSpace();
        memory = currentProgram.getMemory();
        listing = currentProgram.getListing();
        relocationTable = currentProgram.getRelocationTable();
        imageBase = currentProgram.getImageBase().getOffset();

        decompiler = new DecompInterface();
        DecompileOptions options = new DecompileOptions();
        decompiler.setOptions(options);
        decompiler.openProgram(currentProgram);

        try {
            addressChecks = buildAddressChecks();
            evaluateAddressChecks(addressChecks);
            mappingConfirmed = computeMappingConfirmed(addressChecks);
            writeAddressMappingJson();
            writeAddressModelMd();

            if (!mappingConfirmed) {
                writeBlockedReport();
                println("PHASE3C_STOPPED_ADDRESS_MODEL_NOT_CONFIRMED");
                return;
            }

            for (Group group : buildGroups()) {
                exportGroupJson(group);
                writeGroupMd(group);
            }
            writeInvalidationsMd();
            writeFinalReport();
            println("PHASE3C_EXPORT_OK");
            println(phase3cDir.getAbsolutePath());
        } finally {
            decompiler.dispose();
        }
    }

    private List<MapCheck> buildAddressChecks() {
        List<MapCheck> rows = new ArrayList<MapCheck>();
        rows.add(new MapCheck("RequestAvatarServerList", "string", 0x21c409fL, ".rodata", "RequestAvatarServerList"));
        rows.add(new MapCheck("EVENTID_AVATARSERVERLIST_RETURN", "string", 0x217dbf5L, ".rodata", "EVENTID_AVATARSERVERLIST_RETURN"));
        rows.add(new MapCheck("GameServerBackupIpList", "string", 0x2180ba5L, ".rodata", "GameServerBackupIpList"));
        rows.add(new MapCheck("SyncPayloadToGameServer", "string", 0x221f64aL, ".rodata", "SyncPayloadToGameServer"));
        rows.add(new MapCheck("/Script/UEDSToolkit", "string", 0x226211aL, ".rodata", "/Script/UEDSToolkit"));
        rows.add(new MapCheck("OpenServerList", "string", 0x226d0b2L, ".rodata", "OpenServerList"));
        rows.add(new MapCheck("ServerListName", "string", 0x2130708L, ".rodata", "ServerListName"));
        rows.add(new MapCheck("socket_http.cpp", "string", 0x2235c36L, ".rodata", "socket_http.cpp"));
        rows.add(new MapCheck("DSControllerComponent.cpp", "string", 0x21c0196L, ".rodata", "DSControllerComponent.cpp"));
        rows.add(new MapCheck("RegisterDSControllerComponent", "string", 0x2120940L, ".rodata", "RegisterDSControllerComponent"));
        rows.add(new MapCheck("OnServerAboutToReconnect", "string", 0x21f5446L, ".rodata", "OnServerAboutToReconnect"));
        rows.add(new MapCheck("OnPreReconnectOnServer", "string", 0x221d27aL, ".rodata", "OnPreReconnectOnServer"));
        rows.add(new MapCheck("ClientNotifyReconnectedSuccessfully", "string", 0x218a330L, ".rodata", "ClientNotifyReconnectedSuccessfully"));
        rows.add(new MapCheck("RequestAvatarServerList code ref 1", "code", 0x7941d2cL, ".text", null));
        rows.add(new MapCheck("RequestAvatarServerList code ref 2", "code", 0x7941d6cL, ".text", null));
        rows.add(new MapCheck("SyncPayloadToGameServer code ref 1", "code", 0x7c1472cL, ".text", null));
        rows.add(new MapCheck("SyncPayloadToGameServer code ref 6", "code", 0x7eb815cL, ".text", null));
        rows.add(new MapCheck("RequestAvatar metadata 1", "metadata", 0xa8db6c8L, ".data.rel.ro", null));
        rows.add(new MapCheck("RequestAvatar metadata 2", "metadata", 0xa8e7b70L, ".data.rel.ro", null));
        rows.add(new MapCheck("GameServerBackup metadata 1", "metadata", 0xae65f20L, ".data.rel.ro", null));
        rows.add(new MapCheck("SyncPayload metadata 1", "metadata", 0xab489d0L, ".data.rel.ro", null));
        rows.add(new MapCheck("UEDSToolkit metadata", "metadata", 0xa98c0b8L, ".data.rel.ro", null));
        return rows;
    }

    private void evaluateAddressChecks(List<MapCheck> rows) {
        for (MapCheck row : rows) {
            row.ghidraAddress = row.elfVa + imageBase;
            row.fileOffset = fmtNullable(elfVaToFileOffset(row.elfVa));
            Address direct = addr(row.elfVa);
            Address ghidra = addr(row.ghidraAddress);
            MemoryBlock directBlock = memory.getBlock(direct);
            MemoryBlock block = memory.getBlock(ghidra);
            row.directGhidraBlock = directBlock == null ? null : directBlock.getName();
            row.ghidraBlock = block == null ? null : block.getName();
            row.content = describeContent(ghidra, row.kind);
            if (row.expectedText != null) {
                String ascii = readAscii(ghidra, 160);
                row.match = ascii != null && ascii.startsWith(row.expectedText);
            } else {
                row.match = block != null && row.expectedSection.equals(block.getName());
            }
        }
    }

    private boolean computeMappingConfirmed(List<MapCheck> rows) {
        if (imageBase != EXPECTED_IMAGE_BASE) {
            return false;
        }
        Map<String, Boolean> sectionOk = new LinkedHashMap<String, Boolean>();
        sectionOk.put(".rodata", Boolean.FALSE);
        sectionOk.put(".text", Boolean.FALSE);
        sectionOk.put(".data.rel.ro", Boolean.FALSE);
        int yes = 0;
        for (MapCheck row : rows) {
            if (row.match) {
                yes++;
                if (sectionOk.containsKey(row.expectedSection)) {
                    sectionOk.put(row.expectedSection, Boolean.TRUE);
                }
            }
        }
        return yes >= 20 && sectionOk.get(".rodata") && sectionOk.get(".text") && sectionOk.get(".data.rel.ro");
    }

    private List<Group> buildGroups() {
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group(
            "requestavatar",
            "RequestAvatarServerList rebased",
            "01_requestavatar_rebased.md",
            "requestavatar_rebased.json",
            Arrays.asList(
                t("RequestAvatarServerList string", "string", 0x21c409fL, 0x21c409fL, "RequestAvatarServerList"),
                t("RequestAvatar metadata 1", "metadata", 0xa8db6c8L, 0xa8db6c8L, null),
                t("RequestAvatar metadata 2", "metadata", 0xa8e7b70L, 0xa8e7b70L, null),
                t("RequestAvatar metadata 3", "metadata", 0xa8ecb10L, 0xa8ecb10L, null),
                t("RequestAvatar code 1", "code", 0x7941d2cL, 0x7941d2cL, null),
                t("RequestAvatar code 2", "code", 0x7941d6cL, 0x7941d6cL, null)
            ),
            new long[] { 0x7941d10L },
            new long[] { 0x7a41d2cL, 0x7a41d6cL },
            new String[] { "RequestAvatarServerList", "FNativeFunctionRegistrar", "RegisterFunction", "NativeFunc", "ProcessEvent", "UFunction", "StaticRegisterNatives", "execRequestAvatarServerList" },
            new String[] { "RequestAvatarServerList", "NativeFunc", "FNativeFunctionRegistrar", "RegisterFunction", "ProcessEvent", "UFunction", "exec" }
        ));
        groups.add(new Group(
            "native_registration",
            "Native registration rebased",
            "02_native_registration_rebased.md",
            "native_registration_rebased.json",
            Arrays.asList(
                t("RequestAvatarServerList string", "string", 0x21c409fL, 0x21c409fL, "RequestAvatarServerList"),
                t("SyncPayloadToGameServer string", "string", 0x221f64aL, 0x221f64aL, "SyncPayloadToGameServer"),
                t("OpenServerList string", "string", 0x226d0b2L, 0x226d0b2L, "OpenServerList")
            ),
            new long[] {},
            new long[] { 0x7a41d2cL, 0x7a41d6cL, 0x7d1472cL, 0x7d14920L, 0x7d14cecL, 0x7d14e28L, 0x7d14ea8L },
            new String[] { "FNativeFunctionRegistrar", "RegisterFunction", "StaticRegisterNatives", "NativeFunc", "execRequestAvatarServerList", "execSyncPayloadToGameServer", "UFunction" },
            new String[] { "FNativeFunctionRegistrar", "RegisterFunction", "NativeFunc", "StaticRegisterNatives", "exec" }
        ));
        groups.add(new Group(
            "avatar_event",
            "EVENTID_AVATARSERVERLIST_RETURN rebased",
            "03_avatar_event_rebased.md",
            "avatar_event_rebased.json",
            Arrays.asList(
                t("EVENTID_AVATARSERVERLIST_RETURN string", "string", 0x217dbf5L, 0x217dbf5L, "EVENTID_AVATARSERVERLIST_RETURN")
            ),
            new long[] {},
            new long[] {},
            new String[] { "EVENTID_AVATARSERVERLIST_RETURN", "ELuaCppEventType::", "LuaCppEvent", "DispatchEvent", "SendEvent", "TriggerEvent" },
            new String[] { "EVENTID_AVATARSERVERLIST_RETURN", "ELuaCppEventType", "LuaCppEvent", "DispatchEvent", "SendEvent", "TriggerEvent", "case" }
        ));
        groups.add(new Group(
            "loginmgr",
            "LoginMgr rebased",
            "04_loginmgr_rebased.md",
            "loginmgr_rebased.json",
            Arrays.asList(
                t("PureClient/Login/LoginMgr.cpp", "string", 0L, 0L, "PureClient/Login/LoginMgr.cpp"),
                t("ULoginMgrWrapper", "string", 0L, 0L, "ULoginMgrWrapper"),
                t("LoginMgrWrapper.cpp", "string", 0L, 0L, "LoginMgrWrapper.cpp"),
                t("RequestAvatarServerList string", "string", 0x21c409fL, 0x21c409fL, "RequestAvatarServerList"),
                t("OpenServerList string", "string", 0x226d0b2L, 0x226d0b2L, "OpenServerList"),
                t("ServerListName string", "string", 0x2130708L, 0x2130708L, "ServerListName")
            ),
            new long[] {},
            new long[] { 0x7a41d2cL, 0x7a41d6cL },
            new String[] { "PureClient/Login/LoginMgr.cpp", "ULoginMgrWrapper", "LoginMgrWrapper.cpp", "RequestAvatarServerList", "OpenServerList", "ServerListName" },
            new String[] { "LoginMgr", "RequestAvatarServerList", "OpenServerList", "ServerListName", "ProcessEvent" }
        ));
        groups.add(new Group(
            "gameserverbackup",
            "GameServerBackupIpList rebased",
            "05_gameserverbackup_rebased.md",
            "gameserverbackup_rebased.json",
            Arrays.asList(
                t("GameServerBackupIpList string", "string", 0x2180ba5L, 0x2180ba5L, "GameServerBackupIpList"),
                t("GameServerBackup metadata 1", "metadata", 0xae65f20L, 0xae65f20L, null),
                t("GameServerBackup metadata 2", "metadata", 0xae65f70L, 0xae65f70L, null)
            ),
            new long[] {},
            new long[] {},
            new String[] { "GameServerBackupIpList", "ArrayProperty", "StrProperty", "NameProperty", "StructProperty", "MapProperty", "SetProperty", "ObjectProperty" },
            new String[] { "GameServerBackupIpList", "ArrayProperty", "StrProperty", "NameProperty", "StructProperty", "MapProperty", "SetProperty", "ObjectProperty" }
        ));
        groups.add(new Group(
            "syncpayload",
            "SyncPayloadToGameServer rebased",
            "06_syncpayload_rebased.md",
            "syncpayload_rebased.json",
            Arrays.asList(
                t("SyncPayloadToGameServer string", "string", 0x221f64aL, 0x221f64aL, "SyncPayloadToGameServer"),
                t("SyncPayload metadata 1", "metadata", 0xab489d0L, 0xab489d0L, null),
                t("SyncPayload metadata 2", "metadata", 0xab48ac8L, 0xab48ac8L, null),
                t("SyncPayload metadata 3", "metadata", 0xab48c20L, 0xab48c20L, null),
                t("SyncPayload metadata 4", "metadata", 0xad87bf8L, 0xad87bf8L, null),
                t("SyncPayload metadata 5", "metadata", 0xad87c18L, 0xad87c18L, null),
                t("SyncPayload metadata 6", "metadata", 0xad87c68L, 0xad87c68L, null)
            ),
            new long[] { 0x7c14710L, 0x7c148b0L, 0x7c14cacL, 0x7c14e44L, 0x7eb80dcL, 0x7eb8224L, 0x7eb8364L },
            new long[] { 0x7d1472cL, 0x7d14920L, 0x7d14cecL, 0x7d14e28L, 0x7d14ea8L, 0x7fb815cL, 0x7fb8284L, 0x7fb83a4L },
            new String[] { "SyncPayloadToGameServer", "ProcessEvent", "ProcessRemoteFunction", "CallRemoteFunction", "UNetDriver", "UNetConnection", "UChannel", "ActorChannel", "SendBunch", "ReplicateActor" },
            new String[] { "SyncPayloadToGameServer", "ProcessEvent", "ProcessRemoteFunction", "CallRemoteFunction", "UNetDriver", "UNetConnection", "UChannel", "ActorChannel", "SendBunch", "ReplicateActor", "RPC" }
        ));
        groups.add(new Group(
            "reconnect",
            "Reconnect rebased",
            "07_reconnect_rebased.md",
            "reconnect_rebased.json",
            Arrays.asList(
                direct("OnServerAboutToReconnect string", "string", 0x22f5446L, 0x22f5446L, "OnServerAboutToReconnect"),
                direct("OnPreReconnectOnServer string", "string", 0x231d27aL, 0x231d27aL, "OnPreReconnectOnServer"),
                direct("ClientNotifyReconnectedSuccessfully string", "string", 0x228a330L, 0x228a330L, "ClientNotifyReconnectedSuccessfully"),
                t("OnServerAboutToReconnect metadata 1", "metadata", 0xad72b80L, 0xad72b80L, null),
                t("OnServerAboutToReconnect metadata 2", "metadata", 0xad72c18L, 0xad72c18L, null),
                t("OnServerAboutToReconnect metadata 3", "metadata", 0xad72e40L, 0xad72e40L, null),
                t("OnPreReconnectOnServer metadata 1", "metadata", 0xa966210L, 0xa966210L, null),
                t("OnPreReconnectOnServer metadata 2", "metadata", 0xa967210L, 0xa967210L, null),
                t("OnPreReconnectOnServer metadata 3", "metadata", 0xa969838L, 0xa969838L, null),
                t("ClientNotifyReconnectedSuccessfully metadata 1", "metadata", 0xa9ae008L, 0xa9ae008L, null),
                t("ClientNotifyReconnectedSuccessfully metadata 2", "metadata", 0xa9b0698L, 0xa9b0698L, null),
                t("ClientNotifyReconnectedSuccessfully metadata 3", "metadata", 0xa9ba988L, 0xa9ba988L, null)
            ),
            new long[] {},
            new long[] {},
            new String[] { "OnServerAboutToReconnect", "OnPreReconnectOnServer", "ClientNotifyReconnectedSuccessfully", "Host", "Port", "RemoteAddr", "IpAddr", "InternetAddr", "ServerConnection", "PendingNetGame", "FURL", "URL", "getaddrinfo", "socket" },
            new String[] { "Reconnect", "Host", "Port", "RemoteAddr", "IpAddr", "InternetAddr", "ServerConnection", "PendingNetGame", "FURL", "URL", "getaddrinfo", "socket" }
        ));
        groups.add(new Group(
            "uedstoolkit",
            "UEDSToolkit rebased",
            "08_uedstoolkit_rebased.md",
            "uedstoolkit_rebased.json",
            Arrays.asList(
                direct("socket_http.cpp", "string", 0x2335c36L, 0x2335c36L, "socket_http.cpp"),
                direct("DSControllerComponent.cpp", "string", 0x22c0196L, 0x22c0196L, "DSControllerComponent.cpp"),
                direct("RegisterDSControllerComponent", "string", 0x2220940L, 0x2220940L, "RegisterDSControllerComponent"),
                direct("/Script/UEDSToolkit", "string", 0x236211aL, 0x236211aL, "/Script/UEDSToolkit"),
                t("UEDSToolkit metadata", "metadata", 0xa98c0b8L, 0xa98c0b8L, null)
            ),
            new long[] {},
            new long[] {},
            new String[] { "socket_http.cpp", "DSControllerComponent.cpp", "RegisterDSControllerComponent", "/Script/UEDSToolkit", "socket", "connect", "send", "recv", "getaddrinfo" },
            new String[] { "UEDSToolkit", "DSController", "socket", "connect", "send", "recv", "getaddrinfo" }
        ));
        return groups;
    }

    private Target t(String label, String kind, long phase2Va, long phase3bAddress, String expectedText) {
        return new Target(label, kind, phase2Va, phase3bAddress, phase2Va + imageBase, expectedText);
    }

    private Target direct(String label, String kind, long ghidraAddress, long phase3bAddress, String expectedText) {
        return new Target(label, kind, ghidraAddress - imageBase, phase3bAddress, ghidraAddress, expectedText);
    }

    private void writeAddressMappingJson() throws Exception {
        PrintWriter out = new PrintWriter(new File(outputDir, "address_mapping.json"), StandardCharsets.UTF_8.name());
        Json j = new Json(out);
        j.objStart();
        j.prop("program", currentProgram.getName(), true);
        j.prop("image_base", hx(imageBase), true);
        j.prop("mapping_rule", "GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + image_base", true);
        j.prop("confirmed", mappingConfirmed, true);
        j.propName("memory_blocks", true);
        j.arrayStart();
        MemoryBlock[] blocks = memory.getBlocks();
        for (int i = 0; i < blocks.length; i++) {
            if (i > 0) {
                j.comma();
            }
            MemoryBlock b = blocks[i];
            j.objStart();
            j.prop("name", b.getName(), true);
            j.prop("start", fmt(b.getStart()), true);
            j.prop("end", fmt(b.getEnd()), true);
            j.prop("read", b.isRead(), true);
            j.prop("write", b.isWrite(), true);
            j.prop("execute", b.isExecute(), false);
            j.objEnd();
        }
        j.arrayEnd();
        j.propName("checks", false);
        j.arrayStart();
        for (int i = 0; i < addressChecks.size(); i++) {
            if (i > 0) {
                j.comma();
            }
            writeMapCheck(j, addressChecks.get(i));
        }
        j.arrayEnd();
        j.objEnd();
        out.close();
    }

    private void writeMapCheck(Json j, MapCheck row) {
        j.objStart();
        j.prop("label", row.label, true);
        j.prop("kind", row.kind, true);
        j.prop("elf_virtual_address", hx(row.elfVa), true);
        j.prop("file_offset", row.fileOffset, true);
        j.prop("image_base_added", hx(row.ghidraAddress), true);
        j.prop("old_direct_ghidra_block", row.directGhidraBlock, true);
        j.prop("ghidra_block", row.ghidraBlock, true);
        j.prop("expected_section", row.expectedSection, true);
        j.prop("content", row.content, true);
        j.prop("match", row.match, false);
        j.objEnd();
    }

    private void writeAddressModelMd() throws Exception {
        PrintWriter out = md("00_address_model.md");
        out.println("# Phase3C - Modele d'adresses Ghidra");
        out.println();
        out.println("Date locale: " + LocalDate.now());
        out.println();
        out.println("Mode Ghidra utilise: `-process libUE4.so -noanalysis -readOnly`.");
        out.println();
        out.println("## Conclusion");
        out.println();
        if (mappingConfirmed) {
            out.println("CONFIRMED: le projet a une image base `0x100000` et les controles `.rodata`, `.text` et `.data.rel.ro` confirment la transformation:");
            out.println();
            out.println("`GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000`");
        } else {
            out.println("UNKNOWN: le mapping n'est pas confirme. Les autres etapes Phase3C ne doivent pas etre interpretees.");
        }
        out.println();
        out.println("## Blocs memoire Ghidra");
        out.println();
        out.println("| Bloc | Debut | Fin | R | W | X |");
        out.println("|---|---:|---:|:---:|:---:|:---:|");
        for (MemoryBlock b : memory.getBlocks()) {
            out.println("| `" + b.getName() + "` | `" + fmt(b.getStart()) + "` | `" + fmt(b.getEnd()) + "` | " + yesNo(b.isRead()) + " | " + yesNo(b.isWrite()) + " | " + yesNo(b.isExecute()) + " |");
        }
        out.println();
        out.println("## Verifications d'adresses");
        out.println();
        out.println("| Cible | Type | ELF VA Phase2 | FILE_OFFSET | + imageBase | Bloc direct ancien | Bloc Ghidra | Contenu | Correspondance |");
        out.println("|---|---|---:|---:|---:|---|---|---|:---:|");
        for (MapCheck row : addressChecks) {
            out.println("| " + escMd(row.label) + " | " + row.kind + " | `" + hx(row.elfVa) + "` | `" + row.fileOffset + "` | `" + hx(row.ghidraAddress) + "` | `" + nullDash(row.directGhidraBlock) + "` | `" + nullDash(row.ghidraBlock) + "` | " + escMd(limit(row.content, 120)) + " | " + yesNo(row.match) + " |");
        }
        out.println();
        out.println("Le JSON complet est dans `output/address_mapping.json`.");
        out.close();
    }

    private void exportGroupJson(Group group) throws Exception {
        PrintWriter out = new PrintWriter(new File(outputDir, group.jsonName), StandardCharsets.UTF_8.name());
        Json j = new Json(out);
        j.objStart();
        j.prop("group", group.id, true);
        j.prop("title", group.title, true);
        j.prop("image_base", hx(imageBase), true);
        j.propName("targets", true);
        j.arrayStart();
        for (int i = 0; i < group.targets.size(); i++) {
            if (i > 0) {
                j.comma();
            }
            writeTargetJson(j, group.targets.get(i));
        }
        j.arrayEnd();
        j.propName("functions", true);
        j.arrayStart();
        LinkedHashMap<String, Function> funcs = collectGroupFunctions(group);
        int idx = 0;
        for (Function f : funcs.values()) {
            if (idx++ > 0) {
                j.comma();
            }
            writeFunctionJson(j, f, idx <= 12, group.focusTerms);
        }
        j.arrayEnd();
        j.propName("search_terms", false);
        writeSearchTermsJson(j, group.searchTerms);
        j.objEnd();
        out.close();
    }

    private void writeTargetJson(Json j, Target t) {
        Address a = addr(t.ghidraAddress);
        MemoryBlock block = memory.getBlock(a);
        j.objStart();
        j.prop("label", t.label, true);
        j.prop("kind", t.kind, true);
        j.prop("phase2_elf_virtual_address", t.phase2Va == 0L ? null : hx(t.phase2Va), true);
        j.prop("phase2_file_offset", t.phase2Va == 0L ? null : fmtNullable(elfVaToFileOffset(t.phase2Va)), true);
        j.prop("phase3b_address_used", t.phase3bAddress == 0L ? null : hx(t.phase3bAddress), true);
        j.prop("ghidra_address", hx(t.ghidraAddress), true);
        j.prop("ghidra_block", block == null ? null : block.getName(), true);
        j.prop("content", describeContent(a, t.kind), true);
        j.propName("function_containing", true);
        writeFunctionSummary(j, functionContaining(a));
        j.propName("ghidra_xrefs_to", true);
        writeReferences(j, currentProgram.getReferenceManager().getReferencesTo(a), 80);
        j.propName("ghidra_xrefs_from", true);
        writeReferences(j, currentProgram.getReferenceManager().getReferencesFrom(a), 80);
        j.propName("relocation_references", true);
        writeRelocs(j, findAssociatedRelocations(t, 80));
        j.propName("raw_pointer_references", false);
        writeRawPointers(j, findRawPointersTo(targetValues(t), 80));
        j.objEnd();
    }

    private void writeGroupMd(Group group) throws Exception {
        PrintWriter out = md(group.mdName);
        out.println("# Phase3C - " + group.title);
        out.println();
        out.println("Statut modele d'adresses: " + (mappingConfirmed ? "CONFIRMED" : "UNKNOWN") + ".");
        out.println();
        out.println("## Cibles rebased");
        out.println();
        out.println("| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |");
        out.println("|---|---|---:|---:|---|---|---:|---:|---:|");
        for (Target t : group.targets) {
            Address a = addr(t.ghidraAddress);
            int xrefs = countReferences(currentProgram.getReferenceManager().getReferencesTo(a), 200);
            int relocs = findAssociatedRelocations(t, 80).size();
            int raw = findRawPointersTo(targetValues(t), 80).size();
            MemoryBlock block = memory.getBlock(a);
            out.println("| " + escMd(t.label) + " | " + t.kind + " | `" + (t.phase2Va == 0L ? "-" : hx(t.phase2Va)) + "` | `" + hx(t.ghidraAddress) + "` | `" + (block == null ? "-" : block.getName()) + "` | " + escMd(limit(describeContent(a, t.kind), 120)) + " | " + xrefs + " | " + relocs + " | " + raw + " |");
        }
        out.println();

        writeGroupSpecificNotes(out, group);

        out.println("## Fonctions corrigees");
        out.println();
        LinkedHashMap<String, Function> funcs = collectGroupFunctions(group);
        if (funcs.isEmpty()) {
            out.println("UNKNOWN: aucune fonction contenant les adresses corrigees n'a ete trouvee.");
        } else {
            out.println("| Fonction | Entry | Body | Callers | Callees | Focus hits |");
            out.println("|---|---:|---|---:|---:|---|");
            int decompileCount = 0;
            for (Function f : funcs.values()) {
                Set<Function> callers = safeCallingFunctions(f);
                Set<Function> callees = safeCalledFunctions(f);
                DecompileInfo dec = decompileCount < 12 ? decompile(f, group.focusTerms, 6) : skippedDecompile();
                decompileCount++;
                out.println("| `" + f.getName() + "` | `" + fmt(f.getEntryPoint()) + "` | `" + fmt(f.getBody().getMinAddress()) + "-" + fmt(f.getBody().getMaxAddress()) + "` | " + callers.size() + " | " + callees.size() + " | " + escMd(limit(dec.focusHits, 160)) + " |");
            }
        }
        out.println();
        out.println("## Recherches texte");
        out.println();
        out.println("| Terme | Occurrences | Premieres adresses |");
        out.println("|---|---:|---|");
        for (String term : group.searchTerms) {
            List<Address> hits = findBytes(term, byteSearchLimit(term));
            out.println("| `" + escMd(term) + "` | " + hits.size() + " | " + escMd(joinAddresses(hits, 8)) + " |");
        }
        out.println();
        out.println("JSON detaille: `output/" + group.jsonName + "`.");
        out.close();
    }

    private void writeGroupSpecificNotes(PrintWriter out, Group group) {
        if ("requestavatar".equals(group.id)) {
            Function oldFun = currentProgram.getFunctionManager().getFunctionAt(addr(0x7941d10L));
            Function f1 = functionContaining(addr(0x7a41d2cL));
            Function f2 = functionContaining(addr(0x7a41d6cL));
            boolean same = oldFun != null && (sameFunction(oldFun, f1) || sameFunction(oldFun, f2));
            out.println("## Comparaison ancienne fonction");
            out.println();
            out.println("- Ancienne `FUN_07941d10`: " + functionOneLine(oldFun) + ".");
            out.println("- Fonction contenant `0x7a41d2c`: " + functionOneLine(f1) + ".");
            out.println("- Fonction contenant `0x7a41d6c`: " + functionOneLine(f2) + ".");
            out.println("- EST-CE LA MEME FONCTION ? " + yesNo(same) + ".");
            if (!same) {
                out.println("- INVALIDATED: l'ancienne conclusion liant directement `FUN_07941d10` a `RequestAvatarServerList` ne doit plus etre conservee.");
            }
            out.println();
        } else if ("gameserverbackup".equals(group.id)) {
            out.println("## Comparaison metadata Phase3B");
            out.println();
            out.println("Les anciennes zones `0xae65f20` / `0xae65f70` et les zones rebased `0xaf65f20` / `0xaf65f70` sont comparees dans le JSON. Si les pointeurs et chaines different, l'attribution Phase3B reste INVALIDATED.");
            out.println();
        } else if ("syncpayload".equals(group.id)) {
            out.println("## Comparaison anciennes candidates");
            out.println();
            out.println("Les fonctions contenant les adresses rebased `0x7d...` / `0x7fb...` remplacent les candidates `0x7c...` / `0x7eb...` avant toute conclusion RPC.");
            out.println();
        }
    }

    private void writeInvalidationsMd() throws Exception {
        PrintWriter out = md("09_phase3b_invalidations.md");
        out.println("# Phase3C - Invalidations Phase3B");
        out.println();
        out.println("Regle appliquee: une conclusion fondee sur une adresse Phase2 non rebased est marquee INVALIDATED si la fonction ou la zone Ghidra corrigee differe.");
        out.println();
        out.println("| Cible | Ancienne Phase2 | Ancienne Phase3B | Ghidra reelle | Delta | Ancienne fonction | Nouvelle fonction | Ancienne conclusion valide ? |");
        out.println("|---|---:|---:|---:|---:|---|---|:---:|");
        invalidationRow(out, "RequestAvatarServerList", 0x21c409fL, 0x21c409fL, 0x22c409fL, 0x7941d10L, 0x7a41d2cL);
        invalidationRow(out, "EVENTID_AVATARSERVERLIST_RETURN", 0x217dbf5L, 0x217dbf5L, 0x227dbf5L, 0L, 0L);
        invalidationRow(out, "OpenServerList", 0x226d0b2L, 0x226d0b2L, 0x236d0b2L, 0L, 0L);
        invalidationRow(out, "ServerListName", 0x2130708L, 0x2130708L, 0x2230708L, 0L, 0L);
        invalidationRow(out, "GameServerBackupIpList", 0x2180ba5L, 0x2180ba5L, 0x2280ba5L, 0L, 0L);
        invalidationRow(out, "SyncPayloadToGameServer", 0x221f64aL, 0x221f64aL, 0x231f64aL, 0x7c14710L, 0x7d1472cL);
        invalidationRow(out, "OnServerAboutToReconnect", 0x21f5446L, 0x22f5446L, 0x22f5446L, 0L, 0L);
        invalidationRow(out, "OnPreReconnectOnServer", 0x221d27aL, 0x231d27aL, 0x231d27aL, 0L, 0L);
        invalidationRow(out, "ClientNotifyReconnectedSuccessfully", 0x218a330L, 0x228a330L, 0x228a330L, 0L, 0L);
        invalidationRow(out, "RegisterDSControllerComponent", 0x2120940L, 0x2220940L, 0x2220940L, 0L, 0L);
        invalidationRow(out, "/Script/UEDSToolkit", 0x226211aL, 0x226211aL, 0x236211aL, 0L, 0L);
        out.println();
        out.println("Les details par cible sont dans les fichiers `01_...` a `08_...` et dans les JSON `output/*_rebased.json`.");
        out.close();
    }

    private void invalidationRow(PrintWriter out, String label, long oldPhase2, long oldPhase3b, long ghidra,
            long oldFuncAddr, long newProbeAddr) {
        Function oldFunc = oldFuncAddr == 0L ? null : currentProgram.getFunctionManager().getFunctionAt(addr(oldFuncAddr));
        Function newFunc = newProbeAddr == 0L ? null : functionContaining(addr(newProbeAddr));
        boolean valid = oldPhase3b == ghidra && (oldFuncAddr == 0L || sameFunction(oldFunc, newFunc));
        String oldF = oldFunc == null ? "-" : oldFunc.getName() + " " + fmt(oldFunc.getEntryPoint());
        String newF = newFunc == null ? "-" : newFunc.getName() + " " + fmt(newFunc.getEntryPoint());
        out.println("| " + escMd(label) + " | `" + hx(oldPhase2) + "` | `" + hx(oldPhase3b) + "` | `" + hx(ghidra) + "` | `" + hx(ghidra - oldPhase2) + "` | `" + oldF + "` | `" + newF + "` | " + (valid ? "YES" : "NO") + " |");
    }

    private void writeFinalReport() throws Exception {
        PrintWriter out = md("REPORT_PHASE3C.md");
        out.println("# Phase3C - Rapport final");
        out.println();
        out.println("Date locale: " + LocalDate.now());
        out.println();
        out.println("Projet: `Phase3\\ghidra_project\\ApexMobileUE4`, programme `libUE4.so`, mode `-process libUE4.so -noanalysis -readOnly`.");
        out.println();
        out.println("## Reponse courte");
        out.println();
        out.println("CONFIRMED: le modele `GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000` est confirme par les controles `.rodata`, `.text` et `.data.rel.ro`.");
        out.println();
        out.println("INVALIDATED: les conclusions Phase3B qui utilisaient directement des VA Phase2 comme adresses Ghidra doivent etre rejetees, notamment pour les anciennes fonctions `FUN_07941d10` et candidates `0x7c...` / `0x7eb...`.");
        out.println();
        out.println("UNKNOWN: les chemins reseau et RPC restent non demontres si les liens `STRING -> RELOCATION/RAW_POINTER -> METADATA -> CODE` ne sont pas presents dans les sorties corrigees.");
        out.println();
        out.println("## Questions finales");
        out.println();
        out.println("1. CONFIRMED - `+0x100000` est la transformation ELF VA -> Ghidra VA dans ce projet.");
        out.println("2. INVALIDATED - toute conclusion basee sur une ancienne adresse Phase2 non rebased; voir `09_phase3b_invalidations.md`.");
        out.println("3. " + requestAvatarSameFunctionStatus() + " - `FUN_07941d10` n'est valide que si elle correspond a la fonction contenant `0x7a41d2c`/`0x7a41d6c`.");
        out.println("4. " + newRequestAvatarFunctionAnswer());
        out.println("5. " + relationAnswer("requestavatar", "RequestAvatarServerList string", "metadata"));
        out.println("6. UNKNOWN - aucune native `RequestAvatarServerList` n'est confirmee sans slot NativeFunc relie.");
        out.println("7. UNKNOWN - `LoginMgr -> RequestAvatarServerList` doit venir du callgraph corrige; aucun lien ne doit etre repris de Phase3B sans preuve.");
        out.println("8. UNKNOWN - la valeur/handler `EVENTID_AVATARSERVERLIST_RETURN` reste a confirmer par table enum ou references brutes.");
        out.println("9. UNKNOWN - classe/type/writer `GameServerBackupIpList` non confirme tant qu'une metadata rebased n'est pas reliee a la chaine.");
        out.println("10. UNKNOWN - `SyncPayloadToGameServer` n'est pas classe RPC sans chemin vers `ProcessRemoteFunction`/`UNet*`/`SendBunch`.");
        out.println("11. UNKNOWN - native `SyncPayloadToGameServer` non confirmee sans registration rebased.");
        out.println("12. UNKNOWN - reconnect ne revele pas Host/IP/Port sans lien corrige vers `Host`, `Port`, `RemoteAddr`, `FURL` ou socket.");
        out.println("13. UNKNOWN - transport UEDSToolkit non confirme sans callgraph vers `socket/connect/send/recv/getaddrinfo`.");
        out.println("14. UNKNOWN - aucun chemin complet server list -> adresse -> game server n'est etabli par defaut.");
        out.println("15. UNKNOWN - restent a prouver: native exacte, handlers d'evenements, writers de metadata, valeurs enum et transport effectif.");
        out.println();
        out.println("## Fichiers produits");
        out.println();
        out.println("- `00_address_model.md`");
        out.println("- `01_requestavatar_rebased.md`");
        out.println("- `02_native_registration_rebased.md`");
        out.println("- `03_avatar_event_rebased.md`");
        out.println("- `04_loginmgr_rebased.md`");
        out.println("- `05_gameserverbackup_rebased.md`");
        out.println("- `06_syncpayload_rebased.md`");
        out.println("- `07_reconnect_rebased.md`");
        out.println("- `08_uedstoolkit_rebased.md`");
        out.println("- `09_phase3b_invalidations.md`");
        out.println("- JSON dans `output\\`");
        out.close();
    }

    private void writeBlockedReport() throws Exception {
        PrintWriter out = md("REPORT_PHASE3C.md");
        out.println("# Phase3C - Rapport final");
        out.println();
        out.println("UNKNOWN: le modele d'adresses n'a pas ete confirme. Arret conforme a la consigne.");
        out.println("Voir `00_address_model.md` et `output/address_mapping.json`.");
        out.close();
    }

    private String requestAvatarSameFunctionStatus() {
        Function oldFun = currentProgram.getFunctionManager().getFunctionAt(addr(0x7941d10L));
        Function f1 = functionContaining(addr(0x7a41d2cL));
        Function f2 = functionContaining(addr(0x7a41d6cL));
        return oldFun != null && (sameFunction(oldFun, f1) || sameFunction(oldFun, f2)) ? "CONFIRMED" : "INVALIDATED";
    }

    private String newRequestAvatarFunctionAnswer() {
        Function f1 = functionContaining(addr(0x7a41d2cL));
        Function f2 = functionContaining(addr(0x7a41d6cL));
        if (f1 == null && f2 == null) {
            return "UNKNOWN - aucune fonction contenant les code-xrefs corrigees n'a ete trouvee.";
        }
        String a = f1 == null ? "-" : f1.getName() + " @ " + fmt(f1.getEntryPoint());
        String b = f2 == null ? "-" : f2.getName() + " @ " + fmt(f2.getEntryPoint());
        return "PROBABLE - fonctions contenant les code-xrefs corrigees: " + a + " ; " + b + ".";
    }

    private String relationAnswer(String groupId, String stringLabel, String metadataKind) {
        for (Group group : buildGroups()) {
            if (!group.id.equals(groupId)) {
                continue;
            }
            Target stringTarget = null;
            for (Target t : group.targets) {
                if (t.label.equals(stringLabel)) {
                    stringTarget = t;
                }
            }
            if (stringTarget == null) {
                return "UNKNOWN - cible string introuvable.";
            }
            int raw = findRawPointersTo(targetValues(stringTarget), 20).size();
            int rel = findAssociatedRelocations(stringTarget, 20).size();
            if (raw > 0 || rel > 0) {
                return "PROBABLE - references brutes/relocations vers la chaine presentes; verifier le JSON pour relier aux metadata.";
            }
            return "UNKNOWN - aucune relation brute/relocation suffisante entre chaine et metadata corrigee.";
        }
        return "UNKNOWN - groupe introuvable.";
    }

    private LinkedHashMap<String, Function> collectGroupFunctions(Group group) {
        LinkedHashMap<String, Function> funcs = new LinkedHashMap<String, Function>();
        for (long entry : group.oldFunctionEntries) {
            addFunction(funcs, currentProgram.getFunctionManager().getFunctionAt(addr(entry)));
        }
        for (long probe : group.probeAddresses) {
            addFunction(funcs, functionContaining(addr(probe)));
        }
        for (Target t : group.targets) {
            addFunction(funcs, functionContaining(addr(t.ghidraAddress)));
        }
        LinkedHashMap<String, Function> expanded = new LinkedHashMap<String, Function>(funcs);
        for (Function f : funcs.values()) {
            for (Function next : safeCalledFunctions(f)) {
                if (expanded.size() >= 80) {
                    break;
                }
                if (functionMatches(next, group.focusTerms)) {
                    addFunction(expanded, next);
                }
            }
            for (Function next : safeCallingFunctions(f)) {
                if (expanded.size() >= 80) {
                    break;
                }
                if (functionMatches(next, group.focusTerms)) {
                    addFunction(expanded, next);
                }
            }
        }
        return expanded;
    }

    private void addFunction(LinkedHashMap<String, Function> map, Function f) {
        if (f == null) {
            return;
        }
        map.put(fmt(f.getEntryPoint()), f);
    }

    private boolean functionMatches(Function f, String[] terms) {
        if (f == null || terms == null) {
            return false;
        }
        String text = f.getName() + " " + f.getSignature().toString();
        return containsAny(text, terms);
    }

    private Set<Function> safeCalledFunctions(Function f) {
        Set<Function> out = new LinkedHashSet<Function>();
        if (f == null) {
            return out;
        }
        try {
            out.addAll(f.getCalledFunctions(TaskMonitor.DUMMY));
        } catch (Exception e) {
            // Keep export best-effort.
        }
        return out;
    }

    private Set<Function> safeCallingFunctions(Function f) {
        Set<Function> out = new LinkedHashSet<Function>();
        if (f == null) {
            return out;
        }
        try {
            out.addAll(f.getCallingFunctions(TaskMonitor.DUMMY));
        } catch (Exception e) {
            // Keep export best-effort.
        }
        return out;
    }

    private void writeFunctionJson(Json j, Function f, boolean withDecompile, String[] focusTerms) {
        if (f == null) {
            j.nullValue();
            return;
        }
        j.objStart();
        j.prop("name", f.getName(), true);
        j.prop("entry", fmt(f.getEntryPoint()), true);
        j.prop("body_min", fmt(f.getBody().getMinAddress()), true);
        j.prop("body_max", fmt(f.getBody().getMaxAddress()), true);
        j.prop("signature", f.getSignature().toString(), true);
        j.propName("callers", true);
        writeFunctionSet(j, safeCallingFunctions(f), 30);
        j.propName("callees", true);
        writeFunctionSet(j, safeCalledFunctions(f), 40);
        j.propName("body_data_refs", true);
        writeBodyDataRefs(j, f, 80);
        j.propName("decompile", false);
        if (withDecompile) {
            writeDecompile(j, decompile(f, focusTerms, 8));
        } else {
            j.nullValue();
        }
        j.objEnd();
    }

    private void writeFunctionSummary(Json j, Function f) {
        if (f == null) {
            j.nullValue();
            return;
        }
        j.objStart();
        j.prop("name", f.getName(), true);
        j.prop("entry", fmt(f.getEntryPoint()), true);
        j.prop("body_min", fmt(f.getBody().getMinAddress()), true);
        j.prop("body_max", fmt(f.getBody().getMaxAddress()), false);
        j.objEnd();
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
        j.arrayEnd();
    }

    private void writeBodyDataRefs(Json j, Function f, int max) {
        j.arrayStart();
        int count = 0;
        if (f != null) {
            Instruction ins = listing.getInstructionAt(f.getBody().getMinAddress());
            Address end = f.getBody().getMaxAddress();
            int scanned = 0;
            while (ins != null && ins.getAddress().compareTo(end) <= 0 && count < max && scanned < 500) {
                Reference[] refs = ins.getReferencesFrom();
                for (Reference r : refs) {
                    if (count >= max) {
                        break;
                    }
                    Address to = r.getToAddress();
                    MemoryBlock b = memory.getBlock(to);
                    if (b != null && !b.isExecute()) {
                        if (count > 0) {
                            j.comma();
                        }
                        j.objStart();
                        j.prop("from", fmt(r.getFromAddress()), true);
                        j.prop("to", fmt(to), true);
                        j.prop("to_block", b.getName(), true);
                        j.prop("reference_type", r.getReferenceType().toString(), true);
                        j.prop("content", describeContent(to, "data"), false);
                        j.objEnd();
                        count++;
                    }
                }
                ins = ins.getNext();
                scanned++;
            }
        }
        j.arrayEnd();
    }

    private void writeDecompile(Json j, DecompileInfo d) {
        j.objStart();
        j.prop("completed", d.completed, true);
        j.prop("error", d.error, true);
        j.prop("focus_hits", d.focusHits, true);
        j.prop("c_limited", d.cLimited, false);
        j.objEnd();
    }

    private DecompileInfo decompile(Function f, String[] focusTerms, int timeoutSecs) {
        DecompileInfo out = new DecompileInfo();
        if (f == null) {
            out.completed = false;
            out.error = "null_function";
            out.focusHits = "";
            out.cLimited = null;
            return out;
        }
        try {
            DecompileResults res = decompiler.decompileFunction(f, timeoutSecs, TaskMonitor.DUMMY);
            out.completed = res.decompileCompleted();
            if (!out.completed) {
                out.error = res.getErrorMessage();
                out.focusHits = "";
                out.cLimited = null;
                return out;
            }
            String c = res.getDecompiledFunction().getC();
            out.error = null;
            out.focusHits = focusLines(c, focusTerms, 40);
            out.cLimited = limitLines(c, 90);
            return out;
        } catch (Exception e) {
            out.completed = false;
            out.error = e.toString();
            out.focusHits = "";
            out.cLimited = null;
            return out;
        }
    }

    private DecompileInfo skippedDecompile() {
        DecompileInfo out = new DecompileInfo();
        out.completed = false;
        out.error = "decompile_limit_reached";
        out.focusHits = "";
        out.cLimited = null;
        return out;
    }

    private void writeSearchTermsJson(Json j, String[] terms) {
        j.arrayStart();
        for (int i = 0; i < terms.length; i++) {
            if (i > 0) {
                j.comma();
            }
            String term = terms[i];
            List<Address> hits = findBytes(term, byteSearchLimit(term));
            j.objStart();
            j.prop("term", term, true);
            j.prop("count_returned", hits.size(), true);
            j.propName("occurrences", false);
            j.arrayStart();
            for (int k = 0; k < hits.size(); k++) {
                if (k > 0) {
                    j.comma();
                }
                Address a = hits.get(k);
                j.objStart();
                j.prop("address", fmt(a), true);
                j.prop("elf_virtual_address", hx(a.getOffset() - imageBase), true);
                j.prop("block", blockName(a), true);
                j.prop("preview", readAscii(a, 160), true);
                j.propName("xrefs_to", false);
                writeReferences(j, currentProgram.getReferenceManager().getReferencesTo(a), 30);
                j.objEnd();
            }
            j.arrayEnd();
            j.objEnd();
        }
        j.arrayEnd();
    }

    private List<Address> findBytes(String text, int max) {
        String cacheKey = text + "|" + max;
        if (byteSearchCache.containsKey(cacheKey)) {
            return byteSearchCache.get(cacheKey);
        }
        List<Address> out = new ArrayList<Address>();
        if (text == null || text.length() == 0 || max <= 0) {
            byteSearchCache.put(cacheKey, out);
            return out;
        }
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
        byteSearchCache.put(cacheKey, out);
        return out;
    }

    private int byteSearchLimit(String term) {
        if (term == null || term.length() < 4) {
            return 0;
        }
        if ("ELuaCppEventType::".equals(term)) {
            return 160;
        }
        String lower = term.toLowerCase();
        if (lower.equals("socket") || lower.equals("send") || lower.equals("recv") || lower.equals("url") || lower.equals("host") || lower.equals("port")) {
            return 40;
        }
        return 20;
    }

    private long[] targetValues(Target t) {
        LinkedHashSet<Long> values = new LinkedHashSet<Long>();
        values.add(Long.valueOf(t.ghidraAddress));
        if (t.phase2Va != 0L) {
            values.add(Long.valueOf(t.phase2Va));
            Long fileOff = elfVaToFileOffset(t.phase2Va);
            if (fileOff != null) {
                values.add(fileOff);
            }
        }
        long[] out = new long[values.size()];
        int i = 0;
        for (Long v : values) {
            out[i++] = v.longValue();
        }
        return out;
    }

    private List<RawPointerRef> findRawPointersTo(long[] values, int max) {
        String cacheKey = rawPointerKey(values, max);
        if (rawPointerCache.containsKey(cacheKey)) {
            return rawPointerCache.get(cacheKey);
        }
        List<RawPointerRef> out = new ArrayList<RawPointerRef>();
        Set<Long> targets = new HashSet<Long>();
        for (long value : values) {
            targets.add(Long.valueOf(value));
        }
        for (MemoryBlock block : memory.getBlocks()) {
            if (!isPointerTableBlock(block)) {
                continue;
            }
            Address cur = block.getStart();
            Address last = safeSubtract(block.getEnd(), 7);
            while (cur != null && last != null && cur.compareTo(last) <= 0 && out.size() < max) {
                try {
                    long value = memory.getLong(cur);
                    if (targets.contains(Long.valueOf(value))) {
                        RawPointerRef ref = new RawPointerRef();
                        ref.slot = cur;
                        ref.value = value;
                        ref.block = block.getName();
                        ref.function = functionContaining(cur);
                        ref.referencesToSlot = countReferences(currentProgram.getReferenceManager().getReferencesTo(cur), 20);
                        out.add(ref);
                    }
                } catch (MemoryAccessException e) {
                    // Ignore unreadable bytes.
                }
                cur = safeAdd(cur, 8);
            }
        }
        rawPointerCache.put(cacheKey, out);
        return out;
    }

    private boolean isPointerTableBlock(MemoryBlock block) {
        String name = block.getName();
        return ".data.rel.ro".equals(name) || ".data".equals(name) || ".got".equals(name) ||
            ".got.plt".equals(name);
    }

    private String rawPointerKey(long[] values, int max) {
        long[] copy = Arrays.copyOf(values, values.length);
        Arrays.sort(copy);
        StringBuilder sb = new StringBuilder();
        sb.append(max).append(":");
        for (long value : copy) {
            sb.append(hx(value)).append(",");
        }
        return sb.toString();
    }

    private void writeRawPointers(Json j, List<RawPointerRef> refs) {
        j.arrayStart();
        for (int i = 0; i < refs.size(); i++) {
            if (i > 0) {
                j.comma();
            }
            RawPointerRef r = refs.get(i);
            j.objStart();
            j.prop("kind", "RAW_POINTER_REFERENCE", true);
            j.prop("slot", fmt(r.slot), true);
            j.prop("slot_block", r.block, true);
            j.prop("value", hx(r.value), true);
            j.prop("references_to_slot", r.referencesToSlot, true);
            j.propName("function_containing_slot", false);
            writeFunctionSummary(j, r.function);
            j.objEnd();
        }
        j.arrayEnd();
    }

    private List<RelocInfo> findAssociatedRelocations(Target t, int max) {
        String cacheKey = t.label + "|" + hx(t.ghidraAddress) + "|" + max;
        if (relocationCache.containsKey(cacheKey)) {
            return relocationCache.get(cacheKey);
        }
        List<RelocInfo> out = new ArrayList<RelocInfo>();
        Set<Long> targetValues = new HashSet<Long>();
        for (long v : targetValues(t)) {
            targetValues.add(Long.valueOf(v));
        }
        Address targetAddr = addr(t.ghidraAddress);
        Iterator<Relocation> it = relocationTable.getRelocations();
        while (it.hasNext() && out.size() < max) {
            Relocation r = it.next();
            Address ra = r.getAddress();
            boolean match = false;
            String reason = "";
            if (ra != null && Math.abs(ra.getOffset() - t.ghidraAddress) <= 0x200L) {
                match = true;
                reason = "near_target";
            }
            long[] values = r.getValues();
            if (!match && values != null) {
                for (long v : values) {
                    if (targetValues.contains(Long.valueOf(v))) {
                        match = true;
                        reason = "relocation_value_matches_target";
                        break;
                    }
                }
            }
            if (!match && ra != null) {
                try {
                    long patched = memory.getLong(ra);
                    if (targetValues.contains(Long.valueOf(patched))) {
                        match = true;
                        reason = "patched_slot_matches_target";
                    }
                } catch (Exception e) {
                    // Ignore.
                }
            }
            if (match) {
                RelocInfo info = new RelocInfo();
                info.address = ra;
                info.type = r.getType();
                info.typeName = relocationTypeName(r.getType());
                info.status = r.getStatus() == null ? null : r.getStatus().toString();
                info.symbolName = r.getSymbolName();
                info.values = values;
                info.bytes = bytesHex(r.getBytes());
                info.block = blockName(ra);
                info.reason = reason;
                try {
                    info.patchedLong = hx(memory.getLong(ra));
                } catch (Exception e) {
                    info.patchedLong = null;
                }
                out.add(info);
            }
        }
        relocationCache.put(cacheKey, out);
        return out;
    }

    private void writeRelocs(Json j, List<RelocInfo> relocs) {
        j.arrayStart();
        for (int i = 0; i < relocs.size(); i++) {
            if (i > 0) {
                j.comma();
            }
            RelocInfo r = relocs.get(i);
            j.objStart();
            j.prop("kind", "RELOCATION_REFERENCE", true);
            j.prop("address", fmt(r.address), true);
            j.prop("block", r.block, true);
            j.prop("type", r.type, true);
            j.prop("type_name", r.typeName, true);
            j.prop("status", r.status, true);
            j.prop("symbol_name", r.symbolName, true);
            j.prop("reason", r.reason, true);
            j.prop("patched_long", r.patchedLong, true);
            j.prop("bytes", r.bytes, true);
            j.propName("values", false);
            j.arrayStart();
            if (r.values != null) {
                for (int k = 0; k < r.values.length; k++) {
                    if (k > 0) {
                        j.comma();
                    }
                    j.stringValue(hx(r.values[k]));
                }
            }
            j.arrayEnd();
            j.objEnd();
        }
        j.arrayEnd();
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

    private void writeReferences(Json j, ReferenceIterator refs, int max) {
        j.arrayStart();
        int count = 0;
        while (refs.hasNext() && count < max) {
            Reference r = refs.next();
            if (count > 0) {
                j.comma();
            }
            j.objStart();
            j.prop("kind", "GHIDRA_XREF", true);
            j.prop("from", fmt(r.getFromAddress()), true);
            j.prop("to", fmt(r.getToAddress()), true);
            j.prop("type", r.getReferenceType().toString(), true);
            j.prop("operand_index", r.getOperandIndex(), true);
            j.propName("from_function", true);
            writeFunctionSummary(j, functionContaining(r.getFromAddress()));
            j.propName("to_function", false);
            writeFunctionSummary(j, functionContaining(r.getToAddress()));
            j.objEnd();
            count++;
        }
        j.arrayEnd();
    }

    private void writeReferences(Json j, Reference[] refs, int max) {
        j.arrayStart();
        int count = 0;
        if (refs != null) {
            for (Reference r : refs) {
                if (count >= max) {
                    break;
                }
                if (count > 0) {
                    j.comma();
                }
                j.objStart();
                j.prop("kind", "GHIDRA_XREF", true);
                j.prop("from", fmt(r.getFromAddress()), true);
                j.prop("to", fmt(r.getToAddress()), true);
                j.prop("type", r.getReferenceType().toString(), true);
                j.prop("operand_index", r.getOperandIndex(), true);
                j.propName("from_function", true);
                writeFunctionSummary(j, functionContaining(r.getFromAddress()));
                j.propName("to_function", false);
                writeFunctionSummary(j, functionContaining(r.getToAddress()));
                j.objEnd();
                count++;
            }
        }
        j.arrayEnd();
    }

    private int countReferences(ReferenceIterator refs, int max) {
        int count = 0;
        while (refs.hasNext() && count < max) {
            refs.next();
            count++;
        }
        return count;
    }

    private String describeContent(Address a, String kind) {
        if (a == null || memory.getBlock(a) == null) {
            return "NO_MEMORY";
        }
        String ascii = readAscii(a, 160);
        if (ascii != null && ascii.length() >= 3) {
            return "ascii:" + ascii;
        }
        Instruction ins = listing.getInstructionAt(a);
        if (ins != null) {
            return "instruction:" + ins.toString();
        }
        try {
            long value = memory.getLong(a);
            Address dst = addr(value);
            MemoryBlock b = memory.getBlock(dst);
            String suffix = b == null ? "" : " -> " + b.getName() + " " + describeContent(dst, "pointer_target");
            return "u64:" + hx(value) + suffix;
        } catch (Exception e) {
            return "bytes:" + bytesAt(a, 16);
        }
    }

    private String readAscii(Address a, int max) {
        if (a == null || memory.getBlock(a) == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Address cur = a;
        for (int i = 0; i < max; i++) {
            try {
                int c = memory.getByte(cur) & 0xff;
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
        if (sb.length() == 0) {
            return null;
        }
        return redact(sb.toString());
    }

    private String bytesAt(Address a, int max) {
        byte[] bytes = new byte[max];
        try {
            int got = memory.getBytes(a, bytes);
            byte[] clipped = Arrays.copyOf(bytes, Math.max(0, got));
            return bytesHex(clipped);
        } catch (Exception e) {
            return "UNREADABLE";
        }
    }

    private String bytesHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02x", bytes[i] & 0xff));
        }
        return sb.toString();
    }

    private Long elfVaToFileOffset(long va) {
        for (LoadSegment load : LOADS) {
            if (va >= load.vaddr && va < load.vaddr + load.filesz) {
                return Long.valueOf(load.offset + (va - load.vaddr));
            }
        }
        return null;
    }

    private Address addr(long offset) {
        return space.getAddress(offset);
    }

    private Function functionContaining(Address a) {
        if (a == null) {
            return null;
        }
        return currentProgram.getFunctionManager().getFunctionContaining(a);
    }

    private boolean sameFunction(Function a, Function b) {
        if (a == null || b == null) {
            return false;
        }
        return a.getEntryPoint().equals(b.getEntryPoint());
    }

    private String functionOneLine(Function f) {
        if (f == null) {
            return "UNKNOWN";
        }
        return "`" + f.getName() + "` @ `" + fmt(f.getEntryPoint()) + "` body `" + fmt(f.getBody().getMinAddress()) + "-" + fmt(f.getBody().getMaxAddress()) + "`";
    }

    private String blockName(Address a) {
        MemoryBlock b = a == null ? null : memory.getBlock(a);
        return b == null ? null : b.getName();
    }

    private Address safeAdd(Address a, long add) {
        try {
            return a.addNoWrap(add);
        } catch (AddressOverflowException e) {
            return null;
        }
    }

    private Address safeSubtract(Address a, long sub) {
        try {
            return a.subtractNoWrap(sub);
        } catch (AddressOverflowException e) {
            return null;
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

    private String focusLines(String text, String[] terms, int max) {
        if (text == null || terms == null) {
            return "";
        }
        String[] lines = text.split("\\R", -1);
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String line : lines) {
            if (containsAny(line, terms)) {
                if (count > 0) {
                    sb.append('\n');
                }
                sb.append(line.trim());
                count++;
                if (count >= max) {
                    break;
                }
            }
        }
        return limit(redact(sb.toString()), 4000);
    }

    private String limitLines(String s, int maxLines) {
        if (s == null) {
            return null;
        }
        String[] lines = s.split("\\R", -1);
        if (lines.length <= maxLines) {
            return redact(s);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxLines; i++) {
            sb.append(lines[i]).append('\n');
        }
        sb.append("/* TRUNCATED: ").append(lines.length - maxLines).append(" additional lines */");
        return redact(sb.toString());
    }

    private String limit(String s, int max) {
        if (s == null) {
            return "";
        }
        if (s.length() <= max) {
            return s;
        }
        return s.substring(0, Math.max(0, max - 14)) + "...<TRUNCATED>";
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

    private PrintWriter md(String name) throws Exception {
        phase3cDir.mkdirs();
        return new PrintWriter(new File(phase3cDir, name), StandardCharsets.UTF_8.name());
    }

    private String fmt(Address a) {
        if (a == null) {
            return null;
        }
        return hx(a.getOffset());
    }

    private String hx(long value) {
        return "0x" + Long.toHexString(value);
    }

    private String fmtNullable(Long value) {
        return value == null ? null : hx(value.longValue());
    }

    private String nullDash(String s) {
        return s == null ? "-" : s;
    }

    private String yesNo(boolean b) {
        return b ? "YES" : "NO";
    }

    private String escMd(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("|", "\\|").replace("\n", "<br>");
    }

    private String joinAddresses(List<Address> addrs, int max) {
        if (addrs == null || addrs.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        int count = Math.min(max, addrs.size());
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(fmt(addrs.get(i)));
        }
        if (addrs.size() > max) {
            sb.append(", ...");
        }
        return sb.toString();
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

        void stringValue(String value) {
            out.print(q(value));
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
