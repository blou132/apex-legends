# Next step

Do not continue with root preparation. The phone remains locked and stock.

Before any separately authorized retry, the operator should reassemble and
health-check the PRA-LX1. A retry must start from the beginning, revalidate all
hashes and live identity, confirm the already-installed temporary fastboot
driver, and reproduce bootrom enumeration without repeated probing. It must use
the same `Kirin 65x (A)` profile and keep FBLOCK unchanged.

If bootrom cannot be reproduced cleanly, stop permanently rather than repeat
testpoint attempts.

```text
NEXT_STEP = SEPARATE_RETRY_DECISION_BEFORE_ANY_PHASE16G_WORK
PHASE16G_MAGISK_ROOT_PREPARATION = NOT_READY
```
