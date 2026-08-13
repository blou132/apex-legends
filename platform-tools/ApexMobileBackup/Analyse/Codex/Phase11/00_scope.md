# Phase11 scope

Date: 2026-08-13

Phase11 is a static, read-only investigation of
`Java_com_epicgames_ue4_GameActivity_nativeResumeMainInit` in the existing
local `libUE4.so` Ghidra program. The program was opened with `-noanalysis`
and `-readOnly`; its validated image base is `0x100000`.

The search was limited to the exact JNI name, the authoritative ELF dynamic
tables, possible `JNINativeMethod` data, relocations to the exact name, and
AArch64 address materializations for that name. No backend was contacted or
emulated. No client was launched. No APK, library, asset, account state, or
device was modified.

The entrypoint was not resolved to one unique function. The required decision
is therefore:

```text
FINAL_GATE = F JNI_ENTRYPOINT_NOT_RESOLVED
```
