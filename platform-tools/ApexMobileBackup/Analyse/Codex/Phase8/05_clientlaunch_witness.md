# Phase8 - ClientLaunch witness

`Client/Launch/ClientLaunch.lua` remains raw-visible as ASCII/UTF-8 in `launch.pak` at exact offset `0x250517`. This confirms the build witness and textual spelling only.

The offset lies in the PAK body, far before the internal index at `0x851a746`. Nearby readable values include script-level names such as `NewLaunchFromLaunchMap`. Consequently, the hit may be a string referenced by Lua content rather than the filename of the containing archive entry.

The witness does not provide:

- a readable PAK index record;
- an entry start or size;
- a mount point;
- a loader call that presents this exact string to the backend;
- a validated module-name-to-entry transformation.

```text
BUILD_WITNESS_MATCH = YES
PATH_MAPPING_CONFIRMED = NO
CLIENTLAUNCH_ENTRY_BOUNDARY = UNKNOWN
```

Using the raw hit itself as validation of the virtual-file mapping is therefore **INVALIDATED**.
