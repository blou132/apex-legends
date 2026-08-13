# Apex Mobile analysis workspace

This public repository contains cleaned reverse-engineering notes, Ghidra helper scripts, generated reports, and sanitized JSON exports used to track the Apex Mobile analysis work.

For a new ChatGPT or coding-agent session, start with [`CHATGPT_CONTEXT.md`](CHATGPT_CONTEXT.md). It gives the reading order, current conclusions, confidence rules, and the next useful analysis targets. `AGENTS.md` provides the same operating constraints in a compact agent-oriented form.

Proprietary binaries, private backups, raw logs, local Ghidra databases, and generated compilation caches are intentionally excluded from Git:

- APK, OBB, PAK, SO and media files
- Android data and phone-storage backups
- raw execution logs and temporary Ghidra output
- Ghidra project databases
- temporary Java compilation outputs

The repository keeps only the smaller, cleaned reports and machine-readable exports needed to reproduce the documented conclusions. See [`PUBLIC_DATA_POLICY.md`](PUBLIC_DATA_POLICY.md) for the publication rules and security checks.

Current focus:

- `CHATGPT_CONTEXT.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9D/REPORT_PHASE9D.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9C/REPORT_PHASE9C.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9B/REPORT_PHASE9B.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase9/REPORT_PHASE9.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase8/REPORT_PHASE8.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase7C_resume/REPORT_PHASE7C_RESUME.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`

Phase3C confirms the rebased Ghidra address model:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
```

Phase3B addresses must not be reused directly. Phase3C is the authoritative source whenever an older report conflicts with the rebased results.

Phase9D is the current investigation boundary. Reanalysis of the local-only Huawei log confirms that the first runtime-started backend operation is a TDM POST to `https://tdm.mgapex.com:8013/tdm/v1/route`; DNS fails before connection or TLS. A second run was deliberately skipped because the installed environment cannot guarantee Huawei-to-PC connectivity while blocking every Internet route. `libUE4.so`, ClientLaunch, EventSystem, the effective provider, and the final open path remain unconfirmed.
