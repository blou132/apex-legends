# Evidence inventory

| Phase | Retained evidence used | Availability |
|---|---|---|
| Phase15U | Raw logcat and launch/checkpoint material | YES |
| Phase16I | Raw and filtered logs, module/app baseline, thread evidence | YES |
| Phase16J | Committed static and historical executor analysis | YES |
| Phase16M | Raw logs, task snapshots, maps, tracer diagnostics | YES |

Phase16M contains five same-name task records and sufficient same-run log
correlation to anonymize them. Its tracer timeline is partial: ownership is
confirmed at the first actionable correlation, but no `TracerPid` sample was
taken at the earlier candidate event.

Raw paths, numeric identifiers, timestamps, addresses, binaries, and exports
are intentionally excluded from committed output.
