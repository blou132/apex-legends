# Control validation

The exact-target scanner recovered the known direct edge:

```text
DolphinCallback::OnDolphinFirstExtractSuccess
ELF 0x07ff8834 -> ELF 0x07ff6f04
```

It retained only the four authorized method anchors and the exact CheckUpdate
FDE range. No generic indirect-call or callgraph scan was used.

```text
EXACT_BRANCH_SCANNER_CONTROL_VALID = YES
METHOD_CALLER_ANALYSIS_INDEPENDENT_OF_VTABLE = YES
```
