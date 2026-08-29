# Frida deployment

The verified binary was pushed to `/data/local/tmp/frida-server`, given mode
755, and executed through Magisk `su`. Its phone-side size and SHA256 matched
the verified PC binary, and `--version` returned 17.17.0.

`frida-ps -U` enumerated Android processes on repeated checks. However, the
foreground server command remained silent, no persistent `frida-server`
process or TCP/Unix listener could be observed, and the attach API later
reported a closed server connection.

```text
FRIDA_SERVER_DEPLOYED = YES_REMOVED_AFTER_TEST
FRIDA_SERVER_RUNNING_AS_ROOT = NO_NOT_OBSERVED
FRIDA_USB_DISCOVERY = YES
FRIDA_PROCESS_ENUMERATION = YES
```

No alternate Frida version, remote listener, autostart service, policy
softener, module, or fallback framework was attempted. The binary and helper
DEX files were removed from the phone after the bounded test.
