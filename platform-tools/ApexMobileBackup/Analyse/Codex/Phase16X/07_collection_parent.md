# Collection parent

The exact class name of `[argument0+0x168]` is not proven. Its internal layout
is, however, distinguishable:

```text
parent = [SkipAppUpdate argument0+0x168]
wrapper = parent+0xe8
source registry = wrapper+0x10 = parent+0xf8
derived class cache = wrapper+0x60
```

`FUN_04214768` inserts a class-filtered result list into the derived cache. It
reads the source registry but does not mutate it.

```text
GAMEUPDATEMGR_COLLECTION_PARENT = UNKNOWN_CLASS_OBJECT_AT_[ARG0_PLUS_0X168]
GAMEUPDATEMGR_COLLECTION_FIELD_CHAIN = PARENT_PLUS_0XE8_WRAPPER_PLUS_0X10_SOURCE_REGISTRY
```
