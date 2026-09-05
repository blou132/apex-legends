# GameUpdateMgr property array

No GameUpdateMgr-local reflected property array is directly reachable from
the recovered class accessor arguments. Therefore neither an array base nor a
property count can be established without broadening beyond the authorized
class-scoped metadata path.

`GAMEUPDATEMGR_REFLECTED_PROPERTY_COUNT = UNKNOWN`

`GAMEUPDATEMGR_PROPERTY_ARRAY = UNKNOWN`

This is not evidence that the class has no reflected properties. It is only a
limit of the statically exposed registration path in this binary.
