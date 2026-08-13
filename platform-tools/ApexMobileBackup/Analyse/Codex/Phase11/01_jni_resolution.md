# JNI resolution

## DEX declaration

The local DEX declares an instance method in
`com.epicgames.ue4.GameActivity`:

```text
public native nativeResumeMainInit()V
```

This confirms a Java return type of `void`, no Java arguments, and an instance
receiver. The corresponding native ABI, if represented as a conventional JNI
function, is `void(JNIEnv *, jobject)`.

## ELF evidence

The exact full JNI name occurs once in `.dynstr` at Ghidra `0x21dcc9d`
(ELF virtual offset `0x20dcc9d`). Its `nativeResumeMainInit` suffix begins at
Ghidra `0x21dccc1` (ELF virtual offset `0x20dccc1`). A string occurrence alone
does not identify code.

The authoritative dynamic tags are:

| Item | ELF virtual offset | Ghidra address |
|---|---:|---:|
| `DT_STRTAB` | `0x20dc658` | `0x21dc658` |
| `DT_SYMTAB` | `0xb8cc5b8` | `0xb9cc5b8` |
| `DT_HASH` | `0x5904` | `0x105904` |

`DT_HASH` reports 824 dynamic symbols. None names the target. The section-sized
`.dynsym` view contains 835 entries and also contains no target entry. Ghidra
has no exact symbol or named function for the JNI name.

## Registration fallbacks

The exporter found no unique candidate through:

- an exact Ghidra symbol or function;
- the authoritative dynamic symbol table;
- the section-sized dynamic symbol table;
- absolute or image-relative `JNINativeMethod` pointers;
- `.rela.dyn` addends to the full name or its method-name suffix;
- direct AArch64 `ADR` or `ADRP+ADD` materialization of either address.

The AArch64 pass checked 1,179,307 decoded `ADRP` instructions while retaining
only exact target matches; it found no owner function. No candidate was chosen
by proximity or naming guess.

```text
NATIVE_RESUME_MAIN_INIT_FUNCTION = UNKNOWN
GHIDRA_ADDRESS = UNKNOWN
ELF_VIRTUAL_OFFSET = UNKNOWN
RESOLUTION_METHOD = EXHAUSTED_EXACT_SYMBOL_DYNAMIC_TABLE_RELOCATION_AND_NAME_XREFS
CONFIDENCE = CONFIRMED_ENTRY_NOT_RESOLVED_BY_TESTED_STATIC_PATHS
```
