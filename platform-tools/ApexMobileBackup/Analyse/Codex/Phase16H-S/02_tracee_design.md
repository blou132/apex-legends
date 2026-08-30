# Disposable tracee design

`tracee_function.c` defines visible, non-inlined `trace_target` with exactly
eight `uint64_t` arguments. Volatile inputs carry distinct values from
`0x1111` through `0x8888`. The function returns their deterministic sum,
`0x26664`, and the caller verifies every result.

The call repeats every 100 ms and periodically reports a successful result.
The program performs no network access, privileged action, or file mutation.
It handles SIGINT and SIGTERM for a clean exit.
