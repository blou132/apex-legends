# microSD audit

The read-only storage-manager audit returned no disk and only the mounted
internal private/emulated volumes.

```text
MICROSD_PRESENT = NO
PORTABLE_STORAGE_PRESENT = NO
ADOPTABLE_STORAGE_SUPPORTED = NO
```

`sm has-adoptable` returned `false`. No card was formatted, partitioned,
mounted, or otherwise modified.
