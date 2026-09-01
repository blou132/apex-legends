# Known virtual slots

No DolphinUpdater table base was recovered, so the Shutdown cell cannot be
converted into a class-relative virtual slot. OnNoticeInstallApk,
first-extract, and CheckUpdate have no exact cells in the permitted sections.

`DOLPHINUPDATER_KNOWN_VIRTUAL_SLOTS = SHUTDOWN_SINGLE_CELL_ONLY; SLOT_OFFSET_UNKNOWN`

The previously proven gcloud interface slots remain separate ABI evidence:
Init `+0x10`, cleanup `+0x18`, post-Init `+0x28`, and shutdown-related dispatch
`+0x40`. They are not DolphinUpdater owner-vtable slots.
