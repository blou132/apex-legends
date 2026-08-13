# Phase8 - EventSystem mapping

## Best-supported logical chain

```text
PROBABLE Script/Tools/EventSystem/EventSystem.lua
  -> CONFIRMED case-insensitive .lua removal in FUN_06be427c
  -> PROBABLE extensionless Lua module lookup
  -> UNKNOWN Lua package searcher/file candidate
  -> CONFIRMED optional provider or Android file-backend fallback
  -> UNKNOWN container and entry
```

The first eight UTF-16 code units of the path remain encoded in static data, so the complete logical path stays PROBABLE. All exact ASCII/UTF-8 and UTF-16LE scans across the four recovered PAK bodies returned zero matches for EventSystem and the server-list terms.

The fallback Android file layer and optional provider architecture mean EventSystem could be supplied by a PAK layer, Android asset, loose/downloaded file, or another runtime provider. No accessible metadata selects one of them.

```text
EVENTSYSTEM_PROVIDER = UNKNOWN
EVENTSYSTEM_CONTAINER = UNKNOWN
EVENTSYSTEM_ENTRY_ID = UNKNOWN
EVENTSYSTEM_ENTRY_OFFSET = UNKNOWN
EVENTSYSTEM_ENTRY_SIZE = UNKNOWN
ENTRY_BOUNDARY = UNKNOWN
EVENTSYSTEM_BYTES_ACCESSIBLE_WITHOUT_BYPASS = UNKNOWN
CONTENT_TYPE = UNKNOWN
```

No entry was copied to `local_only_recovered/`.
