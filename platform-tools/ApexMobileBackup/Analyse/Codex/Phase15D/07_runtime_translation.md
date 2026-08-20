# ARM64 runtime translation

Phase15C established that the unchanged guest is x86_64, advertises
`arm64-v8a`, configures `/system/lib64/libndk_translation.so`, enables native
bridge execution, and maps ARM64 to x86_64.

Phase15D adds direct application-runtime evidence rather than relying on APK
installation or process lifetime alone. Android's native loader successfully
loaded multiple libraries from the Apex app's ARM64 library directory. The
loader explicitly reported successful ARM64 loads for plugin libraries and for
`libUE4.so` while Apex was running in that x86_64 guest.

This evidence chain confirms translated ARM64 execution:

1. unchanged x86_64 guest with the verified ARM64 native bridge;
2. package primary ABI `arm64-v8a`;
3. successful runtime loader events from the app's ARM64 directory;
4. successful `libUE4.so` loader event and a stable live process.

```text
ARM64_TRANSLATION_RUNTIME = CONFIRMED
```

No runtime address, mapping range, or load bias is inferred from a loader event.
