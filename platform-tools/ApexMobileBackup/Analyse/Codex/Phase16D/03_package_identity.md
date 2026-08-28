# Package identity

## Outer archive

```text
LOCAL_FILENAME = PRA-LX1_8.0.0.364_C33_05014GCW_original.zip
BYTES = 2540397881
PACKAGE_FORMAT = SERVICE_FULL_DLOAD_ZIP
```

The outer archive contains release documentation and the exact `dload`
layout expected by the embedded upgrade guide:

| Member | Bytes | Purpose |
| --- | ---: | --- |
| `Software/dload/update_sd.zip` | 1878232715 | Main package |
| `Software/dload/PRA-L11_altice_all/update_sd_PRA-L11_altice_all.zip` | 676188585 | Altice `all` customization package |

The nested archives contain `UPDATE.APP` and
`update_PRA-L11_altice_all.app`, respectively.

## Exact internal identity

- The release notes name internal build
  `Prague-L11 8.0.0.364(C33)` and external build
  `PRA-LX1 8.0.0.364(C33)`.
- The English upgrade guide names product `PRA-LX1`, target
  `PRA-LX1 8.0.0.364(C33)`, and the same main plus Altice package layout.
- Extracted CUST data contains `altice/all` and
  `Cust-033000 8.0.0.1(O.EXT4)`.
- Both `.APP` files identify themselves as `OFFLINE_UPDATE` packages.

```text
EXACT_MODEL_MATCH = YES
EXACT_BUILD_MATCH = YES
EXACT_CUST_MATCH = YES
EXACT_FIRMWARE_IDENTITY_CONFIDENCE = HIGH
```
