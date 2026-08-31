# Init caller

No exact client-side virtual dispatch to the Dolphin Init slot was proven.
The `libUE4.so` chain rooted at `FUN_05b311a8` reaches
`FUN_080f6f04` and `FUN_080f87d0`, but exact embedded identifiers classify
the upper function as `DolphinCallback::OnDolphinFirstExtractSuccess` in
`DolphinUpdaterCallback.cpp`. This is post-extraction callback/path handling,
not the bootstrap owner or Init caller.

The direct factory call inside that chain is invalidated by its incompatible
PLT/ABI sequence. Promoting any member of the chain as the Init caller would
therefore be circular.

```text
DOLPHIN_INIT_CALLER = UNKNOWN
DOLPHIN_INIT_CALLER_MODULE = UNKNOWN
DOLPHIN_INIT_CALL_SITE = UNKNOWN
```
