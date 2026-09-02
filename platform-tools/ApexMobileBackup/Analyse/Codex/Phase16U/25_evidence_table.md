# Evidence table

| Claim | Evidence | Result |
| --- | --- | --- |
| Primary AP is valid | exact `0 / 0 / executable` ABI header | Confirmed |
| Relocation/data alias exists | ELF parser plus Ghidra relocation cross-check | No |
| VTT exists | no alias cluster | Not recovered |
| Group-relative construction exists | eight page sites, zero in-range results | No |
| Literal/GOT route exists | zero literals and zero alias cells | No |
| Vptr writer exists | merged candidate set empty | Not recovered |
| Constructor or fields are resolved | no writer anchor | No |
