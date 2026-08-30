# Official NDK and ptrace probe

No suitable Android NDK was initially installed. The independent attach branch
therefore used the official Google Android NDK r27d for Windows:

```text
NDK_REVISION = 27.3.13750724
NDK_ARCHIVE_SIZE = 781506724
NDK_ARCHIVE_SHA1 = 56607CBCCD3642D4A1991F6BB3114A00F884F426
CLANG_VERSION = 18.0.4
TARGET = aarch64-linux-android26
```

The committed sources implement a harmless sleeping tracee and a minimal
read-only probe. The probe performs only `PTRACE_ATTACH`, `waitpid`,
`PTRACE_GETREGSET` with `NT_PRSTATUS`, and `PTRACE_DETACH`. It does not write
memory, patch instructions, change registers, inject a library, or persist.

Build binaries and the NDK remain local-only.

```text
ANDROID_NDK_AVAILABLE = YES_OFFICIAL_R27D_27_3_13750724
```
