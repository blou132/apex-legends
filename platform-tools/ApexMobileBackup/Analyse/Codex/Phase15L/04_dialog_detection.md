# Dialog detection

`GameActivity` reached the resumed state 13.216 seconds after the single Apex
launch. This resume point defined post-resume time zero.

Read-only window polling produced:

| Target | Actual | Apex windows | Wrap-content windows | Third dialog | SystemUI ANR |
| --- | ---: | ---: | ---: | --- | --- |
| +10 s | +10.430 s | 2 | 0 | No | No |
| +15 s | +15.390 s | 3 | 1 | Yes | No |

Polling stopped at the first positive detection as required. The dialog appear
time is recorded as `+15.391 s` post-resume. No application or system control
was clicked.
