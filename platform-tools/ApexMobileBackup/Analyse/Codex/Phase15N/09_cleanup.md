# Cleanup and preservation

Every mode ended with `am force-stop local.graphicsprobe`, an emulator console
shutdown, and confirmation that its ADB endpoint disappeared. A final delayed
check reported no ADB endpoint and no `emulator` or `qemu-system-x86_64`
process. No physical Android device appeared during the phase.

`ApexGraphicsProbe` remains available for reproducibility. It contains only the
original probe application and no account or Apex material.

The preserved files matched their preflight SHA-256 values and timestamps:

| `ApexPhase9Lab` file | SHA-256 |
|---|---|
| `ApexPhase9Lab.ini` | `55095F2B7E9B24845E90D446D3F5C2B70B3FA83FE3359A50723FC36A459D5AC2` |
| `config.ini` | `0E863E6AF73E9104E8D08A00DBDF943EF0573BF5462B81F562F0C43B69E4B6BE` |
| `hardware-qemu.ini` | `E2E78955A6E2BAC9306C29655EF3FFBB584DFDB14F86205A358E43F4D2A72045` |

```text
ALL_EMULATORS_STOPPED = YES
NO_PHYSICAL_DEVICE = YES
APEX_PHASE9LAB_UNCHANGED = YES
PROBE_AVD_RETAINED = YES
```
