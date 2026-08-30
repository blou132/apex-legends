# debuggerd test

The Android 8 build reported the supported syntax as `debuggerd [-b] PID`.
One disposable ARM64 loopback ping process was used. Root requested a bounded
backtrace only; debuggerd returned an ARM64 stack through `recvmsg`, ping's
main loop, ping main, libc initialization, and process start.

The disposable process remained alive after the dump and was then terminated
cleanly. This proves native stack observation only, not function-entry or
callback tracing.

```text
DEBUGGERD_TEST_SUPPORTED = YES
DEBUGGERD_TEST_PROCESS_STACK_OBTAINED = YES
```
