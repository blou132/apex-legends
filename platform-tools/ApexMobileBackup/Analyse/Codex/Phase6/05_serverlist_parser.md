# Phase6 - Server-list parser

## Current data flow

```text
HTTP completion
  -> response body FString
  -> event 0x138
  -> native-to-Lua bridge
  -> consumer UNKNOWN
  -> parser UNKNOWN
```

The response representation at the native boundary is a `FString` (**CONFIRMED**). Its wire format is **UNKNOWN**. HTTP GET and status 200 do not imply JSON.

No consumer was recovered, so no JSON, protobuf, custom deserializer, split routine or Lua parser can be attributed to `response_body`. No candidate was promoted based only on engine-wide parser availability.

## Fields

| Field notion | Type | Required | Destination | Status |
| --- | --- | --- | --- | --- |
| server ID/name | UNKNOWN | UNKNOWN | UNKNOWN | UNKNOWN |
| region/zone/channel | UNKNOWN | UNKNOWN | UNKNOWN | UNKNOWN |
| host/IP/address | UNKNOWN | UNKNOWN | UNKNOWN | UNKNOWN |
| port | UNKNOWN | UNKNOWN | UNKNOWN | UNKNOWN |
| status/backup | UNKNOWN | UNKNOWN | UNKNOWN | UNKNOWN |

No server-list field is observed in the native emitter at Ghidra `0x6be3f4c` / ELF `0x6ae3f4c`. Therefore there is currently no confirmed host, IP or port.

Machine evidence: `output/serverlist_parser.json`.
