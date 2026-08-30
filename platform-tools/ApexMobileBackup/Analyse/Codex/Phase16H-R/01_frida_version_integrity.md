# Frida version integrity

The first diagnostic used only official Frida 17.17.0 Android ARM64 material.
The host core and server both reported 17.17.0. The isolated host environment
used frida-tools 14.10.4.

The official compressed archive was 16168344 bytes with SHA256:

`09D1FAD867B27D69562A79289F4C412E85867F5D38AB72877036ED35E4223021`

The extracted server was 53539200 bytes with SHA256:

`55EF78C3F3E7A55122CA7E0051E2A356D0FF1D9744D84C1660291F90400588E7`

Host, local binary, and deployed binary checks matched. The ELF architecture
was AArch64.

```text
FRIDA_CORE_VERSION_MATCH = YES
FRIDA_SERVER_HASH_MATCH = YES
FRIDA_SERVER_ARCH = ANDROID_ARM64
```
