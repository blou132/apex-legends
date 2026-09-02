# Selected entry class

`SkipAppUpdate` checks the selected value against the exact selector class.
The check compares inheritance depth and the superclass table entry, proving
that the value is an instance of `/Script/PureClient.GameUpdateMgr` or a
derived class.

The generic wrapper `FUN_045f0d64` independently performs the same lookup and
class validation before returning the object.

```text
SELECTED_ENTRY_CLASS = /Script/PureClient.GameUpdateMgr_OR_DERIVED
SELECTED_ENTRY_CLASS_CONFIDENCE = CONFIRMED
SELECTED_ENTRY_CLASS_CHECK_KIND = UNREAL_INHERITANCE_DEPTH_AND_SUPERCLASS_TABLE_CHECK
SELECTED_ENTRY_EXPECTED_CLASS_ANCHOR = UCLASS_/Script/PureClient.GameUpdateMgr
```
