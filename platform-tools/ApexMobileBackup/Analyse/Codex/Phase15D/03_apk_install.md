# APK installation

The Apex package was absent before installation. The exact `base.apk` was
installed with the standard `adb install` operation and no policy, permission,
downgrade, test, or replacement flags.

Android returned `Success`. Package metadata then reported:

```text
PACKAGE = com.ea.gp.apexlegendsmobilefps
VERSION_NAME = 1.3.672.546
VERSION_CODE = 64003140
PRIMARY_CPU_ABI = arm64-v8a
SECONDARY_CPU_ABI = null
NATIVE_LIBRARY_DIR = APP_PRIVATE_ARM64_LIBRARY_DIR
APK_ARM64_ACCEPTED = YES
```

The randomized app-private installation path is intentionally not published.
Successful installation proves ABI acceptance, not by itself native execution.
