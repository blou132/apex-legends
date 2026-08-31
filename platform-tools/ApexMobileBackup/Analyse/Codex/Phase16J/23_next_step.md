# Next step

The remaining blocker is above `GCloudDolphinImp::Init`: identify the
client-side indirect call or state predicate that selects the Dolphin version
bootstrap. The next work should remain PC-only and bounded to the
`CreateDolphin` owner/registration and the caller of the Dolphin Init vtable
slot.

Do not perform another active breakpoint attempt yet. Thread-10 is resolved,
but an unchanged offline launch does not deterministically invoke the target.
Do not clear AppData or reinstall unless a later static result proves a fresh
state requirement and the owner separately authorizes that destructive step.
