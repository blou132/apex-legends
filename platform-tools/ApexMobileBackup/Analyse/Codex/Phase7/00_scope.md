# Phase7 - Scope

Date: 2026-08-13.

Phase7 is a read-only localization study for the probable EventSystem Lua module. It combines cleaned prior PAK observations with a targeted Ghidra export run against the existing local `libUE4.so` project using `-process libUE4.so -noanalysis -readOnly`.

No APK, OBB, PAK, SO, phone, account, or server was modified or contacted. No root, hook, MITM, brute force, key search, decryption, or network request was used. No recovered proprietary Lua source is included.

## Input availability

The public analysis tree is available. The local-only roots `Analyse/APK`, `Analyse/MAIN`, and `Analyse/PATCH` and the four known PAK files are absent from this workspace. Consequently, the requested Phase7 raw-byte scan cannot be rerun. Existing Phase1/Phase2 results are cited explicitly as historical evidence and are never represented as fresh hits.

## Address model

The Ghidra image base is `0x100000`:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
```

Confidence labels are restricted to `CONFIRMED`, `PROBABLE`, `UNKNOWN`, and `INVALIDATED`.
