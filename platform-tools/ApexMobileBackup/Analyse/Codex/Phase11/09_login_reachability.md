# Login reachability

No Phase11 edge can be evaluated for `ULoginMgrWrapper`, `LoginMgr`,
`OpenServerList`, `RequestAvatarServerList`, or `ServerListName` because the
native resume root is unresolved.

```text
LOGIN_NATIVE_REACHABLE = UNKNOWN
REQUEST_AVATAR_SERVER_LIST_REACHABLE = UNKNOWN
OPEN_SERVER_LIST_REACHABLE = UNKNOWN
```

Phase5 and Phase6 still describe the request and callback path when invoked;
they do not prove that the path is reached during resume startup.
