# Error mapping

`FUN_004ffd44` is a confirmed Puffer-internal failure classifier: it maps RPC
setup, timeout, connection, callback, and cancellation states to the internal
`0x043000xx` family. `FUN_004fcaf4` packages that result for the result object,
and `FUN_004ee1dc` forwards it to a dynamic manager callback.

No client-visible mapper, switch, pair table, lookup array, or formatting
function connected this family to `I54140714`. The visible code has neither a
literal nor numeric anchor in the two relevant native binaries checked.

```text
PUFFER_INTERNAL_FAILURE_MAPPING_FUNCTION = FUN_004ffd44
ERROR_MAPPING_FUNCTION = UNKNOWN
ERROR_MAPPING_TABLE = NOT_FOUND_IN_BOUNDED_CHAIN
NETWORK_FAILURE_TO_CLIENT_ERROR_MAPPING = UNKNOWN
I54140714_PUFFER_DIRECT_LINK = NO_DIRECT_STATIC_EDGE
```

The runtime timing remains useful correlation, but it does not promote the
missing client-error edge.
