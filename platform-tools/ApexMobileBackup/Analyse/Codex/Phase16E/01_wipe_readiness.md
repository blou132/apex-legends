# Wipe readiness

Any future bootloader unlock is treated as a full userdata wipe. This is an
expected consequence for planning, not authorization to wipe the phone in
Phase16E.

The Apex base APK and both OBB files are preserved on the PC and still have the
known hashes documented in Phase16D. Reinstallation material is ready. Private
Apex application state is not preserved.

A non-invasive ADB audit cannot prove that no unique personal file, message,
photo, authenticator state, or application state exists only on the phone.
Therefore the owner must perform a final backup review and explicitly confirm
that all wanted userdata may be lost before Phase16F. Failure to obtain that
confirmation is a hard abort condition.

```text
BOOTLOADER_UNLOCK_WILL_BE_TREATED_AS_FULL_USERDATA_WIPE = YES
WIPE_ACCEPTED_AS_EXPECTED_CONSEQUENCE = YES_FOR_FUTURE_PHASE_PLANNING
APEX_REINSTALL_READY_AFTER_WIPE = YES
PRIVATE_APEX_STATE_PRESERVED = NO
UNIQUE_PERSONAL_DATA_ABSENCE_PROVEN = NO_NOT_VERIFIABLE_NONINVASIVELY
DEVICE_WIPED = NO
```
