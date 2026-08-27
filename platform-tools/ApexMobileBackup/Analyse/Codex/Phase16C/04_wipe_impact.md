# Bootloader unlock wipe impact

Android's bootloader state documentation states that locked/unlocked state
transitions wipe data partitions and require user confirmation:
[Android device state](https://source.android.com/docs/security/features/verifiedboot/device-state).
No attempt will be made to bypass this behavior.

## Data expected to be lost

- Installed Apex package state and private app data.
- Apex validation/cache state built during Phase15U.
- OBB copies currently stored on the phone.
- Android settings and developer-option state.
- Current ADB authorization.
- Other user-installed application data and user files.

The PC-side APK/OBB preservation described in `05_apex_restore_readiness.md`
mitigates reinstallation risk, but it does not preserve private Apex state or
the complete phone user partition.

```text
BOOTLOADER_UNLOCK_EXPECTED_TO_WIPE_USERDATA = YES
DATA_WIPE_RISK = HIGH
```
