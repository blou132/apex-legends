# Future Apex target boundary

No Apex installation, OBB restore, launch, process inspection, or runtime
address work occurred.

The future design target remains:

```text
module = libgcloud.so
symbol = CVersionMgrImp::Init
Ghidra address = 0x00576180
```

Any future phase must independently recompute the module-relative offset,
runtime base, absolute target, and executing TID. A single-thread disposable
tracee result would not solve Apex thread selection.
