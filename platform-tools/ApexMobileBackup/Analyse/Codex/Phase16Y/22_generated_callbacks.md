# Generated callback classification

| Callback role | Address (Ghidra) | Classification |
| --- | --- | --- |
| Native registration | `0x0a2aed54` | `NON_DISASSEMBLED_OR_OPAQUE` |
| Class constructor | `0x081abe34` | `NON_DISASSEMBLED_OR_OPAQUE` |
| VTable helper | `0x0a2aed58` | `NON_DISASSEMBLED_OR_OPAQUE` |
| AddReferencedObjects | `0x083e660c` | `GENERIC_ENGINE_STUB` |
| Superclass accessor | `0x07a43ac8` | `NORMAL_EXECUTABLE_FUNCTION` |
| Within-class accessor | `0x0647eb28` | `NON_DISASSEMBLED_OR_OPAQUE` |

The normal superclass accessor and generic stub support the accepted map.
An earlier helper also requested decompilation of the opaque targets. Its
bad-data output supplies no valid ABI, field-write, or lifecycle evidence and
is excluded. See [30_boundary.md](30_boundary.md).
