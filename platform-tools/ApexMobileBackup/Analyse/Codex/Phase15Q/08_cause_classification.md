# Cause classification

The matrix rules out three sole explanations:

- Read-only interaction: not supported because writable host also fails.
- Host renderer: not supported as the sole cause because auto/SwiftShader also
  fails.
- `ApexPhase9Lab` userdata: not supported because the disposable AVD fails
  without Apex use.

The strongest environment classification is broader Android 36.1 Google Play
image/SystemUI startup instability under the current host. The immediate ANR
reasons are exactly identified as service-execution timeouts, but the deeper
cause of the CPU saturation remains unknown.

```text
READONLY_INTERACTION_SUSPECTED = NO
HOST_RENDERER_INSTABILITY_SUSPECTED = NO_AS_SOLE_CAUSE
APEXPHASE9LAB_STATE_SPECIFIC_SUSPECTED = NO
BROADER_SYSTEMUI_OR_IMAGE_INSTABILITY_SUSPECTED = YES
FINAL_GATE = E EXACT_SYSTEMUI_ANR_REASON_RESOLVED
```
