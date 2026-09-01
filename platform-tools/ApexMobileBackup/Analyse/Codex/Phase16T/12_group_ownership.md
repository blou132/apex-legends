# Group ownership

Two independent evidence classes support ownership:

1. Semantic: primary entry 81 is the exact known Shutdown implementation and
   carries the exact `DolphinUpdater::Shutdown` identifier.
2. ABI structural: the adjacent secondary table has offset-to-top `-0x28`
   and its first entry is an exact `this - 0x28` thunk to that Shutdown.

This resolves the local primary/secondary relationship but does not recover a
second distinct owner method or a constructor. Ownership is therefore
`PROBABLE`, not `CONFIRMED`.
