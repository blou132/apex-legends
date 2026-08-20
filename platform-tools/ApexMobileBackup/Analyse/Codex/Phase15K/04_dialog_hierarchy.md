# Dialog hierarchy

The baseline capture capability was confirmed, but the Apex hierarchy capture
was not attempted after the persistent SystemUI ANR triggered the stop gate.

No package, class, resource ID, or node text can be assigned to the unresolved
Apex dialog from this phase. Phase15J's Apex-owned `GameActivity` window
boundary remains valid, but it does not resolve the hierarchy.

```text
DIALOG_PACKAGE = UNKNOWN_NOT_CAPTURED
DIALOG_CLASS = UNKNOWN_NOT_CAPTURED
DIALOG_RESOURCE_IDS = UNKNOWN_NOT_CAPTURED
APEX_DIALOG_VISUAL_CONFIRMED = NO_NOT_LAUNCHED
```
