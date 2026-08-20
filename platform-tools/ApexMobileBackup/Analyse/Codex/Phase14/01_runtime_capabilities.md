# Runtime capabilities

## Audit result

| Capability | Result |
| --- | --- |
| Android build type | `CONFIRMED USER` |
| Device `ro.debuggable` | `CONFIRMED 0` |
| Package debuggable | `CONFIRMED NO` |
| `debuggerd` binary | `CONFIRMED AVAILABLE` |
| `debuggerd -b` interface | `CONFIRMED ADVERTISED` |
| `debuggerd -b` process dump | `CONFIRMED BLOCKED_BY_OS_POLICY` |
| `showmap` | `CONFIRMED BINARY_ABSENT` |
| Linker app logging | `CONFIRMED NO_BY_DEBUGGABLE_REQUIREMENT` |

The installed production package does not expose the `DEBUGGABLE` application
flag. Official per-app linker logging was therefore not configured. No
`debug.ld.app.*` or `debug.ld.all` property existed before or after the run.

`debuggerd` accepted the backtrace command form but returned a process-dump
failure before any frame was emitted. This is a capability result, not evidence
about the target's native execution state.
