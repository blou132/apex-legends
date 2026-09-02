# Collection lookup ABI

`FUN_04214458(collection, &key)` performs a hash lookup keyed by `UClass*`.
It returns the signed element index on success and `-1` on failure. The
consumer then calculates `elements + index * 0x18`.

The helper hashes the class pointer and follows the element's third field as
an index chain. Analysis stopped after establishing this bounded ABI.

```text
COLLECTION_LOOKUP_HELPER = FUN_04214458
COLLECTION_LOOKUP_RETURN_SEMANTICS = SIGNED_INDEX_IN_0X18_BYTE_ELEMENT_STORAGE
COLLECTION_LOOKUP_NOT_FOUND_VALUE = -1
```
