# Initial ptrace and hardware state

The tracer attached to the exact disposable tracee and received a bounded
SIGSTOP. Initial state checks completed before active programming:

- general register set length: 272 bytes;
- hardware-breakpoint register set length: 264 bytes;
- hardware execution slots reported: 6;
- every intended execution-slot control was clear.

The userspace ABI buffer capacity remains 16 slots, while six slots are
actually implemented on this device. The submitted iovec still covered only
the complete slot 0.

- `PTRACE_ATTACH_RESULT = SUCCESS`
- `GENERAL_REGSET_VALID = YES`
- `HW_REGSET_VALID = YES`
- `HW_EXEC_SLOTS = 6`
- `HW_INITIAL_STATE_CLEAR = YES`
