# Dolphin owner

`libUE4.so` remains the confirmed import module, but the only exact static
factory call does not retain its return. Its containing chain reaches the
already identified post-extraction callback rather than a persistent
bootstrap object.

No owner object, global, service field, wrapper, or returned interface can be
promoted from this path.

```text
DOLPHIN_OWNER = UNKNOWN
DOLPHIN_OBJECT_STORAGE = NONE_AT_PROVEN_CALLSITE
DOLPHIN_OWNER_FIELD = NONE
```
