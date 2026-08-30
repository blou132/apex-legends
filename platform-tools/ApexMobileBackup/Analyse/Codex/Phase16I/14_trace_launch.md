# Trace launch

The watcher was prepared before launching Apex. It required the fresh process,
the executable offset-zero `libgcloud.so` map, and exact `Thread-10` before
invoking the tracer.

An initial pre-active run exposed 32-bit arithmetic in the Android shell
watcher. The resulting invalid address failed the tracer's read-only entry-byte
check. The tracer detached with all hardware slots still clear; no active
`SETREGSET` occurred, so the authorized attempt count remained zero. Apex was
force-stopped.

The load-bias addition was moved into 64-bit C, rebuilt, rehashed, redeployed,
and all gates were repeated. The dedicated active launch then used the corrected
calculation. This was the only active attempt.
