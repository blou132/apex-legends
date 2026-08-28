# Huawei VCOM driver validation

PotatoNV upstream links a Huawei testpoint driver package for Kirin bootrom
enumeration as `USB SER` / `HUAWEI USB COM 1.0`. The exact public
DC-Unlocker share was archived locally as `Huawei drivers testpoint.rar`.
Android File Host independently lists the same filename and published MD5.

Sources:

- [PotatoNV upstream](https://github.com/kitsuned/PotatoNV)
- [DC-Unlocker Huawei phone drivers index](https://www.dc-unlocker.com/file-list/Drivers/Huawei/Phones)
- [Android File Host mirror metadata](https://androidfilehost.com/?fid=14943124697586361242)

## Archive identity

```text
FILENAME = Huawei drivers testpoint.rar
SIZE = 10227102
MD5 = DDE3EEBE4054A3FEC113A172A3F915BA
AFH_PUBLISHED_MD5_MATCH = YES
SHA256 = A5C9A980228A3505792A97C9AD445A88582A8417578453D13F4E0115EA241BD3
```

## Relevant x64 package

The archive contains `Driver/X64/hw_usbvcom.inf`, `hw_usbvcom.cat`, and
`hw_usbvcom.sys` for `HUAWEI USB COM 1.0` (`VID_12D1`). The INF declares:

```text
Provider = HUAWEI Incorporated
Class = Ports
DriverVer = 11/06/2015,2.0.7.1
CatalogFile = hw_usbvcom.cat
```

Windows SDK `signtool verify /kp /v` succeeded with zero warnings and zero
errors for the catalog. Verification through that catalog also succeeded for
both the INF and SYS. The catalog is signed by Microsoft Windows Hardware
Compatibility Publisher and timestamped `2015-12-07`. The Huawei setup
executables also return a valid Huawei Authenticode signature.

The SYS does not carry an embedded signature; its valid kernel-mode signature
is supplied by the signed catalog. No matching `hw_usbvcom` package is
currently installed in the Windows driver store. Phase16E deliberately did
not install it.

```text
VCOM_DRIVER_SOURCE = POTATONV_UPSTREAM_LINKED_DC_UNLOCKER_PUBLIC_SHARE
VCOM_DRIVER_HASH = A5C9A980228A3505792A97C9AD445A88582A8417578453D13F4E0115EA241BD3
VCOM_DRIVER_VERSION = 2.0.7.1
VCOM_DRIVER_SIGNATURE_STATE = MICROSOFT_WHCP_CATALOG_VALID_INF_AND_SYS_CATALOG_VERIFIED
VCOM_DRIVER_READY = YES_VALIDATED_OFFLINE_NOT_INSTALLED
```
