# Next step

## Decision

```text
FINAL_GATE = E BOOTSTRAP_GATE_STILL_UNKNOWN
```

TDM, GCloud RemoteConfig, PlayCommon, and the OBB downloader are not the unresolved global gate. The last confirmed game transition is the successful return from `nativeResumeMainInit`; no later subsystem is identified.

## Safe next analysis

1. Statically resolve `Java_com_epicgames_ue4_GameActivity_nativeResumeMainInit` in the existing local `libUE4.so` Ghidra project.
2. Trace its state changes, worker/thread launch, and first waits or callbacks without performing global reanalysis.
3. Search the reached callgraph for existing Phase7/Phase8 Lua loader boundaries, `ClientLaunch`, and the native-to-Lua dispatcher.
4. If static analysis remains ambiguous, design a separate authorized local-only observation at that exact boundary. Do not answer TDM or GCloud, emulate auth, patch the APK, install a CA, or contact historical services.

```text
NEXT_BACKEND_TO_STUDY = UNKNOWN; no backend is justified before the native startup gate is resolved
```
