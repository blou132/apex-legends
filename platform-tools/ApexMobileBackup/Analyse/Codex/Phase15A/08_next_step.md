# Next step

Stop Phase15A before any host modification.

After the user explicitly enables Windows Hypervisor Platform and reboots, a
separate phase should:

1. Run `emulator.exe -accel-check` and require a usable WHPX result.
2. Confirm no emulator instance is already running.
3. Boot the unchanged `ApexPhase9Lab` without installing or launching Apex.
4. Require Android boot completion, x86_64 ABI, and active
   `libndk_translation.so` before any APK/OBB operation is considered.

If acceleration still fails, stop and audit elevated BCD state and VirtualBox
coexistence before changing anything else. Do not fall back to the Huawei or
preserved Samsung.
