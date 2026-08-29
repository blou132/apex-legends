# First successful physical sequence

The local operator transcript gives one explicit sequence immediately before
the successful bootrom enumeration:

1. Phone fully off, laid flat, and USB disconnected.
2. One conductive tip on the already-confirmed large testpoint pad.
3. The other conductive tip on the adjacent shield ground.
4. Establish contact before USB insertion.
5. Insert the direct USB cable while maintaining contact.
6. Hold for about three seconds, then release contact and leave USB connected.
7. Do not use the Power button during this sequence.

The instruction was issued at 15:16:18.902. SetupAPI observed the exact
bootrom endpoint at 15:18:51.431. The operator then acknowledged that the
screen stayed black. This combines a contemporaneous procedure, an operator
observation, and an independent OS enumeration.

The photos show the battery physically installed, but neither the transcript
nor an electrical measurement records whether its connector was electrically
engaged at the exact bootrom instant. That field remains unknown.

```text
PHONE_INITIAL_STATE = OPERATOR_CONFIRMED_OFF
PHONE_SCREEN_STATE = BLACK
USB_INITIAL_STATE = DISCONNECTED
TESTPOINT_CONTACT_BEFORE_USB = YES_IN_INSTRUCTED_AND_ACKNOWLEDGED_SEQUENCE
POWER_BUTTON_USED = NO_IN_DOCUMENTED_SEQUENCE
POWER_BUTTON_DURATION = NOT_APPLICABLE_IN_DOCUMENTED_SEQUENCE
TESTPOINT_RELEASE_TIMING = AFTER_ABOUT_3_SECONDS_IN_INSTRUCTED_SEQUENCE
BATTERY_CONNECTED_STATE = NOT_EXPLICITLY_LOGGED
FIRST_BOOTROM_ENTRY_PROVEN = YES
FIRST_ATTEMPT_BOOTROM_VIDPID = VID_12D1/PID_3609
```

`POWER_BUTTON_USED` describes the documented sequence. It does not claim an
unobserved absolute about every operator movement outside that sequence.
