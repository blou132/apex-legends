# JNI_OnLoad resolution

## Exact symbol paths

The Ghidra program contains no exact `JNI_OnLoad` symbol and no function named
`JNI_OnLoad`. The authoritative ELF dynamic layout is:

| Item | Ghidra address | Count |
|---|---:|---:|
| `DT_STRTAB` | `0x21dc658` | n/a |
| `DT_SYMTAB` | `0xb9cc5b8` | 824 symbols |
| `DT_HASH` | `0x105904` | 824 symbols |

None of the 824 loader-visible dynamic symbols is `JNI_OnLoad`.

## Loader and conventional paths

`DT_INIT` contains ELF virtual address `0xb8c503c`, converted to Ghidra
`0xb9c503c`. Ghidra has no function at that address. No `DT_INIT_ARRAY` metadata
is present in the dynamic table.

The targeted instruction pass searched for the complete conventional JavaVM
sequence: a vtable load from the first JavaVM argument, an indirect call through
slot `0x30` (`GetEnv`), and preparation of `JNI_VERSION_1_6` (`0x00010006`) in
the version argument. It found zero candidate functions.

```text
JNI_ONLOAD_FUNCTION = UNKNOWN
GHIDRA_ADDRESS = UNKNOWN
ELF_VIRTUAL_OFFSET = UNKNOWN
RESOLUTION_METHOD = EXACT_SYMBOL_DYNAMIC_SYMBOL_LOADER_METADATA_AND_GETENV_PATTERN
CONFIDENCE = CONFIRMED_NOT_RESOLVED_BY_TESTED_STATIC_PATHS
```
