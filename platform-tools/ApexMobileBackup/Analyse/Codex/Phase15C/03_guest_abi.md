# Guest ABI

The guest was not booted, so no runtime property was queried.

```text
PRIMARY_ABI = UNKNOWN_NOT_BOOTED
ABI_LIST = UNKNOWN_NOT_BOOTED
ABI_LIST_64 = UNKNOWN_NOT_BOOTED
ARM64_ABI_ADVERTISED = UNKNOWN_NOT_BOOTED
```

Phase15B's system-image metadata still declares x86_64 plus translated
`arm64-v8a`. That is a static expectation, not a Phase15C guest confirmation.
