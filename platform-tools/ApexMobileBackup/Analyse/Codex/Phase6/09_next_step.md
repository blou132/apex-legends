# Phase6 - Next step

```text
STATIC_NATIVE_LIMIT_REACHED = YES
```

The native path is now resolved through the Lua bridge. It ends at the probable invocation:

```text
Script/Tools/EventSystem/EventSystem.lua
  -> EventSystem.PostCppEvent(0x138, success, response_body)
```

Both full names remain PROBABLE because eight UTF-16 code units in each constructed value are encoded. The confirmed clear fragments and lengths are in `01_event_emitter.md`.

```text
NEXT_REQUIRED_SOURCE = Script/Tools/EventSystem/EventSystem.lua and the Lua module that subscribes to ELuaCppEventType.EVENTID_AVATARSERVERLIST_RETURN
```

Once those scripts are legitimately available, the next targeted static steps are:

1. prove the registration call and exact `0x138` handler;
2. follow only that handler into its parser;
3. list only fields actually read from `response_body`;
4. prove the destination object and type;
5. test for a real `Login+0x150` write and `OpenServerList` call;
6. trace the URL producer from the same script layer if present.

Phase6 does not start PAK decryption or brute force. Native-wide searches are no longer the efficient boundary for the missing consumer.
