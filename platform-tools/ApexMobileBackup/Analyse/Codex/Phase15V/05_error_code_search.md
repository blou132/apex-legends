# Error-code search

Phase15U had already found neither exact `I54140714` nor exact `I54140715` in
the preserved APK resources, DEX material, `libUE4.so`, PAKs, or existing PAK
extraction.

Phase15V added exact numeric checks:

- decimal `54140714` / hex `0x033a1f2a`: no 32-bit or 64-bit anchor in
  `libgcloud.so` or `libUE4.so`;
- decimal `54140715` / hex `0x033a1f2b`: no 32-bit or 64-bit anchor in
  `libgcloud.so` or `libUE4.so`.

The six bounded Puffer pivot functions contain no connected `I54`, `5414`,
`0714`, `0715`, standalone `714`, or standalone `715` token. No partial-token
hit was promoted from outside the proven update neighborhood.

The Puffer internal network codes (`0x0430002e` through `0x04300032`) are not
equal to the visible decimal code and no arithmetic, lookup, prefix formatting,
or mapping from them to `I54140714` was found.

```text
54140714_INTEGER_ANCHOR = NOT_FOUND
54140715_INTEGER_ANCHOR = NOT_FOUND
ERROR_CODE_CONSTRUCTION_STYLE = DYNAMIC_UNKNOWN
I54140714_CONSTRUCTION_CONFIRMED = NO
I54140715_VARIANT_RELATION = UNKNOWN
```
