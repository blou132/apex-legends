# Next authorized step

The exact base APK is now present in ignored local storage with its size, SHA256, and ARM64-only ABI recorded. Phase9 should resume only when the remaining prerequisite exists locally:

1. a secondary ARM64-compatible Android environment that is not the preserved phone.

Before launching, the lab must contain no historical private state or real account and must block the application from external Internet access while preserving only required local host/ADB connectivity.

The next run should proceed in this order:

1. verify package/build identity against the preserved OBB set inside the isolated lab;
2. launch once without hooks and retain raw logcat locally only;
3. stop if startup requires an authentication, anti-emulator, integrity, or encryption bypass;
4. if logcat is insufficient and local instrumentation is already available, derive runtime addresses from the actual `libUE4.so` load base and the Phase9 offset table;
5. capture only loader metadata: relative timestamp, thread, input module/path, transformed candidate, provider branch, final open path, result, size, and chunk name;
6. validate the chain with `ClientLaunch` before drawing conclusions about `EventSystem`;
7. inspect subscriber, parser, and URL construction only if the required Lua modules become legitimately accessible.

Do not use the original phone as a substitute laboratory and do not install large Android components automatically.
