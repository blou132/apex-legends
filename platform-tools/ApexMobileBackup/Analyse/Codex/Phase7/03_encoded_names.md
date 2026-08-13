# Phase7 - Encoded names

## Script path

- **CONFIRMED fragment:** `[8 encoded UTF-16 code units]ools/EventSystem/EventSystem.lua`
- **PROBABLE complete value:** `Script/Tools/EventSystem/EventSystem.lua`
- Encoded data: Ghidra `0x23e6d50`, ELF `0x22e6d50`

## Dynamic function name

- **CONFIRMED fragment:** `EventSystem.Post[8 encoded UTF-16 code units]`
- **PROBABLE complete value:** `EventSystem.PostCppEvent`
- Encoded data: Ghidra `0x23e76e0`, ELF `0x22e76e0`

`FUN_06be3f4c` builds both values and passes them to bridge `FUN_06be427c`. The bridge's conversion helpers `FUN_0427a4ac` and `FUN_0427a69c` perform ordinary UTF-16-to-UTF-8 sizing/copy operations. No static transform of the eight encoded units occurs in the emitter or bridge before lookup.

The strings therefore remain **PROBABLE**. Promoting them to **CONFIRMED** would require the missing initialization/decode path or a legitimate runtime value; neither is available here.
