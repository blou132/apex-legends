# Phase7 - Subscriber search

Event `0x138 / 312` and enum name `EVENTID_AVATARSERVERLIST_RETURN` are **CONFIRMED**. The native path reaches `FUN_06be3f4c`, bridge `FUN_06be427c`, and the **PROBABLE** dynamic target `EventSystem.PostCppEvent`.

No extracted Lua and no PAK body are currently available. Searches in cleaned reports find only prior analysis references. They do not identify a registration call, subscriber table, handler, or parser.

| Question | Status |
| --- | --- |
| Subscriber for `0x138` | **UNKNOWN** |
| Registration mechanism | **UNKNOWN** beyond EventSystem dispatch |
| Exact handler | **UNKNOWN** |
| Response parser | **UNKNOWN** |
| Response format | **UNKNOWN** |
| Server-list fields | **UNKNOWN** |

No +/-64 KiB asset neighborhood was inspected because there was no raw asset hit from which to define such a window.
