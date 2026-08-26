# NormalConnectVersionSvr

`NormalConnectVersionSvr` is owned by `FUN_00550ee4`:

- Ghidra: `0x00550ee4`
- ELF: `0x00450ee4`
- source anchor: `GcloudDolphinVersionAction.cpp`
- direct caller: `FUN_005541a0`

The offline branch checks the network state and constructs the failure code at
Ghidra `0x005516e8`-`0x005516f4`:

```text
mov  w1,#0x2a
mov  x0,x19
movk w1,#0x930, LSL #16
bl   0x00549800
```

This produces `0x0930002a` and passes it to `FUN_00549800` without a table
lookup or arithmetic remap.

NORMAL_CONNECT_VERSION_SVR_FUNCTION = FUN_00550ee4
NORMAL_CONNECT_VERSION_SVR_GHIDRA = 0x00550ee4
NORMAL_CONNECT_VERSION_SVR_ELF = 0x00450ee4
