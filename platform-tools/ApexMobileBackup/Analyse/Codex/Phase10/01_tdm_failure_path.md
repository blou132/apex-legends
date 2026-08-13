# TDM failure path

## Request path

The local `libTDataMaster.so` path is:

```text
RequestRemoteRouteConfig (Ghidra 0x15e6b4)
  -> synchronous route wrapper (0x17db50)
  -> SendClientRoutePostRequest (0x180cac)
  -> curl HttpPost (0x182030)
  -> HTTPRouteProc2 (0x15f298)
```

The runtime request is a POST to the already documented TDM route path. No body is published.

## DNS and HTTP failure

The local-only run reports curl code `6`, HTTP code `0`, and an empty response. The wrapper maps a non-200 result to internal result `5`; an unavailable HTTP manager uses `6`. `HTTPRouteProc2` logs result `5` and enters `FUN_0015e01c`, which advances internal retry/failure state.

The alternate asynchronous callback at `0x15f668` follows the same error-side state update. Neither error path calls a game, Login, Lua, or menu continuation.

## Retry policy

`RequestRemoteRouteConfig` waits while its route-success byte is false. The retry delay increases by five seconds and is capped at 60 seconds. The observed run contains repeated route attempts with increasing spacing.

Success parses the route, marks route success, and permits the TDM reporter continuation. Failure keeps only the TDM route/report worker in retry.

```text
TDM_FAILURE_BEHAVIOR = CONFIRMED TDM_FAILURE_RETRY_ONLY
TDM_FAILURE_FATAL = INVALIDATED for the observed application bootstrap
```

This does not call TDM a game backend. The confirmed role is telemetry/report routing.
