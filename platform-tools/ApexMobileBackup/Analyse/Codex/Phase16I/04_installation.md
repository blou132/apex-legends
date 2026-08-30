# Offline installation

The exact APK installed successfully over USB without automatic permission
grants. Post-install package metadata matched the expected package, version,
and arm64 ABI. The package UID and randomized install path remain local-only.

The exact main and patch OBB files were restored to the package OBB directory.
Device-side SHA-256 verification matched both preserved inputs.

```text
APEX_INSTALL_RESULT = SUCCESS
INSTALLED_VERSION_EXACT = YES
PACKAGE_UID_RESOLVED = YES_LOCAL_ONLY
MAIN_OBB_DEVICE_MATCH = YES
PATCH_OBB_DEVICE_MATCH = YES
OBB_RESTORE_COMPLETE = YES
```
