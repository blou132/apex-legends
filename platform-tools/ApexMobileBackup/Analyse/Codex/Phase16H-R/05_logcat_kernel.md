# Logcat and kernel evidence

The bounded logcat window contains `Fatal signal 11 (SIGSEGV)`, code 1, fault
address zero, for the Frida 17.17.0 server. The sanitized stack traverses
`libart.so` and Frida-relative frames. No separate helper-process fatal record
was observed.

Current-run logcat and kernel/audit windows contained no AVC denial associated
with Frida, `re.frida.helper`, or `app_process`. Raw captures remain local-only.

```text
LOGCAT_FRIDA_FATAL = YES_SIGSEGV
LOGCAT_HELPER_FATAL = NO_SEPARATE_HELPER_PROCESS_FATAL
LOGCAT_SELINUX_DENIAL = NO_CURRENT_RUN_AVC
SELINUX_CAUSAL_EVIDENCE = NO_CURRENT_RUN_AVC
```
