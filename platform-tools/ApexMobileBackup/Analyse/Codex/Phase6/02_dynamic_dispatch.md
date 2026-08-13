# Phase6 - Dynamic dispatch

## Classification

`FUN_06be427c` at Ghidra `0x6be427c` / ELF `0x6ae427c` is **CONFIRMED** as a native-to-Lua invocation bridge.

It is not a UObject `ProcessEvent` implementation and is not a Blueprint dispatcher. Those classifications are **INVALIDATED for this function** because the inspected path does not resolve a `UFunction` or build a UObject parameter buffer. Instead it:

1. reads a global runtime at `0xb696eb8` and its object at `+0x10`;
2. copies and normalizes a UTF-16 script/module path;
3. recognizes the case-insensitive `.LUA` suffix;
4. manipulates a 16-byte typed value stack;
5. installs `FUN_066365bc` with type tag `0x16`;
6. calls `FUN_042bf01c` at `0x42bf01c` for a lookup operation;
7. calls `FUN_04440f2c` at `0x4440f2c` for invocation;
8. delegates dotted-name handling to `FUN_06be4d1c` at Ghidra `0x6be4d1c` / ELF `0x6ae4d1c`.

The receiver/context passed by `FUN_06be3f4c` is forwarded into this machinery, but its exact C++ type is **UNKNOWN**. The evidence proves the Lua runtime boundary, not a concrete game class.

## Generic protocol

For the server-list response, the effective call is:

```text
LuaBridge(
  context,
  probable module "Script/Tools/EventSystem/EventSystem.lua",
  probable callable "EventSystem.PostCppEvent",
  uint16 event_id,
  success,
  response FString
)
```

The module and callable plaintext are **PROBABLE**, not exact decoded strings; the confirmed fragments are documented in `01_event_emitter.md`.

## Callers

Ghidra has exactly one direct code caller:

| Caller | Call site | Role |
| --- | --- | --- |
| `FUN_06be3f4c`, Ghidra `0x6be3f4c`, ELF `0x6ae3f4c` | `0x6be4054` | centralized C++ event wrapper |

The other references to the dispatcher entry are data/indirection references at `0x2a03c18` and `0x2f69050`. A requested 10-20 direct-caller comparison is therefore unavailable; fabricating such a sample would be incorrect. The centralized wrapper explains why every C++ event does not appear as a direct caller of the bridge.

Complete dispatcher/core disassembly, call instructions and the sole caller are in `output/dynamic_dispatch.json`.
