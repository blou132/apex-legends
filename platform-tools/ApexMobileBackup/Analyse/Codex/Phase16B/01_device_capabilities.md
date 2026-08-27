# Device capabilities

| Property | Result |
|---|---|
| Device | Huawei PRA-LX1 |
| Android | 8.0.0 / API 26 |
| Build type | `user` |
| `ro.debuggable` | `0` |
| `ro.secure` | `1` |
| ADB identity | unprivileged `shell` |
| Root facility | unavailable; no `su` path |
| SELinux | `Enforcing` |
| `/proc` mount | `hidepid=2`, guarded by `readproc` group |
| `perf_event_paranoid` | `3` |

This is a production device policy. The shell can perform ordinary ADB
diagnostics, but it has neither root nor `CAP_SYS_PTRACE`. Reading another
non-debuggable application's address-sensitive proc entries remains subject to
the kernel ptrace access check; `hidepid` group membership does not make the
application dumpable.

The current baseline matches the earlier Phase14 device audit.
