# ChatGPT context index

Updated: 2026-08-20 (Phase15C completed retry)

## Purpose

This file is the entry point for a new AI-assisted analysis session. The repository contains static and runtime observations about Apex Legends Mobile, Ghidra scripts, machine-readable exports, and the evidence needed to continue the investigation without relying on an earlier chat history.

The original game binaries, phone backups, raw logs, bulk function inventory, and local Ghidra database are intentionally not published. They remain local because they are proprietary, private, large, or unnecessary for reviewing the cleaned conclusions exported here.

## Read first

Use this order to avoid repeating conclusions that were later corrected:

1. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15C/REPORT_PHASE15C.md`
2. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15B/REPORT_PHASE15B.md`
3. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15A/REPORT_PHASE15A.md`
4. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase14/REPORT_PHASE14.md`
5. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase13/REPORT_PHASE13.md`
6. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase12/REPORT_PHASE12.md`
7. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase11/REPORT_PHASE11.md`
8. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase10/REPORT_PHASE10.md`
9. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9D/REPORT_PHASE9D.md`
10. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9C/REPORT_PHASE9C.md`
11. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9B/REPORT_PHASE9B.md`
12. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9/REPORT_PHASE9.md`
13. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase8/REPORT_PHASE8.md`
14. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7C_resume/REPORT_PHASE7C_RESUME.md`
15. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7/REPORT_PHASE7.md`
16. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase6/REPORT_PHASE6.md`
17. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase5/REPORT_PHASE5.md`
18. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase4/REPORT_PHASE4.md`
19. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`
20. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/00_address_model.md`
21. The relevant topic report and matching JSON from the newest phase
22. Phase2 and Phase3B only as historical evidence, with Phase3C and later phases taking precedence

## Authoritative conclusions

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

The full 467,079-function inventory and raw Ghidra execution logs remain local-only. Their relevant results are summarized in the reports and smaller JSON files committed here.

## Evidence rules

- Cite the source file and exact address for every technical conclusion.
- Keep `CONFIRMED`, `PROBABLE`, `UNKNOWN`, and `INVALIDATED` distinct.
- Do not infer a network endpoint from a nearby string alone.
- Do not infer a native implementation from the containing function alone.
- Do not reuse a Phase2 or Phase3B address without applying and validating the image-base correction.
- Prefer corrected Phase3C JSON over prose when checking exact values.

## Best next analysis targets

1. Phase15C now validates the isolated guest. Any APK/OBB installation or Apex
   launch requires a separate explicit phase with no physical device present,
   blocked Internet, exact artifact identity, and no account or authentication.
2. Preserve the existing AVD and do not wipe it, patch artifacts, enable root,
   or assume diagnostic access merely because `debuggerd` and `showmap` exist.
3. Do not change BCD; WHPX works and the AVD boot is stable.
4. Do not repeat static ownership scans across the 17 exact APK libraries;
   Phase13 exhausted that scope without proving an owner.
5. Do not repeat the Phase14 `debuggerd -b` run on the production Huawei image;
   the OS diagnostic boundary is confirmed and `showmap` is absent.
6. Do not answer TDM or GCloud, install a CA, bypass pinning, patch the APK,
   emulate authentication, or build a backend.
7. Keep both phones excluded and use no real account or copied private state.
8. Do not assign a `libUE4.so` runtime base until a legitimate readable mapping
   or explicit loader event proves it.

The HTTP response reaches a confirmed native-to-Lua event bridge and generic virtual-file Lua loader. Phase8 identifies a probable Android asset/physical fallback and partial prefix/path normalization, but not the Lua package searcher, effective provider, final lookup key, mount, container, or entry. Phase10 proves that the early TDM and GCloud failures do not block the observed path through OBB validation and `nativeResumeMainInit`. Phase13 resolves the other 12 exported `JNI_OnLoad` roots but finds no exact export or target registration row in any of the 17 libraries, so owner/function remain unknown. Phase14 confirms that the production Huawei's permitted OS diagnostics cannot supply a mapping or backtrace. Phase15C confirms the isolated WHPX guest boots stably and exposes the expected ARM64 translation bridge, but Apex is not installed and no proprietary ARM64 execution has occurred; its gate is `A AVD_BOOT_ARM64_BRIDGE_CONFIRMED`.
