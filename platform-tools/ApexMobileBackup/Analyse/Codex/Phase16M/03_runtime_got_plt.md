# Runtime GOT and PLT

The exact PT_LOAD-derived bases produced runtime candidates for the known
libUE4 callsite, return site, PLT stub, and GOT cell, plus the libgcloud
provider entry. Each candidate fell in the expected module mapping. Absolute
addresses remain local-only.

The GOT cell and runtime code bytes were not read: the read-only ptrace probe
failed with `EPERM` because the selected thread already had a tracer. No GOT or
process memory write was attempted.

```text
CALLSITE_RUNTIME_VALID = YES_MAPPING_ONLY
PLT_RUNTIME_VALID = YES_MAPPING_ONLY
GOT_RUNTIME_VALID = YES_MAPPING_ONLY
CREATEDOLPHIN_RUNTIME_VALID = YES_MAPPING_ONLY
CREATEDOLPHIN_GOT_POINTER_READ = NO
CREATEDOLPHIN_GOT_POINTS_TO_PROVIDER = UNKNOWN
GOT_TARGET_MODULE = UNKNOWN
GOT_TARGET_MODULE_RELATIVE = UNKNOWN
```
