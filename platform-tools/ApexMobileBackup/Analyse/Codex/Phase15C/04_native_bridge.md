# Native bridge

No guest existed from which to read properties or test targeted library paths.

```text
NATIVE_BRIDGE_PROPERTY = UNKNOWN_NOT_BOOTED
NATIVE_BRIDGE_LIBRARY_PRESENT = UNKNOWN_NOT_BOOTED
NATIVE_BRIDGE_ENABLED = UNKNOWN_NOT_BOOTED
ARM64_ISA_MAPPING = UNKNOWN_NOT_BOOTED
ARM64_TRANSLATION_GUEST_CONFIG = UNKNOWN_NOT_BOOTED
```

The Phase15B static image configuration for `libndk_translation.so` remains
valid, but it is not promoted to runtime guest evidence.
