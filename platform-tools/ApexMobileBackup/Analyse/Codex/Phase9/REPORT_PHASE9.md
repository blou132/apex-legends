# Phase9 - Isolated Lua runtime observation

Date: 2026-08-13

## Executive result

Phase9 could not start a dynamic client run. No complete base APK was present in local ignored backups, and ADB reported zero authorized devices when the allowed read-only recovery was attempted. The host has an Android Emulator executable but no configured AVD, no running emulator, no secondary Android endpoint, and only an x86_64 system image. No installed ARM64-compatible isolated lab was found.

The investigation stopped without installing tools, launching the client, contacting external services, instrumenting a device, or using the original phone as a laboratory.

```text
DECISION_GATE = E DYNAMIC_LAB_UNAVAILABLE
DYNAMIC_LAB_AVAILABLE = NO
CLIENT_LAUNCHED = NO
CLIENTLAUNCH_OBSERVED = NO
EVENTSYSTEM_OBSERVED = NO
EFFECTIVE_PROVIDER = UNKNOWN
FINAL_OPEN_PATH = UNKNOWN
EVENT_0X138_SUBSCRIBER_FOUND = NO
SERVERLIST_PARSER_FOUND = NO
URL_SOURCE_FOUND = NO
```

## New useful results

- Confirmed that no complete `base.apk` currently exists in the repository-local ignored inputs or historical backup tree.
- Confirmed that the allowed ADB recovery could not proceed because no authorized device was visible; no phone path was queried and no data was pulled.
- Confirmed that the installed Android SDK has zero configured AVDs and only one x86_64 system image.
- Recomputed both OBB SHA256 values and exact sizes; they match Phase7C Resume.
- Converted every requested Phase8 Ghidra target to its corrected ELF virtual offset for a future runtime lab.
- Preserved all runtime questions as `UNKNOWN` rather than promoting static witnesses to dynamic facts.

## Artifact identity

| Artifact | Size | SHA256 | Status |
| --- | ---: | --- | --- |
| base APK | `UNKNOWN` | `UNKNOWN` | unavailable |
| main OBB | 1,942,013,346 | `104B313F86A57F1B2A11D81D87E916EFBAB99BBBFB4A031E2D12B0201D5244E0` | rehashed |
| patch OBB | 1,837,582,506 | `BFE887744EC21CB7CE0829F1C1F1BCAF1C400C358185B52D140D75242264973D` | rehashed |

The complete APK ABI set could not be enumerated. Prior published evidence shows that the analyzed native client artifact was under `lib/arm64-v8a` and that the Ghidra program is AArch64, so a future lab must support ARM64. This prior evidence is not presented as a fresh APK inventory.

## Correct runtime address model

Phase3C remains authoritative:

```text
ELF_VIRTUAL_OFFSET = GHIDRA_ADDRESS - 0x100000
RUNTIME_ADDRESS = LIBUE4_LOAD_BASE + ELF_VIRTUAL_OFFSET
```

| Target | Ghidra | ELF virtual offset |
| --- | --- | --- |
| `FUN_049a8b54` | `0x49a8b54` | `0x48a8b54` |
| `FUN_049a9694` | `0x49a9694` | `0x48a9694` |
| `FUN_048ab4d0` | `0x48ab4d0` | `0x47ab4d0` |
| `FUN_046355e8` | `0x46355e8` | `0x45355e8` |
| OpenRead thunk | `0x49825b8` | `0x48825b8` |
| OpenRead body | `0x49825bc` | `0x48825bc` |
| facade open target | `0x48415b8` | `0x47415b8` |
| resolver | `0x82693bc` | `0x81693bc` |
| optional provider global | `0xb697528` | `0xb597528` |

No runtime base was observed, so these offsets were not converted to process addresses or used for hooks.

## Runtime conclusions

The Lua package searcher, actual loader input, provider branch, optional-provider state, final OpenRead path, chunk name, and returned size all remain `UNKNOWN`. `Client/Launch/ClientLaunch.lua` remains only the Phase8 PAK-body witness. The probable EventSystem path and `PostCppEvent` name remain unconfirmed.

Because no EventSystem chunk became available, no event `0x138` subscriber, response parser, server-list fields, storage writer, or URL producer was identified.

## Safety and publication

No raw logcat, runtime log, memory dump, proprietary script, APK, OBB, PAK, device identifier, account data, or credential is included. The public output consists only of cleaned environment facts, hashes, static addresses, corrected offsets, and conservative statuses.

## Next step

Acquire the exact base APK through an authorized read-only source and provide an already installed secondary ARM64-compatible Android lab. Then run the offline, no-hook startup first and stop if any bypass would be required. Detailed sequencing is in `10_next_step.md`.
