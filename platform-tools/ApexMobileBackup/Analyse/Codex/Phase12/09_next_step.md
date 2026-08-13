# Next step

## Decision

```text
FINAL_GATE = F JNI_ONLOAD_NOT_RESOLVED
```

The exact local `libUE4.so` exposes neither `JNI_OnLoad` nor the long
`nativeResumeMainInit` name through its authoritative dynamic symbol table.
The next useful static step should not repeat broader pattern scans in this
binary.

A later phase may inventory the other native libraries from the same exact APK
for either exported `JNI_OnLoad` or the full/short GameActivity native method
name, then prove library ownership before following registration. If the method
is not exported by any exact APK library, a separately approved local-only
loader observation would be required to identify the resolved native address.
No patching, hooking, backend emulation, or authentication work is justified.
