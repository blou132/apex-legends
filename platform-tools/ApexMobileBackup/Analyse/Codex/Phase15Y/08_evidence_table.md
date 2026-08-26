# Evidence table

| Claim | Evidence | Classification | Confidence |
| --- | --- | --- | --- |
| Puffer takes the timeout branch | exact `connect server timeout` followed by `70254639` | runtime direct | confirmed |
| `70254639` equals `0x0430002f` | exact integer conversion and Phase15V branch table | deterministic | confirmed |
| Dolphin emits an update failure | `UpdateResult`, code `154140714`, result `-1` | runtime direct | confirmed |
| the Dolphin manager is named | `version_mgr_imp.cpp::Init` and `dolphin::gcloud_version_action_imp` | runtime direct | confirmed |
| `ProcessActionError` has a binary owner | `libgcloud.so` Ghidra `0x005be71c` | static exact | confirmed |
| a Puffer/QTCVFS callback exists | `VFS_Puffer_OnUpdateResult`, Ghidra `0x005dfe58` | static exact | confirmed |
| the Phase15W external callback is identified | no runtime class/registration and no direct x2 object edge | negative boundary | unknown |
| Dolphin constructs `I54140714` | timing and numeric suffix only; no formatter/direct edge | correlation only | not confirmed |
| Puffer directly reaches Dolphin manager | no direct runtime or static edge | negative boundary | not confirmed |
