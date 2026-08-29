# Frida attach result

The isolated host script attempted to attach only to the unique disposable
`ping` process. The attach failed before script injection with:

```text
frida.ServerNotRunningError: unable to connect to remote frida-server: closed
```

The ping process remained alive after the failure. No target process was
modified, replaced, or terminated by Frida.

```text
FRIDA_ATTACH_TEST_PROCESS = NO_SERVER_CONNECTION_CLOSED
FRIDA_PROCESS_ARCH = NOT_OBSERVED
FRIDA_PROCESS_PLATFORM = NOT_OBSERVED
FRIDA_MODULE_ENUMERATION = NO_NOT_REACHED
```

Per the phase rule, successful process enumeration followed by attach failure
selects no fallback and leaves the Phase16I method unresolved.
