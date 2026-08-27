# Boundary

The strongest legitimate boundary remains static:

```text
FUN_0050cb38
  -> external callback at CVersionStrategy+0x18
  -> virtual slot +0x28
  -> UNKNOWN
```

Phase16B cannot move that boundary at runtime because the production phone and
package expose no non-invasive native observation mechanism with exact address,
stack, register, or indirect-target visibility.

At the end of the audit:

- Apex was still stopped;
- the package was still installed;
- no APK, data, cache, or OBB was modified;
- no network setting was changed;
- no other phone was targeted;
- no sensitive device identifier is committed.

```text
CURRENT_BLOCKER = PRODUCTION_DEVICE_POLICY_PREVENTS_EXACT_EXTERNAL_CALLBACK_OBSERVATION
APEX_LAUNCHED = NO
APEX_MODIFIED = NO
```
