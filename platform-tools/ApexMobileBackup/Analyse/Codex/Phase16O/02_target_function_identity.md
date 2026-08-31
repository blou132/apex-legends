# Target function identity

The exact ELF FDE covers `0x05a311a8...0x05a31ca8`. With the validated
`+0x00100000` Ghidra image-base bias, this is `FUN_05b311a8` with body bounds
`0x05b311a8...0x05b31ca7`.

The contiguous ELF range contains 704 aligned AArch64 instructions. Ghidra's
function body contains 2556 bytes because its body model excludes recognized
discontiguous noreturn fallthrough regions. That difference does not change
the FDE or raw range.

The existing project was opened with `-process libUE4.so -noanalysis
-readOnly`; no import or reanalysis was performed.

```text
GHIDRA_READ_ONLY = YES
GHIDRA_EXISTING_PROJECT_REUSED = YES
TARGET_FUNCTION_RANGE_VALID = YES
```
