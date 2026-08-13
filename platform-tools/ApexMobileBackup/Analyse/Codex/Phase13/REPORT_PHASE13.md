# Phase13 - APK native library ownership

Date: 2026-08-13

## Executive result

The exact base APK contains 17 ELF64 AArch64 libraries. Authoritative dynamic
tables show 12 global `JNI_OnLoad` function exports, but no library exports
`Java_com_epicgames_ue4_GameActivity_nativeResumeMainInit` or the short method
name. All 12 roots were opened in Ghidra with `-noanalysis -readOnly` and
resolved at their exact ELF values.

Four SDK roots directly use a JNIEnv `RegisterNatives` slot, but none of the 12
libraries contains the GameActivity class or target method strings. No target
registration row is demonstrated. `libUE4.so` remains the only library with the
full name, short name, slash class, and `()V`; those are string witnesses with
no matching dynamic symbol or function pointer.

DEX confirms that GameActivity loads seven plugin libraries, then directly
loads `gnustl_shared`, `gcloud`, `gcloudcore`, `GVoice`, and finally `UE4`.
The manifest confirms `android.app.lib_name=UE4`. These establish loading
context, not ownership of the unresolved method.

The 17-library static scope is exhausted without a proven owner or function.
No arbitrary libUE4 function was selected, and no runtime work was performed.

## Required results

```text
NATIVE_LIBRARY_COUNT = CONFIRMED 17
GAMEACTIVITY_MAIN_LIBRARY = CONFIRMED UE4
LIBRARIES_WITH_JNI_ONLOAD = CONFIRMED [libCrashSight.so, libGPM.so,
  libGVoice.so, libINTLTAB.so, libMSDKCore.so, libTDataMaster.so,
  libanogs.so, libanort.so, libgcloud.so, libgcloudcore.so, libmmkv.so,
  libtgpa.so]
LIBRARIES_WITH_FULL_JNI_NAME = CONFIRMED [libUE4.so STRING_ONLY]
LIBRARIES_WITH_SHORT_METHOD_NAME = CONFIRMED [libUE4.so STRING_ONLY]
OWNER_LIBRARY = UNKNOWN
REGISTER_NATIVES_FOUND = CONFIRMED GENERIC_SDK_ONLY; TARGET_ROW UNKNOWN
NATIVE_RESUME_MAIN_INIT_FUNCTION = UNKNOWN
JNI_ENTRY_CONFIRMED = UNKNOWN
LIBUE4_LOADED = PROBABLE
LIBUE4_LOAD_BASE = UNKNOWN
STATIC_LIBRARY_OWNERSHIP_EXHAUSTED = CONFIRMED YES
FINAL_GATE = CONFIRMED E STATIC_LIBRARY_OWNERSHIP_EXHAUSTED
```

## Reproducibility

`ApexPhase13JNIInventoryExport.py` validates the exact APK hash and 17-entry
count, reads each ZIP member in memory, and exports ELF/dynamic-symbol/string
metadata without publishing libraries. `ApexPhase13JNIInventoryExport.java`
validates each exported `JNI_OnLoad` in a local Ghidra project and records only
compact target-path results.

Raw output, extracted libraries, the Ghidra project, APK, DEX, OBB/PAK content,
and device data remain local-only. Public JSON contains hashes, sizes, ELF
metadata, dependency edges, DEX load sites, candidate classes, and the final
gate only.
