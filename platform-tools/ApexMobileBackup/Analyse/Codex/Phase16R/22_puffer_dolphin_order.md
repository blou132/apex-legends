# Puffer and Dolphin order

In Phase15U, GCloud/Puffer initialization precedes the provider Dolphin Init
witness by about 1.6 seconds. Phase16I and Phase16M reach Puffer activity
without a retained Dolphin witness.

This supports only a same-run Phase15U ordering. It does not establish that
Puffer completion, success, or failure calls Dolphin Init.

```text
PUFFER_DOLPHIN_RUNTIME_ORDER = PUFFER_BEFORE_DOLPHIN_IN_PHASE15U_ONLY; NO_UNIVERSAL_DEPENDENCE
```
