# Phase16F-R - deep postmortem of first PRA-LX1 bootrom entry

Date: 2026-08-29

## Executive result

Phase16F conclusively reached the correct PRA-LX1 bootrom endpoint and then
the expected PotatoNV RAM-fastboot endpoint. SetupAPI proves
`VID_12D1/PID_3609` bound successfully through the signed Huawei VCOM driver at
15:18:51. PotatoNV subsequently uploaded `hisi65x_a` in the manifest order
`xloader -> fastboot`. During its exact source-level D00D wait, Windows
enumerated `Fastboot2.0` as `VID_18D1/PID_D00D` but found no matching driver;
the device had problem code 28. This missing WinUSB binding explains the
observed PotatoNV timeout with high confidence.

The correct signed D00D package, `hw_goadb.inf` published as `oem77.inf`, was
installed after the timeout and remains present with exact D00D coverage.
Current host readiness is therefore yes at the driver-store level, while a
live re-enumeration remains unvalidated because this phase did not touch or
connect the phone.

The successful physical entry is documented as: phone off, USB disconnected,
testpoint-to-shield contact established before USB, direct USB insertion while
contact remained, release after about three seconds, no Power action in the
documented sequence, and black screen. Later retries added forced 15/20-second
Power holds before contact and one added a 10-second wait; they returned normal
Huawei USB. They were not exact reproductions of the first success and did not
test the separate PRA community Power-while-grounded sequence.

The board point and `Kirin 65x (A)` profile are not credible causes of the
first timeout: the correct bootrom appeared, the RAM images uploaded, and the
source-expected D00D state followed. Contact quality, exact electrical
battery state, cold-off state, and residual USB power remain unmeasured.

A future retry is justified at medium confidence only as one separately
authorized attempt reproducing the exact first-success sequence. Failure to
enumerate bootrom once requires an immediate stop. No alternate probing,
battery disconnection, profile change, FBLOCK change, Magisk, or root branch
is authorized by this result.

## Evidence chain

- [Windows timeline](02_windows_usb_timeline.md)
- [Driver analysis](03_setupapi_driver_analysis.md)
- [First success](04_first_success_sequence.md)
- [Failed retries](05_failed_retry_sequence.md)
- [Sequence differential](06_sequence_diff.md)
- [PotatoNV state machine](07_upstream_state_machine.md)
- [PRA-specific research](08_pra_specific_research.md)
- [Power-state research](09_power_state_research.md)
- [Temporary endpoint](10_temp_fastboot_endpoint.md)
- [Retry decision](11_retry_decision.md)
- [Future one-attempt design](12_future_single_retry.md)

## Final result

```text
FIRST_BOOTROM_ENTRY_PROVEN = YES

FIRST_ATTEMPT_BOOTROM_VIDPID = VID_12D1/PID_3609
FIRST_ATTEMPT_TEMP_FASTBOOT_VIDPID = VID_18D1/PID_D00D

FIRST_ATTEMPT_TIMEOUT_ROOT_CAUSE = TEMP_FASTBOOT_NOT_LIBUSB_VISIBLE_DUE_MISSING_WINUSB_DRIVER
POTATONV_TIMEOUT = YES_DRIVER_FAILURE_EXPLAINS_OBSERVED_WAIT_TIMEOUT_WITH_HIGH_CONFIDENCE

TEMP_FASTBOOT_DRIVER_NOW_READY = YES_DRIVER_STORE_READY_LIVE_REENUM_NOT_VALIDATED

UPSTREAM_GENERIC_TESTPOINT_SEQUENCE = OFF -> GROUND_TESTPOINT -> CONNECT_USB -> RELEASE_AFTER_ABOUT_3S
PRA_LX1_SPECIFIC_SEQUENCE = OFF_AND_UNPLUGGED -> GROUND_TESTPOINT -> CONNECT_USB -> HOLD_POWER_ABOUT_10S_WHILE_GROUNDED -> REQUIRE_BLACK_SCREEN

FIRST_SUCCESS_POWER_BUTTON_USED = NO_IN_DOCUMENTED_SEQUENCE
FAILED_RETRY_POWER_BUTTON_USED = YES_BEFORE_CONTACT_FOR_FORCED_SHUTDOWN

FIRST_SUCCESS_VS_RETRY_DIFFERENCES = PRECONTACT_FORCED_SHUTDOWN_ADDED; SECOND_RETRY_ADDED_10S_WAIT; DRIVER_STATE_CHANGED; OTHER_PHYSICAL_FACTORS_UNMEASURED

TESTPOINT_LOCATION_WRONG = INVALIDATED
POTATONV_PROFILE_WRONG = NO_EVIDENCE

TEMP_FASTBOOT_ENDPOINT_EXPECTED = YES

PHONE_UNLOCKED_SCREEN_ROLE = NOT_CLASSIFIABLE_NO_PRESERVED_TIMESTAMPED_ARTIFACT

POWER_STATE_REMAINS_POSSIBLE_FACTOR = YES_UNMEASURED

CONTROLLED_RETRY_JUSTIFIED = YES_CONDITIONAL_ONE_ATTEMPT

PHONE_TOUCHED = NO
POTATONV_EXECUTED = NO

NEXT_STEP = SEPARATE_AUTHORIZATION_FOR_ONE_EXACT_PHASE16F_RETRY_AFTER_REASSEMBLY_AND_HEALTH_CHECK

FINAL_GATE = A DRIVER_READY_SINGLE_CONTROLLED_RETRY_JUSTIFIED
COMMIT = Deep-analyze PRA-LX1 bootrom retry Phase16F-R
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Privacy and safety boundary

No raw log, transcript, photo, USB instance suffix, COM number, container GUID,
device serial, IMEI, Android ID, account identifier, unlock code, driver
package, firmware, PotatoNV binary, APK, or OBB is committed. No phone or
Windows configuration was changed in Phase16F-R.
