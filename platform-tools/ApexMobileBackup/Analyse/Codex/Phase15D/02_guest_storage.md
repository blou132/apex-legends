# Guest storage

Before installation, both `/data` and shared storage reported `11242712 KiB`
available. This exceeded the combined APK/OBB payload plus a reasonable install
and extraction margin, so the storage gate passed.

After APK and OBB installation, both views reported `6886840 KiB` available.
No AVD resize, wipe, or storage-policy bypass was used.

```text
GUEST_FREE_SPACE = SUFFICIENT
AVAILABLE_BEFORE_KIB = 11242712
AVAILABLE_AFTER_KIB = 6886840
```
