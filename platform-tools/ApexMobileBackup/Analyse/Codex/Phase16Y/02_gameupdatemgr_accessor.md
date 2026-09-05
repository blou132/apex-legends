# GameUpdateMgr accessor

The exact lazy accessor remains:

- Ghidra: `0x081b1348`
- ELF: `0x080b1348`
- UClass cell: Ghidra `0x0b7bec20`
- Package literal: `/Script/PureClient`
- Class literal storage: `UGameUpdateMgr`; the registration argument points
  to `GameUpdateMgr`
- Class size: `0x60`
- Alignment: `0x08`

The accessor returns the cached cell when initialized and otherwise invokes
the bounded class-registration helper once.
