# Phase6 - Event emitter

## Confirmed path

```text
FUN_06be3bdc (Ghidra 0x6be3bdc, ELF 0x6ae3bdc)
  0x6be3e90: event ID = 0x138
  0x6be3e94: BL FUN_06be3f4c

FUN_06be3f4c (Ghidra 0x6be3f4c, ELF 0x6ae3f4c)
  0x6be4054: BL FUN_06be427c
```

`FUN_06be3f4c` is a fixed wrapper around the dynamic bridge. Its effective arguments are:

| Argument | Meaning | Confidence |
| --- | --- | --- |
| `param_1` | target/world context forwarded to the bridge | CONFIRMED forwarding; concrete type UNKNOWN |
| `param_2` | event ID, reduced to `uint16` at `0x6be4020` | CONFIRMED |
| `param_3` | callback success value | CONFIRMED from `FUN_06be3bdc` |
| `param_4` | callback response-body `FString` | CONFIRMED from `FUN_06be3bdc` |

## Constructed UTF-16 values

The wrapper allocates 41 UTF-16 code units including the terminator for the script path, and 25 for the dynamic function name.

| Construction | Ghidra data addresses | Confirmed content | Full value |
| --- | --- | --- | --- |
| script path | `0x23e6d50`, `0x23ef9b0`, `0x23e14a0`, `0x23f2960`, `0x23e2860` | `[8 encoded units]ools/EventSystem/EventSystem.lua` | `Script/Tools/EventSystem/EventSystem.lua` PROBABLE |
| function name | `0x23f0dc0`, `0x23f2970`, `0x23e76e0` | `EventSystem.Post[8 encoded units]` | `EventSystem.PostCppEvent` PROBABLE |

The corresponding ELF addresses are each Ghidra address minus `0x100000`; for example `0x23e6d50 -> 0x22e6d50` and `0x23e76e0 -> 0x22e76e0`.

The full plaintext values are not marked CONFIRMED. Their clear portions, exact lengths, `.lua` suffix and the independent `ELuaCppEventType` metadata make them strong reconstructions, but the eight encoded UTF-16 units in each value were not independently decoded.

## Calls in the wrapper

| Sites | Target | Parameters and role |
| --- | --- | --- |
| `0x6be3f7c`, `0x6be3fe4` | `FUN_04164080` | allocate/reserve the two temporary UTF-16 buffers with capacities `0x29` and `0x19` |
| `0x6be3f98`, `0x6be4000` | `FUN_0453b470` | grow/finalize a temporary string when requested length exceeds capacity |
| `0x6be4054` | `FUN_06be427c` | invoke the dynamic Lua bridge with context, script path, dynamic name, `uint16` event, success and response body |
| `0x6be408c`, `0x6be40b0`, `0x6be4110` | indirect `vtable+0x28` | release temporary/result allocations through the global allocator |
| `0x6be40f0` | `FUN_044d7d60` | clean/reset the typed return container when it holds values |
| `0x6be4118`, `0x6be4124`, `0x6be4130` | `FUN_044183e4` | lazy initialization before allocator release paths |

The emitter does not parse `response_body`, identify server fields or write a Login property. See `output/event_emitter.json` for complete disassembly and decompiler output.
