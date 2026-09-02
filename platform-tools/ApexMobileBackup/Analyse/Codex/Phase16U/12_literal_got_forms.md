# Literal and GOT/data forms

No AArch64 64-bit literal load resolves to either address point. No GOT/data
indirection candidate exists because neither address point nor a VTT has a
relocation-backed alias.

- `AP_LDR_LITERAL_COUNT = 0`
- `AP_GOT_OR_DATA_INDIRECTION_COUNT = 0`
