# Exact static anchor follow-up

Because Phase15U exposed new exact names, the existing Ghidra projects were
searched for only `GemReportHelper`, `UpdateResult`, `version_mgr_imp.cpp`,
`GcloudDolphinVersionAction.cpp`, `ProcessActionError`, and `action_mgr.cpp`.
Direct owners and references were inspected; no general update/Puffer scan was
performed.

## Exact matches

- `libUE4.so`: zero hits for all six terms.
- `libgcloud.so`: exact source anchors for `version_mgr_imp.cpp` and
  `GcloudDolphinVersionAction.cpp`.
- `ProcessActionError` has a direct owner at Ghidra `0x005be71c`
  (`FUN_005be71c`, ELF VA `0x004be71c`).
- `UpdateResult` occurs in exact preserved labels `VFS_OnUpdateResult` and
  `VFS_Puffer_OnUpdateResult`.
- `VFS_Puffer_OnUpdateResult` is owned by `FUN_005dfe58` at Ghidra
  `0x005dfe58` (ELF VA `0x004dfe58`). Its source metadata is
  `puffer_callback_qtcvfs.cpp`.

`FUN_005e0124` registers `FUN_005dfe58` and a companion callback with a bounded
registration routine. On failure, `FUN_005dfe58` preserves the original Puffer
code for one internal update, remaps it into the QTCVFS domain, then reaches
`SetInitReturn` at `FUN_005e1494`. `SetInitReturn` calls slot `+0x10` on a
callback stored at offset `+0x20` in the QTCVFS manager.

This is an exact SDK callback boundary, but no evidence proves that this
QTCVFS callback object is the application object supplied as
`GCloudPufferImp::Init` argument `x2`. It therefore does not resolve the
Phase15X external client class or slot target.

```text
NEW_RUNTIME_ANCHOR = GCLOUD_DOLPHIN_VERSION_MANAGER_AND_UPDATERESULT_EVENT
ANCHOR_BINARY = libgcloud.so
ANCHOR_GHIDRA_ADDRESS = 0x005be71c; 0x005dfe58
ANCHOR_FUNCTION = FUN_005be71c (ProcessActionError); FUN_005dfe58 (VFS_Puffer_OnUpdateResult)
ANCHOR_DIRECT_XREFS = EXACT_DIRECT_OWNERS_AND_ONE_LEVEL_REFERENCES_ONLY
CLIENT_UPDATE_MANAGER = dolphin::gcloud_version_action_imp / GCloud Dolphin version manager
CLIENT_UPDATE_MANAGER_FUNCTION = NormalConnectVersionSvr; ProcessActionError
PUFFER_TO_UPDATE_MANAGER_DIRECT_EDGE = NO
```
