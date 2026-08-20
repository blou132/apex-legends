# Binder and lock analysis

The exact main-thread stack contains no `BinderProxy.transact`, native Binder
wait, `waitForResponse`, or target component call. Binder pool threads are idle
in their normal driver thread-pool waits; they are not the main-thread chain.

No `waiting to lock` or lock-owner relation exists in the matching trace. The
main thread owns several `dagger.internal.DoubleCheck` monitors while creating
providers, but it is `Runnable`; ownership is not evidence of a contested lock
or circular dependency.

```text
SYSTEMUI_WAITING_ON_BINDER = NO
BINDER_TARGET_COMPONENT = NONE_PROVED
LOCK_WAIT_PRESENT = NO
LOCK_OWNER_THREAD = NONE
LOCKED_COMPONENT = NONE
DEADLOCK_EVIDENCE = NO
```
