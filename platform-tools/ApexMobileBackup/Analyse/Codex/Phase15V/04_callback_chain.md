# Failure callback chain

The bounded static chain is:

1. `FUN_00501d0c` (`CPufferInitAction::run`) directly calls
   `FUN_004ffd44` (`MakeSureGetUrlFromServer`) at Ghidra `0x501e94`.
2. `FUN_004ffd44` writes an internal Puffer result code and returns failure.
   Its response path registers `FUN_004fc7c0` (`ResUpdateCallBack`) through
   `FUN_004fd278`; the callback writes response state and marks completion.
3. The runner directly calls `FUN_004fcaf4` at Ghidra `0x501f04` with the
   internal result.
4. `FUN_004fcaf4` directly constructs `CPufferInitActionResult` through
   `FUN_004ee318`, then submits it through the manager callback vtable slot
   `+0x20`.
5. The result vtable has a data reference at Ghidra `0x977c80` to
   `FUN_004ee1dc`, whose source/log identity is
   `CPufferInitActionResult::ProcessResult`.
6. `FUN_004ee1dc` forwards `(success=0, internal_result)` through a second
   virtual callback slot `+0x10`.

The last target is dynamic and could not be resolved without expanding into a
new global analysis. The stop rule applies there. No static edge from this
callback to an Apex error mapper, Lua, login, or a rendered UI was found.

```text
PUFFER_FAILURE_CALLBACK_CHAIN = run -> MakeSureGetUrlFromServer -> internal result -> CPufferInitActionResult -> ProcessResult -> dynamic manager callback
CALLBACK_CHAIN_CONFIDENCE = CONFIRMED_TO_DYNAMIC_MANAGER_BOUNDARY
```
