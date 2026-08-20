# Phase15M gate classification

Phase15M proves that environment compatibility depends on the exact parsed
GLES predicate `major == 3 && minor >= 1`. Float render-target support is
reported separately and is not a rejecting operand in that resolved branch.

The `host` result passes GLES 3.1. Therefore:

```text
PASSING_GLES31_MODES = host
LEGITIMATE_GRAPHICS_MODE_CANDIDATE = host
APEX_PHASE15M_GATE_COMPATIBLE = YES
APEX_FLOAT_RT_DIAGNOSTIC = PASS
FINAL_GATE = A LEGITIMATE_GLES31_AND_FLOAT_RT_MODE_FOUND
```

This is a clean-room environment classification only. No Apex package was
installed or launched, so it does not prove client startup, rendering, or any
later bootstrap stage under `host` mode.
