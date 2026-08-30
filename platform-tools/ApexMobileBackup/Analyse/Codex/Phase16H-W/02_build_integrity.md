# Build integrity

The exact committed Phase16H-V sources were built with official NDK r27d and
the previously validated flags:

```text
-O0 -fno-inline -fno-omit-frame-pointer -fPIE -pie -Wall -Wextra -Werror
```

Both outputs were ELF64 AArch64 PIE files. The visible `trace_target`, atomic
slot setter, and explicit disable symbols were present. Static review
reconfirmed the 24-byte slot-0 iovec, requested control `0x000001e5`, expected
cached readback `0x000041e4`, and absence of `PTRACE_POKE*` code.

Host and device SHA-256 values matched for both binaries. Exact hashes remain
in local raw evidence and are not needed in the public report.

- `TRACE_SOURCE_UNCHANGED = YES`
- `BUILD_SUCCESS = YES`
- `TRACEE_HASH_MATCH = YES`
- `TRACER_HASH_MATCH = YES`
