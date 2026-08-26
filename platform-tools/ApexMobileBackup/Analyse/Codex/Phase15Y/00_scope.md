# Phase15Y scope

Date: 2026-08-26

Phase15Y reuses only the local, gitignored evidence captured by Phase15U. No
device, ADB endpoint, emulator, Apex process, network operation, backend,
debugger, hook, patch, or bypass was used.

The primary log window is `GameActivity t0 +105 s` through `t0 +165 s`. The
launch marker fixes `t0`; older records present in the same raw log are outside
the window and were not used.

The runtime search produced exact source, function, event, and numeric anchors.
That result authorized one narrow static follow-up in the existing
`libgcloud.so` and `libUE4.so` Ghidra projects. Both projects were opened with
`-noanalysis -readOnly`. The search was limited to the exact new terms and
their direct owners/references. No generic Puffer or update scan was resumed.

Raw logcat, screenshots, Ghidra exports, process identifiers, thread
identifiers, and private paths remain local-only.
