# Disposable test process

One host-backed `/system/bin/ping 127.0.0.1` process was started solely for the
capability test. Its runtime PID was used transiently and is not committed.

```text
TEST_PROCESS_NAME = ping
TEST_PROCESS_PID = SANITIZED_EPHEMERAL
TEST_PROCESS_TERMINATED = YES
```

No system service, Huawei service, Magisk process, personal application, or
Apex process was selected.
