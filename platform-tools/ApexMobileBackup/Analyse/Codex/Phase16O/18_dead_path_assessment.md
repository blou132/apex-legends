# Dead-path assessment

The exact factory bytes are statically unreachable from the legitimate
function entry. The reason is proven: they follow a noreturn throw without any
side-entry edge. What produced the malformed region is not proven.

Several calls in the same function physically target the PLT entry immediately
before the operation suggested by their register shape: `memcpy` versus
`memset`, `memcmp` versus `strlen`, `gettimeofday` versus `strncpy`, and
`throw_length_error` versus a string constructor. This supports a systematic
incoherence in the static path, but it does not distinguish compiler dead code,
an intentionally opaque decoy, or another producer artifact.

No anti-cheat or obfuscation label is assigned.

```text
CALLSITE_DEAD_OR_DECOY_CLASS = UNKNOWN
```
