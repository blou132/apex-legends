# Evidence table

| Claim | Evidence | Result | Confidence |
| --- | --- | --- | --- |
| exact input | SHA-256 on two preserved copies | valid | confirmed |
| Shutdown pointer cell | raw exact scan of three data sections | Ghidra `0x0af56888` | confirmed |
| multi-method table | at least two known pointers within `0x200` | none | confirmed bounded negative |
| concrete owner vtable | table start, RTTI, or writer | unavailable | unknown |
| neighbor lifecycle methods | eight before, two after singleton | generic stubs/thunk only | confirmed bounded result |
| owner initializer | exact vptr writer provenance | unavailable | unknown |
| non-null `+0x1f0` writer | proven lifecycle candidate | unavailable | unknown |
| Dolphin symbol family | named UE4/gcloud dynamic symbols | 29 records / 19 canonical entries | confirmed |
| symbol-owner convergence | exact call from proven owner lifecycle | none | confirmed bounded negative |
| real interface Init dispatch | prior exact static analysis | ELF `0x05a2f0ac`, slot `+0x10` | confirmed |
| class identity | prior named owner methods; no new constructor proof | probable | probable |
