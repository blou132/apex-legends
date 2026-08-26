# Internal error constants

An exact scalar scan of the existing `libUE4.so` Ghidra program checked only
the five requested values in instruction operands and defined scalar data.

| Value | Instruction hits | Defined-data hits | Total |
| --- | ---: | ---: | ---: |
| `0x0430002e` | 0 | 0 | 0 |
| `0x0430002f` | 0 | 0 | 0 |
| `0x04300030` | 0 | 0 | 0 |
| `0x04300031` | 0 | 0 | 0 |
| `0x04300032` | 0 | 0 | 0 |

No containing function or xref inspection was possible because there was no
hit. No partial-value global scan was performed.

The resolved `GCloudPufferImp` slot `+0x10` forwarder does not mask, compare,
or translate the error family. It forwards the incoming code to the opaque
client callback.

```text
PUFFER_0430002E_LIBUE4_HITS = 0
PUFFER_0430002F_LIBUE4_HITS = 0
PUFFER_04300030_LIBUE4_HITS = 0
PUFFER_04300031_LIBUE4_HITS = 0
PUFFER_04300032_LIBUE4_HITS = 0
PUFFER_ERROR_FAMILY_MASKING = NO_IN_RESOLVED_FORWARDER
```
