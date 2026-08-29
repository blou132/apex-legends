# Phase16F-2 scope

Date: 2026-08-29

The owner explicitly authorized one PRA-LX1 bootrom-entry attempt, the pinned
PotatoNV operation, and a destructive Huawei bootloader-unlock command with a
full userdata wipe accepted. The single physical attempt was consumed.

The scope excluded Samsung, repeated testpoint attempts, alternate profiles,
FBLOCK disabling, Magisk, root, patched images, TWRP, Apex launch, and any
unplanned recovery action. Stock fastboot first rejected the unlock because OEM
unlock/FRP permission was not enabled. The phase stopped safely. The owner then
enabled the normal Developer-options control and separately authorized one new
fastboot unlock command, explicitly without another testpoint or PotatoNV run.
That command was confirmed on-device and completed the authorized wipe.

Raw logs, the unlock code, and the current board photo remain local-only. No
serial, IMEI, Android ID, Board ID, MAC address, account data, token, unlock
code, raw USB instance ID, or raw photo is committed.
