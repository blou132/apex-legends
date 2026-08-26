# Phase15Z scope

Phase15Z is a static-only, bounded analysis of the existing `libgcloud.so`
Ghidra project. The project was opened with `-noanalysis -readOnly`; no binary
was imported or modified.

No Apex launch, phone access, ADB command, emulator, DNS, HTTP, proxy, backend,
or response emulation occurred. The analysis started at `ProcessActionError`,
the exact `NormalConnectVersionSvr` string, and raw error `0x0930002a`.

The reproducible exporter is `ApexPhase15ZDolphinExport.java`. Its decompiler
output and execution logs remain under gitignored `Analyse/LocalInputs/Phase15Z`.
Only cleaned conclusions and addresses are published.
