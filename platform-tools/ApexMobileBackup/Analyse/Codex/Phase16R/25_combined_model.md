# Static/runtime combined model

The strongest combined model is:

| Stage or edge | Classification | Evidence |
|---|---|---|
| Interface acquisition source | UNKNOWN | Non-null writer remains opaque |
| `owner+0x1f0` present in Phase15U | PROBABLE | Provider Init/version-manager witness, exact client provenance not exclusive |
| `bUseIFSFile` lookup and boolean parser produce W23 | CONFIRMED | Bounded helper analysis |
| W23 selects normal versus first-extract preparation | CONFIRMED | Direct CheckUpdate branches |
| `owner+0x45` selects Init argument mode | CONFIRMED | Zero and nonzero routes join ELF `0x05a2f0ac` |
| Shared client Init dispatch | CONFIRMED_STATIC | ELF `0x05a2f0ac`, slot `+0x10` |
| Phase15U provider Init | CONFIRMED_INDIRECT | `CVersionMgrImp::Init` one-way implication |
| Exact Phase15U client dispatch | PROBABLE | Dynamic interface target/provenance unresolved |
| First-extract success clears `+0x45` | CONFIRMED_STATIC | Callback continuation store |
| First-extract clear in retained runs | UNKNOWN | No post-store marker |

```text
UNKNOWN_ACQUISITION
-> INTERFACE_PRESENT (PHASE15U PROBABLE)
-> W23 NORMAL_OR_FIRST_EXTRACT_SELECTION (VALUE UNKNOWN PER RUN)
-> FLAG45 INIT_ARGUMENT_MODE (VALUE UNKNOWN PER RUN)
-> INIT SLOT +0x10 (STATIC CONFIRMED; PHASE15U EXACT CALL PROBABLE)
-> CVERSIONMGR INIT (PHASE15U CONFIRMED)
-> VERSION ACTION (PHASE15U CONFIRMED)
```
