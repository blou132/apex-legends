# CVersionMgr implication

The provider-side chain is one-way and bounded:

```text
GCloudDolphinImp::Init
-> CVersionMgr creation
-> manager slot +0x10
-> CVersionMgrImp::Init
```

The retained `version_mgr_imp.cpp` Init event therefore proves prior
provider-level Dolphin Init execution. It does not alone prove that the caller
was the exact client field dispatch at ELF `0x05a2f0ac`; that association is
probable because the concrete dynamic vptr/acquisition source remains hidden.

```text
VERSIONMGR_INIT_IMPLIES_DOLPHIN_INIT = YES
```
