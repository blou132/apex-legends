# Phase15R - SystemUI ANR blocking-stack resolution

Date: 2026-08-20

## Executive result

The mandatory ADB inventory found no physical device, WHPX was usable, and the
single required `auto + writable` run used only `ApexGraphicsProbe`. The AVD
booted, no application was launched, and a visible SystemUI ANR appeared at the
`+30.265 s` checkpoint. The exact event was emitted at `+15.813 s` and reports
`KeyguardService` waiting `20131 ms`.

One valid local-only bugreport provides the exact matching SystemUI trace. The
main thread is `Runnable` in Dagger construction called from
`SystemUIService.onCreate`; it is not waiting on Binder, a lock, a condition, or
SurfaceFlinger. `KeyguardService` has not reached its lifecycle callback because
its create/bind work is queued behind this prolonged startup on the same main
thread.

The mechanism is CPU starvation: the main thread records about `2.780 s` of
runtime versus `16.950 s` of scheduler wait, while total guest CPU reaches
`96-97%` with `82-83%` kernel CPU. SurfaceFlinger is the largest explicit
consumer (`94%`, with RenderEngine at `78%` in a focused interval), but there is
no direct SystemUI-to-SurfaceFlinger wait edge. Memory, swap, Binder, lock, OOM,
and watchdog evidence do not explain the timeout.

This closes the fourth matrix quadrant. The current Android 36.1 Google Play
image/environment reproduces SystemUI service ANRs across host/readonly,
host/writable, auto/readonly, and auto/writable without Apex. The startup
blocking mechanism is resolved; the deeper reason for the image/emulator's
kernel CPU behavior remains unknown. Testing a different disposable system
image is now justified, but none is currently installed and none was downloaded.

## Required result

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED WHPX(10.0.26200) INSTALLED_AND_USABLE; EXIT_CODE_0

SYSTEM_IMAGE = system-images;android-36.1;google_apis_playstore;x86_64
GUEST_VCPUS = 4
GUEST_RAM = 3072 MB

AUTO_WRITABLE_BOOTED = YES
AUTO_WRITABLE_SYSTEMUI_STABLE = NO
AUTO_WRITABLE_ANR_TIME = +30.265 s FIRST_VISIBLE_CHECKPOINT; +15.813 s EVENT

BUGREPORT_CAPTURED = YES
SYSTEMUI_ANR_TRACE_PRESENT = YES

ANR_REASON = executing service com.android.systemui/.keyguard.KeyguardService, waited 20131ms
ANR_SERVICE = com.android.systemui/.keyguard.KeyguardService

SYSTEMUI_MAIN_THREAD_STATE = RUNNABLE
SYSTEMUI_MAIN_TOP_FRAME = com.android.systemui.communal.posturing.data.model.PositionState.<init>
SYSTEMUI_MAIN_BLOCKING_CHAIN = SystemUIService.onCreate -> SystemUIApplicationImpl.startSystemUserServicesIfNeeded -> startServicesIfNeeded -> Dagger provider construction -> NoOpPosturingRepository.<init> -> PositionState.<init>

SERVICE_EXECUTION_ENTRY = KEYGUARDSERVICE_CALLBACK_NOT_REACHED; SYSTEMUISERVICE.ONCREATE_ACTIVE
SERVICE_EXECUTION_BLOCKING_FRAME = NONE; CURRENT_FRAME=PositionState.<init> DURING DAGGER PROVIDER CONSTRUCTION

SYSTEMUI_WAITING_ON_BINDER = NO
BINDER_TARGET_COMPONENT = NONE_PROVED

LOCK_WAIT_PRESENT = NO
LOCK_OWNER_THREAD = NONE

SURFACEFLINGER_HIGH_CPU_CONFIRMED = YES
SURFACEFLINGER_BUSY_THREAD = RenderEngine
SYSTEMUI_DIRECTLY_WAITS_ON_SURFACEFLINGER = NO

SYSTEM_SERVER_HEALTH = RESPONSIVE_HIGH_CPU
SYSTEM_SERVER_WAITING_FOR_SYSTEMUI = YES_ASYNC_SERVICE_COMPLETION_PENDING; NO_SYNC_BLOCK_PROVED

TOTAL_GUEST_CPU_SATURATION = YES
KERNEL_CPU_DOMINANT = YES
MEMORY_PRESSURE = NO
SWAP_PRESSURE = NO

IMMEDIATE_CAUSE = SERVICE_EXECUTION_TIMEOUT
BLOCKING_MECHANISM = SYSTEMUI_MAIN_RUNNABLE_BUT_CPU_STARVED_DURING_SYSTEMUISERVICE_STARTUP
RESOURCE_CAUSE = CPU_SCHEDULER_CONTENTION_DOMINATED_BY_KERNEL_WORK; SURFACEFLINGER_RENDERENGINE_TOP_EXPLICIT_CONSUMER
DEEP_ROOT_CAUSE = UNKNOWN

GENERAL_SYSTEM_IMAGE_ENVIRONMENT_INSTABILITY = STRONGLY_SUPPORTED
ALTERNATE_LOCAL_IMAGES = []
ALTERNATE_SYSTEM_IMAGE_TEST_JUSTIFIED = YES

APEXPHASE9LAB_BOOTED = NO
APEX_INSTALLED_OR_LAUNCHED = NO
ALL_EMULATORS_STOPPED = YES

FINAL_GATE = B SYSTEMUI_BLOCKING_STACK_RESOLVED_ROOT_CAUSE_UNKNOWN
COMMIT = Resolve SystemUI ANR blocking stack Phase15R
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

The exact trace proves a runnable, scheduler-delayed SystemUI startup path and
disproves a direct Binder, lock, or renderer wait in that snapshot. It does not
prove whether the unresolved deep cause belongs specifically to the Android
36.1 image, emulator implementation, host scheduling, or an interaction among
them. No Apex runtime conclusion is introduced.
