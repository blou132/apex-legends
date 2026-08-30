# Preflight

The repository started clean on `main`, equal to `origin/main`, at commit
`25f9489`.

The only ADB endpoint was the PRA-LX1. Android 8.0 reported boot complete,
battery 100 percent with a normal temperature, stable USB ADB, root `uid=0`,
and SELinux `Enforcing`. Apex was absent before installation as required.

```text
PHASE16I_PREFLIGHT = PASS
TARGET_DEVICE_CONFIRMED = PRA-LX1_ONLY
ROOT_READY = YES
SELINUX_READY = YES_ENFORCING
APEX_INITIAL_PACKAGE_STATE = ABSENT
```
