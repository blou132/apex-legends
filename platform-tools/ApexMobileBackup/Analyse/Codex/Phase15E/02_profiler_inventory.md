# Host profiler inventory

The Android SDK tree, command search path, common Android Studio locations, and
installed-application metadata were inspected read-only.

```text
SIMPLEPERF_AVAILABLE = CONFIRMED NO_HOST_TOOL
PERFETTO_HOST_TOOL_AVAILABLE = CONFIRMED NO
TRACE_PROCESSOR_AVAILABLE = CONFIRMED NO
ANDROID_STUDIO_PROFILER_TOOLING = CONFIRMED NO
```

No profiler was downloaded, installed, or run. Guest binaries were not audited
because Phase15E forbids booting the AVD.

Even if a host executable were later installed, the exact production package
does not opt into shell profileability and is not debuggable. The current
official non-root native profiling decision is therefore:

```text
NON_ROOT_NATIVE_PROFILING_POSSIBLE = NO_BY_APP_POLICY
```
