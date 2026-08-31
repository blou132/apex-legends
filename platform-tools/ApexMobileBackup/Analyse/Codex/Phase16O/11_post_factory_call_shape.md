# Post-factory call shape

The exact sequence is:

```text
0x05a314cc  bl  CreateDolphin@plt
0x05a314d0  mov x1, x0
0x05a314d4  sub x0, x29, #0x38
0x05a314d8  add x2, sp, #0x40
0x05a314dc  bl  std::__throw_length_error(char const*)@plt
```

As machine register preparation, `x0 = destination`, `x1 = source`, and
`x2 = allocator-like temporary` resembles a three-argument string
constructor. That shape matches the PLT entry adjacent to the actual throw
stub, but it does not change the independently confirmed branch target.

The real throw helper semantically consumes only `x0`. The factory return in
`x1` is not stored, returned, released, dispatched through, or otherwise used.

```text
RETURN_X1_SEMANTIC_ROLE = UNUSED_BY_ACTUAL_CALLEE; ADJACENT_STRING_CONSTRUCTOR_SOURCE_SHAPE
POST_CREATEDOLPHIN_PREPARED_CALL_SHAPE = STRING_CONSTRUCTION_LIKE
CREATEDOLPHIN_RESULT_SEMANTICALLY_USED = NO
```
