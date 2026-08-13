# nativeResumeMainInit resolution

Phase11 confirmed the DEX declaration and the orphaned full JNI-name occurrence,
but did not resolve a native address. Phase12 does not improve that mapping
because `JNI_OnLoad` itself is unresolved.

```text
NATIVE_RESUME_MAIN_INIT_FUNCTION = UNKNOWN
GHIDRA_ADDRESS = UNKNOWN
ELF_VIRTUAL_OFFSET = UNKNOWN
JNI_REGISTRATION_METHOD = UNKNOWN
JNI_TABLE_ROW = UNKNOWN
JNI_ENTRY_CONFIRMED = UNKNOWN
LIBUE4_LOADED = PROBABLE
LIBUE4_LOAD_BASE = UNKNOWN
FIRST_DIRECT_CALLEES = UNKNOWN
```

The manifest still declares `android.app.lib_name=UE4`, and runtime Java still
invokes and returns from the native method. The stricter proof that this method
maps into the exact imported `libUE4.so` remains unavailable, so
`LIBUE4_LOADED` is not promoted to `CONFIRMED`.
