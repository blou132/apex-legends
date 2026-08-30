# PRA and hi6250 source correlation

| Function/path | PRA 4.1.18 vs Lineage 4.9.148 | Relevant result |
|---|---|---|
| `encode_ctrl_reg` / `decode_ctrl_reg` | Semantically equivalent for EL0; Lineage adds EL1-in-hyp handling | Same mask/SSC/len/type/privilege/enabled encoding |
| `ptrace_hbp_get_ctrl` | Identical expression | Returns cached perf architecture control |
| `ptrace_hbp_set_ctrl` | Semantically equivalent | Decode, fill attributes, modify event |
| `ptrace_hbp_set_addr` | Semantically equivalent | Update perf attribute address |
| `modify_user_hw_breakpoint` | Different implementation revision, equivalent first-enable/disable behavior | Disable first; validation sees disabled cache; enable later |
| `arch_build_bp_info` | Semantically equivalent for AArch64 execute/len4 | Rebuilds SSC=1 and enabled from disabled state |
| `hw_breakpoint_control` | Semantically equivalent | Install forces bit 0; uninstall writes BCR zero |
| `flush_ptrace_hw_breakpoint` | Identical behavior | Unregister and null every slot |

Android common 4.4 has the same generic ptrace/perf/install/uninstall shape but
does not contain Huawei's mask/SSC extension. It confirms that cached GETREGSET,
perf enable/disable ordering, forced install enable, and BCR-zero uninstall are
historical ARM64 behavior, while the `0x4000` SSC field is Huawei-specific.

Version drift matters:

- PRA source: 4.1.18.
- Exact C33 image: 4.4.23+.
- Android common reference: 4.4.290.
- Lineage source: 4.9.148.

Therefore the PRA source is not labeled byte-identical. It is a strong semantic
reference because the exact C33 configuration, symbols, control field
instructions, and call graph independently match the targeted paths.

```text
PRA_8X_SOURCE_CORRELATION = SEMANTICALLY_EQUIVALENT_FOR_TARGETED_ABI_PATHS
```
