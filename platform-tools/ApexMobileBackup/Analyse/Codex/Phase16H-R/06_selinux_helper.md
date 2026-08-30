# SELinux and Frida helper

The supervised 17.17.0 process ran in `u:r:magisk:s0`. Temporary helper DEX
creation and ART compilation were observed, but polling did not find a stable
`re.frida.helper` process in the current run. Its uid, parent relationship, and
SELinux context therefore cannot be reported.

Older Phase16H logs contain a shell-context helper and AVC denials. Those
historical observations are preserved, but they are not evidence that SELinux
caused the current startup crash: the current run had no AVC and failed before
a stable helper process appeared.

```text
FRIDA_HELPER_OBSERVED = DEX_CREATION_ONLY_NO_STABLE_PROCESS
FRIDA_HELPER_CONTEXT = NOT_OBSERVED_CURRENT_RUN
FRIDA_HELPER_FAILURE_CORRELATED = YES_PROBABLE_ART_HELPER_INITIALIZATION
SELINUX_CAUSAL_EVIDENCE = NO_CURRENT_RUN_AVC
```
