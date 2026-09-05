# Known method convergence

The exact UClass identity and its `0x430` layout are compatible with the
already-known `UDolphinUpdater` methods and fields:

- `StartUpdate` at ELF `0x05a85d18`
- `OnSrcUpdateFinished` at ELF `0x05a89618`
- `OnUpdateSuccess` at ELF `0x05c38254`
- `Shutdown` at ELF `0x04712c6c`
- `OnNoticeInstallApk` at ELF `0x07ff68e8`
- `CheckUpdate` range ELF `0x05a2eb00..0x05a2fdbc`
- real `Init` call at ELF `0x05a2f0ac`

However, the opaque registration callback prevents an independent reflected
name/function-pointer convergence edge.

`REFLECTION_TO_KNOWN_DOLPHIN_METHOD_CONVERGENCE = LAYOUT_AND_CLASS_NAME_COMPATIBLE_ONLY`
