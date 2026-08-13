# ChatGPT context index

Updated: 2026-08-13 (Phase7)

## Purpose

This file is the entry point for a new AI-assisted analysis session. The repository contains static and runtime observations about Apex Legends Mobile, Ghidra scripts, machine-readable exports, and the evidence needed to continue the investigation without relying on an earlier chat history.

The original game binaries, phone backups, raw logs, bulk function inventory, and local Ghidra database are intentionally not published. They remain local because they are proprietary, private, large, or unnecessary for reviewing the cleaned conclusions exported here.

## Read first

Use this order to avoid repeating conclusions that were later corrected:

1. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7/REPORT_PHASE7.md`
2. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase6/REPORT_PHASE6.md`
3. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase5/REPORT_PHASE5.md`
4. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase4/REPORT_PHASE4.md`
5. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`
6. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/00_address_model.md`
7. The relevant topic report and matching JSON from the newest phase
8. Phase2 and Phase3B only as historical evidence, with Phase3C and later phases taking precedence

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
- `CONFIRMED`: the current workspace contains no original PAK, extracted Lua, `Analyse/APK`, `Analyse/MAIN`, or `Analyse/PATCH` input, so Phase7 raw scans could not be rerun.
- `CONFIRMED`: Phase7 decision gate is `D`, `EVENTSYSTEM_CONTAINER_UNKNOWN = YES`.
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

## Machine-readable evidence

- `Phase3/output/elf_libUE4_summary.json`: ELF structure summary.
- `Phase3/output/ghidra_nearest_functions.json`: nearest-function lookups used by the earlier analysis.
- `Phase3/Phase3B/output/*.json`: target callgraph and xref exports; interpret addresses through Phase3C corrections.
- `Phase3/Phase3C/output/*.json`: corrected target exports and address mapping.
- `Phase4/output/*.json`: Unreal registration tables, event metadata, property metadata, and request runtime probe.
- `Phase5/output/*.json`: request construction, callback vtables, response event, response representation, storage check, and SyncPayload thunk.
- `Phase6/output/*.json`: event emitter, Lua dispatcher/core, event-consumer boundary, parser/storage status, URL source and SyncPayload receiver evidence.
- `Phase7/output/*.json`: local inventory and PAK-search availability, encoded names, Lua loader, asset mapping, EventSystem location, subscriber status, and URL-source status.

The full 467,079-function inventory and raw Ghidra execution logs remain local-only. Their relevant results are summarized in the reports and smaller JSON files committed here.

## Evidence rules

- Cite the source file and exact address for every technical conclusion.
- Keep `CONFIRMED`, `PROBABLE`, `UNKNOWN`, and `INVALIDATED` distinct.
- Do not infer a network endpoint from a nearby string alone.
- Do not infer a native implementation from the containing function alone.
- Do not reuse a Phase2 or Phase3B address without applying and validating the image-base correction.
- Prefer corrected Phase3C JSON over prose when checking exact values.

## Best next analysis targets

1. Restore the four original local-only PAK inputs or obtain an authorized readable manifest/index/cache from the same build; do not attempt decryption or key search.
2. Rerun exact ASCII, UTF-8, and UTF-16LE searches for EventSystem and the `0x138` subscriber, then correlate hits only through proven entry boundaries.
3. If EventSystem is legitimately readable, identify the exact subscriber, parser, and fields it actually reads; publish only a cleaned summary, not proprietary Lua.
4. Prove a concrete `Login` instance before looking for writes at `Login+0x150`.
5. Resolve the script/reflection caller that supplies the URL `FString`.
6. Identify the concrete receiver/vtable behind `SyncPayloadToGameServer +0xa58`.

The HTTP response reaches a confirmed native-to-Lua event bridge and a confirmed generic virtual-file Lua loading pipeline. EventSystem's concrete container, source/bytecode, subscriber, wire format, server-list fields, storage, and game-server destination remain unknown.
