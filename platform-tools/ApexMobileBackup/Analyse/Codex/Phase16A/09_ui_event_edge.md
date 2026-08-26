# UI and event edge

The resolved callback does not naturally reach a UE4 `ProcessEvent`,
Blueprint/UMG dispatcher, Lua bridge, EventSystem module, Android Java method,
or visible error formatter.

No conditional `libUE4.so` lookup was performed because the final virtual
target remains an externally supplied object with no exact class, symbol,
relocation, or function-pointer anchor.

```text
DOLPHIN_TO_UE4_EDGE = NO_NEW_EDGE
DOLPHIN_TO_LUA_EDGE = NO_NEW_EDGE
UPDATERESULT_EVENT_ROLE = REPORTING_ONLY
```

The Phase15Z `UpdateResult` path remains separate and reporting-only.
