# Phase12 scope

Date: 2026-08-13

Phase12 attempts to resolve the exact `JNI_OnLoad` function in the existing
local `libUE4.so` Ghidra program before following any GameActivity registration
path. The program was opened with `-process libUE4.so -noanalysis -readOnly` and
the validated image base `0x100000`.

The allowed resolution paths were exact Ghidra symbols/functions, the
authoritative ELF dynamic symbol table, loader initialization metadata, and the
conventional JavaVM `GetEnv(..., JNI_VERSION_1_6)` pattern. No unique function
was obtained, so analysis stopped before `FindClass` or `RegisterNatives` as
required.

No client was launched and no hook, patch, backend, DNS, certificate, account,
device, APK, or native library change was used.

```text
FINAL_GATE = F JNI_ONLOAD_NOT_RESOLVED
```
