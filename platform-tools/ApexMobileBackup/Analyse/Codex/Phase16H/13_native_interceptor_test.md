# Native interceptor test

The prepared test script would have attached read-only observers to innocuous
libc calls used by loopback ping, without changing arguments, return values, or
memory. Because the Frida session could not attach, the script was never
injected and no hook was installed.

```text
FRIDA_NATIVE_INTERCEPTOR_ATTACH = NO_NOT_REACHED
FRIDA_NATIVE_CALL_OBSERVED = NO
FRIDA_CLEAN_DETACH = NOT_APPLICABLE_NO_SESSION
```

No replacement hook, patch, spawn injection, critical-process attach, or
policy change occurred.
