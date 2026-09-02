# Selector key

The selector is not an FName, integer, or string key. It is a `UClass*` held
in a lazy global cell. The exact class-registration literals are:

```text
PACKAGE = /Script/PureClient
CLASS = GameUpdateMgr
CLASS_SIZE = 0x60
CLASS_ALIGNMENT = 0x08
```

`FUN_081b1348` is the bounded class accessor: it returns the cell value and
initializes the class metadata when needed.

```text
GAMEUPDATEMGR_SELECTOR_KEY_KIND = UCLASS_POINTER_IN_LAZY_GLOBAL_CELL
GAMEUPDATEMGR_SELECTOR_KEY_IDENTITY = /Script/PureClient.GameUpdateMgr
SELECTOR_KEY_CONFIDENCE = CONFIRMED
```
