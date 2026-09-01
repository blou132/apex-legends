# Non-null invariant

The readable path tests `W23 == 0` and owner `+0x45 == 0`, then directly loads
`[X19+0x1f0]`, dereferences its vtable, and dispatches slot `+0x10`. There is no
null test for `+0x1f0` between the readable frontier and this dereference.

Therefore the path assumes a non-null field value established before the
readable frontier; the establishing condition or write is not recoverable.

```text
OWNER_PLUS_0X1F0_NULL_GUARD_SITE = NONE_IN_READABLE_CONTINUATION
OWNER_PLUS_0X1F0_NON_NULL_INVARIANT_SOURCE = ESTABLISHED_BEFORE_READABLE_FRONTIER
```
