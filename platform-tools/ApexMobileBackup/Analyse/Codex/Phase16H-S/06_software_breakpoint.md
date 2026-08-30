# Software breakpoint result

The software probe attached, observed the expected 272-byte general register
set, read the enclosing executable word, and confirmed the expected first
instruction. Its only `PTRACE_POKETEXT` call then failed with errno 5 (`EIO`).

No target byte changed, so instruction restoration and single-step were not
applicable. The probe detached cleanly. The tracee remained alive with
`TracerPid: 0` and continued reporting the correct `0x26664` result.

```text
PTRACE_ATTACH_RESULT = SUCCESS
ORIGINAL_WORD_SAVED = YES
SOFTWARE_BREAKPOINT_SUPPORTED = NO
BREAKPOINT_WRITE_RESULT = FAILURE_EIO
BREAKPOINT_READBACK_VALID = NOT_APPLICABLE_NO_WRITE
SIGTRAP_RECEIVED = NO_NOT_CONTINUED
PTRACE_DETACH_RESULT = SUCCESS
TRACEE_CONTINUED_AFTER_DETACH = YES
TRACEE_FUNCTION_RESULT_STILL_CORRECT = YES
SOFTWARE_FUNCTION_ENTRY_BREAKPOINT = FAILURE_WRITE_UNSUPPORTED
```
