# Phase6 - Event 0x138 consumer

## Proven boundary

The native path reaches the Lua event system:

```text
FUN_06be3f4c
  -> FUN_06be427c native-to-Lua bridge
  -> probable EventSystem.PostCppEvent(0x138, success, response_body)
  -> Lua registration/subscriber UNKNOWN
```

`EVENTID_AVATARSERVERLIST_RETURN = 0x138 / 312` is **CONFIRMED** by the response call site and by the `ELuaCppEventType` metadata entry. The metadata name is at Ghidra `0x227dbe3` / ELF `0x217dbe3`; its table slot is Ghidra `0xad25078` / ELF `0xac25078`.

## Registration search

The dispatcher-related native functions implement the Lua call boundary, but do not contain a table that maps `0x138` to a native callback. No related slot, switch case, map entry, callback pointer or handler name was proven.

The enum metadata table is a name/value registration table for constants, not evidence of the runtime subscriber. It must not be misreported as the event handler registry.

## Result

| Item | Status |
| --- | --- |
| registration mechanism after `PostCppEvent` | UNKNOWN |
| exact consumer | UNKNOWN |
| consumer kind | PROBABLE Lua script |
| consumer location | PROBABLE `PAK/Lua` |
| native consumer candidate | none proven |

`CONSUMER_LOCATION_PROBABLE = PAK/Lua` is supported by the confirmed Lua bridge, the probable EventSystem module path, the absence of a native subscriber mapping and the absence of extracted Lua sources in the accessible dataset. It is not upgraded to CONFIRMED without the script.

Machine evidence: `output/event_consumer.json`.
