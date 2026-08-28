# Root preparation gate

## Criteria

| Criterion | Result | Evidence |
| --- | --- | --- |
| A. Exact PRA-LX1 C33 identity strongly validated | PASS | Published MD5, exact internal docs, CUST contents, all checksums valid |
| B. Exact stock RAMDISK local and hashed | PASS | `RAMDISK.img`, 16777216 bytes, pinned SHA256 |
| C. Stock recovery material local | PASS | Recovery ramdisk/vendor/vbmeta set extracted and hashed |
| D. Installed build sufficiently matches package | PASS | Model/build/CUST exact, confidence HIGH |
| E. Credible testpoint reference validated | PASS_WITH_MEDIUM_CONFIDENCE | Three matching visuals plus exact-model functional corroboration |
| F. PotatoNV Kirin655/PRA support confirmed | PASS | Current upstream CPU and tested-device tables |
| G. Apex restorable after expected wipe | PASS | APK and both OBBs rehashed; install docs present |
| H. Recovery procedure documented | PASS_WITH_MEDIUM_CONFIDENCE | Exact dload guide plus probable partition restoration paths |

```text
ROOT_PREPARATION_GATE = GO
FINAL_GATE = A ROOT_PREPARATION_GO_PC_EVIDENCE_COMPLETE
```

`GO` means that the Phase16C PC-evidence blockers are resolved. It does not
authorize opening the phone, entering testpoint mode, running PotatoNV,
unlocking, wiping, flashing, or rooting. Those actions require a distinct
explicit authorization after a final preflight.
