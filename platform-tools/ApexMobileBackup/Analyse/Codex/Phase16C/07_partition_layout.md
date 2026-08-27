# EMUI 8 partition layout

Normal-boot read-only inspection of
`/dev/block/platform/hi_mci.0/by-name` confirmed these relevant names:

- `kernel`
- `ramdisk`
- `recovery_ramdisk`
- `erecovery_kernel`
- `erecovery_ramdisk`
- `erecovery_vbmeta`
- `erecovery_vendor`
- `recovery_vbmeta`
- `recovery_vendor`
- `system`
- `vendor`
- `frp`
- `fastboot`

No classic `boot` or `recovery` name appeared. This EMUI 8 layout separates
the normal kernel and ramdisk, and separates recovery ramdisk from its other
components.

No partition contents, block numbers, unique identifiers, or raw symlink
targets were published. No `dd`, readback, write, erase, or flash operation was
performed.
