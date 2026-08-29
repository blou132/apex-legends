# Phase16F-R scope

Date: 2026-08-29

This phase is a read-only postmortem of the first successful PRA-LX1 bootrom
entry performed in Phase16F. It reconstructs the USB, driver, PotatoNV, and
operator sequence from evidence that already existed on the PC.

The phone remained powered off and disconnected. This phase did not open the
phone, contact a testpoint, use ADB or fastboot, run PotatoNV, alter a driver,
launch Apex, or perform an unlock, flash, erase, wipe, FBLOCK change, or root
operation.

Evidence was limited to:

- existing Phase16D through Phase16F reports and local-only inputs;
- the bounded Phase16F operator transcript;
- historical SetupAPI and Windows event logs for the Phase16F time window;
- the current read-only Windows driver-store inventory;
- the pinned PotatoNV source and public upstream/community documentation.

Raw logs, API responses, photos, transcripts, driver packages, and binaries
remain local-only under ignored storage. Committed files contain no device
serial, USB instance suffix, COM number, container GUID, IMEI, Android ID,
account identifier, token, or unlock code.

```text
PHONE_TOUCHED = NO
POTATONV_EXECUTED = NO
DRIVER_STATE_CHANGED = NO
APEX_LAUNCHED = NO
```
