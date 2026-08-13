# REPORT PHASE6

Date: 2026-08-13. Static Ghidra analysis with `-process libUE4.so -noanalysis -readOnly`.

## Mandatory answers

1. **What does `FUN_06be427c` do?** **CONFIRMED**: native-to-Lua bridge at Ghidra `0x6be427c`, ELF `0x6ae427c`; it checks the runtime at `0xb696eb8`, handles a `.LUA` module path, pushes typed values, resolves and invokes through `FUN_042bf01c`, `FUN_04440f2c` and core `FUN_06be4d1c`.
2. **Lua, Blueprint, ProcessEvent-like or other?** **CONFIRMED Lua**. Blueprint and UObject ProcessEvent-like are **INVALIDATED for this function**: no `UFunction` lookup or UObject parameter buffer is used; `.LUA` and the Lua-style typed stack are explicit.
3. **What dynamic name is built?** **PROBABLE** `EventSystem.PostCppEvent`, with confirmed fragment `EventSystem.Post[8 encoded UTF-16 units]`, length 24 plus null, built by `FUN_06be3f4c` at `0x6be4014..0x6be402c`; encoded suffix data is at Ghidra `0x23e76e0`, ELF `0x22e76e0`.
4. **How are handlers registered?** **UNKNOWN** beyond the probable Lua `EventSystem.PostCppEvent` boundary. The native enum table slot `0xad25078` names value `0x138` but is not a subscriber table.
5. **Which consumer receives event `0x138`?** **UNKNOWN**. No callback slot/name is proven after the call at Ghidra `0x6be4054`.
6. **Is the consumer native or script?** **PROBABLE script/Lua**, because the confirmed dispatch target is the Lua bridge; no native consumer mapping was found.
7. **Where is it?** **PROBABLE `PAK/Lua`**. No extracted Lua source is accessible; existing PAK evidence shows Lua paths but unreadable/encrypted indexes.
8. **How are `success` and `response_body` transmitted?** **CONFIRMED** through `FUN_06be3f4c(context,event,success,FString)` at Ghidra `0x6be3f4c`, ELF `0x6ae3f4c`; event is masked to `uint16` at `0x6be4020`, then all values are passed to the Lua bridge at `0x6be4054`. Exact Lua handler signature remains UNKNOWN.
9. **Which parser handles `response_body`?** **UNKNOWN** because the Lua consumer is missing. No parser is called by the native emitter.
10. **What is the response format?** **UNKNOWN**. Only its native `FString` representation is **CONFIRMED** by handler `FUN_06be3bdc`, Ghidra `0x6be3bdc`, ELF `0x6ae3bdc`.
11. **Which server-list fields are read?** **UNKNOWN**; none is read in `FUN_06be3bdc`, `FUN_06be3f4c` or `FUN_06be427c`.
12. **Is there a host/IP field?** **UNKNOWN**; no field or literal host/IP is observed in the proven path.
13. **Is there a port?** **UNKNOWN**; no parsed port is observed.
14. **Where are data stored?** **UNKNOWN**; the consumer/parser destination is not available.
15. **Is a Login instance proven?** **UNKNOWN** downstream and **INVALIDATED for the native callback capture**. No typed Login object reaches Phase6.
16. **Is `GameServerBackupIpList +0x150` written?** **UNKNOWN** globally; no write is reached from the resolved path. Offset `0x150` remains **CONFIRMED** metadata.
17. **What does that property contain?** **UNKNOWN**. Owner `Login` and type `TArray<FName>` remain **PROBABLE** from metadata around Ghidra `0xaf65f20`; the elements are not proven to be IPs.
18. **Is `OpenServerList` triggered?** **UNKNOWN**. Its metadata exists at Ghidra `0xaf66080`, but no call is connected to event `0x138`.
19. **What supplies the URL `FString`?** **UNKNOWN**. `FUN_07a31858`, Ghidra `0x7a31858`, ELF `0x7931858`, receives it from an Unreal frame; only indirection/data references `0x2a46298` and `0x315ea38` exist.
20. **Can the exact URL be reconstructed statically?** **UNKNOWN / not with current evidence**. No producer, config field or concrete value is reached; no endpoint was contacted.
21. **Which concrete receiver uses `SyncPayloadToGameServer`?** **UNKNOWN**. Thunk `FUN_0a220f70`, Ghidra `0xa220f70`, ELF `0xa120f70`, has no direct code caller.
22. **Is target `+0xa58` resolved?** **UNKNOWN**. The virtual slot is confirmed, but no concrete vtable/class target is proven.
23. **Do we have GET -> FString -> event -> parser -> fields -> storage?** **CONFIRMED only through `GET -> response FString -> event 0x138 -> Lua bridge`**; parser, fields and storage are **UNKNOWN**.
24. **Do we have game-server host/IP/port?** **UNKNOWN**. No such destination is proven.
25. **Is the next limit native or PAK/script?** **CONFIRMED as a PAK/script evidence limit** for this path. `STATIC_NATIVE_LIMIT_REACHED = YES`; the next required sources are the probable EventSystem Lua module and the `0x138` subscriber.

## Major result

```text
RequestAvatarServerList
  -> HTTP GET
  -> FUN_06be3bdc(response status/body)
  -> FUN_06be3f4c(event 0x138, success, FString)
  -> FUN_06be427c native-to-Lua bridge
  -> probable EventSystem.PostCppEvent
  -> subscriber/parser UNKNOWN
```

## Reproducibility and safety

The seven required JSON exports are under `output/`; the reproducible script is `Phase3/ghidra_scripts/ApexPhase6Export.java`. No binary, raw log, account/device data, credential, network request, runtime hook or PAK modification is part of this phase.
