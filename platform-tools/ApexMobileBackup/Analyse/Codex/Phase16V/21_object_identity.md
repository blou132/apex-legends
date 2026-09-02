# Object identity

The existing ABI evidence remains valid:

- confirmed primary address point at ELF `0x0ae56608`;
- confirmed secondary address point at ELF `0x0ae568b0`;
- confirmed `-0x28` secondary adjustment;
- confirmed Shutdown method and adjustor thunk;
- exact caller-side methods and persistent callback owner field.

However, the callback field does not converge with CheckUpdate or an external
Shutdown caller, and no non-null assignment or vptr writer is known. The class
semantic identity therefore remains probable rather than confirmed.

```text
TOP_OBJECT_NORMALIZATION_VALID = YES
DOLPHINUPDATER_CLASS_IDENTITY = PROBABLE
METHOD_CALLER_ANALYSIS_INDEPENDENT_OF_VTABLE = YES
```
