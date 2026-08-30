# Tombstone analysis

Tombstone metadata was recorded before and after the supervised starts without
deleting existing entries. Two new entries were attributable to the Frida
17.17.0 server. The failure signal was SIGSEGV with a null fault address; the
sanitized upper stack included `libart.so` and relative Frida frames.

Direct unprivileged pulling of the complete tombstone was not available. The
bounded raw logcat evidence retained locally was sufficient to classify the
signal and executable. No registers, memory dump, device identifier, or full
tombstone is committed.

```text
NEW_FRIDA_TOMBSTONE = YES
FRIDA_CRASH_SIGNAL = SIGSEGV
FRIDA_FAULTING_EXECUTABLE = FRIDA_SERVER_17_17_0
```
