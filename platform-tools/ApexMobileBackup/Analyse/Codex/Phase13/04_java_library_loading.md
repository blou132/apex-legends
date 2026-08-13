# Java and manifest library loading

## GameActivity static initializer

DEX `classes.dex`, `GameActivity.<clinit>` at code item `0x59209c`, establishes
this order before the final UE4 load:

| DEX instruction | Mechanism | Library | Conditional |
|---:|---|---|---|
| `0x592206` | `loadPluginByReflection` | `TDataMaster` | reflection, System fallback |
| `0x592210` | `loadPluginByReflection` | `anogs` | reflection, System fallback |
| `0x59221a` | `loadPluginByReflection` | `GVoice` | reflection, System fallback |
| `0x592224` | `loadPluginByReflection` | `MSDKCore` | reflection, System fallback |
| `0x59222e` | `loadPluginByReflection` | `GPM` | reflection, System fallback |
| `0x592238` | `loadPluginByReflection` | `tgpa` | reflection, System fallback |
| `0x592242` | `loadPluginByReflection` | `gcloud` | reflection, System fallback |
| `0x592406` | `System.loadLibrary` | `gnustl_shared` | no local catch |
| `0x59240c` | `System.loadLibrary` | `gcloud` | caught if unavailable |
| `0x59243c` | `System.loadLibrary` | `gcloudcore` | caught if unavailable |
| `0x592466` | `System.loadLibrary` | `GVoice` | caught if unavailable |
| `0x592492` | `System.loadLibrary` | `UE4` | final direct load |

`GameActivity.loadPluginByReflection` at `0x5945a4` invokes
`com.gcore.gcloud.plugin.PluginUtils.loadLibrary(String)` and falls back to
`System.loadLibrary(String)` on reflection failure.

The register dataflow is stable: `v4` remains `gcloud`, `v1` remains `GVoice`,
and `v2` remains `UE4` through the corresponding direct calls.

## Other confirmed DEX loads

Static DEX sites also load `anort`, `anogs`, `GVoice`, `gcloudcore`,
`transceiver`, `TDataMaster`, `GPM`, `mmkv`, `INTLTAB`, and `CrashSight` from
their SDK/application classes. Some are class-initializer loads; MMKV and crash
handling also expose conditional loader paths. Neither `classes2.dex` nor the
searched startup classes add another exact GameActivity owner candidate.

## Manifest distinction

`android.app.lib_name=UE4` is `CONFIRMED` for the NativeActivity. It identifies
the main NativeActivity library. It does not, by itself, prove that
`nativeResumeMainInit` is exported by or registered from `libUE4.so`.

```text
GAMEACTIVITY_MAIN_LIBRARY = CONFIRMED UE4
JAVA_NATIVE_LIBRARIES_LOADED = CONFIRMED 15 distinct APK libraries
DYNAMIC_LIBRARY_LOAD_PATH = CONFIRMED Java reflection/System load paths;
                            UNKNOWN native dlopen startup path
```
