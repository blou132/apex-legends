# Scope

Phase15M is a static, local-only reconstruction of the native graphics
compatibility branch that produced the Phase15L dialog. No ADB endpoint,
emulator, phone, application runtime, network access, binary modification,
hook, debugger, or profiler was used.

The analyzed file is the previously established local-only `libUE4.so`:

- size: `190192944` bytes;
- SHA256: `0D45F92C5D0D2805DA35F223B8E3D352041943C9C2B2B5C914F0E1A1C1EC2977`;
- identity: matches Phase13 and Phase15I.

Addresses use the Phase3C model:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
```

No runtime address is assigned. `LIBUE4_LOAD_BIAS` remains `UNKNOWN`.
Raw Ghidra output and scripts remain under ignored `Analyse/LocalInputs/Phase15M`.
