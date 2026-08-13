# ChatGPT context index

Updated: 2026-08-13 (Phase8)

## Purpose

This file is the entry point for a new AI-assisted analysis session. The repository contains static and runtime observations about Apex Legends Mobile, Ghidra scripts, machine-readable exports, and the evidence needed to continue the investigation without relying on an earlier chat history.

The original game binaries, phone backups, raw logs, bulk function inventory, and local Ghidra database are intentionally not published. They remain local because they are proprietary, private, large, or unnecessary for reviewing the cleaned conclusions exported here.

## Read first

Use this order to avoid repeating conclusions that were later corrected:

1. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9/REPORT_PHASE9.md`
2. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase8/REPORT_PHASE8.md`
3. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7C_resume/REPORT_PHASE7C_RESUME.md`
4. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7/REPORT_PHASE7.md`
5. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase6/REPORT_PHASE6.md`
6. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase5/REPORT_PHASE5.md`
7. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase4/REPORT_PHASE4.md`
8. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`
9. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/00_address_model.md`
10. The relevant topic report and matching JSON from the newest phase
11. Phase2 and Phase3B only as historical evidence, with Phase3C and later phases taking precedence

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
- `CONFIRMED`: Phase9 found no complete local `base.apk`; ADB reported zero authorized devices, so no APK was pulled and the original phone was not accessed.
- `CONFIRMED`: the host has the Android Emulator executable but zero configured AVDs, zero Android endpoints, and only an x86_64 system image; no installed ARM64-compatible isolated lab was available.
- `CONFIRMED`: Phase9 did not launch or instrument the client, contact an external service, or produce runtime logs. Its decision gate is `E`, `DYNAMIC_LAB_UNAVAILABLE`.
- `CONFIRMED`: the requested runtime targets were converted with `ELF_VIRTUAL_OFFSET = GHIDRA_ADDRESS - 0x100000`; no process load base was available, so no runtime address was used.
- `UNKNOWN`: the complete base APK size, SHA256, and Phase9 ABI inventory. Prior artifact evidence still establishes an `arm64-v8a`/AArch64 native client requirement.
- `UNKNOWN`: all Phase9 runtime values, including the Lua search sequence, effective provider, final open path, ClientLaunch/EventSystem chunk metadata, event `0x138` subscriber, response parser, and URL producer.
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

The full 467,079-function inventory and raw Ghidra execution logs remain local-only. Their relevant results are summarized in the reports and smaller JSON files committed here.

## Evidence rules

- Cite the source file and exact address for every technical conclusion.
- Keep `CONFIRMED`, `PROBABLE`, `UNKNOWN`, and `INVALIDATED` distinct.
- Do not infer a network endpoint from a nearby string alone.
- Do not infer a native implementation from the containing function alone.
- Do not reuse a Phase2 or Phase3B address without applying and validating the image-base correction.
- Prefer corrected Phase3C JSON over prose when checking exact values.

## Best next analysis targets

1. Obtain an authorized read-only copy of the exact base APK and record its size, SHA256, package identity, and complete ABI set.
2. Provide an already installed secondary ARM64-compatible Android lab; never substitute the preserved phone and do not install large SDK images automatically.
3. Isolate the lab from external Internet and run the copy once without hooks, keeping raw logcat local-only.
4. Stop if startup requires an authentication, anti-emulator, integrity, or encryption bypass.
5. If runtime metadata is still absent, use the corrected Phase9 ELF offsets with the observed `libUE4.so` load base and capture loader metadata only.
6. Validate the Lua search/provider chain against `ClientLaunch` before applying it to EventSystem.
7. If EventSystem is legitimately readable, identify the exact event `0x138` subscriber, parser, fields, and local URL construction without contacting the URL.

The HTTP response reaches a confirmed native-to-Lua event bridge and generic virtual-file Lua loader. Phase8 identifies a probable Android asset/physical fallback and partial prefix/path normalization, but not the Lua package searcher, effective provider, final lookup key, mount, container, or entry. Phase9 could not cross that boundary because the base APK and an ARM64-compatible isolated lab were unavailable; decision gate `E DYNAMIC_LAB_UNAVAILABLE` is authoritative for the attempted runtime phase.
