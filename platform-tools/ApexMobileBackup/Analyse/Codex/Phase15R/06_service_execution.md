# Service execution correlation

ActivityManager attributes the timeout to `KeyguardService`, but the matching
thread trace does not show a `KeyguardService` lifecycle callback. The service
record explains the distinction:

- bind requested and marked bound, but its binder is still null;
- result not received;
- execution nesting is 2;
- execution has been pending for about 20.1 seconds.

Android serializes service lifecycle callbacks on the application main thread.
That thread is still inside the earlier `SystemUIService.onCreate` startup path,
so the queued Keyguard create/bind work has not reached its callback.

Several logged SystemUI startables are abnormally slow, including
`SystemActions` at `13781 ms`, `CommunalSuppressionStartable` at `3476 ms`, and
later `AssistantAttentionMonitor` at `12973 ms`. This supports broadly delayed
startup rather than a Keyguard-specific callback deadlock.

```text
SERVICE_EXECUTION_ENTRY = KEYGUARDSERVICE_CALLBACK_NOT_REACHED; SYSTEMUISERVICE.ONCREATE_ACTIVE
SERVICE_EXECUTION_BLOCKING_FRAME = NONE; CURRENT_FRAME=PositionState.<init> DURING DAGGER PROVIDER CONSTRUCTION
```
