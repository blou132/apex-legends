# APK installation test

## Result

```text
APK_INSTALL_RESULT = NOT_ATTEMPTED_LAB_NOT_BOOTED
```

The local base APK remained unchanged and ignored by Git:

- size: `96,228,800` bytes
- SHA256: `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0`
- native ABI: `arm64-v8a` only

Installation was gated on a boot-complete emulator and runtime ABI/native-bridge properties. Since the emulator never exposed an ADB endpoint, no install command was issued and no install result was inferred.

No APK patch, repack, signature change, or ABI modification was attempted.
