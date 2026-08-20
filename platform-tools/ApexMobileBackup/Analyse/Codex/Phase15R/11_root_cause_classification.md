# Root-cause classification

The exact failure layers are kept separate:

```text
IMMEDIATE_CAUSE = SERVICE_EXECUTION_TIMEOUT
BLOCKING_MECHANISM = SYSTEMUI_MAIN_RUNNABLE_BUT_CPU_STARVED_DURING_SYSTEMUISERVICE_STARTUP
RESOURCE_CAUSE = CPU_SCHEDULER_CONTENTION_DOMINATED_BY_KERNEL_WORK; SURFACEFLINGER_RENDERENGINE_TOP_EXPLICIT_CONSUMER
DEEP_ROOT_CAUSE = UNKNOWN
```

The direct evidence resolves why the Keyguard service missed its timeout: its
main-thread lifecycle work was queued behind prolonged SystemUIService startup,
and the runnable main thread accumulated about 16.95 seconds of scheduler wait
under 96-97% guest CPU use. It does not resolve why this Android image/emulator
combination drives so much kernel rendering and system startup CPU.

The result is not a Binder dependency, lock deadlock, memory shortage, or
proved direct SurfaceFlinger wait. The exact trace plus resource correlation
supports gate B rather than claiming a deeper renderer or host-hardware cause.

The fourth matrix quadrant now fails too. Across `host/readonly`,
`host/writable`, `auto/readonly`, and `auto/writable`, the current disposable
Android 36.1 environment produces SystemUI service ANRs without Apex.

```text
GENERAL_SYSTEM_IMAGE_ENVIRONMENT_INSTABILITY = STRONGLY_SUPPORTED
FINAL_GATE = B SYSTEMUI_BLOCKING_STACK_RESOLVED_ROOT_CAUSE_UNKNOWN
```
