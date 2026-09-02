# Stack callback candidates

Provider ABI mapping selects `X5`, not `X1` or `X2`, as the callback. The two
stack locals therefore are not callback candidates and were not traced beyond
their already established client sources.

```text
SP680_OBJECT_CONSTRUCTION = NOT_SELECTED
SP680_TABLE_VALUE = NOT_APPLICABLE
SP680_PLUS8_VALUE = NOT_APPLICABLE
SP280_OBJECT_CONSTRUCTION = NOT_SELECTED
SP280_TABLE_VALUE = NOT_APPLICABLE
SP280_PLUS8_VALUE = NOT_APPLICABLE
CALLBACK_CONSTRUCTION_STATIC_FRONTIER = OPAQUE_PRELUDE
```
