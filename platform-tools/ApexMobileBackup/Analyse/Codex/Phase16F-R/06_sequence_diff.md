# Sequence differential

## Proven differences

| Dimension | First success | Later failures | Confidence |
| --- | --- | --- | --- |
| Pre-contact Power action | None in documented sequence | Forced Power hold for 15 or 20 seconds | High |
| Delay after forced shutdown | None documented | Second retry added about 10 seconds | High |
| D00D driver state | Not installed when PotatoNV later produced D00D | Signed exact package installed before retries | High |
| Result | Bootrom `12D1:3609` | Normal Huawei USB | High |

## Shared documented elements

The intended starting state was off and USB-disconnected. Contact preceded USB
insertion, the same already-confirmed pad and shield were used, and release
was instructed after about three seconds. The retries did not introduce a
Power hold during grounded USB insertion.

## Unmeasured candidates

Contact pressure/quality, exact sub-second timing, battery electrical state,
true cold-off state, and residual USB power were not measured. They remain
possible factors, not proven differences.

The successful `12D1:3609` enumeration invalidates the hypothesis that the
confirmed board point was categorically wrong. Successful `hisi65x_a` upload
followed by the source-expected D00D endpoint provides no evidence that the
selected profile caused the timeout.

```text
SEQUENCE_DIFFERENCES = PRECONTACT_FORCED_SHUTDOWN_ADDED; SECOND_RETRY_ADDED_10S_WAIT; DRIVER_STATE_CHANGED
FIRST_SUCCESS_VS_RETRY_DIFFERENCES = DOCUMENTED_ABOVE; OTHER_PHYSICAL_FACTORS_UNMEASURED
TESTPOINT_LOCATION_WRONG = INVALIDATED
PROFILE_CAUSED_FIRST_TIMEOUT = NO_EVIDENCE
POTATONV_PROFILE_WRONG = NO_EVIDENCE
```
