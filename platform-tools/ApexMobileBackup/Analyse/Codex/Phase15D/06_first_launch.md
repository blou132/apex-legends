# First isolated launch

Android resolved the launcher to the package's `GameActivity`. The activity was
started once after the isolation gate passed.

The same process remained alive and the Apex activity remained visible at all
three bounded observations:

| Observation | Process alive | Foreground activity seen |
| --- | --- | --- |
| About +5 s | YES | YES |
| About +20 s | YES | YES |
| About +60 s | YES | YES |

No fatal Java exception, native fatal signal, ABI mismatch,
`UnsatisfiedLinkError`, or process restart was observed. No account or user
input was supplied. The offline bootstrap emitted repeated DNS failures as
expected, while the process stayed alive through the observation window.
