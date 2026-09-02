# +0x1f0 and +0x1f8 routes

The phase did not recover a new owner producer region. The consumer read of
`+0x1f0` and the prior confirmed callback relation at `+0x1f8` do not by
themselves authorize new writer scans.

```text
NEW_PLUS1F0_PROVENANCE_ROUTE = NO
NEW_PLUS1F8_PROVENANCE_ROUTE = NO
```
