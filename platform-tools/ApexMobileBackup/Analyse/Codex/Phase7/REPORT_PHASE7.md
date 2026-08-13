# REPORT PHASE7

Date: 2026-08-13. Read-only local inventory and targeted Ghidra analysis.

## Major result

The native loader is now resolved beyond the Phase6 dispatch boundary:

```text
probable Lua module path
  -> FUN_049a8b54 virtual path lookup
  -> optional provider / FUN_049a9694 file read
  -> FUN_046355e8 generic file manager
  -> thunk_FUN_049825bc backend open
  -> bytes, with optional UTF-8 BOM removed
  -> FUN_048ab4d0 probable Lua chunk load/compile
```

This confirms a generic virtual/platform file pipeline, not a specific PAK reader or physical container. The original PAK inputs are absent, so Phase7 cannot rerun raw searches or identify the EventSystem entry. Decision gate **D** applies.

## Mandatory answers

1. **Are `EventSystem.lua` and `PostCppEvent` now CONFIRMED?** **PROBABLE**, not confirmed. The clear fragments and lengths are confirmed, but eight encoded UTF-16 units in each value have no statically proven transform before lookup.
2. **Where is `EventSystem.lua` stored?** **UNKNOWN**. No physical asset, index entry, or cache record is available.
3. **Which PAK/container?** **UNKNOWN**. The loader reaches a generic virtual file layer only.
4. **What exact entry/path?** **PROBABLE** module name `Script/Tools/EventSystem/EventSystem.lua`; exact virtual lookup name and physical entry are **UNKNOWN**.
5. **How does the client load a Lua module?** **CONFIRMED in part**: `FUN_049a8b54` builds/resolves a virtual path, obtains bytes through an optional provider or `FUN_049a9694`, removes an optional UTF-8 BOM, and hands the buffer to **PROBABLE** Lua chunk loader `FUN_048ab4d0`.
6. **Which system reads module bytes?** **CONFIRMED generic virtual/platform file manager** through `FUN_046355e8` and backend open `thunk_FUN_049825bc`; concrete Pak/custom/physical backend is **UNKNOWN**.
7. **Is the script text, bytecode, compressed, or encrypted?** **UNKNOWN**. No script bytes are recovered. No transform occurs after the reader except BOM removal, so any decompression/decryption would be below that boundary.
8. **Is the container index readable?** **PROBABLE no** for the four historically inspected PAKs: indexes appeared encrypted or unreadable to the minimal parser. They are absent now and were not retested.
9. **Is the entry itself accessible?** **UNKNOWN**. No EventSystem entry boundary is identified.
10. **Can EventSystem be recovered without bypass?** **UNKNOWN with present evidence**, and not from the current workspace because source containers are absent. No bypass was attempted.
11. **Was the `0x138` subscriber found?** **UNKNOWN / no proven subscriber**.
12. **Which handler?** **UNKNOWN**.
13. **Which parser?** **UNKNOWN**.
14. **Is the `response_body` format identified?** **UNKNOWN**. Only its native `FString` transport is **CONFIRMED**.
15. **Which server-list fields are actually read?** **UNKNOWN**.
16. **Was a source URL for `RequestAvatarServerList` found?** **UNKNOWN / no**. The native dynamic `FString` source remains unresolved.
17. **Does `GameServerBackupIpList` participate?** **UNKNOWN**. Offset `Login+0x150` is confirmed metadata, but no writer or link to event `0x138` is proven.
18. **Do we have host/IP/port?** **UNKNOWN / no proven value**.
19. **What exact technical limit remains?** **CONFIRMED limit**: the static native path ends at a generic file manager and probable Lua chunk loader, while mount prefix, concrete backend, container, entry boundary, script bytes, subscriber, and parser remain unavailable. The original PAKs are absent.
20. **What next step is justified?** Restore the original local-only PAKs or obtain an authorized readable manifest/index/cache, then rerun targeted raw scans and entry correlation. If access requires decryption or protection bypass, stop and document it.

## Decision gate

```text
EVENTSYSTEM_CONTAINER_UNKNOWN = YES
DECISION_GATE = D
```

## Reproducibility and safety

Eight JSON outputs are under `output/`. `ApexPhase7Export.java` produced the four Ghidra exports from the read-only local project. No proprietary Lua, game binary, raw log, credential, account/device identifier, network request, runtime hook, decryption, or modified game artifact is included.
