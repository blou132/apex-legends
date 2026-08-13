# Phase12 - JNI_OnLoad and RegisterNatives resolution

Date: 2026-08-13

## Executive result

`JNI_OnLoad` is not uniquely resolvable in the exact local `libUE4.so` Ghidra
program. It is absent from Ghidra's exact symbols and named functions and from
the authoritative 824-entry ELF dynamic symbol table. Dynamic loader metadata
has `DT_INIT` but no `DT_INIT_ARRAY`; `DT_INIT` does not resolve to a Ghidra
function. A targeted conventional JavaVM pass found no function combining
vtable slot `0x30` with `JNI_VERSION_1_6`.

Per the Phase12 stop rule, no unrelated stripped function was followed. The
GameActivity `FindClass`, `RegisterNatives`, table pointer, target row, and
`nativeResumeMainInit` function therefore remain unknown. Phase11's full JNI
name in `.dynstr` remains an orphaned string witness, not a registration row.

## Required results

```text
JNI_ONLOAD = UNKNOWN
JNI_ONLOAD_FUNCTION = UNKNOWN
GAMEACTIVITY_FINDCLASS = UNKNOWN
REGISTER_NATIVES = UNKNOWN
TABLE_POINTER = UNKNOWN
TARGET_ROW = UNKNOWN
NATIVE_RESUME_MAIN_INIT_FUNCTION = UNKNOWN
GHIDRA_ADDRESS = UNKNOWN
ELF_VIRTUAL_OFFSET = UNKNOWN
JNI_REGISTRATION_METHOD = UNKNOWN
JNI_TABLE_ROW = UNKNOWN
OTHER_ROWS_VALIDATED = 0
JNI_ENTRY_CONFIRMED = UNKNOWN
LIBUE4_LOADED = PROBABLE
LIBUE4_LOAD_BASE = UNKNOWN
FIRST_DIRECT_CALLEES = UNKNOWN
FINAL_GATE = CONFIRMED F JNI_ONLOAD_NOT_RESOLVED
```

## Reproducibility and safety

`ApexPhase12JNIExport.java` runs against `libUE4.so` with `-noanalysis` and
`-readOnly`. It checks exact Ghidra and loader-visible ELF symbols, reports
loader initialization metadata, and requires the complete JavaVM GetEnv/version
pattern before accepting a conventional candidate. It emits a depth-five local
graph only after one unique root is established; this run correctly stopped
before graph traversal.

Raw output, the Ghidra database, complete binaries, DEX, APK/OBB/PAK content,
logs, and device data remain local-only and uncommitted.
