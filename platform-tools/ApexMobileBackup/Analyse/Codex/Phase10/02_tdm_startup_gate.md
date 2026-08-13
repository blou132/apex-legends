# TDM startup gate

## Local subsystem behavior

TDM success and failure do not immediately converge inside the route worker. Success permits the report-manager continuation; failure returns to a timed retry loop. TDM reporting therefore depends on route success.

## Application behavior

The application does not wait for that worker. After the first curl code `6`, the same Apex process continues MSDK initialization, GCloud service registration, GCloud/GVoice lifecycle startup, OBB validation, and eventually the native UE4 resume-main-init handoff.

The distinction is:

```text
TDM_REPORT_ROUTE_GATE = CONFIRMED YES
TDM_APPLICATION_STARTUP_GATE = CONFIRMED NO
```

No static or runtime edge links the TDM route response to `RequestAvatarServerList`, Login, ClientLaunch, or event `0x138`.
