# Phase9D - Isolated local backend observation

Date: 2026-08-13

## Executive result

Reanalysis of the existing Huawei Phase9C log identifies the first runtime-started backend request without issuing a DNS query or contacting an old service. At approximately `+1.727 s`, TDM dispatches an HTTP POST through curl to:

```text
https://tdm.mgapex.com:8013/tdm/v1/route
```

At approximately `+1.742 s`, the same operation reports HTTP response code `0`, libcurl return code `6`, and an empty response body. DNS therefore failed before a connection or TLS handshake.

```text
FIRST_BACKEND_HOST = tdm.mgapex.com
DESTINATION_PORT = 8013
PROTOCOL = HTTPS_CONFIGURED
HTTP_METHOD = POST
HTTP_PATH = /tdm/v1/route
TLS_ATTEMPT = NO
REQUEST_PURPOSE = TDM_CLIENT_ROUTE_BOOTSTRAP
CLIENT_PROGRESS = UNCHANGED_FROM_PHASE9C
LIBUE4_LOADED = UNKNOWN
EVENTSYSTEM_OBSERVED = NO
FINAL_GATE = A FIRST_BACKEND_REQUEST_CONFIRMED
```

## Runtime sequence

The first TDM POST predates the GCloudCore remote-config GET and PlayCommon logging request:

1. `+1.727 s`: TDM POST to `tdm.mgapex.com:8013/tdm/v1/route`; curl `6` at `+1.742 s`.
2. `+1.958 s`: GCloudCore GET to `cloudctrl.mgapex.com/cfgpush/getConfig`; `UnknownHost` at `+1.969 s`.
3. `+2.723 s`: PlayCommon HTTPS timestamp request to `play.googleapis.com`; explicit `UnknownHostException` at `+2.731 s`.

MSDK and GVoice endpoint values printed during configuration are not promoted to request or DNS evidence.

## Request classification

The method, scheme, host, explicit port, and path come directly from TDM's runtime `HttpCurl::HttpPost` entry. Content type and request body size are not logged and remain `UNKNOWN`. No sensitive query/body values are retained.

The component and function names classify this as a TDM client-route bootstrap. It is not linked to Phase5 `RequestAvatarServerList`: that native path builds a GET, while this operation is a TDM POST to `/tdm/v1/route`.

## Connection and TLS boundary

No destination address was resolved, so transport remains `UNKNOWN_NOT_CONNECTED`. HTTPS is configured, but no TCP connection, TLS ClientHello, SNI, TLS version, certificate exchange, or HTTP response was observed.

`TLS_ATTEMPT = NO` is therefore distinct from a certificate-boundary result.

## Why no second run occurred

The installed environment cannot safely satisfy both required properties: Huawei-to-PC connectivity and proven Huawei-to-Internet blocking. There is no installed DNS/capture tool, Windows hosted networking is unsupported, the Huawei is non-root/non-debuggable, and ADB reverse is TCP-only.

A local listener alone would not solve DNS, while leaving ordinary Wi-Fi enabled would violate the isolation rule. Phase9D therefore made no resolver, route, firewall, proxy, certificate, or client changes and performed no second run.

## Client and loader status

Client progress remains the Phase9C black-screen state. Reanalysis found no Lua package searcher, ClientLaunch, EventSystem, PostCppEvent, provider, OpenRead path, or chunk metadata.

`libUE4.so` remains `UNKNOWN`: UE4-tagged Java/startup messages are insufficient, process maps are unavailable to the shell, and no explicit native loader event exists.

## Safety and publication

The preserved Samsung was not targeted. No public DNS lookup, old backend connection, packet capture, raw log, header/body dump, account, token, cookie, device identifier, APK, OBB, PAK, Lua, memory data, hook, root, or bypass is published or used.
