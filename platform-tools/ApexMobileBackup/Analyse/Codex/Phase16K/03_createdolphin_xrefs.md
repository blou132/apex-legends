# CreateDolphin references

Three exact client references were retained in the sole consumer. The provider
export is recorded separately as symbol identity, not counted as a client
xref.

| Class | Site | Result |
|---|---|---|
| `RELOCATION` | `libUE4.so` Ghidra `0x0b47e8c8`, ELF `0x0b37e8c8` | Confirmed GOT cell / `R_AARCH64_JUMP_SLOT` |
| `COMPUTED_CALL` | `libUE4.so` Ghidra `0x0a4c18fc` | Confirmed internal PLT dispatch |
| `DIRECT_CALL` | `libUE4.so` Ghidra `0x05b314cc` | Exact call, but semantic owner candidate invalidated |

The direct call is in `FUN_05b311a8`. Its surrounding block consistently
calls the PLT entry immediately before the API matching each argument shape.
At the target site, the returned pointer is passed to a one-argument throw
helper instead of being stored or virtually dispatched. The same block logs a
current-APK-path operation, whose matching Dolphin helper is the adjacent PLT
entry. As decoded, this path cannot establish factory ownership and is marked
`INVALIDATED`, not silently promoted.

There is no semantic function-pointer table or address-taken use. The GOT cell
is import plumbing.

```text
CREATEDOLPHIN_REFERENCE_COUNT = 3
CREATEDOLPHIN_CODE_CALLERS = 1 exact; 0 proven semantic owners
CREATEDOLPHIN_ADDRESS_TAKEN = NO
```
