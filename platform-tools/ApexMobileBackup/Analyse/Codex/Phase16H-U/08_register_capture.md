# Register capture

The initial 272-byte general register-set capability was reconfirmed, but there
was no function-entry trap. PC, SP, LR, PSTATE, and function-entry argument
registers were therefore not captured.

```text
PC_CAPTURED = NO_NOT_REACHED
SP_CAPTURED = NO_NOT_REACHED
LR_CAPTURED = NO_NOT_REACHED
```
