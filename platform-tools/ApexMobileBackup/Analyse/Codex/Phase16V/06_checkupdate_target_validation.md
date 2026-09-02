# CheckUpdate target validation

The target is 4-byte aligned, equals the exact FDE start, and is represented by
Ghidra function `FUN_05b2eb00`. Its first bytes are not ordinary decodable
AArch64 (`a4fb390a1dd7df64ad66628a3c999609`), matching the protected/opaque prefix
already established by Phase16Q rather than an arbitrary data landing.

Four independent external direct calls all target this same FDE entry. Their
callers establish valid owner state before branching. The readable continuation
inside the same FDE retains the known DolphinUpdater owner ABI.

```text
CHECKUPDATE_VALID_EXTERNAL_CALLSITE_COUNT = 4
TARGET_VALIDATION = ALIGNED_EXACT_FDE_START_PROTECTED_ENTRY
```
