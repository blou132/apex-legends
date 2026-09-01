# Real Init proxy

The shared client dispatch remains statically proven at ELF `0x05a2f0ac`.
Return bit zero enters a conditional failure-log branch containing
`UDolphinUpdater::CheckUpdate, init Dolphin failed`. Return bit one immediately
invokes interface slot `+0x28`; no bounded log is tied to that success call.

Phase15U contains the nested version-manager Init witness. This confirms real
provider Init execution, while the exact client `owner+0x1f0` call site remains
probable. Phase16I contains no real-Init proxy.

```text
REAL_INIT_SUCCESS_MARKER = NONE; POST_INIT_SLOT_0X28_DISPATCH_IS_STATIC_ONLY
REAL_INIT_FAILURE_MARKER = UDolphinUpdater::CheckUpdate, init Dolphin failed
PHASE15U_REAL_DOLPHIN_INIT = PROBABLE
PHASE16I_REAL_DOLPHIN_INIT = NOT_OBSERVED
```
