# Phase7C - Scope

Date: 2026-08-13.

Phase7C attempts a read-only recovery of the original Apex Mobile OBB files from the preserved phone. Allowed operations are limited to ADB discovery, read-only package/storage queries, and `adb pull` if the phone is reported as `device`.

No uninstall, data/cache clearing, root, unlock, flash, reinstall, push, delete, move, copy-on-device, permission change, or write command is allowed. Original phone files must never be modified.

ADB was found locally, but no device was detected. The phase stopped before package or storage queries, as required. No serial number, raw ADB log, OBB, PAK, Lua file, or device/account data is published.
