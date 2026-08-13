# ChatGPT context index

Updated: 2026-08-13 (Phase11)

## Purpose

This file is the entry point for a new AI-assisted analysis session. The repository contains static and runtime observations about Apex Legends Mobile, Ghidra scripts, machine-readable exports, and the evidence needed to continue the investigation without relying on an earlier chat history.

The original game binaries, phone backups, raw logs, bulk function inventory, and local Ghidra database are intentionally not published. They remain local because they are proprietary, private, large, or unnecessary for reviewing the cleaned conclusions exported here.

## Read first

Use this order to avoid repeating conclusions that were later corrected:

1. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase11/REPORT_PHASE11.md`
2. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase10/REPORT_PHASE10.md`
3. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9D/REPORT_PHASE9D.md`
4. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9C/REPORT_PHASE9C.md`
5. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9B/REPORT_PHASE9B.md`
6. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9/REPORT_PHASE9.md`
7. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase8/REPORT_PHASE8.md`
8. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7C_resume/REPORT_PHASE7C_RESUME.md`
9. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7/REPORT_PHASE7.md`
10. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase6/REPORT_PHASE6.md`
11. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase5/REPORT_PHASE5.md`
12. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase4/REPORT_PHASE4.md`
13. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`
14. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/00_address_model.md`
15. The relevant topic report and matching JSON from the newest phase
16. Phase2 and Phase3B only as historical evidence, with Phase3C and later phases taking precedence

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

The full 467,079-function inventory and raw Ghidra execution logs remain local-only. Their relevant results are summarized in the reports and smaller JSON files committed here.

## Evidence rules

- Cite the source file and exact address for every technical conclusion.
- Keep `CONFIRMED`, `PROBABLE`, `UNKNOWN`, and `INVALIDATED` distinct.
- Do not infer a network endpoint from a nearby string alone.
- Do not infer a native implementation from the containing function alone.
- Do not reuse a Phase2 or Phase3B address without applying and validating the image-base correction.
- Prefer corrected Phase3C JSON over prose when checking exact values.

## Best next analysis targets

1. Resolve the exact exported `JNI_OnLoad` function, if unique, in the existing local `libUE4.so` Ghidra project.
2. Follow only its `GameActivity` class lookup and `RegisterNatives` branches to recover a runtime-constructed method-to-function mapping for `nativeResumeMainInit`.
3. After one function is proven, trace only its reachable state changes, workers, waits, and callbacks to the first concrete gate or Lua-loader boundary.
4. Do not answer TDM or GCloud, install a CA, bypass pinning, patch the APK, emulate authentication, or build a backend.
5. Keep the preserved Samsung excluded and use no real account or copied private state.
6. Do not assign a `libUE4.so` runtime base until a legitimate readable mapping or explicit loader event proves it.

The HTTP response reaches a confirmed native-to-Lua event bridge and generic virtual-file Lua loader. Phase8 identifies a probable Android asset/physical fallback and partial prefix/path normalization, but not the Lua package searcher, effective provider, final lookup key, mount, container, or entry. Phase10 proves that the early TDM and GCloud failures do not block the observed path through OBB validation and `nativeResumeMainInit`. Phase11 cannot map that Java native method to one function in the exact local Ghidra program, so gate `F JNI_ENTRYPOINT_NOT_RESOLVED` is authoritative.
