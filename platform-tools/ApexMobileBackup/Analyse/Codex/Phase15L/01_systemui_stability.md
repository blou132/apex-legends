# SystemUI stability

No physical endpoint appeared in the mandatory initial ADB inventory. WHPX was
usable and the unchanged AVD reached `sys.boot_completed=1`.

After boot completion, no UI input or application launch occurred for 120
seconds. Read-only window and activity snapshots found no
`Application Not Responding: com.android.systemui` overlay:

| Checkpoint | Actual elapsed time | SystemUI ANR |
| --- | ---: | --- |
| +0 s | +2.269 s | No |
| +30 s | +30.727 s | No |
| +60 s | +60.684 s | No |
| +90 s | +90.421 s | No |
| +120 s | +120.268 s | No |

The final immediate prelaunch window check was also clear. The Phase15K stop
condition did not recur, so the one authorized Apex launch was allowed.
