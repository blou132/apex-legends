# Cleanup actions

The two verified phone-side RAMDISK transfer copies were deleted from shared
storage. No other user file was removed.

```text
USER_STORAGE_FILES_REMOVED = 2
USER_STORAGE_BYTES_RECOVERED = 33603584
LOCAL_TMP_CLEANED = NO_PREEXISTING_ARTIFACT_REMOVED
LOCAL_TMP_BYTES_RECOVERED = 0
CACHE_BYTES_RECOVERED = 0
THIRD_PARTY_PACKAGES_REMOVED = 0
```

The measured free-space delta is slightly larger than the 33554432-byte file
payload because filesystem accounting changed during the measurement window.
No direct deletion occurred under system, vendor, product, app data, account,
or credential locations.
