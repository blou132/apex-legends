# W23 source

The readable path resolves the immediate `W23` producer:

1. ELF `0x05a2f02c` calls `FUN_04a93d9c`.
2. If return bit 0 is clear, ELF `0x05a2f320` forces `W23 = 0`.
3. Otherwise ELF `0x05a2f04c` calls `FUN_04a94aac`, and
   ELF `0x05a2f050` keeps its return bit 0 in `W23`.

The gate at ELF `0x05a2f084` requires `W23 == 0`. The exact business meaning
of both helpers is not proven, so this remains a local status predicate.

```text
W23_VALUE_SOURCE = ZERO_IF_FUN_04a93d9c_BIT0_CLEAR_ELSE_BIT0_OF_FUN_04a94aac_RETURN
W23_SEMANTIC_ROLE = LOCAL_HELPER_STATUS_PREDICATE_UNKNOWN
```
