# Direct target symbol

The manually decoded branch target is the exact stub whose instructions load
GOT `0x0b37e8c8`, whose relocation entry 529 names dynamic symbol 375,
`CreateDolphin`.

The factory itself requires no arguments, so the incoming volatile-register
state does not contradict its ABI. The return handling is separately
incompatible with persistent ownership: `x0` is copied to `x1`, then the code
calls the imported noreturn helper `std::__throw_length_error(char const*)`
with a different `x0`.

No `.rela.dyn` entry covers the bounded callsite window, so no loader
`CALL26` relocation changes these branch targets.

```text
DIRECT_CALL_REAL_IMPORTED_SYMBOL = CreateDolphin
PHASE16K_DIRECT_CALL_ATTRIBUTION = CONFIRMED
DIRECT_CALL_ARGUMENT_FLOW_CONSISTENT = YES; factory takes no arguments
```
