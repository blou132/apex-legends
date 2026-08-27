# Post-root trace capability design

## What root would and would not provide

| Capability | Assessment | Reason |
| --- | --- | --- |
| Read `/proc/<Apex PID>/maps` | YES | A root shell can normally bypass the current shell visibility limit |
| Resolve `libgcloud.so`/`libUE4.so` runtime bases | YES, from verified mappings | PT_LOAD correlation is still required |
| Obtain native stacks | PROBABLE_WITH_ADDITIONAL_TOOLING | Root alone does not install a native debugger or stack sampler |
| Observe callback/vtable target | PROBABLE_WITH_ADDITIONAL_TOOLING | Requires controlled ptrace/debugger observation at the exact call boundary |

```text
ROOT_WOULD_ENABLE_PROC_MAPS = YES
ROOT_WOULD_ENABLE_NATIVE_STACKS = PROBABLE_WITH_ADDITIONAL_TOOLING
ROOT_WOULD_ENABLE_CALLBACK_TRACE = PROBABLE_WITH_ADDITIONAL_TOOLING
```

Root is necessary for the current device policy boundary, but it is not a
complete debugger workflow. SELinux policy, anti-debug behavior, tool ABI, and
runtime perturbation would still need separate validation.

## Minimal future observation design

After a separately approved root phase, the least-invasive useful sequence
would be:

1. Keep the run offline and bounded, and confirm the unchanged exact client.
2. Read the target process mappings as root and derive load bases from ELF
   `PT_LOAD` segments.
3. Use a root-capable AArch64 debugger in attach/read mode at the already
   resolved `CVersionStrategy_Win32::FUN_0050cb38` call boundary.
4. Stop immediately before the indirect call, record only the callback object
   pointer relationship at `CVersionStrategy+0x18`, its vtable, and slot
   `+0x28` target, then detach.
5. Correlate that runtime target to the exact local ELF without patching code,
   changing responses, or emulating a backend.

This design is not executed in Phase16C. It may still fail if attach is denied
or destabilizes the client.
