# Cleanup

Each case used one emulator instance and a clean console shutdown. Before the
next case, ADB contained no endpoint. Final inventory found no Android endpoint
and no emulator process.

`ApexPhase9Lab` was never booted. Its three tracked files retain the exact
Phase15P hashes and UTC timestamps:

| File | SHA-256 | Last write UTC |
|---|---|---|
| `ApexPhase9Lab.ini` | `55095F2B7E9B24845E90D446D3F5C2B70B3FA83FE3359A50723FC36A459D5AC2` | `2026-08-13T13:54:16.6076728Z` |
| `config.ini` | `0E863E6AF73E9104E8D08A00DBDF943EF0573BF5462B81F562F0C43B69E4B6BE` | `2026-08-13T13:54:16.6116129Z` |
| `hardware-qemu.ini` | `E2E78955A6E2BAC9306C29655EF3FFBB584DFDB14F86205A358E43F4D2A72045` | `2026-08-20T14:43:38.2667672Z` |

No network setting was changed. No application was launched.
