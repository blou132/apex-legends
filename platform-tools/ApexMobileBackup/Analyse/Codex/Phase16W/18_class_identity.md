# Class identity

The provider ABI, known callback slot layout, callback forwarding semantics,
and CheckUpdate owner consumers all support the existing DolphinUpdater and
DolphinCallback labels. The concrete callback instance's table installation
and owner pointer assignment are still missing.

```text
CLIENT_INIT_CALLBACK_IS_DOLPHINCALLBACK = PROBABLE
DOLPHINUPDATER_CLASS_IDENTITY = PROBABLE
```

Independent storage convergence required for `CONFIRMED` was not recovered.
