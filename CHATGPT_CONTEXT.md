# ChatGPT context index

Updated: 2026-08-20 (Phase15N completed)

## Purpose

This file is the entry point for a new AI-assisted analysis session. The repository contains static and runtime observations about Apex Legends Mobile, Ghidra scripts, machine-readable exports, and the evidence needed to continue the investigation without relying on an earlier chat history.

The original game binaries, phone backups, raw logs, bulk function inventory, and local Ghidra database are intentionally not published. They remain local because they are proprietary, private, large, or unnecessary for reviewing the cleaned conclusions exported here.

## Read first

Use this order to avoid repeating conclusions that were later corrected:

1. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15N/REPORT_PHASE15N.md`
2. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15M/REPORT_PHASE15M.md`
3. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15L/REPORT_PHASE15L.md`
4. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15K/REPORT_PHASE15K.md`
5. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15J/REPORT_PHASE15J.md`
6. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15I/REPORT_PHASE15I.md`
7. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15H/REPORT_PHASE15H.md`
8. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15G/REPORT_PHASE15G.md`
9. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15F/REPORT_PHASE15F.md`
10. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15E/REPORT_PHASE15E.md`
11. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15D/REPORT_PHASE15D.md`
12. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15C/REPORT_PHASE15C.md`
13. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15B/REPORT_PHASE15B.md`
14. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase15A/REPORT_PHASE15A.md`
15. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase14/REPORT_PHASE14.md`
16. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase13/REPORT_PHASE13.md`
17. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase12/REPORT_PHASE12.md`
18. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase11/REPORT_PHASE11.md`
19. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase10/REPORT_PHASE10.md`
20. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9D/REPORT_PHASE9D.md`
21. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9C/REPORT_PHASE9C.md`
22. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9B/REPORT_PHASE9B.md`
23. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9/REPORT_PHASE9.md`
24. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase8/REPORT_PHASE8.md`
25. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7C_resume/REPORT_PHASE7C_RESUME.md`
26. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7/REPORT_PHASE7.md`
27. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase6/REPORT_PHASE6.md`
28. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase5/REPORT_PHASE5.md`
29. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase4/REPORT_PHASE4.md`
30. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`
31. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/00_address_model.md`
32. The relevant topic report and matching JSON from the newest phase
33. Phase2 and Phase3B only as historical evidence, with Phase3C and later phases taking precedence

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

## Evidence rules

- Cite the source file and exact address for every technical conclusion.
- Keep `CONFIRMED`, `PROBABLE`, `UNKNOWN`, and `INVALIDATED` distinct.
- Do not infer a network endpoint from a nearby string alone.
- Do not infer a native implementation from the containing function alone.
- Do not reuse a Phase2 or Phase3B address without applying and validating the image-base correction.
- Prefer corrected Phase3C JSON over prose when checking exact values.

## Best next analysis targets

1. Phase15H confirms the Phase15G validation cache is reused, with result `1`
   returned in `0.957 s`. Do not repeat downloader/cache/CRC observations.
2. Preserve the existing AVD and do not wipe it, patch artifacts, enable root,
   or assume diagnostic access merely because `debuggerd` and `showmap` exist.
3. Do not change BCD; WHPX works and the AVD boot is stable.
4. Do not repeat static ownership scans across the 17 exact APK libraries;
   Phase13 exhausted that scope without proving an owner.
5. Do not repeat or escalate Phase15D diagnostics or attempt profiling on the
   non-debuggable, non-profileable package. Do not repeat the equivalent
   Phase14 Huawei diagnostic either.
6. Do not answer TDM or GCloud, install a CA, bypass pinning, patch the APK,
   emulate authentication, or build a backend.
7. Keep both phones excluded and use no real account or copied private state.
8. Do not assign a `libUE4.so` runtime base from the confirmed loader event. A
   legitimate readable mapping plus PT_LOAD correlation is still required.
9. Treat the current runtime frontier as the transition from confirmed
   process-scoped graphics initialization and rendered Lightspeed splash/wait
   UI to the first named Lua, ClientLaunch, Login, or server-list stage. Do not
   infer those stages from a splash, wait UI, SDK lifecycle callback, or TDM
   retry.
10. Phase15J has exhausted the readable public SDK logs for this wait state.
    Do not repeat broad SDK-log collection.
11. Phase15M resolves the exact ES31/ES2 native compatibility branch and its
    EGL/OpenGL success path. Do not repeat the broad native scan or perform a
    third dialog-capture launch on this AVD.
12. Treat float RT as a separately reported capability, not a rejecting branch
    operand. A future clean-room diagnostic may test the exact two extensions
    and FBO completeness without using or launching Apex.
13. Restrict follow-up to the still-protected MessageBox helper, the unknown
    native splash-dismiss trigger, or a separately justified legitimately
    compatible graphics environment. Do not patch or bypass the gate.

The HTTP response reaches a confirmed native-to-Lua event bridge and generic virtual-file Lua loader. Phase8 identifies a probable Android asset/physical fallback and partial prefix/path normalization, but not the Lua package searcher, effective provider, final lookup key, mount, container, or entry. Phase10 proves that the early TDM and GCloud failures do not block its observed path through OBB validation and `nativeResumeMainInit`. Phase13 resolves the other 12 exported `JNI_OnLoad` roots but finds no exact export or target registration row in any of the 17 libraries, so owner/function remain unknown. Phase14 confirms that the production Huawei's permitted OS diagnostics cannot supply a mapping or backtrace. Phase15C confirms the isolated WHPX guest boots stably and exposes the expected ARM64 translation bridge. Phase15D confirms one offline translated Apex runtime and an explicit `libUE4.so` loader event. Phase15E adds direct Berberis timing and Android displayed-window boundaries while confirming the package is not shell-profileable. Phase15F resolves the exact downloader trigger and local OBB validator. Phase15G then observes that the unchanged validator completes after 87.926 seconds, returns result `1`, sets `HasAllFiles=true`, and resumes `GameActivity`; the downloader is no longer an unresolved blocker. Phase15H confirms the resulting cache fast path, stable `GameActivity` through `+300 s`, process-scoped EGL/Vulkan initialization, and a rendered Lightspeed splash/wait state that is not pixel-black. Phase15I separates the Android SystemUI wait action from the APK video splash and finds official public SDK logs. Phase15J reads those logs before and after one bounded offline run: they remain SDK-only, while the third Apex Android dialog window reappears with its text obscured. Phase15K proves hierarchy capture but stops on a prelaunch SystemUI ANR. Phase15L captures the full compatibility dialog and identifies `MessageBox01` as the probable non-cancelable Java creator. Phase15M resolves the exact native ES31/ES2 branch and EGL/OpenGL success path, while correcting float RT to a diagnostic-only capability on this path. Lua/Login reachability, the protected MessageBox implementation, native dialog wait, native splash-dismiss trigger, mapping, load bias, and gameplay frame remain unknown.
