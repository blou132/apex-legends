# First backend request

```text
FIRST_REQUEST = {
  host: tdm.mgapex.com,
  port: 8013,
  protocol: HTTPS_CONFIGURED,
  method: POST,
  path: /tdm/v1/route,
  content_type: UNKNOWN,
  body_size: UNKNOWN,
  sensitive_fields_redacted: true
}
```

## Evidence

TDM logs `HttpCurl::HttpPost` with the complete URL. Fifteen milliseconds later it logs HTTP response code `0`, libcurl code `6`, and an empty response body. This confirms request construction and dispatch into curl, not network delivery.

## Purpose

Function names `SendClientRoutePostRequest` and `HTTPRouteProc2`, plus the TDM component and `/tdm/v1/route` path, classify the purpose as:

```text
REQUEST_PURPOSE = TDM_CLIENT_ROUTE_BOOTSTRAP
```

No evidence links this POST to Phase5 `RequestAvatarServerList`, which constructs a GET from a caller-supplied URL. The two operations must remain separate.

No HTTP 404 response was sent because the request never reached a controlled local server.
