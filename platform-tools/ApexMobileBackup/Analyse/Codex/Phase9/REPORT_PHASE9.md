# Phase9 - Isolated Lua runtime observation

Date: 2026-08-13

## Executive result

Phase9 could not start a dynamic client run. No complete base APK was initially present in local ignored backups, and the first allowed read-only recovery attempt found no ADB device. After the preserved phone was explicitly reconnected, `base.apk` was recovered read-only into ignored local storage; no further phone command was issued. The host has an Android Emulator executable but no configured AVD, no running emulator, no secondary Android endpoint, and only an x86_64 system image. The recovered APK contains only `arm64-v8a` native libraries, so no installed compatible isolated lab was found.

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

- Confirmed that no complete `base.apk` existed in the repository-local ignored inputs or historical backup tree before recovery.
- Recovered only `base.apk` through the explicitly allowed read-only phone operation, then stopped all phone use for Phase9.
- Established the APK size, SHA256, ZIP entry count, native library count, and exact ARM64-only ABI set.
- Confirmed that the installed Android SDK has zero configured AVDs and only one x86_64 system image.
- Recomputed both OBB SHA256 values and exact sizes; they match Phase7C Resume.
- Converted every requested Phase8 Ghidra target to its corrected ELF virtual offset for a future runtime lab.
- Preserved all runtime questions as `UNKNOWN` rather than promoting static witnesses to dynamic facts.

## Artifact identity

| Artifact | Size | SHA256 | Status |
| --- | ---: | --- | --- |
| base APK | 96,228,800 | `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0` | recovered read-only, local-only |
| main OBB | 1,942,013,346 | `104B313F86A57F1B2A11D81D87E916EFBAB99BBBFB4A031E2D12B0201D5244E0` | rehashed |
| patch OBB | 1,837,582,506 | `BFE887744EC21CB7CE0829F1C1F1BCAF1C400C358185B52D140D75242264973D` | rehashed |

The complete APK ZIP contains 978 entries and only one native ABI, `arm64-v8a`, with 17 shared libraries. Its `libUE4.so` entry has an uncompressed size of 190,192,944 bytes. This independently confirms that a future isolated lab must support ARM64.

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

Provide an already installed secondary ARM64-compatible Android lab. The exact base APK is now available locally and ignored by Git. Then run the offline, no-hook startup first and stop if any bypass would be required. Detailed sequencing is in `10_next_step.md`.
