# Trace tool inventory

| Facility | Presence | Capability for this target |
|---|---|---|
| `debuggerd -b` | present and advertised | `NOT_SUPPORTED`: Phase14 proved OS process-dump denial for this package |
| `showmap` | absent | `NOT_SUPPORTED` |
| `simpleperf` | absent on device | `NOT_SUPPORTED` |
| `perf` | absent | `NOT_SUPPORTED` |
| `atrace` | present | `SUPPORTED` for framework/kernel trace events, not native PCs or registers |
| `systrace` | absent on device | `NOT_SUPPORTED` |
| Perfetto / `traced` | absent | `NOT_SUPPORTED` |
| `heapprofd` | absent | `NOT_SUPPORTED` |
| JDWP | package ineligible | `NOT_SUPPORTED` |
| `run-as` | package rejected | `NOT_SUPPORTED` |
| `am profile` | command interface present | `NOT_SUPPORTED` for exact native target/arguments on this package |
| package compile/profile commands | present | `NOT_SUPPORTED`: ART profile management, not a native runtime callback trace |

`atrace --list_categories` exposes normal Android framework, scheduler,
Bionic, Binder, and application event categories. These categories do not
sample AArch64 user-space program counters, unwind native stacks, expose
registers, or report an indirect `BLR` destination.

No trace was started and no tool was attached to any process.
