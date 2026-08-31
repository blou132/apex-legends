# Exception role

The FDE uses CIE augmentation `zR` and has no LSDA pointer. The target block is
not referenced as a landing pad, and there is no function-local branch that
enters it as manual cleanup.

Two explicit calls to the same imported throw helper bound the area:

```text
0x05a3144c  bl std::__throw_length_error(char const*)
...
0x05a314cc  bl CreateDolphin
0x05a314dc  bl std::__throw_length_error(char const*)
```

Explicit throw calls are terminal callsites, not evidence that the intervening
bytes are an exception landing pad. The first call prevents legitimate entry
into the factory region.

```text
LSDA_PRESENT = NO
EXCEPTION_LANDING_PAD_ROLE = NO
```
