# Login reachability

Phase5 and Phase6 statically prove this later path exists:

```text
RequestAvatarServerList
  -> HTTP GET
  -> response FString
  -> event 0x138
  -> native-to-Lua bridge
  -> probable EventSystem.PostCppEvent
```

Phase10 finds no static edge from TDM route handling or GCloud RemoteConfig to that path. The runtime log does not name Login, `RequestAvatarServerList`, event `0x138`, EventSystem, or its parser.

```text
UE4_RUNTIME_STAGE_REACHED = CONFIRMED
LUA_RUNTIME_STAGE_REACHED = UNKNOWN
CLIENTLAUNCH_STAGE_REACHED = UNKNOWN
LOGIN_STAGE_REACHED = UNKNOWN
REQUESTAVATARSERVERLIST_REACHED = UNKNOWN
```

The existing request path must not be treated as the next backend until runtime or a reliable static startup edge proves that execution reaches it.
