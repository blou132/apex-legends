# Cross-run comparison

Phase15U directly associates a `Thread-10` instance with CVersionMgr `Init`,
GCloudCore, and Puffer activity. Phase16I repeats the GCloudCore/Puffer role.
Phase16M identifies the same role sequence on `THREAD10_A` within one fresh
run.

The component role and initialization stage are repeatable. The numeric TID,
duplicate-name ordinal, and direct CreateDolphin caller are not repeatably
proven.

`CROSS_RUN_THREAD_ROLE_STABILITY = MEDIUM`
