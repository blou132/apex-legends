# Apex Mobile analysis workspace

This repository contains reverse-engineering notes, Ghidra helper scripts, generated reports, and textual Ghidra evidence used to track the Apex Mobile analysis work.

For a new ChatGPT or coding-agent session, start with [`CHATGPT_CONTEXT.md`](CHATGPT_CONTEXT.md). It gives the reading order, current conclusions, confidence rules, and the next useful analysis targets. `AGENTS.md` provides the same operating constraints in a compact agent-oriented form.

Large proprietary binaries, local Ghidra databases, and generated compilation caches are intentionally excluded from Git:

- APK, OBB, PAK, SO and media files
- Ghidra project databases
- temporary Java compilation outputs

The repository does include the complete exported Ghidra function inventory and the non-binary Phase3 execution logs. This keeps the remote repository useful for AI-assisted searches without publishing the original game binaries.

Current focus:

- `CHATGPT_CONTEXT.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/REPORT.md`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase2/`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/`
- `platform-tools/ApexMobileBackup/Analyse/Codex/Phase3/Phase3C/REPORT_PHASE3C.md`

Phase3C confirms the rebased Ghidra address model:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
```

Phase3B addresses must not be reused directly. Phase3C is the authoritative source whenever an older report conflicts with the rebased results.
