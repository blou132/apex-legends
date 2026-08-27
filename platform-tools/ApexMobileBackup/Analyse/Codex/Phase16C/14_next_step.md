# Next step

Do not unlock, root, flash, disassemble, or launch Apex yet.

The next bounded phase should be a PC-only firmware and recovery-material
validation:

- obtain the exact C33 package from an auditable source;
- hash it and confirm exact model/CUST/build metadata;
- extract stock kernel, ramdisk, and recovery ramdisk;
- verify the images structurally without patching them;
- establish a stock restoration procedure and verify an exact board/testpoint
  reference.

If exact recovery material or board reference cannot be established, retain
`ROOT_PREPARATION_GATE = NO_GO`. If they are established, conduct another
read-only readiness review before requesting any destructive authorization.
