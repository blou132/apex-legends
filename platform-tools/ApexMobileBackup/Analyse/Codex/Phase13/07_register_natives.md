# RegisterNatives result

## Confirmed generic registration

Targeted Ghidra decompilation confirms a direct JNIEnv vtable call at offset
`0x6b8`, consistent with `RegisterNatives`, in:

- `libanogs.so::JNI_OnLoad`
- `libGPM.so::JNI_OnLoad`
- `libmmkv.so::JNI_OnLoad`
- `libtgpa.so::JNI_OnLoad`

These paths also show class lookup behavior. They are SDK registrations, not a
GameActivity match: none of the four libraries contains the exact GameActivity
class, target method, or full JNI name.

## Target table

No library provides a demonstrated row with all three required fields:

```text
name      = nativeResumeMainInit
signature = ()V
fnPtr     = unique executable address
```

Consequently no two neighboring GameActivity rows can be validated. Phase11
and Phase12 already found no direct row or resolved registration root in
`libUE4.so`; Phase13 found no target row in any other exact APK library.

```text
REGISTER_NATIVES_FOUND = CONFIRMED GENERIC_SDK_ONLY
TARGET_REGISTER_NATIVES_ROW = UNKNOWN
OTHER_GAMEACTIVITY_ROWS_VALIDATED = 0
```
