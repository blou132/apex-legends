# Entry reachability

The raw graph marks every block reachable because it gives every `BL` a normal
fallthrough. That model reaches `CreateDolphin` through:

```text
0x05a31444 -> 0x05a3144c throw_length_error -> 0x05a31450 ...
             -> 0x05a314b8 -> 0x05a314c8
```

The call at `0x05a3144c` is independently resolved as
`std::__throw_length_error(char const*)`. This imported C++ helper is
noreturn, so the legitimate path terminates there. Removing that impossible
fallthrough leaves the target and all of its structural predecessors outside
the entry-reachable semantic graph.

No separate direct reference enters `0x05a31450`, `0x05a314c8`,
`0x05a314cc`, or the post-call block at `0x05a314e0`.

```text
STATIC_CREATEDOLPHIN_CALLSITE_REACHABLE = NO
CREATEDOLPHIN_CALLSITE_PATH_CLASS = STATICALLY_UNREACHABLE
```
