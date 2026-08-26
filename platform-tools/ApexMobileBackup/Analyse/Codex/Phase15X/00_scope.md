# Phase15X scope

Date: 2026-08-26

Phase15X is a bounded, read-only static trace starting only at the exact
`libUE4.so` wrapper `FUN_080d1ac8` and its `CreatePuffer` tail-call at
`0x080d1b84`.

The existing `libUE4.so` Ghidra project was opened with `-noanalysis
-readOnly`. The trace inspected the wrapper, its direct caller set to a maximum
depth of two, and the two exact data/indirection references attached to the
wrapper. No global function, class, vtable, Puffer, error-code, Lua, UI, PAK, or
resource search was performed.

No phone, emulator, ADB, Apex launch, network operation, hook, debugger,
reimport, patch, or bypass was used. Raw Ghidra output remains local-only under
the gitignored `LocalInputs/Phase15X` directory.

The search stops because the wrapper has zero statically resolved callers. Its
return enters an indirect Unreal dispatch boundary with no exact code owner or
reader, so the facade pointer cannot be tied to an Init invocation.
