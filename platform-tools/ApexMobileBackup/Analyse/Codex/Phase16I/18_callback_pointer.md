# Callback pointer

Because `CVersionMgrImp::Init` did not trap, `X1` was not captured and the
external callback container was not read.

```text
EXTERNAL_CALLBACK_POINTER_CAPTURED = NO
CALLBACK_POINTER_MAPPING_VALID = NOT_TESTED
CVERSIONMGR_THIS_RUNTIME_VALIDATED = NOT_TESTED
```

No heap scan or substitute inference was attempted.
