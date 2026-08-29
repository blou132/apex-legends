# PRA-LX1-specific research

## Procedure comparison

| Source | Class | Documented sequence | Evidence quality |
| --- | --- | --- | --- |
| PotatoNV [upstream README](https://github.com/kitsuned/PotatoNV/blob/ef04f2924e55137a904fb97656fefe8aaa5d954f/README.md) | UPSTREAM | Turn off; short testpoint to shield; connect USB while shorted; release after about 3 s | Authoritative for generic PotatoNV use; not PRA power-state specific |
| [Discussion 24](https://github.com/kitsuned/PotatoNV/discussions/24) | EXACT_MODEL_COMMUNITY | PRA-LX1 off and unplugged; ground testpoint; connect USB; then hold Power about 10 s while USB/ground remain; blank screen required | Detailed same-model success, but unofficial and single-author |
| [Discussion 125](https://github.com/kitsuned/PotatoNV/discussions/125) | EXACT_MODEL_COMMUNITY | PRA-LX1/Kirin 655 compatibility; two Windows success reports, one mentioning metal wire and driver relevance | Same-model corroboration, but sparse procedure detail |
| [Phase16D testpoint sources](../Phase16D/11_testpoint_validation.md) | SERVICE_TOOL_LOG plus visual references | PRA board location and exact-model USB COM behavior | Useful for location/endpoint, weak for precise power sequence |
| Phase16F direct observation | EXACT_DEVICE_OBSERVATION | Generic 3-second/no-Power documented sequence produced `12D1:3609` on this unit | Strongest evidence for this unit; succeeded once |

The generic and PRA-specific sequences genuinely differ. The exact-device
success means a Power hold while grounded is not proven mandatory for this
unit. Conversely, the community report means power-key-assisted entry is a
plausible PRA variant. It was not tested in Phase16F and must not be mixed into
the documented successful sequence after the fact.

The later failures do not distinguish these procedures: they used Power only
before contact as a forced-shutdown step, not during grounded USB insertion.

```text
UPSTREAM_GENERIC_TESTPOINT_SEQUENCE = OFF -> GROUND_TESTPOINT -> CONNECT_USB -> RELEASE_AFTER_ABOUT_3S
PRA_LX1_SPECIFIC_SEQUENCE = OFF_AND_UNPLUGGED -> GROUND_TESTPOINT -> CONNECT_USB -> HOLD_POWER_ABOUT_10S_WHILE_GROUNDED -> REQUIRE_BLACK_SCREEN
EXACT_DEVICE_SUCCESS_SEQUENCE = UPSTREAM_GENERIC_VARIANT
```
