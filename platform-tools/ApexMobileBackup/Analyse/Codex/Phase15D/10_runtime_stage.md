# Runtime stage observations

The bounded offline log confirms native plugin initialization, successful
`libUE4.so` loading, UE4-tagged splash startup, and the first application
network operation.

The first observed request was an HTTP POST to `tdm.mgapex.com`. It failed at
DNS resolution with curl result code `6`; no connection, TLS session, response,
or backend interaction was observed. Later remote-configuration attempts also
failed with `UnknownHost`.

No new log witness in this run names the targeted client stages. Absence from a
bounded log is not classified as `NOT_REACHED`.

| Target | Phase15D result |
| --- | --- |
| `nativeResumeMainInit` | NO_NEW_EVIDENCE |
| Lua runtime/chunk load | NO_NEW_EVIDENCE |
| `ClientLaunch` | NO_NEW_EVIDENCE |
| `EventSystem` / `PostCppEvent` | NO_NEW_EVIDENCE |
| `Login` | NO_NEW_EVIDENCE |
| `RequestAvatarServerList` | NO_NEW_EVIDENCE |

Earlier static and Huawei-runtime conclusions remain unchanged. In particular,
this run does not prove a Lua virtual path, event `0x138` consumer, server-list
parser, login transition, or game-server endpoint.
