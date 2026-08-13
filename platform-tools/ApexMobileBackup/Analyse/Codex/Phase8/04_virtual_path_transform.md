# Phase8 - Virtual path transform

## Confirmed stages

1. `FUN_06be427c` performs a case-insensitive `.LUA` suffix check and removes four UTF-16 code units before calling the Lua-side dispatcher.
2. The resulting module name is converted UTF-16 to UTF-8 and passed into the Lua lookup/`require` path. The exact package searcher that converts this module name back to a file candidate is not resolved.
3. When `FUN_049a8b54` receives a file candidate, it preserves that candidate in an `FString`, obtains a current base path from `FUN_045a4384`, and obtains an alternate prefix from `0xb7c7020`.
4. `FUN_04166f64` case-insensitively replaces occurrences of the current base prefix with the alternate prefix. It is a string replacement helper, not a name hash.
5. The fallback backend runs `FUN_046095e4`, which performs separator/path cleanup. Plain constants confirm `// -> /` and `/./ -> /` handling; other encoded constants are not assigned unsupported meanings.
6. `FUN_04609488` computes a case-folded, table-driven CRC-like bucket value over UTF-16 code units for its registered-path map, then confirms a hit with a full case-insensitive string comparison. This bucket hash is internal map indexing, not a proven archive entry key.

## What is not resolved

- the exact eight encoded UTF-16 units at the beginning of the EventSystem logical path;
- the Lua package search pattern between extensionless module name and file candidate;
- whether `.lua` is restored, rewritten, or omitted;
- the effective runtime alternate prefix;
- a PAK index key or container entry identifier.

For a relative path already passed to `FUN_049a8b54`, no separate hashing or lowercasing is visible before the file facade. That does not establish which path the Lua package searcher passes in.

```text
PATH_MAPPING_STATUS = UNKNOWN
NO_ARCHIVE_NAME_HASH_PROVEN = YES
```
