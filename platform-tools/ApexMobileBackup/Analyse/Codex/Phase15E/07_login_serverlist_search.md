# Login and server-list runtime search

The existing Phase15D log was searched for:

- `Login`, `LoginMgr`, and `ULoginMgrWrapper`
- `OpenServerList` and `ServerListName`
- `RequestAvatarServerList` and `AvatarServerList`
- `EVENTID_AVATARSERVERLIST_RETURN` and `0x138`

No match was found. Static strings and previously reconstructed functions were
not treated as runtime evidence.

```text
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENT_0X138_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
```

The server-list URL, response parser, event consumer, and Login reachability
remain unchanged and unknown at runtime.
