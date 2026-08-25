# Phase15T scope

Phase15T performed a controlled storage cleanup on the existing Huawei PRA-LX1
lab device. Exactly one physical Android target was present. The target identity
matched `PRA-LX1`, Android 8.0/API 26, ARM64.

The phase was limited to Package Manager removal of individually proven
nonessential preinstalled consumer applications. It did not use raw filesystem
deletion, root, partition changes, a factory reset, or global cache clearing.

Apex was never launched, uninstalled, cleared, or modified. Its package, private
data, cache, and OBB directory were excluded from every cleanup action. Sensitive
device and account identifiers are omitted from all published artifacts.
