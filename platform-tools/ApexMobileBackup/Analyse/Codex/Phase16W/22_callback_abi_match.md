# Callback ABI match

The provider dispatches the same key callback slots established in the known
client table:

| Slot | Provider behavior | Known client role |
|---|---|---|
| `+0x10` | forwards structured version information | `OnDolphinVersionInfo` |
| `+0x20` | forwards error event | `OnDolphinError` |
| `+0x28` | forwards success event | `OnDolphinSuccess` |
| `+0x30` | forwards install-notice event | `OnDolphinNoticeInstallApk` |

```text
PROVIDER_CLIENT_CALLBACK_ABI_MATCH = CONFIRMED
```

This confirms interface compatibility, not the unobserved vptr store on the
concrete owner `+0x1f8` target.
