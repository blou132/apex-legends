# Future single-retry design

This is a design record only. It was not executed and requires separate
explicit authorization. No Magisk/root branch belongs in this state machine.

## PRECHECK

1. Confirm the phone has been reassembled and health-checked after Phase16F.
2. Confirm only the authorized PRA-LX1 is in scope and all unrelated Android
   devices are physically absent.
3. Revalidate the pinned PotatoNV and `hisi65x_a` inputs.
4. Read-only confirm `oem14.inf` and `oem77.inf`, exact VID/PID coverage,
   signatures, and required binaries. Make no driver change.
5. Keep `Kirin 65x (A)`, keep `Disable FBLOCK` unchecked, and define the abort
   path before opening the phone.

## One physical entry

Reproduce the first successful sequence exactly: phone off, USB disconnected,
contact the already-confirmed testpoint and shield before USB, insert direct
USB while maintaining contact, release after about three seconds, and do not
use Power during the sequence. Do not disconnect the battery and do not switch
to the community Power-assisted variant after a failure.

Observe once for `VID_12D1/PID_3609`. If it does not enumerate with the signed
VCOM binding, stop, disconnect, reassemble, and end the phase. There is no
second physical probe.

## Bounded PotatoNV state machine

```text
PRECHECK
-> EXACT_KNOWN_PHYSICAL_SEQUENCE_ONCE
-> REQUIRE 12D1:3609 + VCOM SUCCESS
-> POTATONV PINNED HISI65X_A / FBLOCK UNCHANGED
-> REQUIRE 18D1:D00D + WINUSB SUCCESS
-> REQUIRE DEVICE_INFORMATION_STAGE
-> STOP ON ANY UNEXPECTED OUTPUT OR STATE
```

If D00D is absent, unbound, repeatedly disconnecting, or not visible to
PotatoNV, stop immediately. Do not reinstall drivers during the run, repeat
uploads, change profiles, alter FBLOCK, improvise power sequences, or proceed
into any root workflow.

```text
FUTURE_RETRY_EXECUTED = NO
FUTURE_RETRY_COUNT_LIMIT = ONE
BATTERY_DISCONNECTION_ALLOWED = NO
ALTERNATE_PROFILE_ALLOWED = NO
FBLOCK_CHANGE_REQUESTED = NO
ROOT_WORK_ALLOWED = NO
```
