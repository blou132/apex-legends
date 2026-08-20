# ADB guest identity

Every guest command selected the discovered emulator endpoint explicitly. The
endpoint port is local-only and unnecessary for the published result.

```text
ANDROID_VERSION = 16
ANDROID_SDK = 36
GUEST_BUILD_TYPE = user
GUEST_RO_DEBUGGABLE = 0
GUEST_DEBUGGABLE = CONFIRMED NO
```

The guest remained ADB-visible throughout identity, ABI, native-bridge,
diagnostic, package, and stability checks. No physical endpoint appeared.
