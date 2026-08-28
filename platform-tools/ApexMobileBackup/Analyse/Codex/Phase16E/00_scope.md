# Phase16E scope

Date: 2026-08-28

Phase16E is a read-only physical and host preflight for the authorized Huawei
PRA-LX1. It evaluates whether a later, separately authorized bootloader-unlock
phase can start with bounded risk.

This phase did not open the phone, touch a testpoint, install a driver, execute
PotatoNV, enter bootrom or fastboot, change FBLOCK, issue an unlock command,
flash or erase a partition, patch an image, install Magisk, root the device, or
wipe userdata. Apex was not launched.

Only normal ADB property and battery reads, Windows host inventory, offline
archive extraction, signature verification, hashing, and documentation were
performed. Raw local inputs remain under gitignored `LocalInputs`; no device
identifier, account data, token, signed temporary URL, binary, or proprietary
image is committed.

```text
MODEL = PRA-LX1
SOC = Kirin 655 / hi6250
BUILD = PRA-LX1 8.0.0.364(C33)
PHASE = PREFLIGHT_ONLY
```
