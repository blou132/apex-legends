# Package inventory

## Complete inventory summary

The initial Package Manager inventory contained 166 packages: 165 system
packages and one third-party package. The only third-party package was Apex.
Eighteen system packages had updated code under `/data/app`; these were the
relevant reclaim candidates. Package paths are described by storage class only
so randomized device paths are not published.

After cleanup, 158 packages remained installed for user 0: 157 system packages
and Apex. Exactly eight system applications were absent from user 0.

## Relevant initial packages

`App size` and `cache` are the aggregate values reported by Android 8
`dumpsys diskstats`; the production shell cannot reliably split code, compiled
artifacts, and private data. A nonzero user-data inode was present for each
updated application listed below.

| Package | App size | Cache | Classification | Result |
| --- | ---: | ---: | --- | --- |
| `com.google.android.gms` | 712.04 MiB | 272 KiB | PROTECTED | Preserved: Play Services compatibility stack |
| `com.google.android.googlequicksearchbox` | 655.83 MiB | 140 KiB | SAFE_NONESSENTIAL | Removed |
| `com.google.android.apps.photos` | 237.97 MiB | 40 KiB | SAFE_NONESSENTIAL | Removed; separate system gallery preserved |
| `com.google.android.apps.maps` | 200.94 MiB | 240 KiB | SAFE_NONESSENTIAL | Removed; location stacks preserved |
| `com.google.android.gm` | 189.54 MiB | 52 KiB | SAFE_NONESSENTIAL | Removed; separate system email client preserved |
| `com.google.android.apps.docs.editors.docs` | 160.50 MiB | 72 KiB | SAFE_NONESSENTIAL | Removed |
| `com.google.android.apps.docs.editors.sheets` | 160.40 MiB | 72 KiB | SAFE_NONESSENTIAL | Removed |
| `com.google.android.apps.tachyon` | 157.17 MiB | 40 KiB | SAFE_NONESSENTIAL | Removed; Android telephony stacks preserved |
| `com.android.vending` | 148.48 MiB | 176 KiB | PROTECTED | Preserved: Play Store compatibility stack |
| `com.google.android.tts` | 134.32 MiB | 40 KiB | PROBABLY_NONESSENTIAL | Not touched after stop gate |
| `com.google.android.youtube` | 126.05 MiB | 76 KiB | SAFE_NONESSENTIAL | Removed |
| `com.google.android.apps.docs` | 97.07 MiB | 16 KiB | PROBABLY_NONESSENTIAL | Not touched after stop gate |
| `com.touchtype.swiftkey` | 95.88 MiB | 16 KiB | PROTECTED | Preserved: sole enabled/default input method |
| `com.google.android.videos` | 69.24 MiB | 16 KiB | PROBABLY_NONESSENTIAL | Not touched after stop gate |
| `com.google.android.marvin.talkback` | 58.17 MiB | 40 KiB | PROTECTED | Preserved as accessibility capability |
| `com.huawei.systemmanager` | 53.12 MiB | 28 KiB | PROTECTED | Preserved as Huawei system-management component |
| `com.google.android.music` | 28.29 MiB | 16 KiB | PROBABLY_NONESSENTIAL | Not touched after stop gate |

The launcher, SystemUI, Settings, Package Installer, media/storage providers,
WebView, network, Wi-Fi, Bluetooth, keystore, certificate, Google framework,
Huawei framework, and Apex packages were protected without exception.
