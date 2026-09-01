# Owner register provenance

`X19` is the `DolphinUpdater` base at the field accesses:

- ELF `0x05a2f088`: byte load at `+0x45`;
- ELF `0x05a2f094`: pair load at `+0x1f0/+0x1f8`;
- ELF `0x05a2f0b4`: post-Init reload at `+0x1f0`.

No instruction in the reliable prefix-to-dispatch path defines `X19`. The
function epilogue later restores `X19`, proving callee-saved use, but the
assignment from an argument or stack spill lies before the readable frontier.

```text
CHECKUPDATE_OWNER_REGISTER = X19
CHECKUPDATE_OWNER_PROVENANCE = CALLEE_SAVED_REGISTER_FROM_OPAQUE_PRELUDE_ORIGINAL_SOURCE_UNKNOWN
```
