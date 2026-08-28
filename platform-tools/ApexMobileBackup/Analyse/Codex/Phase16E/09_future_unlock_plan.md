# Phase16F design boundary

Phase16F requires separate explicit authorization because it would include
physical disassembly and destructive state transitions. Its bounded design is:

1. Repeat Git, device identity, battery, temperature, host power, and USB
   exclusivity checks.
2. Obtain final owner confirmation that all wanted userdata is backed up and a
   full wipe is accepted.
3. Open only the authorized PRA-LX1 using an approved physical procedure.
4. Photograph and confirm the actual board revision and every Phase16E
   landmark. Stop unless `TESTPOINT_BOARD_MATCH = CONFIRMED`.
5. Install or bind only the hash-pinned, signed Huawei VCOM driver if needed,
   and verify the exact bootrom enumeration before using PotatoNV.
6. Run only the pinned PotatoNV release with the preselected Kirin 65x (A)
   scope, under a separately reviewed state machine and explicit stop points.
7. Perform the bounded unlock-code operation and then the controlled bootloader
   unlock, treating it as a full userdata wipe.
8. Validate bootloader state, first boot, device identity, storage, and recovery
   availability, then stop.

Phase16F must not include Magisk, a patched RAMDISK, TWRP, application runtime
inspection, or any root workflow. Unlock and root remain separate failure
domains.

```text
NEXT_STEP = SEPARATE_EXPLICIT_AUTHORIZATION_FOR_PHASE16F_DESTRUCTIVE_UNLOCK
```
