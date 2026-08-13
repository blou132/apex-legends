# Runtime boundary

Phase9B did not reach the client runtime. None of the Phase9 dynamic targets was observed:

- Lua package searcher
- `FUN_049a8b54`
- `FUN_049a9694`
- `FUN_048ab4d0`
- file facade/provider selection
- final OpenRead path
- ClientLaunch
- EventSystem
- event `0x138` subscriber
- response parser
- URL source

No `libUE4.so` process mapping exists, so the prepared ELF offsets were not converted into runtime addresses. No hook or instrumentation was attempted.

The boundary is before Android boot completion and APK installation, not at an application bypass, integrity check, or native load failure.
