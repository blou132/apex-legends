# Indirect dispatch visibility

Phase16A resolves the static dispatch site inside `FUN_0050cb38` but not the
dynamic target selected from the external callback's vtable slot `+0x28`.

Resolving that target at runtime requires one of:

- branch tracing that records user-space indirect targets;
- a native debugger/ptrace stop at the `BLR` instruction;
- register or memory capture of the callback object and vtable;
- a native call stack sampled after entering the destination.

The PRA-LX1 exposes none of those paths for this non-debuggable,
non-profileable package under the authorized constraints. Scheduler and app
events from `atrace` cannot identify a `BLR` target.

```text
INDIRECT_CALL_TARGET_TRACE_AVAILABLE = NO
CALLBACK_OBJECT_VTABLE_TRACE_AVAILABLE = NO
```
