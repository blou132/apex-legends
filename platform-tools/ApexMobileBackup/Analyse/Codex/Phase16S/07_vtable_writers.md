# Vtable writers

An exact vptr-write search requires a proven table start. The singleton
Shutdown cell does not provide one, so there was no valid address to use for a
target-specific writer search.

`DOLPHINUPDATER_VPTR_WRITE_COUNT = 0_PROVEN`

This means no writer was proven under the authorized route. It is not a broad
statement that the class has no vptr assignment.
