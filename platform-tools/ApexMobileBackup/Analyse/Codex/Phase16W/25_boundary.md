# Boundary

The analysis stops at the authorized static boundary:

- the callback register is identified;
- its exact client field source is identified;
- the provider persists and dispatches it;
- the concrete table installation remains before the readable frontier;
- only the known null initialization of callback `+0x08` is proven;
- neither callback owner nor `GameUpdateMgr+0x38` has a proven non-null writer.

No generic scan was used to force an answer.

```text
FUTURE_PTRACE_TRACE_GATE = NO_GO
CURRENT_BLOCKER = CONCRETE_CALLBACK_TABLE_INSTALLATION_AND_NON_NULL_OWNER_ASSIGNMENTS_REMAIN_BEFORE_THE_READABLE_FRONTIER
```
