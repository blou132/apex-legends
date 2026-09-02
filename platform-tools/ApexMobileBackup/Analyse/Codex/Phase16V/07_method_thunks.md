# Method thunks

The only method thunk is the confirmed Shutdown secondary-to-primary adjustor:

```asm
sub x0, x0, #0x28
b   0x04712c6c
```

The caller supplies a secondary-subobject pointer. The thunk subtracts `0x28`
to recover the top DolphinUpdater pointer before entering Shutdown.

```text
OWNER_METHOD_THUNK_COUNT = 1
OWNER_METHOD_THUNK_ADJUSTMENTS = SHUTDOWN:THIS_MINUS_0x28
TOP_OBJECT_NORMALIZATION_VALID = YES
```
