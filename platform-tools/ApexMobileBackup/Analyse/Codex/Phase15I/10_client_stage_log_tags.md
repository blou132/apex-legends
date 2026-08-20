# Client-stage log tags

## Exact observed tags useful for a future bounded filter

- `UE4`
- `GCloudCore`
- `GCloud`
- `TDM`
- `[MSDK]`
- `MSDK`
- `[CrashSightReport]`
- `PluginMSDK`

## Stage-name search

The exact client contains the names `ClientLaunch`, `EventSystem`, `LoginMgr`,
`RequestAvatarServerList`, and `OpenServerList`, but local context classifies
them as reflection, property, source, or function names. No exact logging tag or
message template was established for these stages. No Lua-specific stage tag
was established either.

Those names may be bounded search keywords inside an official log file, but
must not be presented as confirmed tags.

```text
NAMED_CLIENT_STAGE_LOG_TAGS_FOUND = NO
TARGET_LOG_TAGS_FOR_FUTURE_RUN = UE4,GCloudCore,GCloud,TDM,[MSDK],MSDK,[CrashSightReport],PluginMSDK
```
