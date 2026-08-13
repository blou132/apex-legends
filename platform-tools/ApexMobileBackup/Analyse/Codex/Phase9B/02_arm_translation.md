# ARM64 translation capability

## Classification

```text
ARM64_TRANSLATION_SUPPORTED = YES
```

This classification is based on explicit local image metadata, not on an assumption about all x86_64 images:

| Evidence | Local value |
| --- | --- |
| system ABI list | `x86_64,arm64-v8a` |
| system 64-bit ABI list | `x86_64,arm64-v8a` |
| package translated ABI | `arm64-v8a` |
| native bridge | `libndk_translation.so` |
| ARM64 ISA mapping | `arm64 -> x86_64` |
| native bridge execution | enabled |

The image notice also inventories the ARM64 program runner, ARM64 linker/configuration files, translated ARM64 libraries, and native-bridge proxy libraries.

## Runtime limit

The image could not boot to an ADB-visible state on the current host. Consequently the expected guest properties could not be read at runtime and an ARM64 APK install could not validate the translation path end to end.

Static image support is therefore `YES`; successful execution of this client remains untested.
