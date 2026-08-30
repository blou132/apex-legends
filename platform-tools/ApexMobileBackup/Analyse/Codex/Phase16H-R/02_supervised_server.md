# Supervised Frida server

Frida 17.17.0 was started as Magisk root under a bounded supervisor that kept
stdout, stderr, process status, SELinux context, start time, stop time, and exit
status local-only.

On both supervised starts the server initially ran as uid 0 in
`u:r:magisk:s0`. It created and compiled a temporary `frida-helper` DEX, then
terminated about one second later with exit code 139. Stdout and stderr were
empty; logcat and tombstone evidence provided the failure classification.

```text
FRIDA_SERVER_UID = root
FRIDA_SERVER_SELINUX_CONTEXT = u:r:magisk:s0
FRIDA_SERVER_INITIAL_STATE = RUNNING_THEN_SIGSEGV
FRIDA_SERVER_EXIT_CODE = 139
```
