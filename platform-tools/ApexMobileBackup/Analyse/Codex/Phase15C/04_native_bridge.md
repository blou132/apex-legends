# Native bridge

Targeted guest properties and path checks establish the bridge configuration:

```text
NATIVE_BRIDGE_PROPERTY = libndk_translation.so
NATIVE_BRIDGE_LIBRARY_PRESENT = CONFIRMED /system/lib64/libndk_translation.so
RO_ENABLE_NATIVE_BRIDGE = EMPTY
RO_ENABLE_NATIVE_BRIDGE_EXEC = 1
NATIVE_BRIDGE_ENABLED = CONFIRMED BY EXEC_PROPERTY
ARM64_ISA_MAPPING = CONFIRMED arm64 -> x86_64
ARM64_TRANSLATION_GUEST_CONFIG = CONFIRMED
```

Only three plausible library paths were tested. The system path existed; the
corresponding system-ext and vendor paths did not. No global filesystem scan or
proprietary ARM64 execution test was performed.
