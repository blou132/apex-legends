# Reflected +0x38 writes

No reflected GameUpdateMgr method candidate was proven, so no class-scoped
setter path could be inspected.

`REFLECTED_PLUS0X38_WRITE_SITE = UNKNOWN`

`REFLECTED_PLUS0X38_WRITE_CLASS = UNKNOWN`

`REFLECTED_PLUS0X38_VALUE_SOURCE = UNKNOWN`

The result does not alter the previously proven non-null `+0x38` consumption
on the CheckUpdate path.
