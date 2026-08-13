# JNI symbol and string scan

## Authoritative dynamic symbols

| Library | `JNI_OnLoad` dynsym index | ELF value | Full target export | Short export |
|---|---:|---:|---|---|
| `libCrashSight.so` | 248 | `0x29920` | no | no |
| `libGPM.so` | 937 | `0xb4a8c` | no | no |
| `libGVoice.so` | 1216 | `0x115174` | no | no |
| `libINTLTAB.so` | 77 | `0xfe18` | no | no |
| `libMSDKCore.so` | 577 | `0xffb90` | no | no |
| `libTDataMaster.so` | 178 | `0x6af00` | no | no |
| `libanogs.so` | 272 | `0x3e4784` | no | no |
| `libanort.so` | 127 | `0xa7788` | no | no |
| `libgcloud.so` | 482 | `0x22104c` | no | no |
| `libgcloudcore.so` | 1365 | `0x89e74` | no | no |
| `libmmkv.so` | 266 | `0xf7e0` | no | no |
| `libtgpa.so` | 94 | `0x83b8` | no | no |

All 12 rows are global function symbols. The other five libraries do not export
`JNI_OnLoad`. None of the 17 exports either
`Java_com_epicgames_ue4_GameActivity_nativeResumeMainInit` or
`nativeResumeMainInit`.

## Exact null-terminated strings

`libUE4.so` is the only library with target identity strings:

| String | File offset | Section | Classification |
|---|---:|---|---|
| full JNI name | `0x20dcc9d` | `.dynstr` | `STRING_ONLY` |
| `nativeResumeMainInit` | `0x20dccc1` | `.dynstr` | `STRING_ONLY` |
| `com/epicgames/ue4/GameActivity` | `0x21b2f21` | `.rodata` | `STRING_ONLY` |
| `()V` | `0x21c1087` | `.rodata` | signature witness only |

No library contains the exact dotted class name or exact `RegisterNatives`
string. The 12 exports naturally contain `JNI_OnLoad` in `.dynstr`; `libUE4.so`
has five string-only occurrences but no corresponding dynamic symbol.

`LIBUE4_FULL_NAME_WITNESS = CONFIRMED STRING_ONLY`. This does not establish
library ownership. Exact occurrence metadata is in `output/jni_symbol_scan.json`
and `output/native_library_inventory.json`.
