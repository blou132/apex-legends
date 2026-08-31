# Future active trace gate

Thread selection is now high confidence, but the required precondition is not
met: there is no reproducible strategy that guarantees the client will call
`GCloudDolphinImp::Init` after attachment without resetting or manipulating
state. Phase16I already installed the exact breakpoint early on Thread-10 and
the branch did not occur.

```text
FUTURE_ACTIVE_TRACE_GATE = NO_GO
FUTURE_TRACE_TRIGGER_STRATEGY = NONE_UNTIL_DOLPHIN_BOOTSTRAP_SELECTION_IS_RESOLVED
```

No future phase should clear AppData or reinstall without separate owner
authorization and stronger evidence that fresh state is required.
