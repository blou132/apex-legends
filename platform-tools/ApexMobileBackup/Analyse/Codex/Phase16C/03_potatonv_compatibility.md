# PotatoNV compatibility

## Upstream evidence

The current upstream [PotatoNV repository](https://github.com/kitsuned/PotatoNV)
documents all of the following:

- Kirin 655 is in the supported CPU list.
- Huawei P8 Lite (2017), model family `PRA`, is in the tested-device table.
- The tested PRA profile is `Kirin 65x (A)`.
- Physical motherboard testpoint access and phone disassembly are required.
- FBLOCK handling is specifically relevant to Kirin 65x, but remains prohibited
  in this phase.

The upstream [PotatoNV usage wiki](https://github.com/kitsuned/PotatoNV/wiki/How-to-use-PotatoNV)
also requires USB COM drivers, physical testpoint access, and a matching
bootloader selection.

## Model and firmware granularity

A community report in the upstream project records a successful PRA-LX1 run on
EMUI 8 build `8.0.0.410(C432)`:
[PRA-LX1 community report](https://github.com/kitsuned/PotatoNV/discussions/24).
This supports same-model/EMUI feasibility, but it is not evidence for this
phone's exact `8.0.0.364(C33)` build or CUST.

No exact-build PotatoNV success or incompatibility report for
`PRA-LX1 8.0.0.364(C33)` was found during this audit.

## Required result

```text
POTATONV_SOC_COMPATIBILITY = CONFIRMED
PRA_LX1_SUPPORTED_BY_POTATONV = YES_UPSTREAM_TESTED_PRA
KIRIN655_SUPPORTED_BY_POTATONV = YES_UPSTREAM_DOCUMENTED
RECOMMENDED_BOOTLOADER_PROFILE = KIRIN_65X_A
TESTPOINT_REQUIRED = YES
PHONE_DISASSEMBLY_REQUIRED = YES
FBLOCK_OPTION_RELEVANT = YES_FOR_KIRIN65X_NOT_AUTHORIZED
EXACT_BUILD_POTATONV_EVIDENCE = NONE_FOUND
SAME_MODEL_EMUI8_EVIDENCE = COMMUNITY_CONFIRMED_DIFFERENT_CUST
KNOWN_INCOMPATIBILITY = NONE_FOUND_EXACT_BUILD_UNVALIDATED
```
