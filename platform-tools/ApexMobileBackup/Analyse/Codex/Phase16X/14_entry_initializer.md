# Entry initializer

The exact class metadata registration provides generated callbacks, but it
does not expose a proven `GameUpdateMgr` instance initializer in the existing
read-only analysis. Several callback addresses are non-disassembled data or
generic stubs; following them would cross into the forbidden protector axis.

Because no instance creation or registration producer was established, none
of these callbacks is accepted as the selected entry initializer.

```text
SELECTED_ENTRY_INITIALIZER = UNKNOWN
SELECTED_ENTRY_INITIALIZER_CONFIDENCE = NONE
```
