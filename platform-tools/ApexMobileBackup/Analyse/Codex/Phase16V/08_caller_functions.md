# Caller functions

Seven reachable caller functions were retained, including the one adjustor
thunk. No malformed, data-like, or post-noreturn source was accepted.

| Method | Ghidra caller | ELF entry | Exact local identity |
|---|---|---|---|
| Shutdown | `FUN_04812c64` | `0x04712c64` | confirmed adjustor thunk |
| OnNoticeInstallApk | `FUN_080f8754` | `0x07ff8754` | `DolphinCallback::OnDolphinNoticeInstallApk` |
| FirstExtract continuation | `FUN_080f87d0` | `0x07ff87d0` | `DolphinCallback::OnDolphinFirstExtractSuccess` |
| CheckUpdate | `FUN_05b85d18` | `0x05a85d18` | `UDolphinUpdater::StartUpdate, AppUpdate` |
| CheckUpdate | `FUN_05b89618` | `0x05a89618` | `UDolphinUpdater::OnSrcUpdateFinished` |
| CheckUpdate | `FUN_05d38254` | `0x05c38254` | `UDolphinUpdater::OnUpdateSuccess` / `OnAppUpdateFinished` |
| CheckUpdate | `FUN_080bf050` | `0x07fbf050` | `SkipAppUpdate`, `LuaClientHelper.cpp` |

```text
OWNER_METHOD_REACHABLE_CALLER_COUNT = 7
OWNER_CALLER_IDENTITIES = SHUTDOWN_ADJUSTOR; DOLPHIN_CALLBACK_ONNOTICE; DOLPHIN_CALLBACK_FIRSTEXTRACT; UDOLPHINUPDATER_STARTUPDATE; UDOLPHINUPDATER_ONSRCUPDATEFINISHED; UDOLPHINUPDATER_ONUPDATESUCCESS; LUA_SKIPAPPUPDATE
```
