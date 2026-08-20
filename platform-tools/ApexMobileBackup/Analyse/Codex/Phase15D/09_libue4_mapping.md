# libUE4 runtime status

The Android native loader reported two successful loads of the app-private
ARM64 `libUE4.so`. This is an unambiguous loader event and confirms that the
library was loaded during the observed run.

The one-shot `showmap` and `debuggerd -b` diagnostics were both denied before
they could expose a mapping or frame. Consequently:

```text
LIBUE4_LOADED = CONFIRMED
LIBUE4_MAPPING_FOUND = NO
LIBUE4_MAPPING_PATH_KIND = APP_PRIVATE_ARM64_LIBRARY
LIBUE4_MAPPING_RANGES_SANITIZED = NONE_AVAILABLE
LIBUE4_LOAD_BIAS = UNKNOWN
RUNTIME_ADDRESS_MODEL_VALIDATED = NO
```

The app-private loader path does not establish a runtime base. No address was
converted to Ghidra because a load bias and runtime PC model are unavailable.
