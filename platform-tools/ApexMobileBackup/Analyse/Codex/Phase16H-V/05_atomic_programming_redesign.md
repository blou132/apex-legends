# Atomic programming redesign

The Phase16H-V tracer has one programming operation before any continuation:

```text
SETREGSET(slot 0 address, control 0x000001e5, iov_len 24)
```

There is no address-plus-disabled-control pre-step and no software breakpoint
or tracee memory write. The tracer immediately reads the full hardware-debug
state and requires:

- unchanged regset length;
- exact target-address match;
- normalized control `0x000041e4`.

Only after those checks does it create the tracee start gate and issue
`PTRACE_CONT`. A failed SET or readback cannot release the tracee into the
target call.

Static source inspection confirms one active programming call, no disabled
pre-step in the main path, no `PTRACE_POKE*`, and a 24-byte slot-0-only iovec.

Result: `DISABLED_PRESTEP_REMOVED = YES`.
