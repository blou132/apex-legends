# Argument validation

The tracee retained the authoritative known arguments `0x1111` through
`0x8888`, but execution never reached the target under ptrace. No x0-x7 runtime
comparison occurred.

```text
X0_MATCH = NOT_REACHED
X1_MATCH = NOT_REACHED
X2_MATCH = NOT_REACHED
X3_MATCH = NOT_REACHED
X4_MATCH = NOT_REACHED
X5_MATCH = NOT_REACHED
X6_MATCH = NOT_REACHED
X7_MATCH = NOT_REACHED
FUNCTION_ARGUMENT_CAPTURE = NO_NOT_REACHED
```
