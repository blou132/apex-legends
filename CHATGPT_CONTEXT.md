# ChatGPT context index

Updated: 2026-08-13

## Purpose

This file is the entry point for a new AI-assisted analysis session. The repository contains static and runtime observations about Apex Legends Mobile, Ghidra scripts, machine-readable exports, and the evidence needed to continue the investigation without relying on an earlier chat history.

The original game binaries, phone backups, raw logs, bulk function inventory, and local Ghidra database are intentionally not published. They remain local because they are proprietary, private, large, or unnecessary for reviewing the cleaned conclusions exported here.

## Read first

Use this order to avoid repeating conclusions that were later corrected:

1. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`
2. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/00_address_model.md`
3. `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/09_phase3b_invalidations.md`
4. The relevant Phase3C topic report and matching JSON under `Phase3/Phase3C/output/`
5. `platform-tools/ApexMobileBackup/Analyse/Codex/REPORT.md` for the broader network and backend inventory
6. Phase2 and Phase3B only as historical evidence, with Phase3C taking precedence on every address conflict

## Authoritative conclusions

- `CONFIRMED`: the Ghidra program uses image base `0x100000`.
- `CONFIRMED`: convert an ELF virtual address with `GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000`.
- `INVALIDATED`: Phase3B conclusions that used Phase2 ELF virtual addresses directly as Ghidra addresses.
- `INVALIDATED`: `FUN_07941d10` as the direct corrected candidate for `RequestAvatarServerList`.
- `PROBABLE`: corrected containing functions for its two code references are `FUN_07a41d0c` at `0x7a41d0c` and `FUN_07a41d4c` at `0x7a41d4c`.
- `UNKNOWN`: the exact native function pointer for `RequestAvatarServerList`.
- `UNKNOWN`: a proven `LoginMgr -> RequestAvatarServerList` call path.
- `UNKNOWN`: the numeric value and handler for `EVENTID_AVATARSERVERLIST_RETURN`.
- `UNKNOWN`: the owner, exact type, and writer of `GameServerBackupIpList`.
- `UNKNOWN`: whether `SyncPayloadToGameServer` is an Unreal RPC and which native function implements it.
- `UNKNOWN`: the effective UEDSToolkit transport.
- `UNKNOWN`: a complete server-list to game-server address and connection path.
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

## Machine-readable evidence

- `Phase3/output/elf_libUE4_summary.json`: ELF structure summary.
- `Phase3/output/ghidra_nearest_functions.json`: nearest-function lookups used by the earlier analysis.
- `Phase3/Phase3B/output/*.json`: target callgraph and xref exports; interpret addresses through Phase3C corrections.
- `Phase3/Phase3C/output/*.json`: corrected target exports and address mapping.

The full 467,079-function inventory and raw Ghidra execution logs remain local-only. Their relevant results are summarized in the reports and smaller JSON files committed here.

## Evidence rules

- Cite the source file and exact address for every technical conclusion.
- Keep `CONFIRMED`, `PROBABLE`, `UNKNOWN`, and `INVALIDATED` distinct.
- Do not infer a network endpoint from a nearby string alone.
- Do not infer a native implementation from the containing function alone.
- Do not reuse a Phase2 or Phase3B address without applying and validating the image-base correction.
- Prefer corrected Phase3C JSON over prose when checking exact values.

## Best next analysis targets

1. Export xrefs, function ranges, callers, callees, and decompilation for the corrected Phase3C targets.
2. Resolve the `STRING -> RELOCATION/RAW_POINTER -> METADATA -> CODE` chain for `RequestAvatarServerList`.
3. Identify the native registration slot and exact function pointer.
4. Trace the server-list return event to its handler and numeric event value.
5. Identify the owner, type, and writer of `GameServerBackupIpList`.
6. Trace `SyncPayloadToGameServer` toward Unreal networking primitives.
7. Trace reconnect and UEDSToolkit paths toward `Host`, `Port`, `FURL`, socket, HTTP, or resolver calls.

Until those links are exported, the concrete lobby, matchmaking, and game-server destination remain unknown.
