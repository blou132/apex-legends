# GameUpdateMgr +0x38 property

The known consumer still loads `GameUpdateMgr+0x38` at ELF `0x07fbf118` and
uses the non-null value as the object passed to `CheckUpdate`. The value is
layout-compatible with the known DolphinUpdater object.

Because no GameUpdateMgr property array was recovered, the reflection status
of `+0x38` cannot be decided:

- Reflected: `UNKNOWN`
- Property name: `UNKNOWN`
- Property kind: `UNKNOWN`
- Property size: `UNKNOWN`

The result is intentionally not promoted to `NO` merely because metadata was
unavailable.
