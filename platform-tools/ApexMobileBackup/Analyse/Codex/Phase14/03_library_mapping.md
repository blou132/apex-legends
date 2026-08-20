# Library mapping

`showmap` is not installed on the Huawei production image. The package is not
debuggable and the only permitted `debuggerd -b` request produced no frames.
Consequently, this run provides no legitimate mapping row for any of the 17 APK
libraries.

In particular, neither a `libUE4.so` frame nor a mapping record was obtained.
Phase13's `PROBABLE` load status is retained; it is not promoted or invalidated.
No first mapping address is treated as an ELF load bias.

```text
LIBUE4_MAPPED = UNKNOWN
LIBUE4_LOADED = PROBABLE
LIBUE4_LOAD_BASE = UNKNOWN
LIBUE4_MAP_METADATA = UNAVAILABLE
```
