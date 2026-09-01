# Thunk targets

Two thunk-class entries exist in the bounded groups:

- Secondary ELF `0x04712c64`: `this - 0x28`, final target Shutdown at
  ELF `0x04712c6c`.
- Primary entry 75: direct PLT branch to an unresolved external target.

Transitive resolution stopped at depth one in both cases. Only the adjustor
thunk converges with a known DolphinUpdater owner method.
