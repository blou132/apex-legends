# Future Windows requirements

Nothing in this file authorizes installation or device connection.

## Expected host components

| Requirement | Source rule | Expected evidence |
| --- | --- | --- |
| Huawei HiSuite | Huawei official support site only | Standard Huawei USB/fastboot support |
| Huawei testpoint/VCOM driver | Hash and signature must be verified before use; prefer upstream-linked Huawei-signed package | Device changes from `USB SER` to `HUAWEI USB COM 1.0` |
| PotatoNV | Official `kitsuned/PotatoNV` GitHub release only | Hash equals the archived Phase16D reference |
| ADB/fastboot | Existing known Android platform-tools | Separate normal and fastboot detection checks |

PotatoNV upstream currently links to HiSuite and a DC-Unlocker-hosted testpoint
driver package. No driver package was downloaded or installed in Phase16D.
A future phase must verify the driver archive hash, file signatures, and Device
Manager state before any hardware operation.

The future expected PotatoNV selection is `Kirin 65x (A)`. The program must
not be started merely because the driver appears; the board revision and all
recovery prerequisites must first be reconfirmed.
