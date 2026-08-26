# Error handling

`FUN_0050cb38` does three things after confirming that the external client
callback exists:

1. It optionally stores the raw code at `CVersionStrategy+0x28`.
2. It inspects selected bit fields and low reason values for internal manager
   control, including `(error >> 20) & 7`, `error & 0xfffff`, and reasons
   `0x27`, `0x70`, and `0x1c`.
3. It always forwards the original 32-bit stage and raw error to the external
   callback, then records unsigned decimal `ERR STAGE` and `ERR CODE` metadata.

The bit tests do not replace the forwarded value. There is no subtraction,
prefix construction, localization lookup, table conversion, or formatter for
`I54140714` in the resolved callback.

```text
CALLBACK_STAGE_ARGUMENT = uint32 stage, forwarded unchanged
CALLBACK_ERROR_ARGUMENT = uint32 raw error, forwarded unchanged
DOLPHIN_CLIENT_ERROR_HANDLING = FORWARDED
STAGE_69_CLIENT_MEANING = UNKNOWN
CLIENT_ERROR_MAPPER = NONE_IN_RESOLVED_CALLBACK
CLIENT_ERROR_MAPPER_INPUT = 0x0930002a
CLIENT_ERROR_MAPPER_OUTPUT = 0x0930002a
I54140714_CONSTRUCTION_CONFIRMED = NO
```
