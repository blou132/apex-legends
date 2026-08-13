# Phase6 - Scope

Date: 2026-08-13.

## Objective

Phase6 starts at `FUN_06be427c` and follows the event `0x138` boundary without scanning all immediates in `.text`. Phase5 remains the authority for the HTTP request and callback path.

The corrected address model is used everywhere:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
```

## Method

The existing Ghidra program was opened read-only with:

```text
-process libUE4.so -noanalysis -readOnly
```

`ApexPhase6Export.java` exports only the emitter, dispatcher, dispatcher core, related call graphs, UFunction thunks, references and decompiler output needed by this phase. No binary was imported again and no global analysis was run.

Existing accessible text, JSON, configuration evidence and prior PAK observations were searched read-only. No PAK decryption, brute force, network request, runtime hook, account, token or device data was used.

## Result boundary

| Question | Result |
| --- | --- |
| Native response path to event `0x138` | CONFIRMED |
| `FUN_06be427c` is a native-to-Lua bridge | CONFIRMED |
| Full Lua module and function names | PROBABLE; clear fragments plus exact lengths, encoded portions not decoded |
| Lua registration and event `0x138` subscriber | UNKNOWN |
| Response parser, fields and storage | UNKNOWN |
| Consumer location | PROBABLE `PAK/Lua` |
| Static native limit reached | YES |

Machine-readable evidence is under `output/`. The large local Ghidra database, proprietary binaries and raw execution logs remain excluded by `PUBLIC_DATA_POLICY.md`.
