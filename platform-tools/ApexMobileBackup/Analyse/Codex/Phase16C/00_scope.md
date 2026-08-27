# Phase16C scope

Date: 2026-08-27

## Purpose

Audit whether the expendable Huawei PRA-LX1 lab phone can be prepared for a
future root-enabled callback trace. This phase is preparation only.

## Authorized work

- Read-only ADB identity, property, package, storage, and partition-name checks.
- Read-only host inventory of preserved Apex artifacts and prerequisite tools.
- Static review of previous project evidence.
- Public documentation research, with upstream sources preferred.
- Cleaned Markdown and JSON reporting.

## Explicit exclusions

No bootloader mode was entered. No unlock, root, flash, erase, wipe, factory
reset, testpoint operation, FBLOCK change, Magisk/TWRP installation, Apex
launch, Apex uninstall, or Apex data/cache clear occurred. PotatoNV was not
downloaded or executed.

Exactly one physical Android target was visible and identified as `PRA-LX1`.
No Samsung was visible or accessed. Device serials and other unique identifiers
are excluded from all published output.

Raw device output, if retained by the operator, belongs under the ignored
`platform-tools/ApexMobileBackup/LocalInputs/Phase16C/` tree. It is not part of
this commit.
