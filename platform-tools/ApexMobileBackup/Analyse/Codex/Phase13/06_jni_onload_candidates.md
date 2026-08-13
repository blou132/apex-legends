# Targeted JNI_OnLoad candidates

Each exported root was opened in its local Ghidra program with `-noanalysis
-readOnly`. Ghidra resolved exactly one imported function at the authoritative
ELF value. Only that function was decompiled. The slot indicators below are
direct observations in that root, not guessed API names for unrelated calls.

| Library | Ghidra address | GetEnv-like `+0x30` | FindClass-like second `+0x30` | RegisterNatives-like `+0x6b8` | Target strings |
|---|---:|---|---|---|---|
| `libCrashSight.so` | `0x129920` | no | no | no | none |
| `libGPM.so` | `0x1b4a8c` | yes | yes | yes | none |
| `libGVoice.so` | `0x215174` | yes | no | no | none |
| `libINTLTAB.so` | `0x10fe18` | no | no | no | none |
| `libMSDKCore.so` | `0x1ffb90` | yes | yes | no | none |
| `libTDataMaster.so` | `0x16af00` | yes | no | no | none |
| `libanogs.so` | `0x4e4784` | yes | yes | yes | none |
| `libanort.so` | `0x1a7788` | no | no | no | none |
| `libgcloud.so` | `0x32104c` | yes | yes | no | none |
| `libgcloudcore.so` | `0x189e74` | yes | yes | no | none |
| `libmmkv.so` | `0x10f7e0` | yes | yes | yes | none |
| `libtgpa.so` | `0x1083b8` | yes | yes | yes | none |

The Ghidra image base for these fresh ELF imports is `0x100000`, so each Ghidra
address equals its ELF value plus `0x100000`. Local raw decompilation was not
published.

Generic direct registration is confirmed in four SDK roots. It does not
register GameActivity: their complete library images lack the required class and
method strings. The other roots may delegate or perform SDK initialization, but
none has target identity evidence.
