# Secondary offset control

The confirmed secondary ABI relationship remains:

- address point: ELF `0x0ae568b0`;
- offset-to-top: `-0x28`;
- expected destination in a top-object writer: `object+0x28`.

No secondary vptr store was recovered, so its actual store offset and ABI
match remain `UNKNOWN`.
