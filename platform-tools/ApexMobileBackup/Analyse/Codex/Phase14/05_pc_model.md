# Debuggerd PC model

No frame was emitted, so there is no symbolized reference frame with which to
validate debuggerd's displayed PC semantics on this image. No runtime value was
converted to an ELF virtual address or Ghidra address.

Phase3C's static conversion remains valid only for already established ELF
virtual addresses:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
```

It is not applied to Phase14 because no runtime PC exists.

```text
DEBUGGERD_PC_MODEL = UNKNOWN
```
