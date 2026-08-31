# Runtime module identity

Fresh offline launches mapped package-owned `libUE4.so` and `libgcloud.so`.
Load biases were derived from their exact PT_LOAD file-offset/virtual-address
pairs, not from an assumed first mapping. All absolute map and ASLR values are
retained only in ignored local evidence.

The installed APK and extracted ELF identities are exact. Runtime byte reads
were not completed because ptrace attach was blocked before the read-only
probe. Runtime identity is therefore probable from exact package ownership and
mapping evidence, but the stricter byte-correlation gate remains unknown.

```text
LIBUE4_RUNTIME_IDENTITY_VALID = PROBABLE
LIBGCLOUD_RUNTIME_IDENTITY_VALID = PROBABLE
LIBUE4_LOAD_BIAS_RESOLVED = YES_LOCAL_ONLY
LIBGCLOUD_LOAD_BIAS_RESOLVED = YES_LOCAL_ONLY
CALLSITE_RUNTIME_BYTES_MATCH = NOT_READ
PLT_RUNTIME_BYTES_MATCH = NOT_READ
CREATEDOLPHIN_RUNTIME_BYTES_MATCH = NOT_READ
```
