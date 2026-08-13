# Next step

The installed Google Play image already declares the required `arm64-v8a` translation. No alternate system image is justified yet.

The next prerequisite is a usable x86_64 emulator hypervisor on this Windows host:

1. enable an official supported Windows hypervisor path, either Windows Hypervisor Platform or the Android Emulator hypervisor driver;
2. reboot if required by that host configuration;
3. run the emulator acceleration check again;
4. cold-boot the existing `ApexPhase9Lab` without Apex;
5. target only its `emulator-XXXX` endpoint and verify `ro.product.cpu.abilist` plus `ro.dalvik.vm.native.bridge`;
6. install the unchanged local APK only if `arm64-v8a` remains advertised;
7. stop on ABI mismatch or any need for an application bypass;
8. copy OBBs and block the lab's external network before the first client launch.

Do not download another multi-gigabyte image automatically and do not use the preserved phone as a runtime substitute.
