# Control validation

The exact unrelated control address point ELF `0x0ae56918` was tested with
the same relocation-alias, raw-pointer, target-page, and direct-ADR methods.
All counts are zero. This is a valid negative control: the method does not
promote nearby page references merely because they share the target page.

One of the eight page references forms a different neighboring address point
at `0x0ae569e8`; it is rejected from both the DolphinUpdater group and the
specified control group.

`ADDRESS_POINT_PROVENANCE_METHOD_CONTROL_VALID = YES`
