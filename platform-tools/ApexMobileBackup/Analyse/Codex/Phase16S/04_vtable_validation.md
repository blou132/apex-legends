# Vtable validation

The Shutdown singleton has local executable-pointer density and a nearby
Itanium-style negative offset/zero boundary. Those features support only the
weak statement that the cell is inside generic C++ dispatch material.

Validation fails for a concrete DolphinUpdater vtable because:

- no second known owner method is present in the bounded window;
- no table start is proven;
- no RTTI/typeinfo identifies DolphinUpdater;
- no constructor vptr write can be anchored to an exact table address.

`DOLPHINUPDATER_VTABLE = UNKNOWN`

`DOLPHINUPDATER_VTABLE_CLASSIFICATION = UNKNOWN`

`DOLPHINUPDATER_VTABLE_CONFIDENCE = NONE`
