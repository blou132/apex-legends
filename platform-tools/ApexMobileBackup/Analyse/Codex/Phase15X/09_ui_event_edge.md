# UI, event, and Lua edge

The exact object-flow chain ends before a client callback implementation. It
does not reach `UObject::ProcessEvent`, a Blueprint/UMG event, a UE4 delegate,
the known native-to-Lua bridge, EventSystem, Android Java, or a UI dispatch
function.

```text
PUFFER_TO_UE4_EVENT_EDGE = NO
UE4_EVENT_FUNCTION = UNKNOWN
UE4_EVENT_NAME = UNKNOWN
PUFFER_TO_LUA_DIRECT_EDGE = NO_NEW_EDGE
UPDATE_UI_OWNER = UNKNOWN
UPDATE_UI_DISPATCH_FUNCTION = UNKNOWN
```
