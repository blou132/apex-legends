# Static/runtime correlation

No new runtime session was performed. The only runtime source reused is the
sanitized Phase9C log analysis documented by Phase10.

| Runtime point | Existing evidence | Static Phase11 correlation |
|---|---|---|
| `nativeResumeMainInit` entry | invoked at about `+27.3 s` | no native address resolved |
| `nativeResumeMainInit` return | later Java lifecycle code executes | no native return site resolved |
| post-resume black screen | confirmed observation | no wait/state/render edge resolved |
| network retries | TDM/GCloud continue independently | no resume-root dependency resolved |

```text
STATIC_RUNTIME_CORRELATION = NONE
```

The runtime invocation is confirmed. The missing correlation is specifically
between that Java method and one function address in the exact local Ghidra
program.
