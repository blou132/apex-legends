# Phase15Q - Isolate host-GPU SystemUI ANR on disposable AVD

Date: 2026-08-20

## Executive result

The mandatory ADB inventory found no physical Android device, WHPX was usable,
and exactly three sequential boots used only the disposable
`ApexGraphicsProbe` AVD. No application was launched, network state was not
changed, and `ApexPhase9Lab` remained untouched.

SystemUI ANRs occurred in all three cases: `host + read-only` at `30.043 s`,
`host + writable` at `30.114 s`, and `auto + read-only` at `60.173 s`.
ActivityManager directly identifies service-execution timeouts: KeyguardService
waited `20053/20259 ms` in the host cases, while SystemUIService waited
`20515 ms` in the auto control.

Renderer selection was correct: Radeon-backed GLES 3.1 for both host cases and
SwiftShader GLES 3.0 for auto. No host log exposes context loss, device
loss/reset, swap failure, or EGL/OpenGL failure. All detailed ANR records show
high SurfaceFlinger kernel CPU, but the auto failure prevents assigning the root
cause specifically to host rendering.

The matrix rejects read-only interaction, host rendering, and preserved Apex
userdata as sole causes. It supports broader system-image/SystemUI startup
instability in this environment. The immediate ANR reason is resolved, while
the deeper source of CPU saturation remains unknown.

## Required result

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED WHPX(10.0.26200) INSTALLED_AND_USABLE; EXIT_CODE_0
TEST_AVD = ApexGraphicsProbe

HOST_READONLY_BOOTED = YES
HOST_READONLY_SYSTEMUI_STABLE = NO
HOST_READONLY_ANR_TIME = 30.043 s

HOST_WRITABLE_BOOTED = YES
HOST_WRITABLE_SYSTEMUI_STABLE = NO
HOST_WRITABLE_ANR_TIME = 30.114 s

AUTO_READONLY_BOOTED = YES
AUTO_READONLY_SYSTEMUI_STABLE = NO
AUTO_READONLY_ANR_TIME = 60.173 s

ANR_REASON = CONFIRMED_SERVICE_EXECUTION_TIMEOUT
ANR_WAITING_COMPONENT = A/B KeyguardService; C SystemUIService
ANR_RENDERER_RELATED_EVIDENCE = CORRELATED_SURFACEFLINGER_KERNEL_CPU_ONLY_NO_DIRECT_FAULT
ANR_INPUT_TIMEOUT_EVIDENCE = NO
ANR_MAIN_THREAD_BLOCK_EVIDENCE = UNKNOWN_NO_THREAD_TRACE
HOST_RENDERER_ACTIVE = YES_BOTH_HOST_CASES
HOST_GPU_ERROR_NEAR_ANR = NO

READONLY_INTERACTION_SUSPECTED = NO
HOST_RENDERER_INSTABILITY_SUSPECTED = NO_AS_SOLE_CAUSE
APEXPHASE9LAB_STATE_SPECIFIC_SUSPECTED = NO
BROADER_SYSTEMUI_OR_IMAGE_INSTABILITY_SUSPECTED = YES

FRESH_HOST_RUNTIME_AVD_JUSTIFIED = NO_FOR_NOW

APEXPHASE9LAB_BOOTED = NO
APEX_INSTALLED_OR_LAUNCHED = NO
NETWORK_CHANGED = NO

ALL_EMULATORS_STOPPED = YES
FINAL_GATE = E EXACT_SYSTEMUI_ANR_REASON_RESOLVED
COMMIT = Isolate host SystemUI instability Phase15Q
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

The direct timeout reasons do not prove why SystemUI services stalled. The high
SurfaceFlinger kernel CPU is correlated across cases but is not a direct
renderer-fault witness. No Apex runtime conclusion is introduced by Phase15Q.
