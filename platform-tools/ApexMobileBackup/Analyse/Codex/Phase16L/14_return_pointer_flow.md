# Return pointer flow

The bounded forward slice is exact:

```text
0x05a314cc  bl  CreateDolphin@plt
0x05a314d0  mov x1, x0
0x05a314d4  sub x0, x29, #0x38
0x05a314d8  add x2, sp, #0x40
0x05a314dc  bl  std::__throw_length_error(char const*)@plt
```

There is no object/global store, return passthrough, virtual dispatch, release,
or owner-field write. The imported throw helper is noreturn and only its `x0`
argument is semantically consumed. No cross-function pointer propagation is
justified.

```text
CREATEDOLPHIN_RETURN_FLOW = x0 -> x1 -> unused by noreturn throw helper; pointer lost
DOLPHIN_OBJECT_STORAGE = NONE_AT_PROVEN_CALLSITE
DOLPHIN_OWNER_FIELD = NONE
```
