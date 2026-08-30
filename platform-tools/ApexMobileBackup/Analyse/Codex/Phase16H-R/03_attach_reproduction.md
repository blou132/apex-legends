# Attach reproduction

Phase16H remains authoritative for its original sequence: process enumeration
succeeded, a minimal attach to disposable ping failed because the remote
server connection closed, and the target survived.

Phase16H-R isolated an earlier failure. Frida 17.17.0 crashed during startup
before three enumeration passes or `device.attach(pid)` could be attempted.
No target process, script, Java API, module enumeration, memory read, or
interceptor was involved.

```text
FRIDA_ENUMERATION_REPEAT_SUCCESS = NO_SERVER_CRASHED_BEFORE_ENUMERATION
FRIDA_SERVER_SURVIVES_ENUMERATION = NOT_TESTED_STARTUP_CRASH
FRIDA_SESSION_CREATION = NOT_ATTEMPTED_SERVER_STARTUP_CRASH
FRIDA_SERVER_STILL_ALIVE_AFTER_ATTACH = NOT_APPLICABLE_CRASHED_BEFORE_ATTACH
```
