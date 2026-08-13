# File provider runtime

## Runtime result

No provider call was observed.

```text
OPTIONAL_PROVIDER_ACTIVE = UNKNOWN
EFFECTIVE_PROVIDER = UNKNOWN
FINAL_OPEN_PATH = UNKNOWN
OPEN_RESULT = UNKNOWN
```

Phase8 static analysis remains authoritative: an optional provider may be selected through global `0xb697528`; otherwise the loader can reach the probable Android asset/physical fallback. Phase9 neither confirms nor invalidates either branch.

## Prepared offsets

| Role | Ghidra address | ELF virtual offset |
| --- | --- | --- |
| fallback factory | `0x46355e8` | `0x45355e8` |
| Lua file facade open target | `0x48415b8` | `0x47415b8` |
| fallback OpenRead thunk | `0x49825b8` | `0x48825b8` |
| fallback OpenRead body | `0x49825bc` | `0x48825bc` |
| resolver | `0x82693bc` | `0x81693bc` |
| optional provider global | `0xb697528` | `0xb597528` |

The object state, vtable, slot selection, arguments, returned size, and final path all require a real runtime observation.
