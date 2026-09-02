# Parent or global owner

The exact source file and callback method identifiers establish the storage
base as a `DolphinCallback` object. Its table is already proven at
`0x0af55170`; the owner backpointer is its field at `+0x08`.

No global cell was reached, and no higher containing parent offset is proven.

```text
DOLPHINUPDATER_PARENT_OBJECT = DOLPHIN_CALLBACK_SUBOBJECT
DOLPHINUPDATER_PARENT_FIELD = +0x08
DOLPHINUPDATER_GLOBAL_STORAGE = NONE
```
