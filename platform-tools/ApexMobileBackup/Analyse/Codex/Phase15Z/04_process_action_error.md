# ProcessActionError

`ProcessActionError` is `FUN_005be71c` at Ghidra `0x005be71c`, ELF
`0x004be71c`.

The effective signature is:

```text
void ProcessActionError(cu::CActionMgr *manager)
```

The function does not receive the code as a direct parameter. It removes the
single pending `(action, error_code)` node from the manager queue, obtains the
stage through action virtual slot `+0x20`, and calls the external result
callback stored at `cu::CActionMgr+0x3b8` through slot `+0x00` with
`(stage, raw_error_code)`. It then cancels the action through slot `+0x18` and
clears associated action state.

For the Phase15U event, runtime stage was `69` (`0x45`) and the queued code was
`0x0930002a`. No mask, subtraction, string formatting, or code translation is
present in `ProcessActionError`.

PROCESS_ACTION_ERROR_SIGNATURE = void FUN_005be71c(cu::CActionMgr *)
PROCESS_ACTION_ERROR_INPUT_CODE = pending queue node word 3, raw uint32
PROCESS_ACTION_ERROR_STAGE = action virtual +0x20; runtime 69
PROCESS_ACTION_ERROR_OUTPUTS = external callback +0x00, action cancel, state cleanup
