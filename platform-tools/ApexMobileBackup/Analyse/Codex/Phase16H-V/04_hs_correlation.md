# Phase16H-S correlation

Phase16H-S submitted slot-0 address and active control `0x000001e5` together.
SETREGSET succeeded, address readback matched, and control readback was
`0x000041e4`.

Phase16H-T subsequently proved that `0x000041e4` is the expected Huawei cached
normalization: it preserves EL0 execution and four-byte length, sets SSC to
NON_SECURE, and exposes cached enable zero while the perf event is enabled by
the surrounding kernel path.

The H-S attempt stopped before continuation because the normalized value was
then treated as invalid. It nevertheless provides the needed programming and
readback proof:

`ATOMIC_ACTIVE_PROGRAMMING_ALREADY_READBACK_PROVEN = YES`.

H-U does not contradict H-S. H-U exercised the disabled branch; H-S exercised
the validating active branch.
