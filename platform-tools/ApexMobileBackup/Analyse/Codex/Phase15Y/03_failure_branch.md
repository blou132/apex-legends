# Failure branch resolution

## Puffer

The bounded log contains the exact Phase15V branch text `connect server
timeout`. The immediately following `CPufferInitAction::run` line returns
decimal `70254639`, which is exactly hexadecimal `0x0430002f`.

No competing Phase15V result code is logged in the window. The exact Puffer
branch is therefore resolved, not inferred from DNS failure alone.

## Dolphin version manager

A separate earlier version action emits decimal `154140714`, hexadecimal
`0x0930002a`, with `UpdateResult` and result `-1`. This is not a Puffer internal
result and must not be relabeled as `0x0430002f`.

```text
RUNTIME_PUFFER_INTERNAL_RESULT = 70254639 (0x0430002f)
PHASE15U_PUFFER_FAILURE_BRANCH = CONNECT_SERVER_TIMEOUT
PHASE15U_PUFFER_INTERNAL_CODE = 70254639 (0x0430002f)
DOLPHIN_UPDATE_RESULT_CODE = 154140714 (0x0930002a)
```
