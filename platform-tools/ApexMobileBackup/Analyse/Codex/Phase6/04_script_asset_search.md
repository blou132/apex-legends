# Phase6 - Script and asset search

## Read-only search

The accessible extracted text set under `ApexMobileBackup` was searched for:

```text
EVENTID_AVATARSERVERLIST_RETURN
AVATARSERVERLIST
AvatarServerList
RequestAvatarServerList
OpenServerList
ServerListName
EventSystem
PostCppEvent
```

The accessible inventory contained 51 JSON and 30 TXT files after the Phase6 exports, but no extracted `.lua` file. Matches occur only in prior analysis reports and exported native metadata. No actual script consumer was found.

## Existing PAK evidence

The prior read-only PAK analysis found visible Lua path strings such as `Client/Launch/ClientLaunch.lua` and several client UI Lua paths. It also found that the PAK indexes appear encrypted or otherwise unreadable by the minimal plain-index inspection. Raw searches did not find `RequestAvatarServerList` or a server host/IP/port.

This supports, but does not prove:

```text
CONSUMER_LOCATION_PROBABLE = PAK/Lua
```

No PAK was modified, brute-forced or decrypted in Phase6. The absence of a raw string is not proof that the compressed or encrypted script does not exist.
