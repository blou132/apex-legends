# Phase16D scope

Date: 2026-08-28

This phase was limited to PC-side acquisition, static inspection, hashing, and
documentation for the exact PRA-LX1 C33 stock package. No device property read
was needed because Phase16C already contained the required sanitized model and
build evidence.

## Safety boundary

No phone was opened or modified. PotatoNV was not executed. There was no
testpoint contact, bootloader operation, flash, erase, wipe, Magisk patch,
TWRP installation, root operation, or Apex launch.

Firmware, extracted images, extraction tools, testpoint reference images, and
the archived PotatoNV release remain under gitignored `LocalInputs`. Only
cleaned metadata and conclusions are published.

## Authoritative target

```text
MODEL = PRA-LX1
SOC = HISILICON_KIRIN_655_HI6250
ANDROID = 8.0.0
EMUI = EMOTIONUI_8.0.0
FULL_BUILD = PRA-LX1 8.0.0.364(C33)
CUST_REGION = C33_ALTICE_ALL
BOOTLOADER_LOCKED = YES
```
