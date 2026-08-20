# Exact ANR event

The exact event for this run is timestamp-correlated across event log,
ActivityManager diagnostics, the visible window, and the matching `/data/anr`
trace.

```text
ANR_PROCESS = com.android.systemui
ANR_TYPE = SERVICE_EXECUTION_TIMEOUT
ANR_SERVICE = com.android.systemui/.keyguard.KeyguardService
ANR_DURATION = 20131 ms
ANR_REASON = executing service com.android.systemui/.keyguard.KeyguardService, waited 20131ms
```

The visible window was `Application Not Responding: com.android.systemui`.
Later startup ANRs in other Google applications are separate events and were
not substituted for the SystemUI trace.
