# Phase9 scope

Date: 2026-08-13

## Objective

Phase9 was intended to observe the Lua loading chain on an isolated copy of the preserved client:

```text
logical module
 -> Lua package searcher
 -> final file candidate
 -> effective provider
 -> OpenRead
 -> Lua chunk loader
```

The priority targets were `ClientLaunch`, `EventSystem`, the event `0x138` subscriber, its response parser, and the local source of the `RequestAvatarServerList` URL.

## Safety boundary

- The original phone was not used as a runtime laboratory.
- A read-only APK recovery was attempted only after the local APK search returned no candidate.
- ADB reported no authorized device, so `pm path` and `adb pull` were not executed and no phone data was accessed.
- No application was installed or launched.
- No network request, DNS lookup, proxy, hook, patch, root operation, or bypass was attempted.
- No proprietary APK, OBB, PAK, Lua content, memory dump, or raw runtime log is published.

## Result

The required isolated environment was unavailable. Phase9 therefore stops at:

```text
E DYNAMIC_LAB_UNAVAILABLE
```

This is an environment gate, not evidence about whether the target Lua modules load on a compatible device.
