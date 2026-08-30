# NDK build and static validation

Both clean sources were compiled PC-only with the official Android NDK r27d
`aarch64-linux-android26-clang` using:

```text
-O0 -fno-inline -fno-omit-frame-pointer -fPIE -pie -Wall -Wextra -Werror
```

Results:

- tracee: ELF64 AArch64 PIE; visible `trace_target` symbol;
- tracer: ELF64 AArch64 PIE;
- all layout `_Static_assert` checks passed;
- warning-as-error compilation passed;
- tracer disassembly shows the atomic setter storing slot-0 address/control,
  loading iovec length 24, and calling ptrace;
- disable disassembly calls the same setter with control zero;
- no executable was run.

Build binaries, disassembly, hashes, and scratch output remain under ignored
`LocalInputs/Phase16H-V` storage and are not committed.

Source invariants passed: one active atomic call, no disabled pre-step, no
address-readback requirement for disable, no POKE operation, and slot-0 iovec
assertion present.
