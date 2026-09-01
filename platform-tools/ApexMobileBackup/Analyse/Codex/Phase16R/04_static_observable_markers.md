# Static observable markers

The bounded regions contain five relevant conditional markers.

| Marker | Static relationship | Position |
|---|---|---|
| `DolphinCallback::OnDolphinFirstExtractSuccess` | Callback entry | Before callback owner null-check and before clear continuation |
| `FirstExtract Need first extract, Check last extract paks` | Set path 1 | Immediately before `+0x45 = 1` when logging is enabled |
| `UDolphinUpdater::FirstExtractPak, Missing first extract file: %s, Need first extract` | Set path 2 | Immediately before `+0x45 = 1` when logging is enabled |
| `CheckUpdate, first extract pak` | `+0x45 != 0` Init-mode branch | Before alternate argument setup and the shared Init dispatch |
| `UDolphinUpdater::CheckUpdate, init Dolphin failed` | Init return-bit failure branch | After the shared Init dispatch |

Neither W23 helper contains a direct log marker. No marker occurs after the
clear store, and no retained success log is tied to the slot-`+0x28` call.

```text
STATIC_OBSERVABLE_MARKER_COUNT = 5
```
