# Detach and post-detach health

PTRACE_DETACH was issued only after explicit disable and the controlled
no-retrap proof. It succeeded without signal injection.

The tracee resumed, completed another deterministic `trace_target` call, and
remained healthy for a bounded observation period. It was then terminated with
SIGTERM and acknowledged `TRACEE_TERMINATED_CLEANLY=YES`.

- `PTRACE_DETACH_RESULT = SUCCESS`
- `TRACEE_CONTINUED_AFTER_DETACH = YES`
- `POST_DETACH_FUNCTION_RESULT_CORRECT = YES`
- `TRACEE_TERMINATED = YES_CLEANLY`
- `TRACER_TERMINATED = YES`
