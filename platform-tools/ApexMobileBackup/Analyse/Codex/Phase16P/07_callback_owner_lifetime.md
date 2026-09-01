# Callback owner lifetime

The callback stores an external owner pointer at `+0x08`; its methods null-test
that pointer before forwarding events. No callback allocation site or non-null
assignment was proven, so which object constructs or stores the callback
remains unknown.

Bounded owner method candidates are:

- `FUN_080f68e8`: `DolphinUpdater::OnNoticeInstallApk`.
- `FUN_080f6f04`: first-extract-success continuation.
- `FUN_04812c6c`: `DolphinUpdater::Shutdown`.
- protected `FUN_05b2eb00` region: `DolphinUpdater::CheckUpdate`.

```text
CALLBACK_OWNER_RELATIONSHIP = CALLBACK_STORES_DOLPHINUPDATER_OWNER_AT_PLUS_0x08 (PROBABLE)
OWNER_DOLPHIN_METHOD_CANDIDATES = FUN_080f68e8; FUN_080f6f04; FUN_04812c6c; FUN_05b2eb00
```
