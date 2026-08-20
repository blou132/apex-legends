# Test matrix

| Case | Renderer | AVD mode | Boot | First visible SystemUI ANR |
|---|---|---|---|---|
| A | `host` | read-only | YES | target `30 s`, actual `30.043 s` |
| B | `host` | writable | YES | target `30 s`, actual `30.114 s` |
| C | `auto` | read-only | YES | target `60 s`, actual `60.173 s` |

Every case began with an empty ADB inventory and ended with disappearance of
the endpoint and target process. Logcat buffer `all` was cleared after boot and
captured locally before the checkpoint clock. No app-start command was issued.

The result excludes `-read-only`, host graphics, and `ApexPhase9Lab` userdata
as sole explanations. All three cases reproduce the SystemUI failure.
