# Init virtual dispatch

Candidate promotion required all of the following: provenance from the exact
factory return, primary vptr `0x00979620`, load of slot `+0x10`, and a client
indirect call. No client callsite met those conditions.

The internal `libgcloud.so` vtable target remains confirmed, but it does not
identify who invokes the interface from the client. A global scan of arbitrary
`BLR` instructions was intentionally not used as proof.

```text
DOLPHIN_INIT_DISPATCH_CANDIDATES = 0 provenance-qualified candidates
DOLPHIN_INIT_DISPATCH_PROVEN_COUNT = 0
```
