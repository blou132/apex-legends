# Phase6 - SyncPayload receiver

`FUN_0a220f70` at Ghidra `0xa220f70` / ELF `0xa120f70` remains a UFunction thunk that invokes a virtual slot at `receiver vtable+0xa58`.

Ghidra reports zero direct code callers. Its only entry references are:

| Reference | ELF VA | Type |
| --- | --- | --- |
| `0x2bdaf30` | `0x2adaf30` | indirection |
| `0x32b0638` | `0x31b0638` | data |

The receiver is decoded dynamically from the Unreal frame. No constructor, vtable assignment or class metadata in the targeted references uniquely identifies it. Therefore:

- concrete receiver class: UNKNOWN;
- concrete function at slot `+0xa58`: UNKNOWN;
- network/RPC classification: UNKNOWN.

The function name alone is insufficient to classify this path as an Unreal RPC. Machine evidence: `output/syncpayload_receiver.json`.
