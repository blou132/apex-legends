# Single physical entry

The phone was normally powered off and USB-disconnected. A current local-only
photo matched the already-confirmed PRA board geometry and showed no obvious
new damage. The operator then reproduced the first Phase16F success sequence:
contact between the confirmed testpoint and validated shield before USB,
direct USB insertion while contact remained, release after about three
seconds, and no Power-button action. The screen remained black.

Windows then observed exactly one `VID_12D1/PID_3609` endpoint and no normal
Huawei or Samsung endpoint. No second probe occurred.

```text
PHONE_OPENED = YES
TESTPOINT_BOARD_MATCH = CONFIRMED
PHONE_COLD_OFF_READY = YES_OPERATOR_CONFIRMED
PHYSICAL_ATTEMPT_COUNT = 1
PHYSICAL_ATTEMPT_LIMIT = ONE
SCREEN_STATE = BLACK
BOOTROM_ENUMERATION = YES_VID_12D1_PID_3609
FAILED_SINGLE_ATTEMPT = NO
```
