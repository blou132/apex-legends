# Next step

Stop here. The next phase, if explicitly authorized, should begin with a
non-destructive preflight and should not assume that `GO` authorizes execution.

Required future checkpoints:

1. Reconfirm that no private data remains solely on the phone and accept a
   full userdata wipe.
2. Compare the exposed board silkscreen and geometry to the three archived
   references before any contact is attempted.
3. Verify the exact Windows VCOM driver archive and signatures before
   installation.
4. Rehash the official PotatoNV release and all exact C33 recovery artifacts.
5. Record the existing boot/FRP/FBLOCK state before allowing any change.
6. Define immediate abort conditions and a tested host-power/cable plan.
7. Obtain separate explicit authorization for disassembly and destructive
   bootloader work.

```text
NEXT_STEP = SEPARATE_EXPLICIT_AUTHORIZATION_FOR_PHYSICAL_PREFLIGHT_AND_CONTROLLED_UNLOCK_PHASE
```
