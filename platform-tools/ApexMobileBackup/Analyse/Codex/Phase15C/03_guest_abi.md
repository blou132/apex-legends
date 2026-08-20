# Guest ABI

Runtime guest properties confirm the translated-ABI configuration previously
seen only in static image metadata.

```text
PRIMARY_ABI = x86_64
ABI_LIST = x86_64,arm64-v8a
ABI_LIST_32 = EMPTY
ABI_LIST_64 = x86_64,arm64-v8a
ARM64_ABI_ADVERTISED = CONFIRMED YES
```

The guest is natively x86_64 and advertises ARM64 as a translated 64-bit ABI.
This confirms guest configuration, not execution of an Apex binary.
