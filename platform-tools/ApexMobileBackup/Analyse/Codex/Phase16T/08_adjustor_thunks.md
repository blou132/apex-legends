# Adjustor thunks

The first secondary entry points to ELF `0x04712c64`:

```asm
sub x0, x0, #0x28
b   0x04712c6c
```

This is a proven non-virtual ARM64 adjustor thunk. It transforms a secondary
subobject pointer at top-object offset `+0x28` into the top-object pointer and
tail-branches to `DolphinUpdater::Shutdown`.
