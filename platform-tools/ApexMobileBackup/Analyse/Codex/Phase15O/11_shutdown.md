# Shutdown and preservation

The SystemUI ANR was not clicked or otherwise interacted with. The diagnostic
path sent an emulator console shutdown request and verified that the ADB
endpoint and target headless QEMU process disappeared.

The network was never changed, so restoration was not required. The AVD ran in
read-only mode. The same three preserved host files retain their exact
preflight SHA-256 values and UTC timestamps:

| File | SHA-256 |
|---|---|
| `ApexPhase9Lab.ini` | `55095F2B7E9B24845E90D446D3F5C2B70B3FA83FE3359A50723FC36A459D5AC2` |
| `config.ini` | `0E863E6AF73E9104E8D08A00DBDF943EF0573BF5462B81F562F0C43B69E4B6BE` |
| `hardware-qemu.ini` | `E2E78955A6E2BAC9306C29655EF3FFBB584DFDB14F86205A358E43F4D2A72045` |

```text
NETWORK_RESTORED = NOT_REQUIRED_NETWORK_UNCHANGED
AVD_SHUTDOWN_CLEAN = YES
NO_ADB_ENDPOINT_REMAINING = YES
APEX_PHASE9LAB_PERSISTENT_CONFIG_UNCHANGED = YES
```
