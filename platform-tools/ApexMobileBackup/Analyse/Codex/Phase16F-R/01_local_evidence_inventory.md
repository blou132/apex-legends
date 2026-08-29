# Local evidence inventory

## Preserved inputs

| Evidence | Location/class | Result | Publication |
| --- | --- | --- | --- |
| PotatoNV text log | Phase16F local input | Ordered output through `Waiting for any device` and `Timeout error` | Local-only |
| PotatoNV log integrity | 168 bytes, SHA256 `4904F7A50F1C4AE677E944E455827BE3E147798FE676C017B6869812EECF779A` | Unmodified | Hash only |
| SetupAPI device log | `C:\Windows\INF\setupapi.dev.log` | Bootrom and D00D driver history | Sanitized conclusions only |
| Windows event logs | DeviceSetupManager and System | Device names, lookup outcome, service start | Sanitized conclusions only |
| Phase16F transcript | Local Codex session record | Physical instructions, acknowledgements, and host actions | Sanitized conclusions only |
| Phase16F reports | [Phase16F](../Phase16F/REPORT_PHASE16F.md) | Prior bounded result | Tracked |
| Driver store | Current Windows inventory | `oem14.inf` and `oem77.inf` remain installed | Sanitized conclusions only |
| PotatoNV source | Pinned local clone at commit `ef04f2924e55137a904fb97656fefe8aaa5d954f` | USB and operation state machine | Public links only |
| GitHub discussions | Public discussions 24 and 125 | Exact-model community procedure and compatibility reports | Raw API JSON local-only |

The local-only Phase16F-R evidence folder contains a bounded SetupAPI extract
and raw GitHub API responses. The repository ignore rule covering
`platform-tools/ApexMobileBackup/*` keeps these artifacts outside Git.

## Evidence limitations

No individual timestamps are preserved for the `xloader` and `fastboot` image
uploads. No historical event gives a precise USB-removal instant between the
two VID/PID states. The PotatoNV log file timestamp records capture time, not
the exact instant at which the timeout text was emitted.

No timestamped Phase16F photo showing `FASTBOOT&RESCUE MODE` or `PHONE
Unlocked` was found in the preserved inputs or transcript. Its relation to the
RAM bootloader therefore cannot be classified from current evidence.

```text
RAW_EVIDENCE_PUBLISHED = NO
ORIGINALS_MODIFIED = NO
PHONE_UNLOCKED_PHOTO_PRESERVED = NO
```
