# Shutdown and preservation

The ANR was not interacted with. Apex had not been launched. The emulator
accepted a console shutdown request; final checks found no ADB endpoint and no
target emulator process. Network state was never changed.

The preserved host files retain the exact preflight SHA-256 values and UTC
timestamps:

| File | SHA-256 | Last write UTC |
|---|---|---|
| `ApexPhase9Lab.ini` | `55095F2B7E9B24845E90D446D3F5C2B70B3FA83FE3359A50723FC36A459D5AC2` | `2026-08-13T13:54:16.6076728Z` |
| `config.ini` | `0E863E6AF73E9104E8D08A00DBDF943EF0573BF5462B81F562F0C43B69E4B6BE` | `2026-08-13T13:54:16.6116129Z` |
| `hardware-qemu.ini` | `E2E78955A6E2BAC9306C29655EF3FFBB584DFDB14F86205A358E43F4D2A72045` | `2026-08-20T14:43:38.2667672Z` |

```text
NETWORK_RESTORED = NOT_REQUIRED_NETWORK_UNCHANGED
AVD_SHUTDOWN_CLEAN = YES
NO_ADB_ENDPOINT_REMAINING = YES
APEX_PHASE9LAB_PERSISTENT_CONFIG_UNCHANGED = YES
```
