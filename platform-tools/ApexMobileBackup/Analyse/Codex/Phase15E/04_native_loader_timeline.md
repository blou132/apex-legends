# Native loader timeline

Phase15C established the unchanged x86_64 guest and ARM64 native-bridge
configuration. The Phase15D process itself logs Berberis initialization at
`+0.134 s`. Every row below is an app-private ARM64 loader event occurring
after that direct bridge witness.

| Relative time | Library | Result | Native bridge indicator |
| ---: | --- | --- | --- |
| +1.663 s | `libanort.so` | OK | ARM64 app path after Berberis init |
| +2.929 s | `libINTLTAB.so` | OK | ARM64 app path after Berberis init |
| +3.336 s | `libgcloudcore.so` | OK | ARM64 app path after Berberis init |
| +3.509 s | `libTDataMaster.so` | OK | ARM64 app path after Berberis init |
| +3.692 s | `libanogs.so` | OK | ARM64 app path after Berberis init |
| +3.960 s | `libGVoice.so` | OK | ARM64 app path after Berberis init |
| +4.245 s | `libMSDKCore.so` | OK | ARM64 app path after Berberis init |
| +4.773 s | `libGPM.so` | OK | ARM64 app path after Berberis init |
| +4.798 s | `libtgpa.so` | OK | ARM64 app path after Berberis init |
| +4.921 s | `libgcloud.so` | OK | ARM64 app path after Berberis init |
| +4.932 s | `libgnustl_shared.so` | OK | ARM64 app path after Berberis init |
| +10.494 s | `libUE4.so` | OK | ARM64 app path after Berberis init |
| +10.572 s | `libUE4.so` | OK | Second loader call, same translated context |
| +11.523 s | `libCrashSight.so` | OK | ARM64 app path after Berberis init |

The sequence is compatible with the static Java load order reconstructed in
Phase13. Loader success confirms loading, not mapping ranges or a load bias.
