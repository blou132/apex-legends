# Dolphin Init dispatch

The proven factory pointer is lost before any vptr load. Consequently the
known `GCloudDolphinImp` primary vtable `0x00979620`, slot `+0x10`, and target
`FUN_005458a0` cannot be connected to a client dispatch from this callsite.

No unproven global indirect-call scan was used.

```text
DOLPHIN_INIT_DISPATCH_PROVEN = NO
DOLPHIN_INIT_CALLER = UNKNOWN
DOLPHIN_INIT_CALL_SITE = UNKNOWN
DOLPHIN_INIT_TARGET_MATCH = NOT_APPLICABLE
```
