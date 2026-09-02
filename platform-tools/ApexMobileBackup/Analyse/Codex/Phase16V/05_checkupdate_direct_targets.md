# CheckUpdate direct targets

Four external `BL` instructions target the exact start of the CheckUpdate FDE.
All land at ELF `0x05a2eb00`, inside the validated opaque prefix.

| Source ELF | Caller ELF | Target ELF | Target class |
|---|---|---|---|
| `0x05a87b78` | `0x05a85d18` | `0x05a2eb00` | `OPAQUE_PREFIX` |
| `0x05a897ec` | `0x05a89618` | `0x05a2eb00` | `OPAQUE_PREFIX` |
| `0x05c38300` | `0x05c38254` | `0x05a2eb00` | `OPAQUE_PREFIX` |
| `0x07fbf14c` | `0x07fbf050` | `0x05a2eb00` | `OPAQUE_PREFIX` |

Branches originating inside the FDE were excluded.

```text
CHECKUPDATE_EXTERNAL_DIRECT_BRANCH_COUNT = 4
```
