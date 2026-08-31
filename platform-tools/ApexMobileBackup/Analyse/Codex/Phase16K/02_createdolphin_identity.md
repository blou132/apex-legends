# CreateDolphin identity

The dynamic symbol table exposes `CreateDolphin` as a global default-visible
function at ELF VA `0x0044514c`, size 40 bytes. Its existing Ghidra address is
`0x0054514c`.

The bounded implementation allocates 0x30 bytes and invokes the constructor
helper at Ghidra `0x00545114`. That helper installs primary vptr
`0x00979620`, secondary vptr `0x009796d8`, initializes two string-like fields,
and clears the retained state fields.

This proves that the factory constructs the previously resolved
`GCloudDolphinImp` object.

```text
CREATEDOLPHIN_GHIDRA = 0x0054514c
CREATEDOLPHIN_ELF_VA = 0x0044514c
CREATEDOLPHIN_SYMBOL_CLASS = FUNC GLOBAL DEFAULT; dynamic export; size 40
CREATEDOLPHIN_CONSTRUCTS_GCLOUDDOLPHINIMP = YES
```
