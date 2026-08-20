# Phase15H - post-downloader native startup observation

Date: 2026-08-20

## Executive result

The mandatory preflight found no Android endpoint or physical device. WHPX was
usable, and the unchanged `ApexPhase9Lab` AVD booted without snapshot load/save
or wipe. Package version/code, both OBB names/sizes, and the two-row validation
cache all matched. No artifact or cache was modified.

Under confirmed strict network isolation, Apex was launched exactly once.
`DownloaderActivity` selected files-present state `4`, emitted `Down POB 2`,
and returned result `1` only `0.957 s` after its Android start. This confirms
reuse of the valid Phase15G cache rather than another full CRC pass.

After `GameActivity` completed its resumed body with `HasAllFiles=true`, the
process remained alive, top-resumed, focused, visible, and reported-drawn
through `+300 s`. At about `+16.305 s` post-resume the Apex process entered EGL
initialization, followed by Vulkan layer discovery at `+17.127 s`. Despite
Mesa/render-node warnings, the local screenshots at `+30 s` and `+120 s`
showed a rendered Lightspeed Studios splash and wait UI beneath Android's
immersive-mode tutorial overlay. The screen was not pixel-black.

This is a new post-resume graphics/rendered-splash stage, but it does not name
an exact UE4 function or prove a gameplay frame. The available thread command
collapsed every thread name to the package process name. No new Lua,
`ClientLaunch`, `EventSystem`, Login, `RequestAvatarServerList`, or event
`0x138` runtime witness was found.

The first post-resume network operation was the already-known TDM POST attempt
at `+0.109 s`; it failed at name resolution. GCloud and TDM retries likewise
received no response, no new post-resume host appeared, and the process did not
exit. Apex was force-stopped, network state was restored exactly, and the AVD
shut down with no remaining endpoint or emulator process. Raw data remains
local-only.

## Required results

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES

VALIDATION_CACHE_PRESENT = YES
VALIDATION_CACHE_CONTENT_VALID = YES
VALIDATION_CACHE_REUSED = YES

DOWNLOADER_ACTIVITY_OBSERVED = YES
DOWNLOADER_DURATION = 0.957S_TO_RESULT_RETURN
DOWNLOADER_RESULT = 1
GAMEACTIVITY_RESUMED = YES

NATIVE_RESUME_RUNTIME_WITNESS = CONFIRMED
UE4_POST_RESUME_STAGE = GRAPHICS_BACKEND_INITIALIZATION_AND_RENDERED_SPLASH

UE4_GAME_THREAD_NAME_VISIBLE = NO
UE4_RENDER_THREAD_NAME_VISIBLE = NO

SCREEN_STATE_5S = OTHER
SCREEN_STATE_30S = OTHER
SCREEN_STATE_120S = OTHER
BLACK_SCREEN_CONFIRMED_BY_PIXEL_CAPTURE = NO
BLACK_SCREEN_CAUSE = NOT_APPLICABLE_SCREEN_NOT_BLACK

LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENTSYSTEM_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
SERVER_LIST_EVENT_0X138_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE

FIRST_POST_RESUME_NETWORK_ATTEMPT = +0.109S_TDM_POST_TDM.MGAPEX.COM
PROCESS_ALIVE_300S = YES

LAST_CONFIRMED_ANDROID_STAGE = GAMEACTIVITY_FOREGROUND_DRAWN_VISIBLE_AT_+300S
LAST_CONFIRMED_UE4_STAGE = APEX_GRAPHICS_BACKEND_INITIALIZATION_AND_RENDERED_SPLASH
LAST_CONFIRMED_CLIENT_STAGE = LIGHTSPEED_SPLASH_WITH_WAIT_UI
FIRST_UNRESOLVED_TRANSITION = GRAPHICS_SPLASH_TO_NAMED_LUA_OR_LOGIN_STAGE

NETWORK_RESTORED = YES
AVD_SHUTDOWN_CLEAN = YES

FINAL_GATE = C NEW_POST_RESUME_UE4_NATIVE_STAGE_CONFIRMED
COMMIT = Observe post-resume native startup Phase15H
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence limit

The graphics evidence is process-scoped and post-resume, but no exact UE4
function name is attached to it. Phase15H does not establish a process mapping,
load bias, native address, gameplay tick, Lua chunk, Login path, server-list
request, backend requirement, or wait-state cause.
