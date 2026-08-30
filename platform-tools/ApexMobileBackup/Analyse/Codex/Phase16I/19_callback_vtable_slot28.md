# Callback vtable slot +0x28

The callback object was unavailable at runtime, so the bounded vptr and virtual
slot reads were not performed.

```text
EXTERNAL_CALLBACK_VTABLE_CAPTURED = NO
EXTERNAL_CALLBACK_SLOT_28_TARGET_CAPTURED = NO
SLOT_28_TARGET_EXECUTABLE_MAPPING_VALID = NOT_TESTED
```

The static dispatch semantics remain confirmed; the concrete target remains
unknown.
