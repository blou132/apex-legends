# nativeResumeMainInit owner

## Evidence comparison

| Evidence | `libUE4.so` | Other 16 libraries |
|---|---|---|
| NativeActivity main library | `UE4` confirmed | no |
| direct `GameActivity` Java load | confirmed | several SDK loads, but not main library |
| full JNI name | one `.dynstr` string | absent |
| short method name | one `.dynstr` string | absent |
| slash GameActivity class | one `.rodata` string | absent |
| exact full/short export | absent | absent |
| target registration row | not demonstrated | not demonstrated |

The libUE4 witness is internally coherent but remains an orphaned string set.
No authoritative `dynsym`, relocation, exact name-address materialization, or
`JNINativeMethod` function pointer connects it to executable code.

```text
LIBUE4_FULL_NAME_WITNESS = CONFIRMED STRING_ONLY
OWNER_EVIDENCE = STRING_ONLY
OWNER_LIBRARY = UNKNOWN
```

Assigning `libUE4.so` as owner would exceed the evidence rule. Conversely, the
12 other JNI roots can be excluded from ordinary target registration because
their complete library images contain no target class or method identity.
