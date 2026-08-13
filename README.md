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

Phase9C is the current investigation boundary. The verified APK and OBBs were installed on a clean secondary Huawei ARM64 test phone, never on the preserved Samsung. The client process and `GameActivity` remain active under an offline network policy, but the screen stays black after an expected DNS/backend failure. `libUE4.so`, ClientLaunch, EventSystem, the effective provider, and the final open path remain unconfirmed. Phase8 remains authoritative for the unresolved static virtual-path and provider analysis.
