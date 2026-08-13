# Native dependency graph

## APK-internal DT_NEEDED edges

```text
libUE4.so
  -> libgnustl_shared.so
  -> libanogs.so
  -> libgcloudcore.so
  -> libgcloud.so
  -> libGPM.so
  -> libGVoice.so
  -> libINTLTAB.so
  -> libMSDKCore.so
  -> libzip.so
  -> libzlib.so
  -> libTDataMaster.so
  -> libanort.so

libINTLTAB.so  -> libTDataMaster.so
libMSDKCore.so -> libTDataMaster.so
```

The remaining edges target Android/system libraries. `libUE4.so` has 12 direct
APK-internal dependencies. It does not directly depend on `libCrashSight.so`,
`libmmkv.so`, `libtgpa.so`, or `libtransceiver.so`.
No other APK library has `libUE4.so` in `DT_NEEDED`; its internal reverse
dependency set is empty.

## Interpretation

- `CONFIRMED`: the graph above is static `DT_NEEDED`, not runtime `dlopen`.
- `CONFIRMED`: GameActivity separately initiates Java/plugin loads, including
  libraries outside this graph.
- `CONFIRMED`: `dlopen` is a dynamic import of `libCrashSight.so`, `libGPM.so`,
  `libGVoice.so`, `libUE4.so`, `libanogs.so`, `libanort.so`, and `libmmkv.so`.
- `UNKNOWN`: no targeted startup call path to those native `dlopen` imports was
  resolved. No library imports `android_dlopen_ext`.

The complete internal edge list and external dependencies are in
`output/native_dependency_graph.json` and the inventory JSON.
