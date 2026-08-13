# Phase8 - Scope

Date: 2026-08-13.

## Objective

Determine whether the probable logical module `Script/Tools/EventSystem/EventSystem.lua` can be mapped to an exact virtual lookup key, provider, container, entry boundary, and byte range using only authorized read-only evidence.

## Evidence used

- ignored local copies of the two recovered OBB files and four PAK files;
- files already extracted from the OBB ZIP layer;
- four small files from the package public-storage tree, copied read-only to ignored `Analyse/LocalInputs/Phase8/`;
- the existing read-only Ghidra project for `libUE4.so`;
- Phase7 and Phase7C Resume reports and JSON.

Ghidra address conversion remains:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
```

## Safety boundary

No APK, OBB, PAK, SO, phone file, or game asset was modified. No root, hook, patch, decryption, key search, brute force, memory dump, MITM, or old-server request was used. Raw ADB output, device identifiers, full public caches, and proprietary assets remain local-only.

## Decision

```text
VIRTUAL_PATH_MAPPING_STILL_UNKNOWN = YES
DECISION_GATE = E
```

Partial path behavior and the fallback Android file backend are resolved, but the Lua package searcher, effective runtime prefix/provider, exact final lookup key, and EventSystem container remain unknown.
