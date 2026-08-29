# Testpoint and bootrom

An initial uncertain contact produced normal Huawei USB mode and was stopped
without running PotatoNV. After the exact point was reconfirmed, the controlled
contact produced a black screen and exactly one `HUAWEI USB COM 1.0` bootrom
endpoint (`VID_12D1/PID_3609`). No Samsung endpoint was present.

After the later PotatoNV timeout, attempts to reproduce the physical entry
returned only normal Huawei USB mode. The phase was stopped instead of turning
the procedure into repeated trial-and-error.

```text
TESTPOINT_CONTACT_ATTEMPTED = YES
EXPECTED_BOOTROM_ENUMERATION = YES
BOOTROM_ENUMERATION_REPRODUCED_AFTER_TIMEOUT = NO
```
