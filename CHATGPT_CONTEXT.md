# ChatGPT context index

Updated: 2026-08-29 (Phase16F-R completed)

## Purpose

This file is the entry point for a new AI-assisted analysis session. The repository contains static and runtime observations about Apex Legends Mobile, Ghidra scripts, machine-readable exports, and the evidence needed to continue the investigation without relying on an earlier chat history.

The original game binaries, phone backups, raw logs, bulk function inventory, and local Ghidra database are intentionally not published. They remain local because they are proprietary, private, large, or unnecessary for reviewing the cleaned conclusions exported here.

## Read first

Start with
`platform-tools/ApexMobileBackup/Analyse/Codex/Phase16F-R/REPORT_PHASE16F_R.md`,
then read Phase16F, Phase16E, Phase16D, Phase16C, Phase16B, Phase16A,
Phase15Z, Phase15Y, Phase15X, Phase15W, Phase15V, Phase15U, and Phase15T before using the
historical order below. This avoids repeating conclusions that were later
corrected:

- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase16F/REPORT_PHASE16F.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase16E/REPORT_PHASE16E.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase16D/REPORT_PHASE16D.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase16C/REPORT_PHASE16C.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase16B/REPORT_PHASE16B.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase16A/REPORT_PHASE16A.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15Z/REPORT_PHASE15Z.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15Y/REPORT_PHASE15Y.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15X/REPORT_PHASE15X.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15W/REPORT_PHASE15W.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15V/REPORT_PHASE15V.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15U/REPORT_PHASE15U.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15T/REPORT_PHASE15T.md`

1. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15S/REPORT_PHASE15S.md`
2. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15R/REPORT_PHASE15R.md`
3. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15Q/REPORT_PHASE15Q.md`
4. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15P/REPORT_PHASE15P.md`
5. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15O/REPORT_PHASE15O.md`
6. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15N/REPORT_PHASE15N.md`
7. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15M/REPORT_PHASE15M.md`
8. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15L/REPORT_PHASE15L.md`
9. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15K/REPORT_PHASE15K.md`
10. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15J/REPORT_PHASE15J.md`
11. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15I/REPORT_PHASE15I.md`
12. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15H/REPORT_PHASE15H.md`
13. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15G/REPORT_PHASE15G.md`
14. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15F/REPORT_PHASE15F.md`
15. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15E/REPORT_PHASE15E.md`
16. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15D/REPORT_PHASE15D.md`
17. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15C/REPORT_PHASE15C.md`
18. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15B/REPORT_PHASE15B.md`
19. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15A/REPORT_PHASE15A.md`
20. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase14/REPORT_PHASE14.md`
21. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase13/REPORT_PHASE13.md`
22. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase12/REPORT_PHASE12.md`
23. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase11/REPORT_PHASE11.md`
24. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase10/REPORT_PHASE10.md`
25. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9D/REPORT_PHASE9D.md`
26. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9C/REPORT_PHASE9C.md`
27. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9B/REPORT_PHASE9B.md`
28. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9/REPORT_PHASE9.md`
29. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase8/REPORT_PHASE8.md`
30. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7C_resume/REPORT_PHASE7C_RESUME.md`
31. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7/REPORT_PHASE7.md`
32. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase6/REPORT_PHASE6.md`
33. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase5/REPORT_PHASE5.md`
34. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase4/REPORT_PHASE4.md`
35. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`
36. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/00_address_model.md`
37. The relevant topic report and matching JSON from the newest phase
38. Phase2 and Phase3B only as historical evidence, with Phase3C and later phases taking precedence

## Authoritative conclusions

- `CONFIRMED`: Phase16E completes the non-destructive PRA-LX1 physical/host
  preflight. Three stable normal-ADB reads reported 97 percent battery, USB
  charging, 25.0 C, and the expected locked/green/enforcing baseline. The
  active Windows USB route uses a root hub without an external hub.
- `CONFIRMED`: the exact PotatoNV-linked `Huawei drivers testpoint.rar` is
  archived locally with SHA256
  `A5C9A980228A3505792A97C9AD445A88582A8417578453D13F4E0115EA241BD3`.
  Its MD5 matches Android File Host metadata. The x64 VCOM `2.0.7.1` INF and
  SYS verify through a Microsoft WHCP-signed, timestamped catalog. The package
  was not installed.
- `CONFIRMED`: PotatoNV archive/executable and exact C33 firmware, kernel,
  ramdisk, and recovery ramdisk hashes all match Phase16D. Recovery material
  remains accessible offline with `MEDIUM` recovery confidence.
- `CONFIRMED`: three local PRA board references were loaded into a landmark
  checklist. The actual phone was not opened, so
  `TESTPOINT_BOARD_MATCH = NOT_INSPECTED` and the Phase16E gate is
  `B READY_PENDING_BOARD_VISUAL_CONFIRMATION`.
- `CONFIRMED`: a future unlock is treated as a full userdata wipe. Apex public
  reinstall material is preserved, but absence of unique personal data cannot
  be proven non-invasively and requires final owner confirmation before any
  destructive phase.
- `CONFIRMED`: Phase16E performed no opening, testpoint contact, driver install,
  PotatoNV execution, fastboot operation, unlock, flash, erase, wipe, root, or
  Apex launch. Phase16F requires separate explicit authorization.
- `CONFIRMED`: Phase16D archives the exact PRA-LX1
  `8.0.0.364(C33)` / Altice `all` / `05014GCW` service package locally. The
  immutable 2,540,397,881-byte archive SHA256 is
  `CC39E24033EEF61F8C477822A66C011ED731AE56C1CBBB822B1D1598150E4C10`;
  its MD5 exactly matches the Android File Host listing.
- `CONFIRMED`: package identity confidence is `HIGH`, while source class stays
  `REPUTABLE_ARCHIVE`, not official. Exact internal release documents,
  `altice/all`, `Cust-033000`, and 43/43 valid `.APP` payload checksum records
  establish model/build/CUST correlation.
- `CONFIRMED`: exact stock `RAMDISK.img` SHA256 is
  `ED91177CF438CCDB256D6507203A42D784FD1B22FC941A25D71B67C153C97D57`;
  exact `RECOVERY_RAMDISK.img` SHA256 is
  `9146C2BE6CC2C77BE08827F9839412C06DB6F85C979C9B5113D1CC3640D46D8A`.
  Both remain local-only and unmodified. Magisk was not used.
- `CONFIRMED`: current PotatoNV upstream documents Kirin 655 support and lists
  PRA with `Kirin 65x (A)`. The official `2022.03` release archive was
  hash-pinned locally but not executed.
- `CONFIRMED`: three independent exact-family visual references agree on the
  PRA mainboard testpoint, with exact-model USB COM functional corroboration.
  Confidence is `VERIFIED_ENOUGH_MEDIUM`; the actual board revision remains
  unconfirmed until separately authorized physical inspection.
- `CONFIRMED`: the local Apex APK and both OBBs still match their known hashes
  and installation documentation is present.
- `CONFIRMED`: Phase16D moves `ROOT_PREPARATION_GATE` to `GO` for PC evidence
  completeness only. Recovery confidence is `MEDIUM`; data-wipe, hard-brick,
  and physical-disassembly risks remain `HIGH`. No phone opening, PotatoNV
  execution, unlock, wipe, flash, root, or Apex launch occurred.
- `CONFIRMED`: Phase16B revalidates the PRA-LX1 as Android 8/API 26 `user`,
  `ro.debuggable=0`, `ro.secure=1`, non-root, SELinux enforcing, with
  `/proc` `hidepid=2` and restrictive perf policy. Apex remained stopped.
- `CONFIRMED`: the installed Apex package is neither debuggable nor
  profileable by shell. `run-as`/JDWP are unavailable by package policy.
- `CONFIRMED`: `debuggerd -b` remains blocked by the Phase14 OS policy result;
  `showmap`, simpleperf, perf, Perfetto, traced, and heapprofd are absent.
  `atrace` cannot expose native PCs, registers, vtables, or indirect branch
  targets.
- `CONFIRMED`: no supported non-invasive method on this device can identify
  the external callback at `CVersionStrategy+0x18`, slot `+0x28`, or provide
  the required runtime library bases. Phase16B gate is
  `D NO_NONINVASIVE_RUNTIME_TRACE_AVAILABLE`; no Phase16C run should occur
  under the current constraints.
- `CONFIRMED`: Phase16A resolves the only proven non-null write to
  `cu::CActionMgr+0x3b8` as `FUN_005bc4bc` at Ghidra `0x005bc4e4`. The method
  is `CActionMgr` vtable slot `+0xe0` and receives a
  `cu::CVersionStrategy_Win32` object.
- `CONFIRMED`: `CVersionMgrImp::Init` creates the heap strategy object and
  stores it at `CVersionMgrImp+0x10`. `CVersionMgrImp::CheckAppUpdate`
  supplies it through strategy slot `+0x48`, resolved to `FUN_0050c4a8`.
- `CONFIRMED`: the callback class implements `cu::IActionMgrCallback`; its
  vtable is Ghidra `0x009789b0`, ELF `0x008789b0`, and slot `+0x00` resolves
  directly to `FUN_0050cb38`.
- `CONFIRMED`: `FUN_0050cb38` forwards the stage and original raw Dolphin
  error unchanged to an externally supplied callback at
  `CVersionStrategy+0x18`, slot `+0x28`. Internal bitfield checks do not alter
  the forwarded value.
- `UNKNOWN`: the external callback class and slot `+0x28` implementation, UE4
  or Lua consumer, UI formatter, and construction of `I54140714` remain
  unresolved. Phase16A gate is
  `C CACTIONMGR_CALLBACK_SLOT_00_IMPLEMENTATION_RESOLVED`.
- `CONFIRMED`: Phase15Z maps `NormalConnectVersionSvr` to `FUN_00550ee4` in
  `libgcloud.so`. Its offline branch constructs `0x0930002a` at Ghidra
  `0x005516e8`-`0x005516f4` with `mov`/`movk` and calls `FUN_00549800`.
- `CONFIRMED`: RTTI maps the action to
  `dolphin::gcloud_version_action_imp`, vtable Ghidra `0x009797d0`, and the
  manager to `cu::CActionMgr`, vtable Ghidra `0x0097b000`.
- `CONFIRMED`: the raw error follows
  `FUN_00549800 -> FUN_005bf94c OnActionError -> FUN_005bf808 ->
  FUN_005be71c ProcessActionError`. The final client callback at
  `cu::CActionMgr+0x3b8`, slot `+0x00`, remains unresolved.
- `CONFIRMED`: the separate `UpdateResult` path is reporting-only. It converts
  the unchanged code with `%u`, then `FUN_005bcce4 -> FUN_004d47cc` writes
  `errcode` and `errmsg` and commits the event.
- `NOT CONFIRMED`: no connected code subtracts `100000000`, extracts
  `54140714`, prepends `I`, or renders a visible code. `I54140714` construction
  and UI ownership remain unknown. Phase15Z gate is
  `D DOLPHIN_ERROR_CREATION_AND_ACTION_CHAIN_RESOLVED`.
- `CONFIRMED`: Phase15Y bounds the existing Phase15U log to `t0 +105 s`
  through `t0 +165 s` and resolves the actual Puffer failure as
  `CONNECT_SERVER_TIMEOUT`, decimal `70254639`, exactly `0x0430002f`.
- `CONFIRMED`: a separate earlier GCloud Dolphin version path starts through
  `version_mgr_imp.cpp`, reports `UpdateResult` code `154140714`
  (`0x0930002a`) with result `-1`, and reaches `ProcessActionError`. This event
  precedes the `+120 s` screenshot; the final Puffer result follows it.
- `CONFIRMED`: exact read-only Ghidra follow-up anchors `ProcessActionError` at
  `libgcloud.so` `0x005be71c` and `VFS_Puffer_OnUpdateResult` at `0x005dfe58`.
  `libUE4.so` has zero hits for the exact new runtime terms.
- `UNKNOWN`: timing and the numeric suffix make the Dolphin result a stronger
  candidate for rendered `I54140714`, but no formatter or direct UI edge is
  present. `I54140714` remains absent from logcat and its construction is not
  confirmed.
- `UNKNOWN`: Phase15Y still does not identify the application callback stored
  at `GCloudPufferImp+0x18` or its slot `+0x10` target. The QTCVFS callback
  boundary is not proven to be the same object supplied to Init as `x2`.
- `CONFIRMED`: Phase15Y gate is `C NAMED_CLIENT_UPDATE_MANAGER_ANCHORED`. The
  existing runtime-log axis is exhausted for external callback identity; a
  future distinct methodology would need a client callback registration trace.
- `CONFIRMED`: Phase15X establishes that `FUN_080d1ac8` restores its frame and
  tail-branches to imported `CreatePuffer` at `0x080d1b84`. The returned facade
  pointer in `x0` is returned directly; the wrapper does not store it.
- `CONFIRMED`: Ghidra resolves zero direct callers for `FUN_080d1ac8`. Its only
  entry references are an `INDIRECTION` at `0x02aa1af0` and `DATA` at
  `0x0347c4e8`; neither has a containing function or xref reader.
- `UNKNOWN`: no static return-value edge ties the facade to a store, owner, or
  same-pointer Init call. Although the known Init ABI uses `x2` for the
  downstream client callback, the client value, class, vtable, and slot `+0x10`
  remain unresolved.
- `CONFIRMED`: Phase15X adds no update-manager, UE4 event, Lua, UI,
  `I54140714`, or progress `2/17` edge. The static Puffer client object-flow
  boundary is exhausted; gate is `G CREATEPUFFER_OBJECT_FLOW_OPAQUE`.
- `CONFIRMED`: Phase15W resolves the callback passed as argument 2 to
  `cu::CPufferInitActionResult::ProcessResult`. It comes from
  `CPufferActionCallBackImp+0x08`, not from a result field.
- `CONFIRMED`: the callback object is the `cu::IPufferCallBack` secondary
  subobject at `this+0x08` of `GCloud::GCloudPufferImp`, created by
  `CreatePuffer`. Its vtable is Ghidra `0x009785f8`, ELF `0x008785f8`.
- `CONFIRMED`: callback slot `+0x10` resolves to `FUN_00503050`, then
  `FUN_00503024`. The latter forwards the success flag and Puffer code without
  observed translation to an externally supplied client callback stored at
  `GCloudPufferImp+0x18`.
- `CONFIRMED`: the earlier result submission object is the distinct
  `cu::CPufferActionCallBackImp`; its slot `+0x20` resolves to `FUN_004f3a14`.
- `CONFIRMED`: exact `libUE4.so` scalar scans return zero hits for each of
  `0x0430002e` through `0x04300032`. The exact `CreatePuffer` import reaches
  only the facade factory; no exact `CreatePufferCallBack` registration anchor
  or downstream callback constructor was found.
- `UNKNOWN`: the concrete external client callback, client error mapper,
  update manager, UI dispatch, and exact `I54140714` construction remain
  unresolved. Phase15W gate is `D CONCRETE_DYNAMIC_CALLBACK_IMPLEMENTATION_RESOLVED`.
- `CONFIRMED`: the Ghidra program uses image base `0x100000`.
- `CONFIRMED`: convert an ELF virtual address with `GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000`.
- `INVALIDATED`: Phase3B conclusions that used Phase2 ELF virtual addresses directly as Ghidra addresses.
- `INVALIDATED`: `FUN_07941d10` as the direct corrected candidate for `RequestAvatarServerList`.
- `PROBABLE`: corrected containing functions for its two code references are `FUN_07a41d0c` at `0x7a41d0c` and `FUN_07a41d4c` at `0x7a41d4c`.
- `CONFIRMED`: `RequestAvatarServerList -> FUN_07a31858 -> FUN_06bc68e8` builds an HTTP GET.
- `CONFIRMED`: callback vtable `0xa732320`, adapter `FUN_06be413c`, handler `FUN_06be3bdc`.
- `CONFIRMED`: the callback emits event `0x138` through `FUN_06be3f4c` with success plus a response-body `FString`.
- `CONFIRMED`: `FUN_06be427c` is a native-to-Lua bridge, not UObject ProcessEvent or a Blueprint dispatcher; it recognizes `.LUA`, pushes typed values and delegates dotted-name dispatch to `FUN_06be4d1c`.
- `PROBABLE`: `FUN_06be3f4c` targets `Script/Tools/EventSystem/EventSystem.lua` and `EventSystem.PostCppEvent`; clear UTF-16 fragments and exact lengths support these values, but eight encoded code units in each value were not independently decoded.
- `CONFIRMED`: `FUN_049a8b54` constructs/resolves a Lua module path, reads bytes through an optional provider or fallback `FUN_049a9694`, removes an optional UTF-8 BOM, and hands the buffer to `FUN_048ab4d0`.
- `PROBABLE`: `FUN_048ab4d0` is the Lua chunk load/compile wrapper. Stripped symbols and a decompiler boundary prevent assigning an exact `lua_load` API name.
- `CONFIRMED`: `FUN_046355e8`, path resolver `FUN_082693bc`, and backend open `thunk_FUN_049825bc` form a generic virtual/platform file pipeline.
- `UNKNOWN`: the concrete Pak/custom/physical backend, runtime mount prefix, exact virtual asset name, physical container, EventSystem entry boundary, and script content type.
- `CONFIRMED`: no decompression or custom decode occurs between the returned byte buffer and chunk handoff except optional BOM removal; any such processing must happen below the reader/provider boundary.
- `PROBABLE`: the four previously inspected PAK indexes were encrypted or otherwise unreadable to the minimal parser. This does not classify individual entry contents.
- `CONFIRMED`: Phase7 originally had no PAK input. Phase7C Resume later recovered local-only ignored copies from the preserved phone; the public repository still contains no OBB, PAK, or extracted original asset.
- `CONFIRMED`: Phase7 decision gate is `D`, `EVENTSYSTEM_CONTAINER_UNKNOWN = YES`.
- `CONFIRMED`: the preserved phone reports Apex package version `1.3.672.546`, code `64003140`, and exposes readable main/patch OBB files through public storage.
- `CONFIRMED`: main OBB SHA256 is `104B313F86A57F1B2A11D81D87E916EFBAB99BBBFB4A031E2D12B0201D5244E0`; patch OBB SHA256 is `BFE887744EC21CB7CE0829F1C1F1BCAF1C400C358185B52D140D75242264973D`. Both exact sizes and known partial hash witnesses match; complete old hashes are unavailable, so status is `SIZE_MATCH_HASH_PARTIAL_ONLY`.
- `CONFIRMED`: all four historical PAKs were recovered locally with exact historical sizes. Their SHA256 values are recorded in `Phase7C_resume/output/recovered_pak_identity.json`.
- `CONFIRMED`: all three historical Lua path witnesses are present; `Client/Launch/ClientLaunch.lua` remains at exact offset `0x250517` in `launch.pak`.
- `CONFIRMED`: none of the eleven requested EventSystem/server-list terms is raw-visible in ASCII/UTF-8 or UTF-16LE across the four PAKs.
- `UNKNOWN`: the EventSystem physical container and entry boundary. Raw-string absence does not distinguish indexed, encoded, compressed, encrypted, or alternate-backend storage.
- `CONFIRMED`: Phase8 found 28 non-PAK OBB-extracted files, all media, and zero PAK sidecars or external entry manifests.
- `CONFIRMED`: the public phone tree is readable and has 94 files, but its only relevant small cache contains two OBB references and no virtual path mapping; raw SDK logs and the 54.8 MB graphics cache were not copied.
- `PROBABLE`: fallback vtable `0xaf710e0` at singleton `0xb7cf9b0` implements Android asset and physical-file access; its OpenRead slot `+0xc0` reaches `0x49825b8/0x49825bc`. The exact Unreal class name remains unknown.
- `CONFIRMED`: loader facade vtable `0xaf721e8` delegates to the fallback singleton, while optional provider global `0xb697528` may supply bytes through slots `+0x28/+0x50`; its concrete type and runtime state are unknown.
- `CONFIRMED`: `FUN_04166f64` performs case-insensitive current-prefix to alternate-prefix replacement. `FUN_046095e4` performs path cleanup, and `FUN_04609488` uses a case-folded CRC-like bucket only for a registered-path map followed by full string comparison.
- `UNKNOWN`: the Lua package searcher between extensionless module lookup and `FUN_049a8b54`, the effective runtime prefix, and the final file candidate.
- `INVALIDATED`: treating raw `Client/Launch/ClientLaunch.lua` at `0x250517` as validation of a virtual path or entry. It is a body witness, not readable index metadata.
- `CONFIRMED`: Phase8 decision gate is `E`, `VIRTUAL_PATH_MAPPING_STILL_UNKNOWN = YES`.
- `CONFIRMED`: Phase9 initially found no complete local `base.apk`. After the preserved phone was explicitly reconnected, only `base.apk` was recovered by read-only `adb pull`; no further phone command was issued.
- `CONFIRMED`: the recovered APK is 96,228,800 bytes with SHA256 `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0`, contains 978 ZIP entries, and has only the `arm64-v8a` ABI across 17 native libraries.
- `CONFIRMED`: the host has the Android Emulator executable but zero configured AVDs, zero Android endpoints, and only an x86_64 system image; no installed ARM64-compatible isolated lab was available.
- `CONFIRMED`: Phase9 did not launch or instrument the client, contact an external service, or produce runtime logs. Its decision gate is `E`, `DYNAMIC_LAB_UNAVAILABLE`.
- `CONFIRMED`: the requested runtime targets were converted with `ELF_VIRTUAL_OFFSET = GHIDRA_ADDRESS - 0x100000`; no process load base was available, so no runtime address was used.
- `UNKNOWN`: all Phase9 runtime values, including the Lua search sequence, effective provider, final open path, ClientLaunch/EventSystem chunk metadata, event `0x138` subscriber, response parser, and URL producer.
- `CONFIRMED`: the installed image is `system-images;android-36.1;google_apis_playstore;x86_64`, revision 4, Android 16/API 36.1, running under emulator 36.4.9.0.
- `CONFIRMED`: the image explicitly declares `x86_64,arm64-v8a`, translated ABI `arm64-v8a`, native bridge `libndk_translation.so`, ARM64-to-x86_64 ISA mapping, and native bridge execution. Phase9B classifies image-level ARM64 translation support as `YES`.
- `CONFIRMED`: Phase9B created the disposable `ApexPhase9Lab` AVD from the existing image without downloading a component or using the preserved phone.
- `CONFIRMED`: no usable WHPX/AEHD hypervisor is active. Accelerated startup was refused; a bounded software TCG attempt exposed no ADB endpoint or completed boot after six minutes.
- `CONFIRMED`: Phase9B did not install the APK, copy OBBs, launch the client, load `libUE4.so`, contact an external service, or use instrumentation. Its final gate is `F SYSTEM_IMAGE_UNSUITABLE_UNKNOWN`.
- `CONFIRMED`: Phase9C identifies the secondary Huawei test phone as `PRA-LX1`, Android 8.0/API 26, with primary and 64-bit ABI `arm64-v8a`; the preserved Samsung was never targeted.
- `CONFIRMED`: before installation, the Huawei had about 6.16 GiB free on `/data`, about 6.14 GiB on `/sdcard`, and no existing Apex package. The exact APK and both exact OBB identities matched.
- `CONFIRMED`: the unchanged APK installed successfully and both OBB remote byte sizes match their verified local copies.
- `CONFIRMED`: under blocked Huawei Wi-Fi/mobile data, Android started `GameActivity`; the client process and activity remained present for more than 90 seconds, with no fatal Java exception, native fatal signal, ABI failure, or `UnsatisfiedLinkError`.
- `CONFIRMED`: the first actionable startup failure is DNS/backend resolution under the deliberate network block; the screen remained black. Phase9C gate is `B HUAWEI_CLIENT_STARTS_THEN_BACKEND_FAILURE`.
- `UNKNOWN`: runtime OBB consumption and `libUE4.so` load state. The shell cannot read process maps and `lsof` exposes no mapped shared objects, so UE4-tagged startup messages are not promoted to native mapping proof.
- `UNKNOWN`: Phase9C still does not reveal ClientLaunch, EventSystem, the Lua package searcher, effective provider, final OpenRead path, event `0x138` subscriber, parser, or URL source. No hook or bypass was attempted.
- `CONFIRMED`: Phase9D reanalysis establishes the first runtime-started backend operation at about `+1.727 s`: TDM `POST https://tdm.mgapex.com:8013/tdm/v1/route` through `HttpCurl::HttpPost`.
- `CONFIRMED`: the TDM operation reports HTTP response code `0`, empty response body, and libcurl code `6` about 15 ms later. DNS fails before any connection or TLS handshake.
- `CONFIRMED`: GCloudCore later starts `GET https://cloudctrl.mgapex.com/cfgpush/getConfig`, followed by `UnknownHost`; PlayCommon later emits an explicit `UnknownHostException` for `play.googleapis.com`.
- `INVALIDATED`: treating early Facebook documentation URLs, MSDK `itop` configuration, or the GVoice endpoint list as earlier runtime requests. The captured run only prints those values.
- `CONFIRMED`: the first TDM POST is a TDM client-route bootstrap and is not linked to Phase5 `RequestAvatarServerList`, which constructs a GET from another caller-supplied URL.
- `CONFIRMED`: no strict Huawei-to-PC-only network could be established with installed tools. The host has no DNS/capture CLI or hosted-network support; the Huawei is non-root/non-debuggable; ADB reverse is TCP-only. No second run or network/system change occurred.
- `CONFIRMED`: Phase9D gate is `A FIRST_BACKEND_REQUEST_CONFIRMED` at the application request-dispatch layer. Transport, TLS parameters, content type, body size, and network delivery remain unobserved.
- `CONFIRMED`: Phase10 traces TDM curl code `6` through `HTTPRouteProc2` into an increasing-delay retry loop confined to the TDM telemetry/report worker. Other application subsystems continue, so TDM is not the observed global startup gate.
- `CONFIRMED`: GCloud RemoteConfig uses a persistent cache, caller-provided typed defaults, three 10-second retries, and success-only observer refresh. The cache was absent in the run; plugin startup continues after failure, while sufficiency for gameplay/Login remains `UNKNOWN`.
- `CONFIRMED`: PlayCommon timestamp/log upload belongs to a separate Google Play/Finsky process and is not an Apex startup dependency.
- `CONFIRMED`: `DownloaderActivity` finds and validates the expansion files, returns result `1`, and `GameActivity` sets `HasAllFiles=true`; the downloader is not the black-screen gate.
- `CONFIRMED`: the manifest declares `android.app.lib_name=UE4`, and `GameActivity::onResumeBody` invokes and returns from `nativeResumeMainInit`. The UE4 native handoff is reached, but its runtime mapping base remains unknown.
- `UNKNOWN`: the first blocking state after `nativeResumeMainInit`, the Lua/ClientLaunch/Login stages, and runtime reachability of `RequestAvatarServerList`.
- `CONFIRMED`: Phase10 decision gate is `E BOOTSTRAP_GATE_STILL_UNKNOWN`. No TDM, GCloud, or Login backend response is justified by current evidence.
- `CONFIRMED`: Phase11 finds the full JNI name once in `libUE4.so` `.dynstr` at Ghidra `0x21dcc9d`, but no matching authoritative dynamic symbol, direct `JNINativeMethod` row, relocation, or exact AArch64 name-address reference.
- `CONFIRMED`: Phase11 does not select an arbitrary stripped function. The native resume callgraph, first wait/state gate, Lua/ClientLaunch/Login reachability, and exact `libUE4.so` runtime base remain `UNKNOWN`.
- `CONFIRMED`: Phase11 decision gate is `F JNI_ENTRYPOINT_NOT_RESOLVED`.
- `CONFIRMED`: Phase12 finds no exact Ghidra or loader-visible ELF dynamic symbol for `JNI_OnLoad` in the local `libUE4.so`; `DT_INIT_ARRAY` is absent and the full JavaVM `GetEnv(..., JNI_VERSION_1_6)` pattern produces zero candidates.
- `UNKNOWN`: Phase12 cannot attribute GameActivity `FindClass`, `RegisterNatives`, a `JNINativeMethod` table, or the `nativeResumeMainInit` function pointer without a confirmed `JNI_OnLoad` root.
- `CONFIRMED`: Phase12 decision gate is `F JNI_ONLOAD_NOT_RESOLVED`.
- `CONFIRMED`: Phase13 inventories exactly 17 ELF64 AArch64 libraries from the
  verified base APK and records each SHA256, SONAME, dependencies, loader
  metadata, and authoritative dynamic symbol count.
- `CONFIRMED`: 12 libraries export a global `JNI_OnLoad` function. None of the
  17 exports the full GameActivity JNI name or the short
  `nativeResumeMainInit` name.
- `CONFIRMED`: all 12 exported roots were resolved individually in Ghidra with
  `-noanalysis -readOnly`. Generic direct `RegisterNatives` paths exist in
  `libanogs.so`, `libGPM.so`, `libmmkv.so`, and `libtgpa.so`, but none of the 12
  libraries contains the target GameActivity class or method identity.
- `CONFIRMED`: only `libUE4.so` contains the full JNI name, short name, slash
  GameActivity class, and `()V`. These remain `STRING_ONLY`; no target export or
  registration row connects them to a native function.
- `CONFIRMED`: GameActivity's DEX initializer loads seven plugin libraries,
  then `gnustl_shared`, `gcloud`, `gcloudcore`, `GVoice`, and finally `UE4`.
  The manifest main library remains `UE4`; library ownership of the target is
  still `UNKNOWN`.
- `CONFIRMED`: Phase13 decision gate is `E STATIC_LIBRARY_OWNERSHIP_EXHAUSTED`.
  No runtime work was performed and no arbitrary libUE4 function was selected.
- `CONFIRMED`: Phase14's Huawei capability audit found a production `user`
  build, `ro.debuggable=0`, and a non-debuggable Apex package. Official
  per-app linker logging was unavailable by requirement.
- `CONFIRMED`: `debuggerd` exists and advertises backtrace mode, but the only
  permitted `debuggerd -b` request failed at the OS process-dump boundary
  before producing a frame. `showmap` is absent. No retry or escalation was
  attempted.
- `CONFIRMED`: the Phase14 launch had no active default network; Apex was
  force-stopped after the failed diagnostic request and airplane mode, Wi-Fi,
  and mobile data were restored exactly. No debug property was used and the
  preserved Samsung was excluded.
- `UNKNOWN`: Phase14 does not establish a native mapping, `libUE4.so` load
  bias, debuggerd PC model, persistent native stall, or JNI runtime address.
  Phase13's `PROBABLE` libUE4 load status remains unchanged.
- `CONFIRMED`: Phase14 decision gate is `E OS_DIAGNOSTIC_PERMISSION_BLOCKED`.
- `CONFIRMED`: Phase15A identifies Windows 11 Home Single Language 25H2 build
  26200.9168 on x86_64 with AMD firmware virtualization, SLAT, and VM monitor
  extensions enabled. No BIOS change is indicated.
- `CONFIRMED`: Windows Hypervisor Platform is available but disabled. Virtual
  Machine Platform is disabled, the full Hyper-V role is not listed on this
  edition, VBS and Memory Integrity are inactive, and no hypervisor is currently
  present.
- `UNKNOWN`: non-elevated BCD inspection was denied, so the configured
  `hypervisorlaunchtype` is `UNKNOWN_ACCESS_DENIED`. No BCD change is justified
  before enabling WHPX, rebooting, and retesting.
- `CONFIRMED`: Android Emulator 36.4.9.0 returns acceleration code 6 with no
  usable hypervisor driver. AEHD and HAXM are absent. VirtualBox 7.2.4 is
  present but is not proven to be the blocker.
- `CONFIRMED`: `ApexPhase9Lab` still targets the Android 36.1 Google Play
  x86_64 image with ARM64 translation through `libndk_translation.so`. Phase15A
  did not boot it or launch Apex.
- `CONFIRMED`: Phase15A decision gate is `B WHPX_ENABLE_REQUIRED`. The minimum
  planned change is to enable Windows Hypervisor Platform and restart; no host
  change was executed.
- `CONFIRMED`: after the user's manual enablement and reboot, Phase15B reads
  `HypervisorPlatform` install-state code `1` (`ENABLED`) and Windows reports a
  currently present hypervisor.
- `CONFIRMED`: Android Emulator 36.4.9.0 now returns acceleration code `0` and
  explicitly reports `WHPX(10.0.26200) is installed and usable`. Phase15A's
  acceleration failure is superseded.
- `CONFIRMED`: `ApexPhase9Lab` remains the only listed AVD and retains the
  Android 36.1 Google Play x86_64 image, translated ABI `arm64-v8a`, and
  `libndk_translation.so`. No AVD was booted.
- `NOT_APPLICABLE`: the Phase15B BCD audit was conditional on acceleration
  failure. Because WHPX works, `hypervisorlaunchtype` was not queried and no
  elevation or BCD change occurred.
- `CONFIRMED`: Phase15B decision gate is `A WHPX_ACCELERATION_CONFIRMED`.
- `CONFIRMED`: the authorized Phase15C retry began with an empty ADB inventory,
  then revalidated WHPX with exit code `0` and the unchanged `ApexPhase9Lab`
  Android 36.1 x86_64 image before launch.
- `CONFIRMED`: the AVD booted without snapshot load/save or wipe, exposed an
  emulator ADB endpoint, reached `sys.boot_completed=1` within the five-minute
  guest limit, remained stable for 30 seconds, and shut down cleanly through
  `emu kill` without forced termination.
- `CONFIRMED`: the Android 16/API 36 `user` guest has primary ABI `x86_64`,
  advertises `arm64-v8a`, configures and contains `libndk_translation.so`,
  enables native-bridge execution, and maps ARM64 to x86_64.
- `CONFIRMED`: `debuggerd` and `showmap` are present but were not used on a
  process. The guest is not debuggable and Apex is not installed.
- `CONFIRMED`: Phase15C decision gate is
  `A AVD_BOOT_ARM64_BRIDGE_CONFIRMED`. This proves lab configuration, not Apex
  ARM64 execution.
- `CONFIRMED`: Phase15D began with no ADB endpoint or physical device, matched
  the exact APK/main-OBB/patch-OBB identities, revalidated WHPX, and booted the
  unchanged `ApexPhase9Lab` without snapshot restore/save or wipe.
- `CONFIRMED`: the exact APK installed without bypass flags as version
  `1.3.672.546`, code `64003140`, primary ABI `arm64-v8a`; both exact OBB files
  were copied with matching guest byte sizes.
- `CONFIRMED`: the guest had no active default network and no default route
  before the only Apex launch. The same process/activity remained present at
  about +5, +20, and +60 seconds without a fatal Java/native or ABI failure.
- `CONFIRMED`: in the verified x86_64/native-bridge guest, Android's loader
  successfully loaded multiple app-private ARM64 libraries and explicitly
  loaded `libUE4.so`. Apex ARM64 translated runtime and the libUE4 load are now
  direct runtime observations.
- `CONFIRMED`: one `showmap` attempt was denied at `/proc/.../smaps`; one
  `debuggerd -b` attempt was denied because root is required. No retry or
  escalation occurred. Mapping, load bias, native frames, runtime address
  model, and JNI runtime addresses remain `UNKNOWN`.
- `CONFIRMED`: the first observed application request was a TDM HTTP POST to
  `tdm.mgapex.com`, which failed at DNS under deliberate isolation. No response
  was received and no new Lua, ClientLaunch, EventSystem, Login, or
  RequestAvatarServerList runtime witness was found.
- `CONFIRMED`: Apex was force-stopped, airplane/Wi-Fi/mobile-data state was
  restored exactly, and the AVD shut down cleanly. Phase15D decision gate is
  `B APEX_TRANSLATED_RUNTIME_CONFIRMED_LIBUE4_MAPPING_UNKNOWN`.
- `CONFIRMED`: Phase15E rehashed the exact APK and parsed its binary manifest
  locally. The application is non-debuggable, `extractNativeLibs=true`, and no
  `<profileable>` element or shell-profileability opt-in exists.
- `CONFIRMED`: no host simpleperf, Perfetto host executable, trace processor,
  Android Studio, or profiler tooling was found. More importantly, official
  non-root native process profiling is unavailable for the exact package by app
  policy. No profiling tool was installed or run.
- `CONFIRMED`: offline-only Phase15D log reanalysis finds direct Berberis
  initialization in the Apex process at `+0.134 s`, the first ARM64 app library
  at `+1.663 s`, `libUE4.so` at `+10.494 s`, and UE4 splash startup at
  `+10.603 s` relative to process creation.
- `CONFIRMED`: generic Android lifecycle reaches `onResume`; system_server
  reports `GameActivity` displayed at `+13.758 s`, then same-process
  `DownloaderActivity` displayed at `+15.864 s`. This proves an Android drawn
  window boundary, not a UE4 gameplay frame or black-screen cause.
- `CONFIRMED`: TDM remains the first application request at `+11.278 s` and
  fails at DNS. GCloud retries and built-in default reads are directly observed.
  The process remains active beyond +60 seconds, so TDM failure does not
  terminate the observed startup run.
- `CONFIRMED`: no Lua, ClientLaunch, EventSystem, Login,
  RequestAvatarServerList, or event `0x138` runtime witness exists in the
  Phase15D log. Phase15E gate is
  `C NEW_RENDER_OR_NATIVE_STARTUP_STAGE_FOUND`.
- `CONFIRMED`: Phase15F resolves `GameActivity` as the sole launcher and
  `DownloaderActivity` as a non-exported same-process helper started by
  `GameActivity$17.run` with request code `0x13881` when `HasAllFiles=false`.
- `CONFIRMED`: the launch is caused by mandatory startup OBB verification, not
  by a missing file or network state. Phase15D state `4` proves both expected
  OBBs passed the production-path and exact-size checks; `ProcessOBBFiles`
  branch `1` proves the validation cache was absent or stale and full local
  ZIP-entry CRC32 validation started.
- `CONFIRMED`: present OBBs can be validated entirely offline. The Google Play
  downloader service is conditional on missing files and was not selected in
  Phase15D. The Android 16 guest could see both OBBs, so there is no evidence of
  an Android 16 storage incompatibility in this path.
- `CONFIRMED`: Phase10 and Phase15D select the same expected DEX/OBB path.
  Phase10 captured result `1`, `HasAllFiles=true`, and `nativeResumeMainInit`;
  Phase15D captured validation start but no completion before its 60-second
  bound. The reason for non-completion in that window remains `UNKNOWN`.
- `PROBABLE`: the reported black interval belongs to the foreground
  `DownloaderActivity`, but its resources define a non-empty progress layout
  and no pixels were captured. The final black-screen cause remains `UNKNOWN`.
- `CONFIRMED`: Phase15F gate is
  `E DOWNLOADER_TRIGGER_RESOLVED_BLOCKER_UNKNOWN`. No new run, ADB/device,
  network, artifact modification, or bypass occurred.
- `CONFIRMED`: Phase15G began with no physical Android endpoint, usable WHPX,
  and the unchanged existing AVD. The installed package/version and both OBB
  names/sizes matched without reinstalling or modifying an artifact.
- `CONFIRMED`: the targeted validation cache was absent before launch. Under
  confirmed network isolation, the only Apex launch again selected downloader
  state `4` and full local validation.
- `CONFIRMED`: the validator remained active at +30 and +60 seconds, then
  reported success 87.926 seconds after full-validation selection. It returned
  exact result `1`; `GameActivity` set `HasAllFiles=true` and resumed.
- `CONFIRMED`: the process stayed stable for 30 additional seconds and the
  downloader did not return. The post-run success cache contains both expected
  OBB names with matching last-modified values.
- `CONFIRMED`: no validator false result, CRC/ZIP/I/O failure, fatal exception,
  or process exit occurred. No direct `nativeResumeMainInit` name appears, so
  this run adds `NO_NEW_EVIDENCE` for that native method.
- `CONFIRMED`: Apex was force-stopped, guest network settings were restored
  exactly, and the AVD shut down with no remaining ADB endpoint or emulator
  process. Phase15G gate is
  `B CURRENT_VALIDATION_COMPLETES_AND_GAMEACTIVITY_RESUMES`.
- `CONFIRMED`: Phase15H began with no Android endpoint or physical device,
  usable WHPX, and the unchanged existing AVD. Package version/code, both OBB
  names/sizes, and the two-row validation cache all matched without modifying
  an artifact or cache.
- `CONFIRMED`: under strict offline isolation, the only Apex launch briefly
  selected downloader files-present state `4`, emitted `Down POB 2`, returned
  result `1` in `0.957 s`, and resumed `GameActivity` with `HasAllFiles=true`.
  The valid Phase15G cache was reused; no full CRC pass repeated.
- `CONFIRMED`: `GameActivity` remained top-resumed, the focused application,
  visible, and reported-drawn through `+300 s` post-resume. The downloader was
  not visible and no other Apex activity replaced the game activity.
- `CONFIRMED`: the Apex process emitted EGL initialization evidence at about
  `+16.305 s` and Vulkan layer discovery at `+17.127 s` post-resume. Local-only
  pixel captures at `+30 s` and `+120 s` show a rendered Lightspeed splash and
  wait UI beneath Android's immersive-mode tutorial overlay.
- `INVALIDATED`: describing the cached Phase15H post-resume screen as confirmed
  pixel-black. The three captures are not black; the cause of the visible wait
  state remains `UNKNOWN`.
- `CONFIRMED`: the permitted thread inventory succeeded but Android collapsed
  every visible thread name to the package process name. No GameThread,
  RenderThread, RHIThread, UE4, or Lua thread name is established.
- `CONFIRMED`: no Lua, ClientLaunch, EventSystem, Login,
  RequestAvatarServerList, or meaningful event `0x138` runtime witness appears
  after post-resume T0. No exact post-resume UE4 function name is established.
- `CONFIRMED`: the first post-resume network attempt is the already-known TDM
  POST at `+0.109 s`; it and later TDM/GCloud retries fail at name resolution.
  No new post-resume host or response appears, and the process stays alive.
- `CONFIRMED`: Apex was force-stopped, guest network state was restored exactly,
  and the AVD shut down with no remaining endpoint or emulator process.
  Phase15H gate is `C NEW_POST_RESUME_UE4_NATIVE_STAGE_CONFIRMED` for the
  process-scoped graphics initialization and rendered-splash boundary; an exact
  UE4 function and gameplay frame remain unconfirmed.
- `PROBABLE`: the event `0x138` consumer is in Lua/PAK content. No extracted Lua source is currently accessible, so its registration, handler, parser and storage remain `UNKNOWN`.
- `CONFIRMED`: `FUN_06bc6ca0` clones the callback delegate; it is not the response handler.
- `UNKNOWN`: the concrete GET URL, supplied dynamically as a UFunction `FString` argument.
- `UNKNOWN`: the response wire format and server-list fields; the native callback does not parse them.
- `UNKNOWN`: a proven `LoginMgr -> RequestAvatarServerList` call path.
- `CONFIRMED`: `EVENTID_AVATARSERVERLIST_RETURN = 0x138 / 312` and is emitted in the response path.
- `PROBABLE`: `GameServerBackupIpList` belongs to `Login` and is a `TArray<FName>`; offset `0x150` is `CONFIRMED`, while the writer and element contents are `UNKNOWN`.
- `UNKNOWN`: whether `SyncPayloadToGameServer` is an Unreal RPC and which native function implements it.
- `UNKNOWN`: the effective UEDSToolkit transport.
- `UNKNOWN`: the exact consumer of event `0x138`, its response parser, the writer of `GameServerBackupIpList`, and a complete game-server connection path.
- `CONFIRMED`: several configuration, update, login, telemetry, payment, and voice endpoints exist in the artifacts; none is proven to be a game-server endpoint.

## Repository map

- `platform-tools/ApexMobileBackup/Analyse/Codex/01_endpoints.md`: confirmed and rejected endpoint indicators.
- `platform-tools/ApexMobileBackup/Analyse/Codex/02_server_flow.md`: conservative backend flow reconstruction.
- `platform-tools/ApexMobileBackup/Analyse/Codex/03_logs.md`: runtime log findings.
- `platform-tools/ApexMobileBackup/Analyse/Codex/04_pak_analysis.md`: PAK observations and limits.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase2/`: static-resource and binary-context research before Ghidra.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/`: Ghidra installation, import, scripts, initial reports, and raw execution evidence.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3B/`: deeper exports produced before the address-model correction.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/`: corrected address model, corrected exports, invalidations, and current conclusions.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase4/`: Unreal registration tables and the first confirmed request runtime path.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase5/`: HTTP callback, response event, URL-source and downstream storage analysis.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase6/`: native-to-Lua event dispatch, consumer search, script boundary, parser/storage limits, URL and SyncPayload follow-up.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7/`: Lua loader, virtual file pipeline, encoded-name status, local asset availability, EventSystem location gate, and next authorized evidence source.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7B/`: local PAK/OBB filename search before phone recovery.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7C/`: initial ADB attempt where no device was detected.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7C_resume/`: successful read-only phone OBB recovery, PAK identities, raw target scans, and build witnesses.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase8/`: OBB metadata and sidecar inventory, public cache search, fallback backend/vtable analysis, path-transform boundary, ClientLaunch validation limit, and EventSystem mapping gate.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9/`: isolated-lab availability audit, refreshed artifact identity, corrected runtime offsets, explicit non-observations, and the safe prerequisites for a future runtime pass.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9B/`: exact x86_64 image inventory, ARM64 native-translation evidence, disposable AVD attempt, host hypervisor boundary, and cleaned startup gate.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9C/`: secondary Huawei inventory, compatibility and installation evidence, offline no-hook startup result, runtime-loader limits, EventSystem status, and safety boundary.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9D/`: local-only Phase9C log reanalysis, ordered bootstrap backend requests, first TDM request metadata, strict-network feasibility audit, and connection/TLS boundary.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase10/`: TDM and GCloud failure paths, default/cache behavior, PlayCommon process attribution, OBB gate validation, ordered bootstrap graph, native startup boundary, and Login reachability.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase11/`: exact JNI-name resolution attempts, dynamic-table evidence, blocked local callgraph, lifecycle correlation, reachability limits, and the next static registration target.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase12/`: exact `JNI_OnLoad` resolution paths, loader metadata, conventional GetEnv-pattern result, blocked GameActivity registration path, and final gate.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase13/`: exact 17-library
  APK inventory, dependency graph, JNI exports and strings, DEX library-loading
  order, targeted JNI_OnLoad validation, candidate classification, unresolved
  owner, and static-exhaustion gate.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase14/`: sanitized Huawei
  native-diagnostic capability audit, offline boundary, failed backtrace-only
  request, unavailable mapping/PC/stall/JNI results, restoration proof, and OS
  diagnostic-permission gate.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15A/`: cleaned Windows
  host, CPU virtualization, WHPX/VBS, emulator acceleration, existing AVD,
  AEHD/HAXM/VirtualBox, required-change, and read-only stop-boundary evidence.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15B/`: cleaned post-reboot
  WHPX enabled state, successful emulator acceleration check, unchanged AVD and
  ARM64-translation metadata, skipped conditional BCD audit, and next gate.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15C/`: cleaned WHPX/AVD
  preflight, successful guest boot, identity and ABI, native-bridge path and ISA
  mapping, diagnostic inventory, Apex absence, stability, clean shutdown, and
  final gate. Complete emulator logs remain local-only.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15D/`: exact artifact and
  install evidence, guest storage, network isolation, one bounded offline Apex
  launch, translated ARM64 loader evidence, libUE4 status, denied native
  diagnostics, cleaned runtime-stage metadata, restoration, and final gate.
  APK/OBB files and complete runtime logs remain local-only.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15E/`: exact manifest
  profileability, host profiler inventory, sanitized Phase15D startup/native
  loader/lifecycle/network/render timelines, Lua/Login searches, official
  profiling decision, and next gate. No new runtime operation occurred.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15F/`: exact activity
  manifest, GameActivity/downloader callgraph, OBB path and validator,
  downloader state/result flow, Android storage/network compatibility,
  Phase10 comparison, sanitized runtime/UI correlation, and final gate. No new
  runtime operation occurred; raw APK/DEX, logs, and Ghidra output remain local.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15G/`: cleaned preflight,
  validation-cache state, network isolation, one bounded CRC timeline, exact
  result/resume transition, failure exclusions, post-run cache, restoration,
  shutdown, and final gate. Raw logcat, activity dumps, endpoint/process data,
  and the local script remain ignored.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15H/`: cleaned cache-reuse
  fast path, post-resume clock, stable activity/window state, constrained thread
  inventory, process-scoped EGL/Vulkan startup, rendered screen classification,
  negative Lua/Login searches, ordered offline network attempts, restoration,
  shutdown, and final gate. Raw logs, dumps, screenshots, endpoint/process data,
  and the local run script remain ignored.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15I/`: cleaned screenshot
  layer classification, APK splash resource and Java exit flow, targeted UE4
  asset exclusion, wait timeline, logging frameworks, official SDK file sinks,
  public accessibility witness, client-stage tag limits, and final gate. No new
  runtime operation occurred; raw screenshots, logs, binaries, and scratch
  analysis remain ignored.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15J/`: cleaned public SDK
  log inventory and comparison, one bounded offline launch, cache reuse,
  filtered tags, Apex Android dialog window boundary, splash-exit limits,
  client-stage non-observations, network timeline, restoration, and final gate.
  Raw official logs, logcat, UI/window dumps, and scripts remain ignored.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15K/`: cleaned physical
  device/WHPX/AVD preflight, baseline UI-capture capability, package/OBB/cache
  validation, persistent prelaunch SystemUI ANR stop gate, restoration, and
  shutdown. Apex was not launched; raw hierarchy and window dumps remain
  ignored.
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15L/`: cleaned 120-second
  SystemUI stability preflight, baseline hierarchy, package/OBB/cache and
  network checks, one offline Apex launch, exact dialog hierarchy/text,
  targeted DEX/native correlation, role and blocking classification,
  restoration, shutdown, and final gate. Raw XML, screenshot attempt, dumps,
  binaries, disassembly, Ghidra output, and scripts remain ignored.

## Machine-readable evidence

- `Phase3/output/elf_libUE4_summary.json`: ELF structure summary.
- `Phase3/output/ghidra_nearest_functions.json`: nearest-function lookups used by the earlier analysis.
- `Phase3/Phase3B/output/*.json`: target callgraph and xref exports; interpret addresses through Phase3C corrections.
- `Phase3/Phase3C/output/*.json`: corrected target exports and address mapping.
- `Phase4/output/*.json`: Unreal registration tables, event metadata, property metadata, and request runtime probe.
- `Phase5/output/*.json`: request construction, callback vtables, response event, response representation, storage check, and SyncPayload thunk.
- `Phase6/output/*.json`: event emitter, Lua dispatcher/core, event-consumer boundary, parser/storage status, URL source and SyncPayload receiver evidence.
- `Phase7/output/*.json`: local inventory and PAK-search availability, encoded names, Lua loader, asset mapping, EventSystem location, subscriber status, and URL-source status.
- `Phase7C_resume/output/*.json`: cleaned phone/OBB identity, recovered PAK identity, EventSystem target scan, and Lua witness evidence.
- `Phase8/output/*.json`: complete non-PAK inventory, backend/vtable evidence, mount and path-transform status, ClientLaunch witness limit, EventSystem mapping, public cache results, and URL follow-up.
- `Phase9/output/*.json`: runtime-lab availability, prepared loader/provider offsets, and sanitized negative results for ClientLaunch, EventSystem, event subscriber, parser, and URL source.
- `Phase9B/output/*.json`: installed image identity, ARM64 translation evidence, gated APK-install result, AVD startup result, and final host/runtime boundary.
- `Phase9C/output/*.json`: sanitized Huawei inventory, compatibility, APK install, client startup, runtime-loader, and EventSystem results.
- `Phase9D/output/*.json`: backend sequence, lab-network audit, first connection and request metadata, unchanged client progress, and `libUE4.so` status.
- `Phase10/output/*.json`: TDM/GCloud failure paths, default configuration sources, bootstrap graph, first unresolved stall region, and Login reachability.
- `Phase11/output/*.json`: JNI resolution evidence, empty blocked callgraph, unresolved state/worker/wait sets, Lua/Login reachability limits, and final gate.
- `Phase12/output/*.json`: `JNI_OnLoad` resolution evidence, unresolved GameActivity lookup/registration/table boundaries, resume target status, validation rows, and final gate.
- `Phase13/output/*.json`: native library identity and loader metadata,
  dependency edges, JNI symbol/string evidence, Java library-loading order,
  candidate classes, resume status, and final gate.
- `Phase14/output/*.json`: sanitized runtime capabilities, mapping status,
  snapshot outcomes, PC-model boundary, stall status, JNI runtime status, and
  final gate. Raw device output remains local-only.
- `Phase15A/output/*.json`: cleaned host/build, CPU virtualization, Windows
  feature/VBS, emulator acceleration, AVD/native-bridge, and required-change
  status without hostname, account, or absolute-path data.
- `Phase15C/output/*.json`: cleaned boot timing and flags, guest identity/ABI,
  native-bridge configuration, diagnostic inventory, Apex absence, stability,
  and clean shutdown. No endpoint port, device identifier, or raw log is
  published.
- `Phase15D/output/*.json`: cleaned exact artifact identities, APK/OBB install,
  guest network isolation, bounded process lifetime, translated ARM64 loader
  evidence, native-diagnostic limits, libUE4 status, runtime-stage metadata,
  and clean restoration/shutdown. No raw log, endpoint port, process identifier,
  private path, or URL query is published.
- `Phase15E/output/*.json`: cleaned manifest profileability, profiler inventory,
  relative startup and ARM64 loader timelines, negative Lua/Login searches,
  sanitized network operations, and Android render/display boundaries. The
  Phase15D raw log remains local-only.
- `Phase15F/output/*.json`: cleaned activity metadata, launch condition,
  downloader trigger, OBB checks, state machine, return flow, Android storage
  compatibility, relative runtime correlation, and final gate. Raw DEX/Ghidra
  output and the Phase15D log remain local-only.
- `Phase15G/output/*.json`: cleaned preflight, validation cache, relative CRC
  timeline, result transition, failure classification, network restoration,
  and shutdown evidence. Raw runtime output remains local-only.
- `Phase15H/output/*.json`: cleaned preflight/cache identity, downloader fast
  path, post-resume timeline, activity/window state, thread-name limits,
  graphics/runtime stage, negative Lua/Login evidence, sanitized network order,
  screen classifications, and shutdown. No raw log, screenshot, process ID,
  endpoint, private path, or URL query is published.
- `Phase15I/output/*.json`: cleaned screenshot classification, Android wait
  ownership, splash exit conditions, relative wait timeline, logging framework
  and sink inventory, client-stage tag limits, and final gate. No raw image,
  log, APK, DEX, native library, process identifier, or private path is
  published.
- `Phase15J/output/*.json`: cleaned preexisting/runtime official-log metadata,
  Apex dialog window counts, splash-exit status, client-stage results, relative
  offline network attempts, and shutdown evidence. No raw log, UI dump,
  process identifier, request body, query, token, account, or private path is
  published.
- `Phase15K/output/*.json`: cleaned preflight, baseline hierarchy capability,
  gated dialog non-observation, unresolved role/callgraph/splash status, network
  restoration, and shutdown evidence. No XML, screenshot, logcat, window ID,
  endpoint, process identifier, or private path is published.
- `Phase15L/output/*.json`: cleaned SystemUI stability checkpoints, hierarchy
  capture status, dialog timing and complete text, targeted creator callgraph,
  role/blocking classification, and shutdown evidence. No raw XML, screenshot,
  log, APK, DEX, native library, endpoint, process identifier, window ID, or
  private path is published.

The full 467,079-function inventory and raw Ghidra execution logs remain local-only. Their relevant results are summarized in the reports and smaller JSON files committed here.

## Phase15I authoritative correction

- `CONFIRMED`: Phase15I was local-only. It used retained Phase15H images,
  window data, logs, exact APK/DEX/native libraries, and prior inventories. No
  ADB, emulator, phone, Apex launch, or network operation occurred.
- `INVALIDATED`: treating the visible Phase15H `Wait` action as one Apex or UE4
  wait UI. It belongs to a SystemUI ANR dialog that was drawn before the Apex
  splash started.
- `CONFIRMED`: the Lightspeed visual is the exact APK's raw splash video,
  inflated by `GameActivity.onCreate` into an Android `AspectVideoView`. No UE4
  PAK asset is required to explain the splash.
- `CONFIRMED`: an additional Apex-owned Android application dialog appears
  around `+25.269 s` and exposes an `OK` action. Its obscured message and exact
  creator remain `UNKNOWN` and it is not a proved blocker.
- `CONFIRMED`: the Java splash exit is
  `AndroidThunkJava_DismissSplashScreen()`. Automatic removal is disabled in
  the exact build, so video completion alone is insufficient. The native
  caller/state that triggers explicit dismissal remains `UNKNOWN`.
- `NO_EVIDENCE`: a direct TDM or GCloud edge controlling splash dismissal.
  Their failures are correlated retries, not established wait dependencies.
- `CONFIRMED`: official SDK file logging is compiled and observed initialized
  for GCloudCore/MSDK, with TDM file writers also present. Phase8 historically
  recorded eight files in a public SDK log directory without collecting their
  contents. This is a shell-readable official sink witness, but not proof of
  named UE4/Lua/Login stage messages.
- `UNKNOWN`: an exact UE4 core file-log sink. No targeted `Saved/Logs`, project
  log, or native file-output chain was established.
- `CONFIRMED`: useful observed tags are `UE4`, `GCloudCore`, `GCloud`, `TDM`,
  `[MSDK]`, `MSDK`, `[CrashSightReport]`, and `PluginMSDK`. No exact named
  client-stage log tag was established for Lua, ClientLaunch, EventSystem,
  LoginMgr, RequestAvatarServerList, or OpenServerList.
- Phase15I gate is `B OFFICIAL_READABLE_APPLICATION_LOG_SINK_FOUND`. The next
  bounded runtime method, only if separately authorized, is
  `READ_OFFICIAL_CLIENT_LOG_ONLY`.

## Phase15J authoritative runtime result

- `CONFIRMED`: no physical device was present. WHPX was usable and the unchanged
  `ApexPhase9Lab` AVD booted without wipe, reinstall, or snapshot load/save.
- `CONFIRMED`: five official SDK files were readable before Apex launch: two
  GCloudCore logs, two empty GCloud logs, and one MSDK XLog file. Their only
  `Login` hits are a generic SDK provider list, not an Apex Login stage.
- `CONFIRMED`: one bounded offline Apex launch was performed. Both OBBs and the
  two-row validation cache matched. Downloader state `4` returned result `1`
  in about `0.257 s`, no full validation occurred, and `GameActivity` resumed.
- `CONFIRMED`: Apex remained alive with application windows present through
  `+180 s` post-resume. No user account or application click was used.
- `CONFIRMED`: official files increased from five to eight by `+30 s`: one new
  GCloudCore log, one empty GCloud log, and one MSDK XLog file. Existing files
  did not grow. New content is SDK initialization/configuration and offline
  request activity only.
- `CONFIRMED`: the established useful logcat tags emitted messages, but neither
  official files nor logcat provide a new Lua, ClientLaunch, EventSystem,
  LoginMgr, RequestAvatarServerList, or event `0x138` runtime witness.
- `CONFIRMED`: the Apex-owned Android window count changes from two at `+15 s`
  to three at `+30 s`. The new wrap-content `GameActivity` window persists
  through `+180 s`, reproducing the Phase15H Apex dialog boundary.
- `UNKNOWN`: the Apex dialog title, message, and role. The permitted UI command
  returned only completion status rather than hierarchy XML while the
  preexisting SystemUI ANR and immersive overlays remained foreground. No
  dialog button was clicked.
- `NO_NEW_EVIDENCE`: splash video completion, Java splash dismissal, or the
  native dismissal trigger. The Android launcher splash exit is a separate OS
  transition and does not prove APK video-splash dismissal.
- `NO_EVIDENCE`: TDM or GCloud as the first blocking dependency. Both fail at
  name resolution while the client remains alive and the dialog persists.
- `CONFIRMED`: Apex was force-stopped, guest network state restored exactly,
  the AVD shut down, and no ADB endpoint or emulator process remains. A later
  collection-only boot copied all eight public logs without launching Apex and
  was likewise restored and shut down.
- Phase15J gate is `D OFFICIAL_LOGS_READABLE_SDK_ONLY_NO_CLIENT_STAGE`. Do not
  repeat broad official-log collection. The narrow future observation, only if
  separately authorized, is a read-only Apex dialog hierarchy capture with the
  unrelated SystemUI overlay absent.

## Phase15K authoritative preflight result

- `CONFIRMED`: no physical Android device was present. WHPX was usable and the
  unchanged `ApexPhase9Lab` AVD booted without wipe, reinstall, root, or
  snapshot load/save.
- `CONFIRMED`: a compressed baseline UI hierarchy was written in the guest,
  pulled read-only, and parsed as valid XML before Apex launch.
- `CONFIRMED`: package version/code, both expected OBB names and sizes, and the
  two-row validation cache matched. Cache reuse was not retested because Apex
  was not launched.
- `CONFIRMED`: the initial window preflight was clear, but a visible SystemUI
  ANR appeared before Apex launch and remained after a passive 90-second wait.
- `CONFIRMED`: no UI action was sent, SystemUI was not restarted or stopped,
  and Apex was never launched. Phase15K therefore adds no dialog text,
  callgraph, role, blocking-status, or splash-dismiss evidence.
- `CONFIRMED`: network isolation had been established and was restored exactly;
  the AVD then shut down with no ADB endpoint or emulator process remaining.
- Phase15K gate is `F SYSTEMUI_OVERLAY_PRESENT_STOP`. A future dialog retry is
  valid only after the SystemUI preflight remains clear.

## Phase15L authoritative dialog result

- `CONFIRMED`: no physical device was present, WHPX was usable, and the
  unchanged `ApexPhase9Lab` AVD booted without wipe or snapshot load/save.
- `CONFIRMED`: SystemUI stayed free of ANRs at every checkpoint through the
  120-second settle window and at the final immediate prelaunch check.
- `CONFIRMED`: the baseline compressed hierarchy was valid. Package version
  `1.3.672.546` / code `64003140`, both OBBs, and the two-row validation cache
  matched before launch.
- `CONFIRMED`: one offline Apex launch followed state `4` to result `1` without
  full validation and resumed `GameActivity`. The third Apex-owned wrap-content
  window appeared at `+15.391 s` post-resume without a SystemUI ANR.
- `CONFIRMED`: the five-node Apex hierarchy exposes title
  `Unable to run on this device!`, a complete message saying GLES 3.1 and
  floating-point render targets are unavailable and no ES2 fallback was
  packaged, and one `OK` button. Its role is `DEVICE_COMPATIBILITY`.
- `PROBABLE`: `com.epicgames.ue4.MessageBox01.createAlert()/show()` is the exact
  creator pipeline. DEX confirms that this mechanism creates a non-cancelable
  `AlertDialog` and waits until a button is selected, so explicit acknowledgement
  is required. Exact native text and JNI method-name strings correlate, but no
  direct native trigger caller was found.
- `PROBABLE`: the compatibility dialog blocks bootstrap. There is
  `NO_EVIDENCE` that it directly controls APK splash dismissal, and the native
  `AndroidThunkJava_DismissSplashScreen()` trigger remains `UNKNOWN`.
- `LIMIT`: the XML pull succeeded, but the wrapper stopped on ADB pull progress
  written to stderr before the screenshot command. No Phase15L screenshot was
  captured, and the single-launch limit was respected without a retry.
- `CONFIRMED`: Apex was force-stopped, network state restored exactly, the AVD
  shut down, and no ADB endpoint or emulator process remained.
- Phase15L gate is `B APEX_DIALOG_TEXT_RESOLVED_TRIGGER_UNKNOWN`. Do not perform
  a third dialog-capture run on this AVD without a new technical justification.

## Phase15M authoritative native graphics-gate result

- `CONFIRMED`: exact title materialization in `FUN_059ef114` at Ghidra
  `0x59ef9b0-0x59ef9b4` resolves the Phase15L native graphics error builder.
- `CONFIRMED`: the primary ES31 path requires parsed `GL_VERSION` major `3`,
  minor at least `1`, `bBuildForES31=true`, and
  `r.Android.DisableOpenGLES31Support=0`.
- `CONFIRMED`: the observed dialog condition is failure of that primary path
  combined with `AndroidRuntimeSettings.bBuildForES2=false`.
- `CONFIRMED`: float render-target support is computed from
  `GL_EXT_color_buffer_half_float` or, on GLES3,
  `GL_EXT_color_buffer_float`. It is diagnostic-only in this branch and does
  not select success versus failure.
- `CONFIRMED`: the success path reaches `FUN_059efff8` for selected EGL/OpenGL
  initialization and `FUN_059efe00` for `eglMakeCurrent`. The test occurs after
  partial EGL setup and before final selected context initialization.
- `CONFIRMED`: the direct dialog target is Ghidra `0x81dea3c` / ELF
  `0x80dea3c`; `PROBABLE`: it is the MessageBox helper. Its protected static
  body prevents proving a synchronous call to `MessageBox01.show()I`.
- `UNKNOWN`: native wait-for-dialog behavior and the native splash-dismiss
  trigger. `NO_EVIDENCE`: the resolved graphics success neighborhood controls
  splash dismissal.
- Phase15M gate is `A EXACT_GRAPHICS_GATE_AND_SUCCESS_PATH_RESOLVED`. Raw
  disassembly and the exact binary remain local-only.

## Phase15N authoritative clean-room graphics result

- `CONFIRMED`: no physical Android device was present, WHPX was usable, and
  the required Android 36.1 Google Play x86_64 image was already local.
- `CONFIRMED`: `ApexGraphicsProbe` is a separate AVD. `ApexPhase9Lab` was not
  booted or modified; its three tracked host-file hashes and timestamps match
  preflight.
- `CONFIRMED`: the original `local.graphicsprobe` application requests no
  Internet permission and contains no Apex identifier or material. It was
  built offline with the existing JDK, API 36.1 platform, and Build Tools.
- `CONFIRMED`: all six GPU modes listed by emulator `36.4.9.0` were cold-booted
  separately with snapshot load/save disabled.
- `CONFIRMED`: `auto`, `software`, `lavapipe`, `swiftshader`, and `swangle`
  resolve to Google SwiftShader OpenGL ES 3.0. They fail Phase15M's exact GLES
  predicate but pass both float-extension and real `RGBA16F` FBO diagnostics.
- `CONFIRMED`: `host` exposes OpenGL ES 3.1, both float extensions, a complete
  `RGBA16F` FBO with no GL error, and remains stable for 30 seconds.
- `CONFIRMED`: `host` is the sole legitimate graphics candidate in this
  installed emulator matrix. `APEX_PHASE15M_GATE_COMPATIBLE=YES` is an
  environment classification only; Apex was neither installed nor launched.
- `CONFIRMED`: all emulator endpoints and processes were stopped after the
  matrix. Phase15N gate is
  `A LEGITIMATE_GLES31_AND_FLOAT_RT_MODE_FOUND`.

## Phase15O authoritative host-mode preflight result

- `CONFIRMED`: no physical device was present, WHPX was usable, and
  `ApexPhase9Lab` cold-booted with the command-line-only `-gpu host` override,
  read-only userdata, and snapshot load/save disabled.
- `CONFIRMED`: after the required 60-second settle period, the targeted window
  dump contained a visible `Application Not Responding: com.android.systemui`
  system alert.
- `CONFIRMED`: the required stop gate fired before package/OBB/cache checks,
  network changes, logcat clearing, screenshots, or an Apex launch. The ANR was
  not clicked or otherwise interacted with.
- `UNKNOWN`: whether host GLES31 clears the Phase15L client compatibility
  dialog and reaches graphics, splash, version/update, login, or server-list
  stages. Phase15N's clean-room host capability must not be promoted to client
  runtime proof.
- `CONFIRMED`: the AVD shut down cleanly, no endpoint or target emulator
  process remains, network state was never changed, and all three preserved
  host-file hashes and timestamps match preflight.
- Phase15O gate is `G SYSTEMUI_PRELAUNCH_ANR_STOP`.

## Phase15P authoritative final host-mode retry result

- `CONFIRMED`: no physical Android device was present, WHPX was usable, and
  `ApexPhase9Lab` booted with command-line `-gpu host`, read-only userdata, and
  snapshot load/save disabled.
- `CONFIRMED`: the SystemUI checkpoint was clear at `+1.731 s`, then the
  targeted `+30 s` check found a visible
  `Application Not Responding: com.android.systemui` window at `+32.169 s`.
- `CONFIRMED`: the hard stop fired immediately. The `60/90/120 s` checks,
  package/OBB/cache checks, network isolation, logcat reset, screenshots, and
  Apex launch were not reached. The ANR was not interacted with.
- `UNKNOWN`: host-mode Apex graphics, version/update, login, and server-list
  behavior. Phase15N remains authoritative only for clean-room host GLES31
  capability, not Apex client execution.
- `CONFIRMED`: shutdown was clean, no ADB endpoint or target process remains,
  network state was never changed, and all preserved AVD hashes/timestamps are
  unchanged.
- Phase15P gate is `G REPEATED_HOST_MODE_SYSTEMUI_ANR_STOP`. Do not repeat
  `-gpu host` on this AVD without a new technical reason that addresses the
  prelaunch SystemUI instability.

## Phase15Q authoritative disposable-AVD SystemUI result

- `CONFIRMED`: no physical Android device was present, WHPX was usable, and
  only the disposable `ApexGraphicsProbe` AVD was booted. `ApexPhase9Lab` was
  never booted and its three tracked hashes/timestamps remain unchanged.
- `CONFIRMED`: no application was manually launched. Neither the graphics
  probe nor Apex was started, and guest network state was not changed.
- `CONFIRMED`: `host + read-only` reproduced a visible SystemUI ANR at the
  `30 s` checkpoint (`+30.043 s`). `host + writable` reproduced it at `30 s`
  (`+30.114 s`). `auto + read-only` reproduced it at `60 s` (`+60.173 s`).
- `CONFIRMED`: ActivityManager identifies service-execution timeouts:
  `KeyguardService` after `20053/20259 ms` in the host cases and
  `SystemUIService` after `20515 ms` in the auto control.
- `CONFIRMED`: both host cases exposed the expected Radeon-backed GLES 3.1
  renderer; the auto control exposed SwiftShader GLES 3.0. No emulator host log
  reported context loss, device reset/loss, swap failure, or EGL/OpenGL failure.
- `CORRELATED`: all three detailed ANR records show high kernel CPU in
  SurfaceFlinger (about `88-104%`). This does not establish a renderer root
  cause because the auto control also fails and no direct graphics fault exists.
- `INVALIDATED AS SOLE EXPLANATIONS`: `-read-only`, the host renderer, and
  `ApexPhase9Lab` userdata. The matrix instead supports broader Android 36.1
  Google Play image/SystemUI startup instability in this environment.
- `CONFIRMED`: all emulator endpoints and processes were stopped.
  `FRESH_HOST_RUNTIME_AVD_JUSTIFIED=NO_FOR_NOW`.
- Phase15Q gate is `E EXACT_SYSTEMUI_ANR_REASON_RESOLVED`; the immediate service
  timeout is direct evidence, while the deeper source of CPU saturation remains
  unknown.

## Phase15R authoritative SystemUI blocking-stack result

- `CONFIRMED`: the missing `auto + writable` matrix quadrant booted on the
  disposable `ApexGraphicsProbe` AVD and reproduced a visible SystemUI ANR at
  the `+30.265 s` checkpoint. The exact ActivityManager event occurred at
  `+15.813 s` and records `KeyguardService` waiting `20131 ms`.
- `CONFIRMED`: exactly one valid local-only bugreport contains the exact matching
  SystemUI ANR trace. The main thread is `Runnable` in Dagger provider
  construction under `SystemUIService.onCreate`; no Binder, lock, condition, or
  renderer wait exists on that stack.
- `CONFIRMED`: the Keyguard callback had not been reached. Its bind was requested
  but the binder/result remained absent because service lifecycle work was
  queued behind prolonged SystemUIService startup on the same main thread.
- `CONFIRMED`: SystemUI main accumulated about `2.780 s` runtime and `16.950 s`
  scheduler wait. Total guest CPU reached `96-97%`, CPU PSI `some avg10` reached
  `79.49`, and kernel CPU accounted for `82-83%`.
- `CONFIRMED`: SurfaceFlinger reached `94%` CPU in the ANR interval and its
  RenderEngine thread accounted for `78%` in a focused interval. It is an
  explicit CPU-pressure contributor, but SystemUI has no direct wait edge to
  SurfaceFlinger.
- `CONFIRMED`: no Binder dependency, contested lock, deadlock, memory pressure,
  swap pressure, OOM/LMK, or watchdog event explains the ANR. system_server
  remained responsive while asynchronously tracking the incomplete service.
- `STRONGLY SUPPORTED`: all four host/auto and readonly/writable quadrants now
  fail before any Apex launch. The current Android 36.1 Google Play
  image/environment is unsuitable as a stable runtime baseline.
- `UNKNOWN`: the deep source of the kernel CPU behavior remains unresolved; it
  is not assigned specifically to the image, emulator, renderer, host, or
  hardware.
- `CONFIRMED`: no alternate image is locally installed. A future test with a
  different image in a new disposable AVD is technically justified, but
  Phase15R downloaded and created nothing.
- Phase15R gate is
  `B SYSTEMUI_BLOCKING_STACK_RESOLVED_ROOT_CAUSE_UNKNOWN`.

## Phase15S authoritative PRA-LX1 lab-readiness result

- `CONFIRMED`: exactly one physical target was present and it is the Huawei
  `PRA-LX1`, Android `8.0.0`/API 26, primary ABI `arm64-v8a`, production `user`
  build, with stable unprivileged ADB control.
- `CONFIRMED`: Apex remains installed as version `1.3.672.546`, code
  `64003140`, primary ABI `arm64-v8a`, and is not debuggable. Phase15S did not
  launch, stop, reinstall, uninstall, clear, or otherwise alter the package.
- `CONFIRMED`: both phone OBB names and exact byte sizes match the authoritative
  preserved PC copies. No phone hash was repeated because no mismatch existed;
  neither OBB was modified.
- `CONFIRMED`: the Mali-T830 platform stack reports OpenGL ES 3.2 and exposes
  both `GL_EXT_color_buffer_half_float` and `GL_EXT_color_buffer_float`. The
  Phase15M GLES31 and floating-point render-target capability requirements are
  satisfied at the platform level without launching Apex.
- `CONFIRMED`: logcat, UIAutomator, screencap, and dumpsys are available.
  `run-as` and private Apex data access are denied, the build is non-rooted by
  the available evidence, verified boot is green, and flash state is locked.
- `CONFIRMED`: storage is not ready. Initial free space was approximately
  `226-246 MiB`; final readings were `413-433 MiB`, below the required `2 GiB`.
  Download/media are empty, Apex is the only third-party package, and the
  remaining large non-Apex consumers are preinstalled system components.
- `CONFIRMED`: no approved deletion could materially improve storage, so
  Phase15S deleted nothing. Apex, its data/cache, and both OBBs remain intact.
- `CONFIRMED`: current Wi-Fi/mobile-data settings were inventoried only as
  booleans and not changed. Apex stayed stopped and made no request.
- Phase15S gate is `B SAFE_FREE_SPACE_TARGET_NOT_REACHED`; the phone is not
  ready for a runtime phase until at least `2 GiB` can be freed safely.

## Phase15T authoritative PRA-LX1 storage result

- `CONFIRMED`: Phase15T inventoried 166 installed packages and removed exactly
  eight individually proven nonessential updated consumer applications through
  supported Package Manager operations. No raw filesystem deletion or global
  cache clearing occurred.
- `CONFIRMED`: `/data` free space rose from `442952 KiB` to a final recorded
  `2121612 KiB`, a delta of `1678660 KiB` (about 1.601 GiB). Cleanup stopped
  immediately after crossing the 2 GiB target.
- `CONFIRMED`: ADB, SystemUI, window policy, Settings, the Huawei launcher, and
  storage remained available after every removal. Final checks also preserved
  Play Services, Play Store, Google Services Framework, Huawei framework
  services, and the sole enabled keyboard.
- `CONFIRMED`: Apex remained installed and stopped at version `1.3.672.546`,
  code `64003140`; its data/cache were not cleared and both OBB names and exact
  byte sizes remained unchanged.
- `CONFIRMED`: no microSD was present and this build reported adoptable storage
  unsupported. No storage device was formatted or modified.
- Phase15T gate is `A FREE_SPACE_2GIB_REACHED`. A later bounded runtime phase is
  storage-possible, but must recheck free space because the final margin above
  2 GiB was only about 24 MiB.

## Phase15U authoritative physical-runtime result

- `CONFIRMED`: exactly one offline Apex launch was performed on the authorized
  Huawei PRA-LX1. No UI input, account, response, redirection, backend,
  debugger, profiler, reinstall, or data/cache clear was used.
- `CONFIRMED`: `GameActivity` started and resumed, `DownloaderActivity` started
  and finished by application request, native/UE4 execution continued, Mali
  EGL surfaces succeeded, and the launch level rendered. The physical device
  clears the earlier emulator graphics compatibility gate.
- `CONFIRMED`: the `+120 s` screenshot shows update initialization at step
  `2/17` and an offline update error with exact code `I54140714`. Historical
  target `I54140715` has `NO_NEW_EVIDENCE`.
- `CONFIRMED`: GCloud Puffer enters
  `CPufferInitAction::MakeSureGetUrlFromServer` for a PAK update and fails DNS,
  connectivity, and later timeout while offline. The direct mapping from this
  component to the rendered error code remains `UNKNOWN`.
- `CONFIRMED`: Lua, ClientLaunch, EventSystem, Login/LoginMgr, avatar/server-list
  stages, and event `0x138` have `NO_NEW_EVIDENCE`. Missing public logs do not
  prove those stages did not execute.
- `CONFIRMED`: the targeted post-stop static search found neither exact
  `I54140714` nor `I54140715` in preserved APK resources, DEX, `libUE4.so`, PAKs,
  or existing PAK extraction. UI owner and error-code location remain unknown.
- `CONFIRMED`: the client was force-stopped at the 180-second bound, network
  state was restored, storage use was about 15.58 MiB, and package/OBB metadata
  remained intact.
- Phase15U gate is `A VERSION_BOOTSTRAP_AND_UPDATE_ERROR_CONFIRMED`. The current
  unresolved transition is exact update UI/error ownership, followed by the
  still-unproven Lua/Login/server-list path.

## Phase15V authoritative Puffer static-ownership result

- `CONFIRMED`: `libgcloud.so` owns
  `CPufferInitAction::MakeSureGetUrlFromServer`. Its implementation is
  `FUN_004ffd44` at Ghidra `0x4ffd44`, ELF VA/file offset `0x3ffd44` under the
  independently derived libgcloud image-base delta `+0x100000`.
- `CONFIRMED`: the coordinator uses `PufferUpdateService.GetUpdateInfo` and
  maps RPC setup, timeout, connection, callback, and cancellation failures to
  internal codes `0x0430002e` through `0x04300032`.
- `CONFIRMED`: the bounded failure chain is `CPufferInitAction::run` ->
  `MakeSureGetUrlFromServer` -> `CPufferInitActionResult` -> `ProcessResult` ->
  dynamic manager callback. The final callback target and UI edge are unknown.
- `CONFIRMED`: exact integer forms of `54140714` and `54140715` are absent from
  preserved `libgcloud.so` and `libUE4.so`; their exact strings were already
  absent in the Phase15U bounded search. No connected partial-token hit,
  formatter, lookup table, or arithmetic construction was found.
- `UNKNOWN`: construction of `I54140714`, its direct mapping from Puffer,
  relation to historical `I54140715`, and the rendered UI owner. UIAutomator's
  lack of client text is compatible with engine rendering but is not ownership
  proof.
- `UNKNOWN`: the static owner and meaning of progress `2/17`. Scalar `17` hits
  in the bounded export are unrelated `EEXIST` comparisons or structure
  offsets.
- `CONFIRMED`: no direct Lua, ClientLaunch, Login, LoginMgr, server-list, or
  event `0x138` edge exists in the resolved Puffer chain.
- Phase15V gate is
  `D PUFFER_FAILURE_CALLBACK_CHAIN_RESOLVED_UI_LINK_UNKNOWN`. No additional
  runtime launch is required for this result.

## Phase16F-R authoritative bootrom postmortem result

- `CONFIRMED`: Phase16F reached `VID_12D1/PID_3609`; signed VCOM package
  `oem14.inf` / `hw_usbvcom.sys` bound and started successfully.
- `CONFIRMED`: PotatoNV uploaded the `Kirin 65x (A)` `hisi65x_a` RAM images in
  `xloader -> fastboot` order. Windows then enumerated the source-expected
  `VID_18D1/PID_D00D` temporary endpoint as `Fastboot2.0`.
- `CONFIRMED`: D00D initially had no matching driver and problem code 28.
  PotatoNV was waiting for that exact VID/PID through libusb and timed out
  before connection, device information, NV writes, reboot, or code output.
- `CONFIRMED`: the signed exact D00D package is now installed as `oem77.inf`
  with `hw_goadb.inf` coverage and WinUSB. Driver-store readiness is confirmed;
  a live D00D re-enumeration after installation is not.
- `CONFIRMED`: the first successful physical sequence was documented as phone
  off, USB disconnected, contact before USB, release after about three seconds,
  no Power action in the documented sequence, and black screen. Later failures
  added forced pre-contact Power holds and returned normal Huawei USB.
- `UNKNOWN`: battery electrical state, exact contact quality, true cold-off
  state, residual USB power, and a timestamped `PHONE Unlocked` screen role.
- `INVALIDATED`: wrong testpoint location as the explanation for the first run.
  `NO_EVIDENCE`: wrong PotatoNV profile as its timeout cause.
- `ASSESSMENT`: one separately authorized exact retry is justified at medium
  confidence because the host blocker is fixed and the successful sequence is
  non-random and documented. It must stop after one failed bootrom entry and
  must not introduce battery disconnection, alternate profiles, FBLOCK changes,
  Magisk, or root work.
- Phase16F-R gate is `A DRIVER_READY_SINGLE_CONTROLLED_RETRY_JUSTIFIED`. The
  postmortem did not touch the phone, execute PotatoNV, change drivers, or
  launch Apex.

## Phase16C authoritative PRA-LX1 root-preparation result

- `CONFIRMED`: the only connected physical target was the authorized Huawei
  `PRA-LX1`; no Samsung was visible or accessed. The running phone reports
  `hi6250` for board, platform, and hardware and is identified as Kirin 655.
- `CONFIRMED`: Android `8.0.0`, `EmotionUI_8.0.0`, exact build
  `PRA-LX1 8.0.0.364(C33)`, CUST `altice/all`, and security patch
  `2019-03-01` were collected without unique device identifiers.
- `CONFIRMED`: normal-boot properties show a locked bootloader/vbmeta state,
  verified boot `GREEN`, verity enforcing, and OEM unlock not currently allowed
  by `sys.oem_unlock_allowed`. FRP and FBLOCK remain unknown because no
  fastboot session was entered.
- `CONFIRMED`: PotatoNV upstream supports Kirin 655 and lists the PRA family as
  tested with profile `Kirin 65x (A)`. A community PRA-LX1 EMUI 8 success uses
  a different C432 build; no exact `364(C33)` PotatoNV report was found.
- `CONFIRMED`: PotatoNV requires physical testpoint access and disassembly.
  Exact PRA-LX1 testpoint-reference confidence remains `LOW`, so no physical
  operation is approved.
- `CONFIRMED`: the device exposes separate `kernel`, `ramdisk`, and
  `recovery_ramdisk` partitions. The Magisk candidate for EMUI 8 is
  `RAMDISK.img`, with `MEDIUM` method confidence pending an exact stock image.
- `PROBABLE`: third-party indexes list exact firmware package `05014GCW` for
  PRA-LX1 `8.0.0.364(C33)`, but no official provenance, local package, hash,
  extraction, stock ramdisk, or stock recovery is available. eRecovery
  partitions exist, while restoration-service availability is untested.
- `CONFIRMED`: the exact Apex APK and both OBBs remain present in ignored PC
  storage with authoritative sizes/hashes and documented installation steps.
  Public client reinstall is ready after a wipe; private app state is not
  preserved.
- `CONFIRMED`: Android bootloader state transitions are expected to wipe
  userdata. Data-wipe, soft-brick, hard-brick, and physical-disassembly risks
  are classified `HIGH`; recovery confidence is `LOW`.
- `ASSESSMENT`: root would expose process mappings, but native stack and
  callback observation would still require separately validated root-capable
  tooling. Root alone is not a debugger workflow.
- Phase16C gate is `D ROOT_PREPARATION_NO_GO`. The next step is PC-only exact
  C33 firmware/recovery-image and exact testpoint-reference validation. No
  unlock, root, flash, wipe, disassembly, Apex launch, or package modification
  occurred.

## Evidence rules

- Cite the source file and exact address for every technical conclusion.
- Keep `CONFIRMED`, `PROBABLE`, `UNKNOWN`, and `INVALIDATED` distinct.
- Do not infer a network endpoint from a nearby string alone.
- Do not infer a native implementation from the containing function alone.
- Do not reuse a Phase2 or Phase3B address without applying and validating the image-base correction.
- Prefer corrected Phase3C JSON over prose when checking exact values.

## Best next analysis targets

Immediate Phase16F-R boundary: no further phone operation is authorized by the
postmortem itself. A future run requires separate explicit authorization and is
limited to one exact reproduction of the documented first-success sequence.
If bootrom does not enumerate once, stop. Do not disconnect the battery, switch
to another power sequence, change the `Kirin 65x (A)` profile, disable FBLOCK,
or begin Magisk/root work.

1. Do not repeat the Phase15U physical launch merely to seek historical code
   `I54140715`; the current client produced a decisive `I54140714` update error.
   Preserve Apex, its data/cache, and both OBBs.
2. Do not repeat the four-case renderer/userdata matrix on the current Android
   36.1 image. If a future phase permits obtaining another system image, test it
   only in a new disposable AVD and establish SystemUI stability before Apex.
3. Preserve the existing AVDs and do not wipe them, patch artifacts, enable
   root, or assume diagnostic access merely because `debuggerd` and `showmap`
   exist.
4. Do not change BCD; WHPX works. The unresolved blocker is guest SystemUI
   startup CPU starvation, not acceleration availability.
5. Do not repeat static ownership scans across the 17 exact APK libraries;
   Phase13 exhausted that scope without proving an owner.
6. Do not repeat or escalate Phase15D diagnostics or attempt profiling on the
   non-debuggable, non-profileable package. Do not repeat the equivalent
   Phase14 Huawei diagnostic either.
7. Do not answer TDM or GCloud, install a CA, bypass pinning, patch the APK,
   emulate authentication, or build a backend.
8. Keep unrelated phones excluded and use no real account or copied private
   state.
9. Do not assign a `libUE4.so` runtime base from the confirmed loader event. A
   legitimate readable mapping plus PT_LOAD correlation is still required.
10. Treat the current runtime frontier as confirmed update initialization and
    offline update failure, followed by the first named Lua, ClientLaunch,
    Login, or server-list stage. Do not infer those later stages from Puffer,
    TDM, remote-config, splash, or SDK lifecycle evidence.
11. Phase15J has exhausted the readable public SDK logs for this wait state.
    Do not repeat broad SDK-log collection.
12. Phase15M resolves the exact ES31/ES2 native compatibility branch and its
    EGL/OpenGL success path. Do not repeat the broad native scan or perform a
    third dialog-capture launch on this AVD.
13. Treat float RT as a separately reported capability, not a rejecting branch
    operand. A future clean-room diagnostic may test the exact two extensions
    and FBO completeness without using or launching Apex.
14. Restrict follow-up to narrow existing static evidence around Puffer update
    and error construction. Do not answer an endpoint, emulate a backend,
    patch the client, or bypass the update failure.

The HTTP response reaches a confirmed native-to-Lua event bridge and generic virtual-file Lua loader. Phase8 identifies a probable Android asset/physical fallback and partial prefix/path normalization, but not the Lua package searcher, effective provider, final lookup key, mount, container, or entry. Phase10 proves that the early TDM and GCloud failures do not block its observed path through OBB validation and `nativeResumeMainInit`. Phase13 resolves the other 12 exported `JNI_OnLoad` roots but finds no exact export or target registration row in any of the 17 libraries, so owner/function remain unknown. Phase14 confirms that the production Huawei's permitted OS diagnostics cannot supply a mapping or backtrace. Phase15C confirms the isolated WHPX guest boots stably and exposes the expected ARM64 translation bridge. Phase15D confirms one offline translated Apex runtime and an explicit `libUE4.so` loader event. Phase15E adds direct Berberis timing and Android displayed-window boundaries while confirming the package is not shell-profileable. Phase15F resolves the exact downloader trigger and local OBB validator. Phase15G then observes that the unchanged validator completes after 87.926 seconds, returns result `1`, sets `HasAllFiles=true`, and resumes `GameActivity`; the downloader is no longer an unresolved blocker. Phase15H confirms the resulting cache fast path, stable `GameActivity` through `+300 s`, process-scoped EGL/Vulkan initialization, and a rendered Lightspeed splash/wait state that is not pixel-black. Phase15I separates the Android SystemUI wait action from the APK video splash and finds official public SDK logs. Phase15J reads those logs before and after one bounded offline run: they remain SDK-only, while the third Apex Android dialog window reappears with its text obscured. Phase15K proves hierarchy capture but stops on a prelaunch SystemUI ANR. Phase15L captures the full compatibility dialog and identifies `MessageBox01` as the probable non-cancelable Java creator. Phase15M resolves the exact native ES31/ES2 branch and EGL/OpenGL success path, while correcting float RT to a diagnostic-only capability on this path. Phase15R closes the four-quadrant SystemUI matrix and resolves the timeout mechanism to CPU-starved runnable SystemUI startup work, while leaving the deep image/emulator kernel-CPU cause unknown. Phase15S confirms that the existing PRA-LX1 has an intact matching client, OBBs, OpenGL ES 3.2, and both float render-target extensions, but blocks runtime work because only about 0.41-0.43 GiB remains free and no authorized safe cleanup can reach 2 GiB. Phase15T raises free space above 2 GiB without altering Apex. Phase15U then confirms one physical offline run reaches update initialization `2/17` and exact error `I54140714`, with Puffer update networking active. Phase15V identifies `libgcloud.so` and `FUN_004ffd44` as the Puffer request owner and resolves the internal failure path through `CPufferInitActionResult::ProcessResult`, where a dynamic manager callback ends the static chain. UI ownership, visible error-code construction/mapping, the meaning of `2/17`, Lua/Login/server-list reachability, load bias, and gameplay frame remain unknown.
