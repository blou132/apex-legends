# Tracee and tracer build

The committed sources are the exact sources used by the test. They were built
with official Android NDK r27d, Clang 18, target
`aarch64-linux-android26`, `-O0`, `-fno-inline`,
`-fno-omit-frame-pointer`, PIE, and warnings as errors.

Both outputs were ELF64 AArch64 PIE binaries. The tracee exposes a non-inlined
eight-argument `trace_target` at ELF value `0x1c58`, size 104 bytes. Its first
instruction is `0xd10103ff`. It waits on a temporary local file gate so no
target call can occur before programming is complete.

Local-only SHA-256 values:

```text
tracee = 680F34AEFD52D4AAA54F2E85C3C563BB8257F3AB345B128713C45AC83267296A
tracer = D0E23798A8F142ACB7733E32099CC745798B8A395AEB91495CA08907A80FF60A
```

Device hashes matched before execution.

```text
TRACEE_BUILD_VALID = YES
```
