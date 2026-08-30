# Runtime address validation

The tracee started behind the Phase16H-W gate and emitted only its ready state.
The gate did not yet exist, so `trace_target` could not execute prematurely.

The runtime target was derived fresh from the committed ELF symbol, matching
PT_LOAD segment, current `/proc/<pid>/maps`, and load bias. It was four-byte
aligned, inside the correct executable RX mapping, and correlated with the
expected first instruction of `trace_target`.

The absolute address and raw maps remain local-only and are not committed.

Result: `TRACE_TARGET_RUNTIME_VALIDATED = YES`.
