# UI capture

The prelaunch compressed hierarchy was pulled and independently parsed as a
well-formed, non-empty XML document with 17 nodes. Baseline hierarchy capture
therefore worked before Apex launch.

At dialog detection, the compressed hierarchy was also written and pulled. A
PowerShell wrapper treated ADB pull progress written to stderr as an exception
after the successful transfer. The retained host XML was independently checked:

- size: 2,296 bytes;
- well formed: yes;
- total nodes: 5;
- Apex-owned nodes: 5.

Because the wrapper stopped immediately after the XML transfer, the requested
single screenshot was not captured. Apex was not relaunched: the phase allowed
one launch only. Visual confirmation is therefore `NO` for Phase15L, while the
complete dialog content is confirmed by the valid hierarchy.
