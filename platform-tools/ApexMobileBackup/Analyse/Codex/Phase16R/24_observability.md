# Gate observability

Retained logging can observe selected outcomes but not reconstruct the full
client selection state:

- the Init-failure message would prove an exact dispatch and failed return;
- the two first-extract messages can prove their `+0x45 = 1` paths when shown;
- the callback-entry marker cannot prove the clear store;
- neither W23 helper logs its result;
- no marker proves `owner+0x1f0` acquisition or the exact success follow-up.

The gate is therefore partially observable in principle and unresolved in the
retained runs.

```text
DOLPHIN_INIT_GATE_LOG_OBSERVABILITY = PARTIAL
ACQUISITION_WRITER_RUNTIME_INFERENCE = UNKNOWN
```
