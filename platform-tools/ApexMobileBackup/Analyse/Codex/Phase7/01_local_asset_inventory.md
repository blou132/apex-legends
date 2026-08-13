# Phase7 - Local asset inventory

## Result

`PARTIAL_INPUT_ABSENT`

The accessible `Analyse/Codex` tree contained 55 JSON files and 30 text files at scan time. It contained no `.lua`, `.luac`, or `.pak` file. `Analyse/APK`, `Analyse/MAIN`, and `Analyse/PATCH` are absent.

The requested terms occur in cleaned reports, JSON exports, and Ghidra scripts. These are analysis references, not asset hits. Representative evidence includes Phase6 reports for `EventSystem.PostCppEvent`, the Phase6 emitter JSON, and `04_pak_analysis.md` for prior PAK observations.

## Classification

| Source | Availability | Result |
| --- | --- | --- |
| Extracted APK assets | absent | not searched |
| MAIN assets | absent | not searched |
| PATCH assets | absent | not searched |
| PAK files | absent | no Phase7 raw hits |
| Cleaned Codex reports/JSON | present | report-only references |
| Extracted Lua/Lua bytecode | absent | none recovered |

Machine-readable details are in `output/local_asset_inventory.json`.
