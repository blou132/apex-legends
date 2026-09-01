# Plus 0x45 initial state

The class initializer is unknown, so the construction value of `+0x45` cannot
be recovered.

`OWNER_PLUS_0X45_INITIAL_VALUE = UNKNOWN`

`BUSEIFSFILE_OWNER_INITIALIZATION_RELATION = NO_PROVEN_PRECHECKUPDATE_OWNER_INITIALIZER_RELATION`

Phase16R's correction remains authoritative: `bUseIFSFile` influences the
fourth argument prepared for the real Init dispatch; it is not an absolute
Init/no-Init gate. No new evidence links it to construction-time writes of
`+0x45`, `+0x1f0`, or `+0x1f8`.
