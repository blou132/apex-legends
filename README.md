# Apex Mobile analysis workspace

This repository contains lightweight reverse-engineering notes, Ghidra helper scripts, and generated reports used to track the Apex Mobile analysis work.

Large proprietary binaries and local Ghidra databases are intentionally excluded from Git:

- APK, OBB, PAK, SO and media files
- Ghidra project databases
- temporary Java compilation outputs

Current focus:

- `platform-tools/ApexMobileBackup/Analyse/Codex/REPORT.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase2/`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`

Phase3C confirms the rebased Ghidra address model:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
```
