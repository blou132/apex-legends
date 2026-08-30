# Source inventory

## Source revisions

| Source | Revision | Kernel line | Role |
|---|---|---:|---|
| `Ramame/android_kernel_huawei_prague`, branch `8.x` | `3d045997c5eb536b0e3f649274ec07bbe45a7241` | 4.1.18 | Highest-priority PRA community reference |
| `LineageOS/android_kernel_huawei_hi6250`, branch `lineage-17.1` | `97a757c4464e687a83ccc2de122b5cd960aa0e3a` | 4.9.148 | Independent hi6250 comparison |
| Android common `deprecated/android-4.4-o` | `edd606b9fcdd6d1d81ebe04bd0738d85283767ce` | 4.4.290 | Historical generic ARM64/perf reference |
| Exact archived C33 image | SHA256 `6993D273EFCD35D91AC3E0029C9FA4E0BC42999F6BE835B35A92C8F2C670006C` | 4.4.23+ | Exact binary/configuration evidence |

The two GitHub trees were fetched as shallow bare repositories under ignored
`Analyse/LocalInputs/Phase16H-T`. The Android common tree was already archived
under ignored Phase16H-S inputs. These source clones are not committed.

## Relevant paths

- `arch/arm64/include/asm/hw_breakpoint.h`
- `arch/arm64/kernel/ptrace.c`
- `arch/arm64/kernel/hw_breakpoint.c`
- `kernel/events/hw_breakpoint.c`
- `kernel/events/core.c`
- `kernel/ptrace.c`
- `kernel/exit.c`
- `arch/arm64/kernel/process.c`
- `arch/arm64/configs/merge_hi6250_defconfig`

## Exact C33 inputs

The exact image remains local-only at the Phase16D recovery-material path. It
was never copied into this report. PC-side extraction found:

- Huawei partition wrapper, with a gzip stream at file offset `0x1800`.
- Decompressed ARM64 Image size: 34,625,856 bytes.
- Decompressed SHA256:
  `1D47D981A2651DE2AE4254EDD4751BE068AC830D8DC5B194803001E9328B9049`.
- ARM64 Image magic `ARMd` at Image-header offset `0x38`.
- Embedded IKCONFIG and KALLSYMS.
- 128,968 recovered symbols; raw symbols and addresses remain local-only.

The community trees bracket but do not match the exact kernel version. Source
identity is therefore not asserted. Relevant ABI semantics are evaluated from
both source and exact C33 disassembly.
