# Tracer build review

The committed source was built with official Android NDK r27d
(`27.3.13750724`), Clang 18, target `aarch64-linux-android26`, PIE flags, and
`-Wall -Wextra -Werror`.

The result was ELF64 AArch64 PIE. Local and device binary hashes matched before
execution. Static source review found:

- one active `0x000001e5` programming call site;
- no `PTRACE_POKETEXT` or `PTRACE_POKEDATA`;
- no remote mmap, process-memory write, `dlopen`, injection, or patching;
- read-only `PTRACE_PEEKDATA` limited to entry validation and callback words.

Compiled binaries, disassembly, and hashes remain local-only.

```text
APEX_TRACER_BUILD_VALID = YES
APEX_TRACER_READ_ONLY_MEMORY_INSPECTION = YES
NO_CODE_PATCHING = YES
```
