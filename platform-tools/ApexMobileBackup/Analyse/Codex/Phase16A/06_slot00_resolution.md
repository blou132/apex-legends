# Callback slot 00

Slot `+0x00` of the `cu::CVersionStrategy_Win32` callback vtable points
directly to `FUN_0050cb38`:

```text
CALLBACK_SLOT_00_TARGET = FUN_0050cb38 (Ghidra 0x0050cb38, ELF 0x0040cb38)
CALLBACK_SLOT_00_THUNK = NO
CALLBACK_SLOT_00_IMPLEMENTATION = FUN_0050cb38
```

The effective argument semantics reconstructed across `ProcessActionError` and
this implementation are:

```text
this = cu::CVersionStrategy_Win32 pointer
argument 1 = uint32 action stage
argument 2 = uint32 raw error code
```

The implementation preserves the second argument as an unsigned 32-bit value.
At Ghidra `0x0050cc04`/`0x0050cc0c`, it invokes slot `+0x28` of the external
client callback stored at `CVersionStrategy+0x18`, forwarding the stage and raw
error unchanged.
