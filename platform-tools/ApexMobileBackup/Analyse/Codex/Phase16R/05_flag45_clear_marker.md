# Owner flag clear marker

`DolphinCallback::OnDolphinFirstExtractSuccess` logs its function name only
when the runtime log level permits it. The log call is before the callback's
owner-pointer null check. The callback invokes `FUN_080f6f04` only for a
non-null owner; that continuation clears `owner+0x45` at ELF `0x07ff6f40`.

Consequently, callback-entry logging would prove callback entry but would not
by itself prove that the continuation or store executed. There is no bounded
post-store marker.

No exact callback marker exists in the retained Phase15U, Phase16I, or
Phase16M logs. Because the marker is conditional and is not post-store, all
three runs remain evidence-incomplete for the clear.

```text
FLAG45_CLEAR_OBSERVABLE_MARKER = NONE_PROVING_STORE; CALLBACK_ENTRY_LOG_IS_PRE_OWNER_GUARD
FLAG45_CLEAR_MARKER_CONFIDENCE = CONFIRMED
PHASE15U_FLAG45_CLEAR = EVIDENCE_INCOMPLETE
PHASE16I_FLAG45_CLEAR = EVIDENCE_INCOMPLETE
PHASE16M_FLAG45_CLEAR = EVIDENCE_INCOMPLETE
```
