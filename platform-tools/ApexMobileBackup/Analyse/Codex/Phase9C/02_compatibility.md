# Compatibility gate

```text
HUAWEI_LAB_COMPATIBLE = YES
```

## Gate evaluation

- Required ABI: `arm64-v8a`; device primary ABI: `arm64-v8a`.
- Historical minimum SDK: `23`; device SDK: `26`.
- Required local input size: 96,228,800-byte APK plus 3,779,595,852 bytes of OBB data.
- Device free space before installation: approximately 6.14 GiB on shared storage and 6.16 GiB on `/data`.
- Apex package before installation: absent.

The available space covered the payload with a practical margin. Both OBBs later copied successfully at their exact expected sizes.

## Verified local inputs

| Artifact | Size | SHA256 |
| --- | ---: | --- |
| base APK | 96,228,800 | `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0` |
| main OBB | 1,942,013,346 | `104B313F86A57F1B2A11D81D87E916EFBAB99BBBFB4A031E2D12B0201D5244E0` |
| patch OBB | 1,837,582,506 | `BFE887744EC21CB7CE0829F1C1F1BCAF1C400C358185B52D140D75242264973D` |

All identities matched before the first write to the test device.
