# Dolphin owner field

`FUN_04812c6c` has the exact `DolphinUpdater::Shutdown` anchor and manages
owner field `+0x1f0` as a persistent interface pointer:

1. Ghidra `0x04812c88` loads and null-tests `owner+0x1f0`.
2. A bounded path can tail-dispatch interface slot `+0x40`.
3. Ghidra `0x04812ebc` forms the exact field address.
4. The value is null-tested and interface slot `+0x18` is invoked.
5. Ghidra `0x04812edc` physically calls the adjacent
   `DolphinHelper::GetCurApkPath` PLT, not `ReleaseDolphin`.
6. Ghidra `0x04812ee0` clears the same field.

The non-null test, virtual use, lifecycle cleanup, and clear promote `+0x1f0`
as an owned Dolphin interface field despite the unresolved import mismatch.

```text
DOLPHIN_RELEASE_OWNER_OBJECT = DolphinUpdater (PROBABLE)
DOLPHIN_RELEASE_OWNER_FIELD = +0x1f0
DOLPHIN_FIELD_CLEARED_AFTER_RELEASE = YES_AFTER_VIRTUAL_CLEANUP_AND_ADJACENT_GETCURAPKPATH_CALL
```
