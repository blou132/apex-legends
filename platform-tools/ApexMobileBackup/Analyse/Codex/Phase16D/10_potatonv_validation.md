# PotatoNV upstream validation

The current [PotatoNV upstream documentation](https://github.com/kitsuned/PotatoNV)
was rechecked without running the program.

## Compatibility

- Kirin 655 is explicitly included in the supported CPU list.
- Huawei P8 Lite (2017), model family `PRA`, is explicitly listed as tested
  with bootloader profile `Kirin 65x (A)`.
- [Upstream discussion 125](https://github.com/kitsuned/PotatoNV/discussions/125)
  has two independent PRA-LX1 users reporting success on Windows.
- The tool requires VCOM/download mode, normally reached by the physical
  testpoint, and expects `USB SER` or `HUAWEI USB COM 1.0` detection.
- PotatoNV uploads its board-software bootloader to RAM, enters temporary
  fastboot, writes the SHA256 form of the chosen unlock key to `USRKEY`, and
  can modify FBLOCK on supported Kirin 65x devices.
- Upstream states that `Disable FBLOCK` permits otherwise restricted secure
  partition writes and OEM commands. That increases recovery capability but
  also increases destructive risk.

## Archived official release

The latest upstream GitHub release was downloaded only for future integrity
comparison and was not executed.

```text
RELEASE = 2022.03
UI_VERSION = v2.2.1
RELEASE_TAG_COMMIT = e958ef8273382a5af56f2c65ada21eaf59a4b262
ARCHIVE = PotatoNV-next-v2.2.1_2022.03-x86.zip
ARCHIVE_BYTES = 6506263
ARCHIVE_SHA256 = 98344A77EEDDEE99F4CA145C586A6656B7F98DA0CC04BE1007DC102EC62AE416
EXECUTABLE_BYTES = 117760
EXECUTABLE_SHA256 = 2C1964BF2E4F8774E1688D01D140EF2BAD3CAF1077E17C6F7C27400F3C7707A7
EXECUTABLE_AUTHENTICODE = NOT_SIGNED
POTATONV_EXECUTED = NO
```

The unsigned executable is acceptable only as a hash-pinned artifact from the
official upstream GitHub release; it is not evidence of publisher signing.

```text
POTATONV_KIRIN655_SUPPORT = YES_UPSTREAM_DOCUMENTED
POTATONV_PRA_SUPPORT = YES_UPSTREAM_TESTED
POTATONV_PROFILE = KIRIN_65X_A
```
