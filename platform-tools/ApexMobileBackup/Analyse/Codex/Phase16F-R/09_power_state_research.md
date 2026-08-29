# Power-state research

## Classified evidence

- **UPSTREAM:** PotatoNV says to turn the phone off, establish the short,
  connect USB, and release after about three seconds. It does not prescribe a
  Power-button hold or battery disconnection.
- **EXACT_MODEL_COMMUNITY:** Discussion 24 starts from off/unplugged and adds a
  roughly 10-second Power hold while USB and ground remain connected. The
  author reports success on the second attempt.
- **EXACT_MODEL_COMMUNITY:** Discussion 125 corroborates Windows/PRA-LX1
  success and notes contact/driver sensitivity, but does not preserve a full
  power sequence.
- **SERVICE_TOOL_LOG:** the prior exact-model service report corroborates USB
  SER/HUAWEI USB COM behavior, not battery or Power timing.
- **WEAK:** unrelated model comments about disconnecting a battery are neither
  exact-model evidence nor a successful PRA procedure and are excluded from
  the recommendation.

## What the local run proves

The operator reported the phone off, USB initially disconnected, the screen
black, and no Power action in the documented successful sequence. The host
then proved bootrom enumeration. The battery appears physically installed in
the available photos, but its exact electrical connection state at T1 is not
logged.

The later retries intended to create a stronger off state with a pre-contact
Power hold, yet returned normal USB. This does not prove that the Power hold
caused failure: contact quality, residual power, and true shutdown state were
not measured.

No source or result in this phase justifies disconnecting the battery. A future
plan must keep battery disconnection outside scope.

```text
COLD_BOOT_REQUIREMENT = NOT_PROVEN
USB_POWERED_BOOTROM = CONSISTENT_WITH_OBSERVATION
BATTERY_CONNECTED_BOOTROM = UNKNOWN
BATTERY_DISCONNECTED_TESTPOINT_MODE = NOT_AUTHORIZED_AND_NOT_SUPPORTED_BY_EXACT_EVIDENCE
POWER_KEY_ASSISTED_BOOTROM_ENTRY = PLAUSIBLE_EXACT_MODEL_COMMUNITY_VARIANT_NOT_TESTED
POWER_STATE_REMAINS_POSSIBLE_FACTOR = YES_UNMEASURED
```
