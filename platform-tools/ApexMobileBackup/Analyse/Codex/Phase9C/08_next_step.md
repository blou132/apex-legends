# Next step

The secondary Huawei is a viable ARM64 installation lab, but the first offline run stalls on a black screen after an expected backend/DNS failure and before any useful Lua-loader metadata appears.

Safe next actions are limited to non-bypass observation:

1. keep the preserved Samsung excluded;
2. retain the Huawei as a clean test installation with no real account or copied private state;
3. repeat a bounded no-hook run only with an explicitly designed lab network policy that cannot reach historical services;
4. collect only cleaned startup and loader metadata;
5. stop if progress requires root, bootloader unlock, APK patching, anti-debug, anti-integrity, authentication, certificate, or encryption bypass;
6. do not claim a `libUE4.so` base until a legitimate readable process mapping or explicit loader event proves it;
7. if the engine reaches ClientLaunch/EventSystem without a bypass, apply the corrected Phase9 ELF offsets only after validating the actual library load base.

No additional runtime action is justified from the current black-screen state alone.
