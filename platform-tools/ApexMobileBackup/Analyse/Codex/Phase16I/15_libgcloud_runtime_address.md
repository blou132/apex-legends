# Runtime libgcloud address

The active-launch watcher found the package-owned `libgcloud.so` executable map
at file offset zero. This matched the exact ELF executable `PT_LOAD`, so the map
start was the fresh load bias. The absolute ASLR value remains local-only.

The tracer computed:

```text
runtime target = fresh load bias + ELF VA 0x00476180
```

The result was aligned and inside the executable mapping. Two bounded
`PTRACE_PEEKDATA` reads matched the exact 16-byte local function-entry
signature before active programming.

```text
LIBGCLOUD_RUNTIME_IDENTITY_VALID = YES
LIBGCLOUD_LOAD_BIAS_RESOLVED = YES_LOCAL_ONLY
CVERSIONMGR_INIT_RUNTIME_VALIDATED = YES
```
