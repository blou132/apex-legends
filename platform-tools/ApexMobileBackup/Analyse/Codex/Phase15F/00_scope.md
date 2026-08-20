# Phase15F scope

Date: 2026-08-20

## Goal

Resolve the offline Android/DEX path from `GameActivity` to
`DownloaderActivity`, the expansion-file decision, and the expected return to
native initialization. The analysis reuses the exact Phase15D artifacts and
the already captured Phase15D log.

## Inputs

- Exact `base.apk`: 96,228,800 bytes, SHA256
  `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0`.
- Exact local DEX and the read-only Phase10 Ghidra project.
- Cleaned Phase10, Phase15D, and Phase15E reports.
- Existing Phase15D raw log, read locally and not published.

## Boundaries

No AVD, Apex process, ADB endpoint, phone, network request, download, APK/OBB
change, root, patch, hook, backend, DNS, certificate, or authentication action
was used. Targeted Ghidra reads opened the existing DEX project with
`-readOnly -noanalysis`; their raw output remains under ignored `LocalInputs`.

Only activity metadata, short resource names/strings, DEX addresses, relative
times, and cleaned conclusions are committed. No APK, DEX, OBB, raw log,
process identifier, account path, or user path is published.
