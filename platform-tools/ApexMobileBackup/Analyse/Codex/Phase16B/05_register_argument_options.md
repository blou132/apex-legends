# Register and argument visibility

The two useful function boundaries would be:

- `CVersionMgrImp::Init`, static Ghidra `FUN_00576180`
- `CVersionStrategy_Win32` slot `+0x00`, static Ghidra `FUN_0050cb38`

Observing the callback pointer, `this`, stage, or raw error at either boundary
requires AArch64 register state or equivalent process memory. None of the
available non-invasive facilities provides this:

- `atrace` records trace events, not arbitrary function arguments;
- JDWP is unavailable and would not provide native AArch64 argument registers;
- `am profile` does not expose native register state;
- `debuggerd`, even if permitted, is a sampled backtrace rather than a
  deterministic function-boundary register probe.

```text
REGISTER_ARGUMENT_TRACE_AVAILABLE = NO
```
