# Native capability assessment

Confirmed capabilities:

- root can read protected process mappings;
- built-in debuggerd can obtain a bounded stack from a disposable ARM64 task;
- an independent root probe can attach, wait for stop, read `NT_PRSTATUS`, and
  detach cleanly while SELinux remains enforcing.

Frida 17.17.0 is unusable because it crashes during startup. Frida 17.5.2 is
also unusable because its write-based ptrace injection primitive returns EIO.
The successful independent read-only probe shows that generic root ptrace is
not wholly blocked; it does not contradict the failure of Frida's different,
write-based mechanism.

```text
ROOT_NATIVE_ATTACH_CAPABILITY = YES
ROOT_CALLBACK_TRACE_PRECONDITION = PROBABLE_NOT_VALIDATED
```
