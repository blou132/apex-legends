# Callback initializer callers

The exact callback initializer remains Ghidra `0x0a2a403c` / ELF
`0x0a1a403c`. A bounded raw AArch64 scan for direct `B` and `BL` targets found
no code caller. The sole exact callback-table construction is inside the
initializer itself; the exact table literal occurs only as relocation data.

No indirect-call scan was attempted because arbitrary `BLR` enumeration is
outside scope.

```text
CALLBACK_INITIALIZER_DIRECT_CALLER_COUNT = 0
CALLBACK_INITIALIZER_REACHABLE_CALLERS = NONE
```
