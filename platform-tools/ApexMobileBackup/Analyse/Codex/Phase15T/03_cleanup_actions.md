# Cleanup actions

Each target was proven to be an updated, standalone consumer application before
removal. Apex-scoped package metadata contained no exact reference to any target.
The active Huawei launcher and all relevant fallback/platform components were
verified before their related consumer application was removed.

For every package, Phase15T used `pm uninstall --user 0` followed by the
supported global `pm uninstall` operation to discard the `/data` update while
leaving immutable factory partitions untouched.

| Package | Why safe on this lab | Free before | Free after | Observed gain |
| --- | --- | ---: | ---: | ---: |
| `com.google.android.googlequicksearchbox` | Search/Assistant client; Huawei launcher independent | 442932 KiB | 936696 KiB | 493764 KiB |
| `com.google.android.apps.photos` | Photo client; system gallery preserved | 936680 KiB | 1083400 KiB | 146720 KiB |
| `com.google.android.apps.maps` | Maps client; fused location and Play Services preserved | 1083404 KiB | 1166268 KiB | 82864 KiB |
| `com.google.android.gm` | Mail client; system email client preserved | 1166272 KiB | 1254784 KiB | 88512 KiB |
| `com.google.android.apps.docs.editors.docs` | Standalone document editor | 1254784 KiB | 1823320 KiB | 568536 KiB |
| `com.google.android.apps.docs.editors.sheets` | Standalone spreadsheet editor | 1823320 KiB | 1950564 KiB | 127244 KiB |
| `com.google.android.apps.tachyon` | Duo/Meet client; telephony and Telecom preserved | 1950564 KiB | 2044664 KiB | 94100 KiB |
| `com.google.android.youtube` | Standalone video client | 2044668 KiB | 2121704 KiB | 77036 KiB |

After every package, ADB returned successfully, SystemUI remained running,
`dumpsys window policy` completed, the Huawei launcher remained the HOME
resolver, Settings remained installed, storage remained mounted, Apex remained
installed and stopped, and both Apex OBB names and sizes remained exact.

No application cache was cleared separately. No additional candidate was
touched after the 2 GiB stop gate was reached.
