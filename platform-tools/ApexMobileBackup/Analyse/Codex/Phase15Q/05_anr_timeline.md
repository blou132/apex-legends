# ANR timeline

All three windows were specifically titled `Application Not Responding:
com.android.systemui`. Window and activity dumps independently witnessed each
visible system alert. No alert was dismissed or otherwise interacted with.

| Case | Last clear check | First ANR check | Direct event reason |
|---|---|---|---|
| A | `15.124 s` | `30.043 s` | executing `KeyguardService`, waited `20053 ms` |
| B | `15.195 s` | `30.114 s` | executing `KeyguardService`, waited `20259 ms` |
| C | `30.112 s` | `60.173 s` | executing `SystemUIService`, waited `20515 ms` |

The immediate stop rule prevented later checkpoints in each case. The
differences in service and timing mean a single blocked component is not common
to all cases, but the service-execution timeout class is common.
