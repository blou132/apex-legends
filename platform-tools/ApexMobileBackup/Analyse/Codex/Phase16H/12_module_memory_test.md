# Module and memory test

Root proc maps confirmed that `libc.so` was mapped in the test process, but the
Frida attach failed before JavaScript execution. Frida therefore did not
enumerate modules, resolve a runtime module base, or read memory.

```text
LIBC_MODULE_FOUND = YES_VIA_PROC_MAPS_ONLY
LIBC_BASE_RESOLVED = NO_NOT_REACHED_BY_FRIDA
FRIDA_MEMORY_READ = NO_NOT_REACHED
FRIDA_MEMORY_WRITE = NO
```

No durable ASLR address is recorded and no memory write was attempted.
