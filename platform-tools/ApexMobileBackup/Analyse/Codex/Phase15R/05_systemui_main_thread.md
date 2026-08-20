# SystemUI main thread

At the exact ANR trace, the SystemUI main thread is `Runnable`. It is executing
ordinary Dagger object construction while `SystemUIService.onCreate` starts
SystemUI services:

```text
PositionState.<init>
NoOpPosturingRepository.<init>
Dagger...SwitchingProvider.get17
dagger.internal.DoubleCheck.get
SystemUIApplicationImpl.startServicesIfNeeded
SystemUIApplicationImpl.startSystemUserServicesIfNeeded
SystemUIService.onCreate
ActivityThread.handleCreateService
```

The top frame is `PositionState.<init>`. No Binder transaction, condition wait,
sleep, renderer wait, or monitor acquisition appears on this stack.

The thread's exact schedstat records about `2.780 s` of runtime and `16.950 s`
of scheduler wait. This large run-queue delay, combined with the CPU pressure
record, classifies the thread as runnable work delayed by CPU contention rather
than internally blocked.

```text
SYSTEMUI_MAIN_THREAD_STATE = RUNNABLE
SYSTEMUI_MAIN_TOP_FRAME = com.android.systemui.communal.posturing.data.model.PositionState.<init>
SYSTEMUI_MAIN_BLOCKING_CHAIN = SystemUIService.onCreate -> SystemUIApplicationImpl.startSystemUserServicesIfNeeded -> startServicesIfNeeded -> Dagger provider construction -> NoOpPosturingRepository.<init> -> PositionState.<init>
MAIN_THREAD_CLASSIFICATION = CPU_BOUND_UNDER_SCHEDULER_CONTENTION
```
