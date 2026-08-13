# Phase7 - PAK raw hits

## Phase7 execution status

`NOT_RERUN_INPUT_ABSENT`

None of the four known PAK files is present in the current workspace. Therefore Phase7 produced no direct ASCII, UTF-8, or UTF-16LE hit and no new offset, context window, entropy measurement, compression marker, or alignment result.

## Historical witness, not a new Phase7 hit

Prior read-only analysis confirmed the visible path `Client/Launch/ClientLaunch.lua` in `launch.pak` at `0x250517`. A zlib marker existed at `0x2513f8`, delta `+3809`; 4 KiB entropy was `7.700` before and `7.287` after the path. No readable Lua body was tied to the path, and the entry boundary was not known.

All four PAK indexes previously appeared encrypted or otherwise unreadable to the minimal parser. This does not prove that individual entry contents are encrypted. No earlier raw result tied `EventSystem.lua`, `PostCppEvent`, or `EVENTID_AVATARSERVERLIST_RETURN` to a PAK entry.

The prior PAK sizes and index offsets are retained in `output/pak_raw_hits.json` for reproducibility.
