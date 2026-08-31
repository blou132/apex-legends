# Runtime versus static

Phase16L's exact static model remains unchanged:

1. libUE4 ELF `0x05a314cc` calls `CreateDolphin@plt`;
2. ELF `0x05a314d0` copies returned X0 to X1;
3. X0 is replaced and X2 is prepared;
4. the next decoded branch is `throw_length_error@plt`.

Phase16M obtained package mappings and an executor candidate but no entry or
return trap. It therefore cannot promote the static caller or throw path to a
runtime confirmation.

```text
RUNTIME_CREATEDOLPHIN_CALLER = NO_ACTIVE_OBSERVATION
RUNTIME_RETURN_FLOW = UNKNOWN
STATIC_THROW_PATH_RUNTIME_CONFIRMED = NOT_REACHED
```
