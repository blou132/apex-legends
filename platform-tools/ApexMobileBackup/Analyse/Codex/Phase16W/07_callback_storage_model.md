# Callback storage model

The owner field at `+0x1f8` contains a nullable pointer. It is not passed as an
inline address (`owner+0x1f8`); the field value is loaded and passed in `X5`.

```text
DOLPHINCALLBACK_STORAGE_MODEL = OWNER_FIELD_POINTER
CONCRETE_TARGET_ALLOCATION_MODEL = UNKNOWN
```

Static evidence does not distinguish an independently allocated object from a
pointer to a subobject owned elsewhere.
