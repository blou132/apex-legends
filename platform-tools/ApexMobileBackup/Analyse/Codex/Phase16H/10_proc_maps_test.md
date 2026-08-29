# Root proc-maps test

Root successfully read `/proc/<test-pid>/maps` for the disposable ping process.
The bounded map showed the main `/system/bin/ping` executable, the 64-bit
linker, `libc.so`, and other mapped shared libraries.

```text
TEST_PROC_MAPS_READABLE = YES
TEST_MAIN_EXECUTABLE_FOUND = YES
TEST_LIBC_MAPPING_FOUND = YES
TEST_LINKER_MAPPING_FOUND = YES
```

The raw map and ASLR addresses remain local-only. This proves protected map
inspection, not Frida attachment or runtime interception.
