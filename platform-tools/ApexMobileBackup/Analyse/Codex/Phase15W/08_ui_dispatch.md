# UI and update dispatch

The resolved callback path does not reach a named client update manager, UE4
widget, UMG dispatch, Android Java method, Lua bridge, or EventSystem edge.
UIAutomator/runtime observations from earlier phases are not used to assign a
static owner.

```text
UPDATE_MANAGER_FUNCTION = UNKNOWN
UPDATE_MANAGER_CLASS = UNKNOWN
UPDATE_UI_DISPATCH_FUNCTION = UNKNOWN
UPDATE_UI_OWNER = UNKNOWN
PUFFER_TO_LUA_DIRECT_EDGE = NO
LOGIN_DIRECT_EDGE_FROM_UPDATE = NO_NEW_EDGE
SERVER_LIST_DIRECT_EDGE_FROM_UPDATE = NO_NEW_EDGE
```
