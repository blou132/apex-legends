# Bootstrap owner

`libUE4.so` is the sole exact native module importing `CreateDolphin`, making
it the confirmed client boundary and strongest owner-module candidate. The
only decoded direct call is not semantically compatible with the factory ABI,
and its caller chain is a post-extraction callback path.

No RTTI, vtable, owner field, or valid Init dispatch resolves the semantic
bootstrap class or function.

```text
DOLPHIN_BOOTSTRAP_OWNER_MODULE = UNKNOWN; libUE4.so is sole import candidate
DOLPHIN_BOOTSTRAP_OWNER_CLASS = UNKNOWN
DOLPHIN_BOOTSTRAP_OWNER_FUNCTION = UNKNOWN
```
