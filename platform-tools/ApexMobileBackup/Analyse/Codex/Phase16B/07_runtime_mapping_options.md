# Runtime mapping options

Runtime mapping bases would be required to convert a sampled PC into the
established ELF/Ghidra address model. No supported facility can provide those
mappings for the unchanged production package:

- `showmap` is absent;
- `debuggerd -b` is blocked by OS policy;
- `/proc/<pid>/maps` access for a non-debuggable process is guarded by the
  ptrace access policy despite shell's `readproc` group;
- no simpleperf/Perfetto process sampler is available or authorized;
- loader logs do not establish an address base.

```text
FUTURE_LIBGCLOUD_MAPPING_OBSERVABLE = NO
FUTURE_LIBUE4_MAPPING_OBSERVABLE = NO
```

Apex was not launched merely to test map access.
