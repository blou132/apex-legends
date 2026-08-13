# EventSystem runtime

## Result

```text
EVENTSYSTEM_OBSERVED = NO
EVENTSYSTEM_RUNTIME_PATH_CONFIRMED = NO
EVENT_0X138_SUBSCRIBER_FOUND = NO
SERVERLIST_PARSER_FOUND = NO
```

Neither `EventSystem` nor `PostCppEvent` appeared in the cleaned startup evidence. No Lua chunk became accessible, so Phase9C did not search proprietary script contents or attempt to recover them.

The following remain `UNKNOWN`:

- actual EventSystem logical module and final path
- effective provider and OpenRead path
- chunk name and size
- event `0x138` registration, subscriber, and handler
- response parser, format, fields, and destination
- relationship to `GameServerBackupIpList`
- local source of the `RequestAvatarServerList` URL
