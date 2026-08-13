# Phase6 - Request URL source

## Confirmed native boundary

`FUN_07a31858` at Ghidra `0x7a31858` / ELF `0x7931858` decodes a `FString` argument from the Unreal frame and forwards it to `FUN_06bc68e8`, which configures the HTTP GET.

Ghidra reports zero direct code callers. The only entry references are:

| Reference | ELF VA | Type |
| --- | --- | --- |
| `0x2a46298` | `0x2946298` | indirection |
| `0x315ea38` | `0x305ea38` | data |

This is consistent with UFunction/reflection or script-driven invocation, but the concrete caller is **UNKNOWN**. No URL construction, configuration key, base URL, host, path or port is reached from the native thunk.

## Conclusion

- URL argument type: `FString` CONFIRMED;
- caller mechanism: Unreal reflection/script PROBABLE;
- concrete producer: UNKNOWN;
- exact URL: UNKNOWN and not reconstructible from the currently accessible static evidence.

No URL or server was contacted. Machine evidence: `output/request_url_source.json`.
