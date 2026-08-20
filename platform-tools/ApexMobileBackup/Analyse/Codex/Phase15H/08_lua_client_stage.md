# Lua and client-launch stage

The Apex-process log after post-resume T0 was searched for exact and controlled
variants of `Lua`, `ClientLaunch`, its known script path, `EventSystem`,
`PostCppEvent`, `LoadFile`, `LoadBuffer`, and `LoadChunk`.

No matching application hit was present. Third-party SDK lifecycle and network
messages were not promoted to Lua or client evidence. The rendered splash does
not prove that a Lua chunk or `ClientLaunch` executed.

```text
LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENTSYSTEM_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LUA_CHUNK_LOAD = UNKNOWN
```
