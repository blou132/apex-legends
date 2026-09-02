# Shutdown this provenance

At thunk entry, `X0` is the secondary-subobject function argument. The exact
thunk normalizes it as:

```text
TOP_DOLPHINUPDATER = SECONDARY_THIS_X0 - 0x28
```

No external direct caller to the thunk was found, so the storage that supplied
the secondary pointer is not visible on this axis.

```text
SHUTDOWN_THIS_SOURCES = SECONDARY_FUNCTION_ARGUMENT_X0_NORMALIZED_MINUS_0x28; EXTERNAL_SOURCE_UNKNOWN
```
