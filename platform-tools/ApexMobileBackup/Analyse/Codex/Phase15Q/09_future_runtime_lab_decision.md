# Future runtime-lab decision

Both host cases reproduced the ANR on the disposable AVD. The prerequisite for
justifying a fresh host-mode Apex runtime AVD is therefore not met.

```text
FRESH_HOST_RUNTIME_AVD_JUSTIFIED = NO_FOR_NOW
```

Do not create another Apex runtime AVD until a new technical reason addresses
the broader SystemUI/system-image startup instability. Further work should not
boot `ApexPhase9Lab` merely to repeat the same comparison.
