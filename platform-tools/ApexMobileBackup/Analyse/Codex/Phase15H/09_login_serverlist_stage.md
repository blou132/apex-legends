# Login and server-list stage

The post-resume Apex-process log was searched for `Login`, `LoginMgr`,
`ULoginMgrWrapper`, `OpenServerList`, `ServerListName`,
`RequestAvatarServerList`, `AvatarServerList`,
`EVENTID_AVATARSERVERLIST_RETURN`, and contextually meaningful `0x138`.

No matching runtime hit was found. The SDK message that no account identifier
was available is telemetry state, not evidence that the game's Login manager
or server-list path executed. No account input or backend response occurred.

```text
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
SERVER_LIST_EVENT_0X138_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_UI_VISIBLE = NO
```
