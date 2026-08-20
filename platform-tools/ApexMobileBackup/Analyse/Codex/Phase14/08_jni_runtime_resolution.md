# JNI runtime resolution

Official linker app logging was unavailable because the production package is
not debuggable. The failed backtrace supplied neither an owner library nor a
runtime address for `GameActivity.nativeResumeMainInit()V`.

Phase13 remains authoritative: the 17-library static scope is exhausted,
`libUE4.so` contains string witnesses only, and no exact export or target
`RegisterNatives` row proves the implementation.

```text
NATIVE_RESUME_RUNTIME_RESOLUTION = UNKNOWN
OWNER_LIBRARY = UNKNOWN
NATIVE_RESUME_MAIN_INIT_FUNCTION = UNKNOWN
LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENTSYSTEM_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
```
