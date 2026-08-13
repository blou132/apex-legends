# JNINativeMethod table

No GameActivity `RegisterNatives` call was resolved, so there is no proven
`JNINativeMethod` base address or count to parse. The expected 64-bit row layout
remains a validation rule, not evidence:

```text
name pointer      +0x00
signature pointer +0x08
function pointer  +0x10
row size          0x18
```

The DEX declaration confirms that a valid target row would need
`nativeResumeMainInit` and signature `()V`, followed by one unique executable
function pointer. No such row is attributed in Phase12.

```text
METHOD_NAME = UNKNOWN
SIGNATURE = CONFIRMED_EXPECTED_()V; UNKNOWN_TABLE_VALUE
FUNCTION_POINTER = UNKNOWN
GHIDRA_TARGET = UNKNOWN
ELF_OFFSET = UNKNOWN
```
