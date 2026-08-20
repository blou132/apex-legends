# Lua runtime search

The complete existing Phase15D log was searched case-insensitively for exact
and reasonable variants of:

- `Lua` / `lua`
- `ClientLaunch`
- `Client/Launch/ClientLaunch.lua`
- `EventSystem`
- `PostCppEvent`
- `Script/Tools/EventSystem`
- `LoadFile`, `LoadBuffer`, and `LoadChunk`

No match was present. There was therefore no generic third-party Lua string to
misattribute and no symbolic log representation of the Phase7/8 loader
boundaries.

```text
LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENTSYSTEM_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
```

This result does not mean those stages were not reached. It only means the
bounded Phase15D log contains no named runtime witness for them.
