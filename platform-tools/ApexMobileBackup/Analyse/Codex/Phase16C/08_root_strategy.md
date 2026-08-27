# Root strategy candidates

This is a design ranking, not authorization to execute.

| Rank | Candidate | Reversibility | Compatibility confidence | Risk | Root-shell potential |
| ---: | --- | --- | --- | --- | --- |
| 1 | Bootloader unlock plus Magisk-patched exact stock `RAMDISK.img` | Best if exact stock ramdisk is retained and tested | MEDIUM at layout level, LOW for exact C33 image today | HIGH | HIGH if successful |
| 2 | Bootloader unlock plus compatible TWRP, then Magisk | More moving parts and recovery replacement | LOW; no exact C33 recovery was validated | HIGH | HIGH if successful |
| 3 | Temporary recovery boot | Potentially reversible | UNKNOWN; support was not established for this Huawei fastboot | UNKNOWN | HIGH if actually supported |
| 4 | Other documented PRA-LX1 method | None accepted | No transparent, sufficiently verified alternative found | UNKNOWN/HIGH | UNKNOWN |

Opaque one-click root packages, KingRoot/KingoRoot, exploit bundles, FRP tools,
and unknown patched images are excluded.

Candidate 1 is the least complex future route, but it remains blocked until the
exact stock package and restore images are locally verified. Candidate 2 is not
a fallback for missing recovery evidence; it increases recovery risk. Candidate
3 must not be assumed supported without a read-only fastboot capability audit in
a separately authorized phase.
