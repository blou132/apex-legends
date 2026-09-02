# CheckUpdate caller gates

The exact callers establish the following bounded pre-call state:

| Caller identity | Meaningful gates and state |
|---|---|
| `UDolphinUpdater::StartUpdate` | non-null update input at function entry; owner argument retained; sets `+0x40=1`, clears `+0x44` before call |
| `UDolphinUpdater::OnSrcUpdateFinished` | completion/result path passes string comparison and helper routing; clears `+0x42` before call |
| `UDolphinUpdater::OnUpdateSuccess` | requires `+0x44 != 0`; sets `+0x40=1`, clears `+0x44`; alternate `+0x41/+0x42` paths route elsewhere |
| `SkipAppUpdate` | selected entry exists; `[entry+0x38]` owner exists; owner `+0x1f0 != 0`; owner `+0x44 != 0`; then sets `+0x40=1` and clears `+0x44` |

Logger-level branches are not treated as lifecycle gates.

```text
CHECKUPDATE_CALLER_GATES = STARTUPDATE_INPUT_AND_STATE; ONSRCUPDATE_RESULT_PATH; ONUPDATESUCCESS_PLUS44; SKIPAPPUPDATE_OWNER_PLUS1F0_PLUS44
```
