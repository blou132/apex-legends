# Throw helper revalidation

The raw `BL` at ELF `0x05a314dc` has opcode `0x95263b95`. Manual AArch64
decoding targets PLT `0x0a3c0330`. That stub loads GOT `0x0b37dde8`, whose
`.rela.plt` entry 181 names dynamic symbol 184:
`_ZSt20__throw_length_errorPKc`.

The demangled identity is `std::__throw_length_error(char const*)`. This C++
throw helper is noreturn by ABI and call semantics. The same exact stub is
called at `0x05a3144c`, before the factory region.

Instructions after either call are only raw fallthrough bytes unless another
proven control-flow edge enters them. None enters the `CreateDolphin` region.

```text
NEXT_CALL_REAL_SYMBOL = std::__throw_length_error(char const*)
NEXT_CALL_IS_THROW_LENGTH_ERROR = YES
NEXT_CALL_NORETURN_CONFIRMED = YES
POST_THROW_FALLTHROUGH_VALID = NO
```
