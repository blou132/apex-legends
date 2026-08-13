# Phase10 - Bootstrap dependency graph and failure paths

Date: 2026-08-13

## Executive result

The first network failure is not the first global startup gate. TDM curl code `6` leaves the TDM reporting worker in an increasing-delay retry loop, while application initialization continues. GCloud RemoteConfig has cache and caller-default mechanisms; its failed fetch uses three 10-second retries, yet GCloud service/plugin startup continues. The completeness of defaults for gameplay/Login remains unknown.

PlayCommon belongs to Google Play/Finsky and performs log upload, not Apex startup. The OBB downloader is also cleared: it finds and validates the local expansion files, returns success, and `GameActivity` calls and returns from `nativeResumeMainInit`.

No log or static edge identifies the next state transition inside native game initialization. Lua, ClientLaunch, Login, and `RequestAvatarServerList` are not proven reached. The correct decision is therefore gate `E`, not a fabricated TDM/GCloud response.

## Required results

```text
TDM_STARTUP_GATE = CONFIRMED NO
TDM_FAILURE_BEHAVIOR = CONFIRMED TDM_FAILURE_RETRY_ONLY
GCLOUD_STARTUP_GATE = CONFIRMED NO_FOR_OBSERVED_PLUGIN_STARTUP; UNKNOWN_FOR_GAMEPLAY_LOGIN
GCLOUD_FALLBACK = CONFIRMED CACHE_AND_CALLER_DEFAULT_MECHANISMS; UNKNOWN_COMPLETENESS
PLAYCOMMON_STARTUP_GATE = CONFIRMED NO
FIRST_CONFIRMED_STARTUP_STALL = UNKNOWN AFTER_NATIVE_RESUME_MAIN_INIT
UE4_RUNTIME_STAGE_REACHED = CONFIRMED
LUA_RUNTIME_STAGE_REACHED = UNKNOWN
LOGIN_STAGE_REACHED = UNKNOWN
NEXT_BACKEND_TO_STUDY = UNKNOWN; RESOLVE_NATIVE_STARTUP_GATE_FIRST
FINAL_GATE = E BOOTSTRAP_GATE_STILL_UNKNOWN
```

## Evidence highlights

- TDM: `HTTPRouteProc2` at Ghidra `0x15f298`; route request loop `0x15e6b4`; curl POST `0x182030`.
- GCloudCore: `Request` `0x186b14`; `OnDataTaskFinished` `0x1863ac`; `_RetryRequest` `0x185544`; `GetFromCache` overloads including `0x184aa4`.
- GCloud defaults: `libgcloud.so` initializer `0x4d35d0` supplies explicit local fallback values for the documented RPC keys.
- DEX: `DownloaderActivity::onCreate` `0x5058d548`; `ProcessOBBFiles` `0x5058ce78`; `GameActivity::onActivityResult` `0x50599458`; `onResumeBody` `0x50594624`.
- Manifest: `GameActivity` is a `NativeActivity` with `android.app.lib_name=UE4`.
- Runtime: TDM/GCloud failures occur before continued plugin startup; downloader result is success; no fatal Java/native event follows.

## Reproducibility and safety

The cleaned JSON exports are under `output/`. `ApexPhase10BootstrapExport.java` is the targeted exporter. Raw Ghidra output, binaries, the full log, and device-specific data remain local-only. No new client run or network request was performed.
