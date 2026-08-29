# Frida source and integrity

The selected release was official upstream Frida 17.17.0 for Android ARM64.
The release asset came from the `frida/frida` GitHub release and matched the
SHA256 digest published by GitHub for that asset.

```text
FRIDA_VERSION = 17.17.0
FRIDA_SERVER_SOURCE = OFFICIAL_GITHUB
FRIDA_SERVER_ARCH = ANDROID_ARM64
FRIDA_SERVER_ARCHIVE_SIZE = 16168344
FRIDA_SERVER_ARCHIVE_SHA256 = 09D1FAD867B27D69562A79289F4C412E85867F5D38AB72877036ED35E4223021
FRIDA_SERVER_BINARY_SIZE = 53539200
FRIDA_SERVER_BINARY_SHA256 = 55EF78C3F3E7A55122CA7E0051E2A356D0FF1D9744D84C1660291F90400588E7
HOST_FRIDA_VERSION = 17.17.0
HOST_FRIDA_TOOLS_VERSION = 14.10.4
```

The decompressed binary was ELF64 AArch64. Host packages were installed only
inside a gitignored Python 3.11 virtual environment. No executable or archive
is committed.
