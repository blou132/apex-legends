# ReleaseDolphin callsites

An exact aligned AArch64 `B`/`BL` scan of `.text` found no direct branch whose
calculated target is the exact `ReleaseDolphin` PLT at ELF `0x0a3c1910`.

```text
RELEASEDOLPHIN_DIRECT_CALLSITE_COUNT = 0
```

This result does not treat PLT adjacency as a call. In particular,
`DolphinUpdater::Shutdown` physically branches to the preceding
`DolphinHelper::GetCurApkPath` stub at ELF `0x0a3c1900`.
