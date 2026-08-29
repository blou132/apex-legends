# Magisk APK integrity

The GitHub release API returned exactly one `Magisk-v28.1.apk` asset. Its API
size and downloaded size both equal 11716982 bytes. Android build tools read
package `com.topjohnwu.magisk`, version `28.1`, code `28100`, and minimum SDK
23. `apksigner` validated v1 and v2 signatures with one John Wu certificate.

```text
MAGISK_APK_SHA256 = 8BFD3346B3DA5814F82EFF6F1B1B5FEDD0AD585F39A25709B23EB54AAC45691D
APK_SIGNATURE_VALID = YES
MAGISK_APP_INSTALLED = YES
INSTALLED_PACKAGE_VERSION = 28.1_28100
APK_COMMITTED = NO
```
