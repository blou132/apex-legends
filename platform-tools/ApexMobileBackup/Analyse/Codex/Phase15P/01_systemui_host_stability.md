# SystemUI host stability

Android reached `sys.boot_completed=1`. Targeted read-only window and activity
checks then produced:

| Target | Actual | SystemUI ANR |
|---|---:|---|
| `0 s` | `1.731 s` | NO |
| `30 s` | `32.169 s` | YES |
| `60 s` | NOT_REACHED | NOT_REACHED_HARD_STOP |
| `90 s` | NOT_REACHED | NOT_REACHED_HARD_STOP |
| `120 s` | NOT_REACHED | NOT_REACHED_HARD_STOP |

The `30 s` window dump contained a visible window titled `Application Not
Responding: com.android.systemui`. No button, key event, force-stop, restart,
or setting change was sent to SystemUI.

```text
SYSTEMUI_PREFLIGHT_STABLE = NO
FINAL_GATE = G REPEATED_HOST_MODE_SYSTEMUI_ANR_STOP
```
