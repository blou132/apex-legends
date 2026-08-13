# nativeResumeMainInit resolution

The DEX declaration remains confirmed:

```text
com.epicgames.ue4.GameActivity.nativeResumeMainInit()V
```

The Java lifecycle invokes and returns from the native declaration in the
preserved Phase10 runtime evidence. Static Phase13 evidence still provides no
unique native address.

```text
NATIVE_RESUME_MAIN_INIT_FUNCTION = UNKNOWN
OWNER_LIBRARY = UNKNOWN
ELF_VIRTUAL_OFFSET = UNKNOWN
GHIDRA_ADDRESS = UNKNOWN
JNI_ENTRY_CONFIRMED = UNKNOWN
LIBUE4_LOADED = PROBABLE
LIBUE4_LOAD_BASE = UNKNOWN
```

`LIBUE4_LOADED` remains `PROBABLE` under the requested strict rule: manifest and
DEX loading are confirmed, and the Java native call returned, but Phase13 did
not prove that this method resolves inside the exact imported `libUE4.so`.

No mini callgraph, state-write set, wait, thread launch, or Lua boundary is
produced because doing so would require choosing an unproven root.
