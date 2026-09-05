# Model consistency

The independent UClass registration reports `DolphinUpdater` size `0x430`,
which is compatible with the proven `CheckUpdate` accesses through `+0x1f8`.
The existing real `Init` dispatch at ELF `0x05a2f0ac` remains proven.

Because no `GameUpdateMgr+0x38` PropertyClass was recovered, consistency is a
layout/lifecycle check and not an ownership-type proof.

`REFLECTION_CHECKUPDATE_MODEL_CONSISTENT = UNKNOWN`

`KNOWN_FIELD_SIZE_COMPATIBLE = YES`

`CALLBACK_OWNER_CLASS_INTERPRETATION = PROBABLE_DOLPHINUPDATER`

`GAMEUPDATEMGR_PLUS0X38_ROLE = UNKNOWN`
