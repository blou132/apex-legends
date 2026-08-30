# Trap and register capture

No function-entry trap was reached. The software branch stopped after the
single failed write. The hardware branch stopped after invalid control-state
readback and before `PTRACE_CONT`.

```text
EXACT_FUNCTION_ENTRY_TRAP = NO_NOT_REACHED
PC_CAPTURED = NO_NOT_AT_FUNCTION_ENTRY
SP_CAPTURED = NO_NOT_AT_FUNCTION_ENTRY
LR_CAPTURED = NO_NOT_AT_FUNCTION_ENTRY
X0_MATCH = NO_NOT_REACHED
X1_MATCH = NO_NOT_REACHED
X2_MATCH = NO_NOT_REACHED
X3_MATCH = NO_NOT_REACHED
X4_MATCH = NO_NOT_REACHED
X5_MATCH = NO_NOT_REACHED
X6_MATCH = NO_NOT_REACHED
X7_MATCH = NO_NOT_REACHED
FUNCTION_ARGUMENT_CAPTURE = NO
```

The initial attach register set remained confirmed at 272 bytes, but that is
not function-entry evidence.
