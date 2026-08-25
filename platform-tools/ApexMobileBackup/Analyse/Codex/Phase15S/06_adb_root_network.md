# ADB, privilege, and network audit

Standard production-build capabilities:

| Capability | Result |
| --- | --- |
| Logcat | readable |
| UIAutomator | binary present |
| Screenshot | `screencap` present |
| Screen recording | standard `screenrecord` binary absent |
| dumpsys | available and used read-only |
| Apex private data | not shell-readable |
| run-as Apex | denied because package is not debuggable |
| Apex process maps | not currently testable because Apex remained stopped; prior same-device evidence records shell denial |

The shell runs as UID 2000. The build has `ro.secure=1`, `ro.adb.secure=1`, and
`ro.debuggable=0`; no `su` binary is present. Verified boot is green and the
reported flash state is locked. `adb root` was not attempted.

Only boolean connectivity settings were retained: airplane mode off, Wi-Fi
enabled, and mobile data enabled. No SSID, interface identifier, address, or
subscriber data was collected. Apex remained stopped, so Phase15S generated no
Apex backend request and changed no network setting.
