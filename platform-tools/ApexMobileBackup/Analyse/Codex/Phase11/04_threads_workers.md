# Threads and workers

No worker launch is attributable to the unresolved JNI entrypoint.

| Start function | Caller | Waited by main | Signal or condition | Status |
|---|---|---|---|---|
| `UNKNOWN` | `UNKNOWN` | `UNKNOWN` | `UNKNOWN` | `UNKNOWN` |

This result does not invalidate the presence of UE4 game, render, task graph,
or Java callback threads elsewhere in the binary. It only records that no such
worker is reachable from a confirmed Phase11 root.
