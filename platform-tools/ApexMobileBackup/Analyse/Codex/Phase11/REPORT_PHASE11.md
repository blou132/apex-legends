# Phase11 - Native resume main init callgraph

Date: 2026-08-13

## Executive result

The Java declaration and ABI are confirmed, but the native function address is
not. The exact JNI name occurs once in `libUE4.so` `.dynstr`; however, it is not
referenced by the authoritative 824-entry dynamic symbol table, the larger
section-sized symbol view, a direct `JNINativeMethod` row, a `.rela.dyn` addend,
or an exact AArch64 name-address materialization.

No arbitrary nearby or stripped function was selected. Without a proven root,
Phase11 cannot build the requested local callgraph or attribute a state variable,
worker, wait, Android/render branch, Lua boundary, ClientLaunch stage, or Login
path to `nativeResumeMainInit`.

The runtime still confirms that Java invokes and returns from the native method.
The manifest declares the NativeActivity library as `UE4`, but the stricter
Phase11 condition for proving residency in the exact imported `libUE4.so` is not
met. The runtime load base remains unknown.

## Required results

```text
NATIVE_RESUME_MAIN_INIT_FUNCTION = UNKNOWN
JNI_ENTRY_CONFIRMED = UNKNOWN
LIBUE4_LOADED = PROBABLE
LIBUE4_LOAD_BASE = UNKNOWN
FIRST_BLOCKING_WAIT = UNKNOWN
FIRST_NATIVE_STARTUP_GATE = UNKNOWN
LUA_INIT_REACHABLE = UNKNOWN
CLIENTLAUNCH_REACHABLE = UNKNOWN
LOGIN_REACHABLE = UNKNOWN
REQUEST_AVATAR_SERVER_LIST_REACHABLE = UNKNOWN
NETWORK_DEPENDENCY_AT_GATE = UNKNOWN
FINAL_GATE = CONFIRMED F JNI_ENTRYPOINT_NOT_RESOLVED
```

## Reproducibility

`ApexPhase11ResumeExport.java` runs against the existing Ghidra program with
`-process libUE4.so -noanalysis -readOnly`. It validates image base `0x100000`,
checks the DEX-correlated name, reads authoritative ELF dynamic tags, evaluates
registration/relocation fallbacks, and emits a depth-five callgraph only after
one unique root is proven. In this run it correctly stopped before graph export.

Raw exporter output, the Ghidra database, the full library, APK/OBB/PAK files,
and device data remain local-only and uncommitted.
