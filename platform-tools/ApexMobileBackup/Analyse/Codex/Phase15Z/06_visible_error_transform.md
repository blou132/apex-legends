# Visible error transform

The arithmetic relation remains exact:

`154140714 - 100000000 = 54140714`

However, the bounded connected path contains no use of decimal `100000000`,
hexadecimal `0x05f5e100`, decimal `54140714`, an eight-digit extraction, or an
`I` prefix formatter.

`FUN_005487ec` formats the unchanged raw code with `%u`, but this string is sent
to the reporting path as `errcode`. It does not subtract `100000000`, prepend
`I`, or render a user-facing message.

REMOVE_100M_TRANSFORM = NO
VISIBLE_ERROR_PREFIX_SOURCE = UNKNOWN
VISIBLE_ERROR_FORMATTER = UNKNOWN
ERROR_FORMAT_FUNCTION = FUN_005487ec (reporting only)
ERROR_FORMAT_STRING = %u
ERROR_FORMAT_INPUT_CODE = 0x0930002a unchanged
I54140714_CONSTRUCTION_CONFIRMED = NO
