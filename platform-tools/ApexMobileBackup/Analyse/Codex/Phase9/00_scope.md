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
- The first ADB attempt found no device. After the preserved phone was explicitly reconnected, one authorized physical device was used only for `pm path` and `adb pull` of `base.apk`.
- No further phone command was issued after the APK copy completed.
- No application was installed or launched.
- No network request, DNS lookup, proxy, hook, patch, root operation, or bypass was attempted.
- No proprietary APK, OBB, PAK, Lua content, memory dump, or raw runtime log is published.

## Result

The base APK is now available locally and ignored by Git, but the required isolated ARM64 environment remains unavailable. Phase9 therefore stops at:

```text
E DYNAMIC_LAB_UNAVAILABLE
```

This is an environment gate, not evidence about whether the target Lua modules load on a compatible device.
