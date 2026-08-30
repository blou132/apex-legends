# Huawei control encoding

With `CONFIG_HAVE_HW_BREAKPOINT_ADDR_MASK`, the PRA and Lineage trees encode:

| Field | Bits |
|---|---:|
| `enabled` | 0 |
| `privilege` | 2:1 |
| `type` | 4:3 |
| `len` | 12:5 |
| `ssc` | 15:14 |
| `mask` | 28:24 |

PRA source: `arch/arm64/include/asm/hw_breakpoint.h:21-67`. It also defines
`AARCH64_BREAKPOINT_EL0=2`, `ARM_BREAKPOINT_EXECUTE=0`,
`ARM_BREAKPOINT_LEN_4=0x0f`, and `ARM_SSC_NON_SECURE=1` at lines 70-96.

## Explicit decode

| Value | enabled | privilege | type | len | ssc | mask |
|---|---:|---:|---:|---:|---:|---:|
| `0x000001e5` | 1 | 2 (`EL0`) | 0 (`EXECUTE`) | `0x0f` (4 bytes) | 0 | 0 |
| `0x000041e4` | 0 | 2 (`EL0`) | 0 (`EXECUTE`) | `0x0f` (4 bytes) | 1 (`NON_SECURE`) | 0 |

The transformation is field-based, not a simple arithmetic addition:

```text
readback = (requested & ~0x1) | (ARM_SSC_NON_SECURE << 14)
         = (0x000001e5 & ~1) | 0x00004000
         = 0x000041e4
```

```text
REQUESTED_CTRL_DECODE = ENABLED_EL0_EXECUTE_LEN4_SSC0_MASK0
READBACK_CTRL_DECODE = DISABLED_CACHED_EL0_EXECUTE_LEN4_SSC_NON_SECURE_MASK0
```
