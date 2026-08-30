# Independent ptrace result

The official-NDK ARM64 tracee and probe were deployed temporarily. One root
attach cycle completed successfully:

```text
PTRACE_ATTACH_RESULT = SUCCESS
PTRACE_WAIT_STOP_RESULT = SUCCESS_STOP_SIGNAL_19
PTRACE_GETREGSET_RESULT = SUCCESS_272_BYTES
PTRACE_DETACH_RESULT = SUCCESS
```

After detach, the tracee was sleeping normally and reported `TracerPid: 0`.
It was then terminated cleanly. SELinux remained `Enforcing` throughout.

This proves generic root ARM64 attach, register-set observation, and clean
detach on a disposable process. It does not yet prove breakpoints, function
entry observation, or callback argument capture.
