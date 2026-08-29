# Failed retry sequence

Two later retries were bounded by operator acknowledgements on 2026-08-29.
Both added an explicit forced-shutdown step before testpoint contact:

- retry 1: unplug USB, hold Power about 15 seconds, establish the same
  testpoint-to-shield contact, insert USB, release after about three seconds;
- retry 2: unplug USB, hold Power about 20 seconds, wait about 10 seconds,
  establish contact, insert USB, release after about three seconds.

Power was used before contact to force shutdown. It was not held while the
testpoint was grounded and USB was being inserted. Neither retry used the
separate PRA community sequence that calls for a roughly 10-second Power hold
while USB and ground remain connected.

Windows observed normal Huawei USB (`VID_12D1/PID_107E`) rather than the
bootrom endpoint. The intended off state was not independently measured, and
the true electrical contact quality and residual USB power were not logged.

```text
RETRY_POWER_BUTTON_USED = YES_BEFORE_CONTACT_FOR_FORCED_SHUTDOWN
RETRY_TESTPOINT_TIMING = CONTACT_BEFORE_USB; RELEASE_AFTER_ABOUT_3_SECONDS_IN_INSTRUCTIONS
RETRY_USB_ORDER = UNPLUG -> FORCE_OFF -> GROUND -> USB
RETRY_PHONE_POWER_STATE = INTENDED_FORCED_OFF_NOT_INDEPENDENTLY_MEASURED
RETRY_BOOTROM_ENUMERATION = NO
RETRY_ENUMERATION = NORMAL_HUAWEI_USB
```
