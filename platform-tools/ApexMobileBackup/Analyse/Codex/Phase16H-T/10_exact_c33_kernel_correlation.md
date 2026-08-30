# Exact C33 kernel correlation

## Container and identity

| Item | Result |
|---|---|
| Exact input size | 25,165,824 bytes |
| Exact input SHA256 | `6993D273EFCD35D91AC3E0029C9FA4E0BC42999F6BE835B35A92C8F2C670006C` |
| Payload | gzip at Huawei-container offset `0x1800` |
| Decompressed size | 34,625,856 bytes |
| Decompressed SHA256 | `1D47D981A2651DE2AE4254EDD4751BE068AC830D8DC5B194803001E9328B9049` |
| Image format | ARM64 Image, `ARMd` at header offset `0x38` |
| Kernel version | `Linux version 4.4.23+ ... #1 SMP PREEMPT Mon Mar 4 06:19:34 CST 2019` |

## Exact configuration

The embedded IKCONFIG directly contains:

```text
CONFIG_ARM64=y
CONFIG_ARCH_HISI=y
CONFIG_HISI_PARTITION_HI6250=y
CONFIG_HAVE_HW_BREAKPOINT=y
CONFIG_HAVE_HW_BREAKPOINT_ADDR_MASK=y
CONFIG_IKCONFIG=y
CONFIG_KALLSYMS=y
CONFIG_KALLSYMS_ALL=y
CONFIG_DEBUG_INFO=y
```

## Exact symbol and code evidence

KALLSYMS recovery found 128,968 symbols and the exact target functions,
including `ptrace_hbp_get_ctrl`, `ptrace_hbp_set_ctrl`,
`ptrace_hbp_set_addr`, `flush_ptrace_hw_breakpoint`,
`hw_breakpoint_control`, `arch_install_hw_breakpoint`,
`arch_uninstall_hw_breakpoint`, `perf_event_enable`,
`perf_event_disable`, and `modify_user_hw_breakpoint`.

Targeted disassembly confirms:

- GETREGSET serializes cached control fields.
- Control decode includes mask and SSC.
- Validation stores SSC value 1 and enabled as logical-not disabled.
- Install writes the address, encodes the control, and forces/clears bit 0
  based on the per-thread global breakpoint-disable state.
- Uninstall writes literal zero to the control register.
- `modify_user_hw_breakpoint` follows the older PRA ordering.
- `do_exit` and `flush_thread` call the flush routine.
- `ptrace_disable` only disables single-step.

Function-size/SHA256 fingerprints are recorded in `output/correlation.json`.
They are correlation metadata only; no raw image, symbol table, address, or
disassembly is committed.

The relevant behavior is strongly matched, but no exact C33 source tree or
reproducible byte-identical build is available. Overall exact-source match
confidence is therefore `MEDIUM`, not `HIGH`.

```text
EXACT_C33_SOURCE_MATCH_CONFIDENCE = MEDIUM
```
