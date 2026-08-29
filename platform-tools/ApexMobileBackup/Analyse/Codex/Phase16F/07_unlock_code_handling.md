# Unlock code handling

The ignored local-only destination was validated before PotatoNV was started.
The operation stopped before the code-generation path, so no unlock code was
created, displayed, stored, committed, or published.

```text
UNLOCK_CODE_OBTAINED = NO
UNLOCK_CODE_PUBLISHED = NO
PRIVATE_CODE_FILE_PRESENT = NO
```
