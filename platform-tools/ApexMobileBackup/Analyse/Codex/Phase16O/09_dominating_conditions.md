# Dominating conditions

No ordinary condition selects the `CreateDolphin` block on a legitimate path.
The raw predecessor region contains conditions at `0x05a31454` and
`0x05a31460` for temporary release, and at `0x05a3147c` for optional logging.
Both branches of each condition reconverge before the factory call.

The only prerequisite that distinguishes the region from legitimate flow is
the impossible return from the noreturn helper at `0x05a3144c`. It is not a
register predicate or comparison that can be backward-sliced to an argument,
field, or status value.

```text
CREATEDOLPHIN_DOMINATING_CONDITIONS = NONE_SELECTIVE; IMPOSSIBLE_NORETURN_FALLTHROUGH_REQUIRED
CALLSITE_SELECTION_VALUE_SOURCE = NO_REGISTER_VALUE
CALLSITE_SELECTION_COMPARISON = NO_BRANCH_COMPARISON; REQUIRES_NORETURN_RETURN
```
