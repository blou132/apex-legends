# Puffer failure branches

The directly reachable failure mapping is split between the init-action runner
and `MakeSureGetUrlFromServer`.

| Function | Ghidra / ELF | Direct condition | Internal result |
| --- | --- | --- | --- |
| `FUN_00501d0c` (`CPufferInitAction::run`) | `0x501d0c` / `0x401d0c` | null config | `0x0430000a` |
| `FUN_00501d0c` | `0x501d0c` / `0x401d0c` | unusable local directory | `0x0430000c` |
| `FUN_004ffd44` (`MakeSureGetUrlFromServer`) | `0x4ffd44` / `0x3ffd44` | RPC init/connect setup fails | `0x0430002e` |
| `FUN_004ffd44` | `0x4ffd44` / `0x3ffd44` | connection wait reaches its bound | `0x0430002f` |
| `FUN_004ffd44` | `0x4ffd44` / `0x3ffd44` | connection poll returns failure | `0x04300030` |
| `FUN_004ffd44` | `0x4ffd44` / `0x3ffd44` | update response callback reports failure | `0x04300031` |
| `FUN_004ffd44` | `0x4ffd44` / `0x3ffd44` | action is stopped/cancelled | `0x04300032` |

The strings `connect server timeout`, `connect server failed`, and
`get server callback failed` directly label the corresponding branches. The
RPC setup failure is labelled `init rpc connect failed`.

`NetworkNotReachable` is present in lower GCloud address-service code at
`FUN_003e9860` (Ghidra) but no direct static call edge from the Puffer
coordinator to that function was found. Phase15U connects it to the same run by
runtime correlation only.

No directly reachable branch was identified for an empty URL, an invalid URL,
or a named DNS enum. Those remain `UNKNOWN`; they must not be inferred from the
runtime sequence.
