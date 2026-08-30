# Evidence-gated Frida 17.5.2 comparator

The comparator was justified because helper DEX/ART initialization was directly
implicated and upstream history identifies a post-17.6 helper architecture
boundary. Only official Frida 17.5.2 was tested, in a separate host virtual
environment with exact 17.5.2 core/server matching. No global Frida package was
downgraded and no third version was tried.

The server remained alive as root in `u:r:magisk:s0`, without a new fatal log,
AVC, or tombstone. Both initial enumeration and one exact-PID attach to the
disposable tracee failed with:

`NotSupportedError: unable to perform ptrace pokedata: I/O error`

The server and tracee survived. Because the first enumeration failed, three
repeat enumerations were neither possible nor claimed. No script, hook, module
enumeration, or memory operation followed.

```text
FRIDA_17_5_2_TEST_AUTHORIZED_BY_EVIDENCE = YES
FRIDA_17_5_2_ENUMERATION = FAILURE_PTRACE_POKEDATA_EIO
FRIDA_17_5_2_ATTACH = FAILURE_PTRACE_POKEDATA_EIO
```
