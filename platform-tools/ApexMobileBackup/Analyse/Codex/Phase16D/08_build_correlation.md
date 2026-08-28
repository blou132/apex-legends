# Installed-build correlation

The comparison uses sanitized Phase16C properties only. The phone was not
queried again.

| Selector | Installed evidence | Package evidence | Result |
| --- | --- | --- | --- |
| Model | `PRA-LX1` | External release build and upgrade guide `PRA-LX1` | MATCH |
| Platform | `hi6250`, Kirin 655 | Prague/PRA service package and partition layout | MATCH |
| Android/EMUI | Android 8.0.0 / EMUI 8 | `8.0.0 r1 EMUI8.0` and EMUI8 metadata | MATCH |
| Build | `PRA-LX1 8.0.0.364(C33)` | External build `PRA-LX1 8.0.0.364(C33)` | MATCH |
| Internal product | PRA family | `Prague-L11 8.0.0.364(C33)` | MATCH |
| CUST | C33 Altice all | `altice/all`, `Cust-033000 8.0.0.1(O.EXT4)` | MATCH |

The generic `CURVER`/`VERLIST` records alone are not exact C33 proof. The
exact release documents and extracted CUST filesystem provide that proof.

```text
EXTRACTED_MODEL_MATCH = YES
EXTRACTED_BUILD_MATCH = YES
EXTRACTED_CUST_MATCH = YES
INSTALLED_TO_STOCK_MATCH_CONFIDENCE = HIGH
```
