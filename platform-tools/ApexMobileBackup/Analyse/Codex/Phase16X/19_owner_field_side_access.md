# Owner field side access

There is no proven registration function in which owner side fields may be
inspected. Therefore no same-function `+0x1f0` or `+0x1f8` access can be
classified.

```text
REGISTRATION_FUNCTION_PLUS1F8_ACCESS = UNKNOWN
REGISTRATION_FUNCTION_PLUS1F0_ACCESS = UNKNOWN
```

The known consumer-side read of owner `+0x1f0` is not relabeled as a
registration-side access.
