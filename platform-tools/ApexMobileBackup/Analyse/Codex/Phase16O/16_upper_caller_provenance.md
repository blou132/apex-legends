# Upper caller provenance

Ghidra reports one direct code caller: `FUN_080f6f04`, at ELF callsite
`0x07ff6fe4`. Immediately before the call:

```text
0x07ff6fdc  add x8, sp, #0x70
0x07ff6fe0  mov x0, x19
0x07ff6fe4  bl  0x05a311a8
```

`x8` is the AArch64 hidden result-buffer register and points to a caller stack
buffer of `0x400` bytes. `x0` carries the callback-owned state pointer. The
target saves that state and treats the hidden buffer as its path-bundle output.

The first physical import call in the target is `memcpy(output, 0, 0x400)`,
which is incoherent. Its register shape is the expected
`memset(output, 0, 0x400)` operation from the adjacent PLT entry, matching the
same systematic mismatch seen near `CreateDolphin`.

```text
TARGET_FUNCTION_DIRECT_CALLER = FUN_080f6f04
TARGET_FUNCTION_ARGUMENT_PROVENANCE = X8_CALLER_STACK_OUTPUT_BUFFER_SP_PLUS_70; X0_CALLBACK_OWNED_STATE_FROM_ONDOLPHINFIRSTEXTRACTSUCCESS_THIS_PLUS_8
```
