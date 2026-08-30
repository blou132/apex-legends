# Hardware SIGTRAP

Only after successful readback did the tracer create the gate and continue the
tracee. The next stop was SIGTRAP. GETSIGINFO reported code 4
(`TRAP_HWBKPT`), and the trap address matched the freshly resolved
`trace_target` entry.

- `SIGTRAP_RECEIVED = YES`
- `SIGTRAP_CODE = 4`
- `SIGTRAP_HW_CLASSIFICATION = TRAP_HWBKPT`
- `SIGTRAP_ADDRESS_MATCH = YES`

No absolute trap address is committed.
