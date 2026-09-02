# Mutation candidates

No exact-key reference mutates a compatible `0x18` source element collection.

`FUN_04214768` is the only same-key-related mutator reached from the consumer
pattern. It creates or refreshes a separate cache entry with a `0x20`-byte
layout, then stores a list of matching source values. This is a derived cache
mutation and is excluded by the required ABI and provenance filters.

```text
SELECTOR_KEY_MUTATION_CANDIDATE_COUNT = 0
DERIVED_CACHE_MUTATION_REJECTED = FUN_04214768
REJECTION_REASON = DIFFERENT_COLLECTION_AND_0X20_BYTE_CACHE_ELEMENT
```
