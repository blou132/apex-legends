# Phase15E - profileability and Phase15D log reanalysis

Date: 2026-08-20

## Executive result

The exact local-only APK again matches `96228800` bytes and the authoritative
SHA256. Local `aapt2` decoding confirms `extractNativeLibs=true`, no application
debuggable opt-in, and no `<profileable>` element or shell-profileability
attribute. Phase14 independently classified the production package as
non-debuggable. The exact package is therefore not profileable by shell.

No host simpleperf, Perfetto host executable, trace processor, Android Studio,
or profiler tooling was found, and nothing was installed or run. App policy is
already sufficient to classify official non-root native process profiling as
unavailable for this exact package.

Offline reanalysis of the existing Phase15D log adds direct Berberis
initialization at `+0.134 s`, the complete ARM64 native-loader sequence,
`libUE4.so` at `+10.494 s`, UE4 splash at `+10.603 s`, Android lifecycle through
`onResume`, and `GameActivity` displayed at `+13.758 s`. A same-process
`DownloaderActivity` is displayed at `+15.864 s`. These are new native/render
startup boundaries, but not proof of a UE4 gameplay frame or a black-screen
cause.

No Lua, ClientLaunch, EventSystem, Login, RequestAvatarServerList, or event
`0x138` witness exists in the log. TDM remains the first request and fails at
DNS; GCloud retries and uses defaults. The Apex process remains alive beyond
+60 seconds, so the observed TDM failure does not terminate this startup run.

Phase15E performed no AVD boot, Apex launch, phone/ADB operation, network
request, profiling capture, or privileged diagnostic. Raw inputs remain
local-only.

## Required results

```text
PACKAGE_DEBUGGABLE = CONFIRMED NO
PROFILEABLE_ELEMENT_PRESENT = CONFIRMED NO
PROFILEABLE_SHELL = CONFIRMED NO
PROFILEABLE_ENABLED = CONFIRMED NO
PROFILEABLE_BY_SHELL = CONFIRMED NO

SIMPLEPERF_AVAILABLE = CONFIRMED NO
PERFETTO_HOST_TOOL_AVAILABLE = CONFIRMED NO
NON_ROOT_NATIVE_PROFILING_POSSIBLE = NO_BY_APP_POLICY

LIBUE4_LOAD_RELATIVE_TIME = +10.494S
NATIVE_RESUME_LOG_WITNESS = NO_NEW_EVIDENCE
RENDER_SURFACE_CREATED = CONFIRMED ANDROID_ACTIVITY_WINDOW
FIRST_FRAME_EVIDENCE = CONFIRMED ANDROID_ACTIVITY_DISPLAYED_BOUNDARY

LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENTSYSTEM_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE

FIRST_APPLICATION_REQUEST = TDM_POST_TDM_MGAPEX_COM_AT_+11.278S_DNS_FAILURE
LAST_OBSERVED_STAGE = DOWNLOADER_ACTIVITY_DISPLAYED_PROCESS_ACTIVE_AFTER_60S
BLACK_SCREEN_CAUSE = UNKNOWN

FINAL_GATE = C NEW_RENDER_OR_NATIVE_STARTUP_STAGE_FOUND
COMMIT = Audit profileability and runtime timeline Phase15E
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence limit

The Android displayed-window event does not identify frame contents. No load
bias, process mapping, native PC, Lua path, login transition, backend response,
or black-screen cause is derived from this reanalysis.
