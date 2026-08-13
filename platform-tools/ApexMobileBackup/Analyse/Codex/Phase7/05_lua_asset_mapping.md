# Phase7 - Lua module to asset mapping

`FUN_06be427c` confirms a case-insensitive `.LUA` suffix check and removal before dispatch. `FUN_049a8b54` confirms path combination using runtime prefix/global values, a trailing slash, virtual path resolution, and file reading.

The following remain **UNKNOWN**:

- the runtime mount prefix;
- the exact final virtual path presented to the file manager;
- whether the extension is restored, rewritten to `.luac`, or omitted by a provider;
- a hash/name-table mapping;
- the physical PAK, downloaded bundle, cache, or loose-file source.

Thus `Script/Tools/EventSystem/EventSystem.lua` remains a **PROBABLE module name**, not a confirmed physical archive entry. The loader reaches a generic virtual/platform file manager, but its callgraph does not identify `FPakPlatformFile` or another concrete container implementation.
