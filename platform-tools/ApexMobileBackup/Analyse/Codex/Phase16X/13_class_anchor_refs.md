# Class anchor references

The class anchor and selector key are the same `UClass*` cell. The bounded
reference set therefore contains:

- 24 lookup/type-check functions;
- 1 class accessor which invokes lazy class metadata registration.

No exact reference is an instance construction or source-registry insertion.

```text
SELECTED_ENTRY_CLASS_REFERENCE_COUNT = 25_UNIQUE_FUNCTIONS
CLASS_REFERENCE_TYPE_CHECK_OR_LOOKUP = 24
CLASS_REFERENCE_REGISTRATION_ACCESSOR = 1
CLASS_REFERENCE_CONSTRUCTION = 0_PROVEN
```
