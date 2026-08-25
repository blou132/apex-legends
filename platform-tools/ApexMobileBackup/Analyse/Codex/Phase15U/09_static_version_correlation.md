# Static version correlation

The runtime confirmed both update progress and an update error, so the
conditional local static follow-up was performed after Apex was stopped and the
network was restored.

The search was restricted to exact `I54140714`, exact `I54140715`, and short
distinctive update/version labels in the already preserved base APK resources,
DEX files/string exports, `libUE4.so`, existing PAK files, and their existing
local extraction. No new broad scan or network access was performed.

No exact anchor was found. The error code may be composed or localized at
runtime, but that is not proven. With no static anchor, no reference trace was
attempted.

```text
STATIC_CORRELATION_PERFORMED = YES_TARGETED_EXACT_STRING_ONLY
I54140714_STATIC_ANCHOR = NOT_FOUND
I54140715_STATIC_ANCHOR = NOT_FOUND
UPDATE_ERROR_CODE_LOCATION = UNKNOWN
UPDATE_UI_OWNER = UNKNOWN
VERSION_BOOTSTRAP_COMPONENT = GCLOUD_PUFFER_UPDATE_CONFIRMED_AT_RUNTIME; EXACT_UI_LINK_UNKNOWN
VERSION_REQUEST_FUNCTION = CPufferInitAction::MakeSureGetUrlFromServer (runtime log witness)
VERSION_REQUEST_METHOD = UNKNOWN_RPC
VERSION_REQUEST_URL_OR_HOST = puffer.4.707369824.dmp.mgapex.com (runtime log witness)
```
