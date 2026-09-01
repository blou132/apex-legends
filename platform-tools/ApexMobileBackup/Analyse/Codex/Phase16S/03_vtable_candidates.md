# Vtable candidates

A valid candidate required at least two known DolphinUpdater method pointers
inside one window no larger than `0x200` bytes. No window met that rule.

`DOLPHINUPDATER_TABLE_CANDIDATE_COUNT = 0`

The singleton Shutdown hit was inspected only in the allowed bounded window
Ghidra `0x0af56708..0x0af568ff`. It lies in a dense sequence of executable
pointers. At `0x0af568a0` the value is `-0x28`, followed by zero at
`0x0af568a8` and another executable-pointer sequence. This is compatible with
stripped C++ vtable material, but it does not establish a table start, RTTI, or
DolphinUpdater ownership.
