# Phase3 - 01 - Import Ghidra libUE4.so

## Source

Fichier importe en lecture seule:

- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libUE4.so`

Projet Ghidra:

- Dossier: `ApexMobileBackup\Analyse\Codex\Phase3\ghidra_project\`
- Nom: `ApexMobileUE4`
- Programme: `/libUE4.so`

Commande principale:

```powershell
C:\Tools\ghidra_12.1.2_PUBLIC\support\analyzeHeadless.bat `
  ApexMobileBackup\Analyse\Codex\Phase3\ghidra_project `
  ApexMobileUE4 `
  -import ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libUE4.so `
  -analysisTimeoutPerFile 7200 `
  -max-cpu 4 `
  -log ApexMobileBackup\Analyse\Codex\Phase3\output\ghidra_import.log `
  -scriptlog ApexMobileBackup\Analyse\Codex\Phase3\output\ghidra_import_script.log
```

## Detection Ghidra

Preuves dans `Phase3\output\ghidra_import_stdout.txt`:

- Loader: `Executable and Linking Format (ELF)`
- Language/compiler: `AARCH64:LE:64:v8A:default`
- Analyse: `ANALYZING all memory and code`

Metadonnees ELF confirmees par parseur local `Phase3\output\elf_libUE4_summary.json`:

- Classe: `ELF64`
- Endian: little
- Type: `ET_DYN`
- Machine: `AArch64`
- Entry point ELF: `0x395a3e0`
- LOAD min vaddr: `0x0`
- Taille fichier: `190192944` octets

## Analyse automatique

Temps Ghidra:

- `Total Time 7199 secs`
- Timeout configure: `7200` secondes
- Resultat: `Analysis timed out at 7200 seconds. Processing not completed`
- Sauvegarde: `Save succeeded for: /libUE4.so`
- Import: `Import succeeded`

Conclusion:

- Import Ghidra: CONFIRMED.
- Analyse automatique complete: UNKNOWN/NO, timeout a 7200 s.
- Projet exploitable partiel sauvegarde: CONFIRMED.

## Fonctions exportees

Script integre utilise:

- `ExportFunctionInfoScript.java`

Sortie:

- `Phase3\output\ghidra_export_functions.json`

Nombre de fonctions exportees:

- `467079`

Exemples:

- Premieres entrees: `FUN_021f250c`, `FUN_022048d4`, `FUN_02210290`
- Dernieres entrees externes: `log10`, `sin`, `__android_log_write`, `mprotect`, `__cxa_finalize`

## Erreurs et limites

Erreurs/import warnings principaux:

- `Failed to properly markup GNU Hash table at 00105700`
- `Failed to parse and markup ELF Note starting at 00100270`
- `Resolving External Symbols of [/libUE4.so] - 720 unresolved symbols, no external libraries configured - skipping`
- `Unable to find DWARF information, skipping DWARF analysis`
- Multiples `Failed to disassemble at ... (GccExceptionAnalyzer)`
- `Failed to create function ... since its body contains referring thunk ...`

Effet sur la Phase3:

- Les adresses et entrees de fonctions Ghidra sont exploitables.
- Les scripts locaux cibles pour callers/callees/decompilation n'ont pas pu etre executes dans Ghidra headless sans installer PyGhidra ou copier un script hors `Phase3`.
- Les fonctions `FUN_...` ci-dessous sont donc des candidates par intervalle d'entrees exportees, pas des corps confirmes par un export de range Ghidra.

## Sorties machine-readable

- `Phase3\output\ghidra_import_stdout.txt`
- `Phase3\output\ghidra_import.log`
- `Phase3\output\ghidra_export_functions.json`
- `Phase3\output\ghidra_nearest_functions.json`
- `Phase3\output\elf_libUE4_summary.json`
