# Explicit disable redesign

Disable is a separate 24-byte slot-0 SETREGSET containing the same address and
control zero. Its success criterion is the SETREGSET return value, not address
readback equality.

GETREGSET after disable is retained only as diagnostic output. This matches
the resolved Huawei semantics: disabling the perf event is the safety action,
while cached architecture fields may not serialize as a zeroed slot.

After successful disable, the future test resumes the disposable tracee for a
second function call and requires a controlled SIGSTOP handshake with no
SIGTRAP. Only then may it report `HW_BREAK_DISABLED_BY_PROVEN_PATH=YES` and
detach.

Result: `DISABLE_ADDRESS_READBACK_REQUIRED = NO`.
