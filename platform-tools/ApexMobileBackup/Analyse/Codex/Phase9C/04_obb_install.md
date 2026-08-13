# OBB installation

## Result

```text
OBB_INSTALLED = YES
```

After APK installation, the package OBB directory was created on the Huawei test device and the two verified local OBBs were copied.

| Remote artifact | Expected size | Observed size | Match |
| --- | ---: | ---: | --- |
| main OBB | 1,942,013,346 | 1,942,013,346 | `YES` |
| patch OBB | 1,837,582,506 | 1,837,582,506 | `YES` |

No OBB was modified. Remote SHA256 was not recomputed because exact local hashes were verified before transfer and exact remote byte sizes were verified afterward.

Runtime OBB consumption remains `NOT_CONFIRMED`: the first-run log did not name either OBB, so file presence is not promoted to proof that the client opened them.
