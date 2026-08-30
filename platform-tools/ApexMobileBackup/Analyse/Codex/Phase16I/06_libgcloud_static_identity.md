# libgcloud static identity

`libgcloud.so` was extracted from the exact verified APK into local-only
storage. It is an ELF64 AArch64 shared object. Its executable `PT_LOAD` starts
at file offset and virtual address zero; the writable load uses the expected
nonzero file/virtual offset relationship.

The exact local binary revalidated the historical Ghidra model:

```text
GHIDRA_ADDRESS = ELF_VA + 0x00100000
CVERSIONMGR_INIT_GHIDRA = 0x00576180
CVERSIONMGR_INIT_ELF_VA = 0x00476180
```

Bounded disassembly and the first 16 entry bytes agreed with that function.
The local library hash and binary remain local-only.

```text
LIBGCLOUD_LOCAL_IDENTITY_VALID = YES
CVERSIONMGR_INIT_STATIC_BYTES_VALIDATED = YES
ADDRESS_MODEL_REVALIDATED = YES
```
