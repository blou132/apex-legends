# Trace-tool inventory

The stock Android command inventory was checked before deployment.

```text
BUILTIN_STRACE = NO
BUILTIN_GDB = NO
BUILTIN_GDBSERVER = NO
BUILTIN_LLDB_SERVER = NO
BUILTIN_SIMPLEPERF = NO
BUILTIN_PERF = NO
BUILTIN_DEBUGGERD = YES
```

Frida remained the single selected test candidate. No secondary invasive
framework, Magisk module, Zygisk component, policy softener, or SELinux change
was introduced.
