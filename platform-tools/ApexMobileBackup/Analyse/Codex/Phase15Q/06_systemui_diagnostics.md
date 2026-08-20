# SystemUI diagnostics

ActivityManager supplies a direct reason in every case: SystemUI failed to
complete an executing service within about 20 seconds. These are service
timeouts, not input-dispatch timeouts.

Detailed ANR records also report high CPU pressure and SurfaceFlinger using
mostly kernel CPU: approximately `104%` in A, `96%` in B, and `88%` in C.
Memory pressure was zero in those records. This is a repeatable correlation,
not proof that SurfaceFlinger caused the service stall.

`dumpsys activity lastanr` is supported, but at the immediate diagnostic point
it reported no archived ANR since boot. No usable main-thread trace was exposed.

```text
ANR_REASON = CONFIRMED_SERVICE_EXECUTION_TIMEOUT
ANR_WAITING_COMPONENT = A/B KeyguardService; C SystemUIService
ANR_INPUT_TIMEOUT_EVIDENCE = NO
ANR_MAIN_THREAD_BLOCK_EVIDENCE = UNKNOWN_NO_THREAD_TRACE
```
