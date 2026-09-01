# W23 runtime correlation

The CheckUpdate value is now statically resolved:

```text
W23 = 0 when bUseIFSFile is absent/unresolved
W23 = parsed bUseIFSFile when the setting is present
```

`W23 != 0` selects the first-extract preparation branch rather than the
immediate normal Init route. No helper result or dominated retained marker
reveals the historical value in Phase15U or Phase16I. Later
`version_mgr_imp.cpp` execution cannot establish W23 because the first-extract
branch can prepare state and rejoin the shared Init dispatch.

```text
PHASE15U_W23_GATE = UNKNOWN
PHASE16I_W23_GATE = UNKNOWN
```
