# Phase15V scope

Phase15V is a bounded, local, read-only static analysis of the preserved Apex
Mobile inputs. Apex was not launched. No phone, emulator, ADB endpoint, DNS,
network service, proxy, response emulation, patch, or bypass was used.

The analysis reused the existing read-only Ghidra project and focused on
`libgcloud.so`, with one exact integer check in the preserved `libUE4.so`.
The Ghidra script searched only Puffer init/action and version-service terms,
then exported direct owners and one caller/callee level. Its bulk output remains
under gitignored `LocalInputs/Phase15V/`.

Inputs used:

- preserved `libgcloud.so` from the Phase13 library set;
- preserved `libUE4.so` from Phase15I for exact integer checks only;
- existing Phase10 Ghidra project, opened with `-noanalysis -readOnly`;
- published Phase6, Phase7, Phase8, Phase10, and Phase15U evidence.

No proprietary binary or bulk decompilation output is included in Git.
