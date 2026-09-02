# Callback table validation

The known client table remains Ghidra `0x0af55170` / ELF `0x0ae55170`.
Its established slots include version info at `+0x10`, error at `+0x20`,
success at `+0x28`, and install notice at `+0x30`.

The exact provider callback forwarding path dispatches the stored callback at
the same slots `+0x10`, `+0x20`, `+0x28`, and `+0x30`, plus interface slots
`+0x18` and `+0x48`. This confirms ABI compatibility. It does not expose a
store of `0x0ae55170` into the concrete object loaded from owner `+0x1f8`.

```text
CALLBACK_TABLE_INSTALLATION_PROVEN = NO
CALLBACK_TABLE_INSTALLATION_SITE = CLASS_INITIALIZER_ONLY_ELF_0x0a1a403c_NOT_CONNECTED_TO_CONCRETE_INSTANCE
CLIENT_INIT_CALLBACK_IS_DOLPHINCALLBACK = PROBABLE
```
