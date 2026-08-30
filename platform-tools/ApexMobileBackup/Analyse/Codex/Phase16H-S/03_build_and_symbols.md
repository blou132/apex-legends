# Build and symbols

All binaries were built with official Android NDK r27d, revision
27.3.13750724, Clang 18.0.4, targeting `aarch64-linux-android26`.

The tracee used `-O0 -fno-inline -fno-omit-frame-pointer -fPIE -pie` and was
not stripped. Host inspection confirmed ELF64, AArch64, and DYN/PIE.

```text
TRACE_TARGET_SYMBOL_FOUND = YES
TRACE_TARGET_ELF_VALUE = 0x19a0_RELATIVE
TRACE_TARGET_SIZE = 104
TRACE_TARGET_FIRST_INSTRUCTION = 0xd10103ff
TRACEE_ARCH = AARCH64
```

Local-only binary hashes:

```text
tracee_function = 52F3A1800755ADA6468582C6B374A3367C67694B2D7AD1BECBDE20BD8E396E31
ptrace_breakpoint_probe = 796A45A5DF52B136D9B6DC03012C4BDF4DDDC597F293F98958380FD7396E9626
ptrace_hw_breakpoint_probe = ADD9FA01EB1BFCBB6E9A1D06FE3A16BA7665304C3602B593F5EBC5E463358654
```
