# Phase8 - Native file backend

## Confirmed call path

```text
FUN_049a8b54 Lua file loader
  -> optional provider at global 0xb697528 when enabled
  -> otherwise FUN_049a9694 whole-file read
  -> facade vtable 0xaf721e8, OpenRead +0x18 / path check +0x48
  -> FUN_046355e8 singleton at 0xb7cf9b0
  -> backend vtable 0xaf710e0
  -> OpenRead +0xc0 -> thunk 0x49825b8 -> body 0x49825bc
```

## Backend classification

The fallback object is **PROBABLE Android asset/physical platform-file backend**. This is supported by the OpenRead body, not by a recovered class name:

- `FUN_046095e4` normalizes/splits the input path;
- `FUN_04609488` searches a case-insensitive registered-path table at object offset `+0x10`;
- a registered match is wrapped with handle vtable `0xaf712c8`;
- one fallback branch opens an Android asset-like object and constructs a handle through `FUN_0825240c`;
- the other opens a physical descriptor-like value and constructs a handle through `FUN_049829e0`;
- those handles expose size/read/status/release operations used by `FUN_049a9694`.

Several imported calls have incorrect stripped/PLT labels in Ghidra, so the exact Unreal class name is not promoted to CONFIRMED. The null RTTI words before the vtable address point and absence of symbolic relocations prevent a precise `FAndroidPlatformFile` name assignment.

## Provider limit

The optional provider global `0xb697528` is checked through vtable slots `+0x28` and `+0x50`. The full static code-reference scan found the loader reads and a static reset to null, but no direct assignment of a concrete provider. It may be installed indirectly at runtime.

Therefore:

```text
FALLBACK_BACKEND = PROBABLE_ANDROID_ASSET_AND_PHYSICAL_FILE
FULL_VIRTUAL_PROVIDER = UNKNOWN
PAK_PROVIDER_IDENTIFIED = NO
PAK_NOT_ONLY_BACKEND = YES
```

Selected raw slots and the complete scanned pointer ranges for vtables `0xaf710e0`, `0xaf712c8`, and `0xaf721e8` are in `output/file_backend.json`.
