# Native stack options

The future callback trace needs at least one exact native anchor. The available
production facilities cannot provide one:

| Required evidence | Classification | Reason |
|---|---|---|
| Native function call addresses | `NOT_SUPPORTED` | no permitted sampler or debugger |
| Indirect branch/call targets | `NOT_SUPPORTED` | no branch trace or register capture |
| Native stacks containing `libgcloud`/`libUE4` | `NOT_SUPPORTED` | `debuggerd` denied; sampler unavailable |
| Registers at known functions | `NOT_SUPPORTED` | requires prohibited debugger/ptrace class capability |
| Callback object/vtable address | `NOT_SUPPORTED` | requires memory/register observation |

`debuggerd -b` is not retried: Phase14 already performed the single authorized
test against this exact environment and received an OS process-dump failure
before any frame. A deliberate crash/tombstone is not a controlled trace method
and was not attempted.

```text
NATIVE_CALL_STACK_AVAILABLE = NO
```
