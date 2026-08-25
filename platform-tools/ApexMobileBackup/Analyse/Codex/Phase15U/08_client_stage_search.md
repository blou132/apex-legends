# Named client stage search

The sanitized current-run boundary was searched for the requested stage names.
Missing log strings do not prove that a stage did not execute.

| Stage | Result |
| --- | --- |
| `GameActivity` | `CONFIRMED` |
| `DownloaderActivity` | `CONFIRMED` |
| UE4/native runtime | `CONFIRMED` |
| client launch level | `CONFIRMED` |
| version/update bootstrap | `CONFIRMED` |
| Lua | `NO_NEW_EVIDENCE` |
| ClientLaunch | `NO_NEW_EVIDENCE` |
| EventSystem | `NO_NEW_EVIDENCE` |
| Login / LoginMgr | `NO_NEW_EVIDENCE` |
| RequestAvatarServerList | `NO_NEW_EVIDENCE` |
| AvatarServerList | `NO_NEW_EVIDENCE` |
| OpenServerList | `NO_NEW_EVIDENCE` |
| event `0x138` | `NO_NEW_EVIDENCE` |

```text
LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
FIRST_NEW_RUNTIME_STAGE = UPDATE_MODULE_INITIALIZATION_2_OF_17_THEN_UPDATE_ERROR
FIRST_UNRESOLVED_TRANSITION = EXACT_UPDATE_UI_OWNER_AND_ERROR_CODE_TO_SERVICE_MAPPING
```
