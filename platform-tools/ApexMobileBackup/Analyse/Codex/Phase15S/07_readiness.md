# Runtime-lab readiness

The device identity, package, OBB state, ADB stability, and graphics capability
all pass. Storage does not: final free space is about `0.41-0.43 GiB`, below the
mandatory `2 GiB` minimum.

No approved expendable target can close this gap. Apex is the only third-party
package, its OBBs are the dominant shared-storage consumer, normal media and
Download directories are empty, and all remaining large non-Apex packages are
preinstalled system software outside this phase's cleanup authorization.

The phone must not proceed to the runtime phase under the current storage
condition. A later action requires either a separately approved, package-by-
package cleanup of expendable preinstalled applications or a different device;
Apex itself must remain intact.

```text
PHONE_STORAGE_READY = NO
PHONE_LAB_READY_FOR_RUNTIME = NO
FINAL_GATE = B SAFE_FREE_SPACE_TARGET_NOT_REACHED
```
