# Root validation

The first shell request ended with `Permission denied` because the UI grant
arrived after that request had already completed. With the saved Shell policy,
the immediate repeat returned `uid=0(root)` in Magisk context. This was not a
version retry or image change.

Read-only checks then confirmed Magisk CLI `28.1:MAGISK:R`, code `28100`,
SELinux `Enforcing`, and successful reading of the first line of
`/proc/1/maps` as root.

```text
ADB_SHELL_ROOT_GRANTED = YES
ROOT_UID_CONFIRMED = YES
SELINUX_STATE = Enforcing
ROOT_PROC_ACCESS = YES
```
