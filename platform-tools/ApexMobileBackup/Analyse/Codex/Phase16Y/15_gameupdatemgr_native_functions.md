# GameUpdateMgr native functions

The class registration supplies native-registration callback Ghidra
`0x0a2aed54`. Its bytes do not decode as a coherent ARM64 function in the
existing project and the target is classified as
`NON_DISASSEMBLED_OR_OPAQUE`.

No class-scoped name/function-pointer table is independently reachable from
the accessor.

`GAMEUPDATEMGR_NATIVE_FUNCTION_COUNT = UNKNOWN`

`GAMEUPDATEMGR_NATIVE_FUNCTION_TABLE = UNKNOWN`

`GAMEUPDATEMGR_NATIVE_FUNCTIONS = NONE_RECOVERABLE`
