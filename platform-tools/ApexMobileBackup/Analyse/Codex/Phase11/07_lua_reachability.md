# Lua reachability

Known corrected Lua-related Ghidra functions were prepared as comparison
targets:

- `FUN_049a8b54`
- `FUN_049a9694`
- `FUN_048ab4d0`
- `FUN_06be427c`
- `FUN_06be3f4c`

Because there is no resolved resume root and no local callgraph, none can be
classified as directly or indirectly reachable from `nativeResumeMainInit`.
Their global presence in `libUE4.so` is not reachability evidence.

```text
LUA_INIT_REACHABLE = UNKNOWN
```
