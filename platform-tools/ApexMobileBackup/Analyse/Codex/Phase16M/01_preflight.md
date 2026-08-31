# Preflight

The repository began clean on `main`, equal to `origin/main`, at
`7b3daa5b27980b9df03f64382f6ffedd5feab895`. Only the PRA-LX1 was visible to
ADB; no Samsung or emulator endpoint was present.

The expected PRA-LX1/C33 build was booted. Battery was 100 percent at 28 C,
root returned `uid=0`, and SELinux remained `Enforcing`. Apex was installed and
not running. Wi-Fi and mobile data were disabled and no IPv4 or IPv6 route was
present. Airplane mode was re-enabled before launch and remained enabled.

The exact installed APK was pulled read-only to ignored local storage and
matched the preserved SHA-256. Its two native inputs matched the Phase16L
sizes and hashes.

```text
PHASE16M_PREFLIGHT = PASS
TARGET_DEVICE_VALID = YES_PRA_LX1_ONLY
ROOT_READY = YES
SELINUX_READY = YES_ENFORCING
NETWORK_ISOLATION_GATE = PASS
APEX_INSTALLED = YES
APEX_INITIAL_PROCESS_RUNNING = NO
```
