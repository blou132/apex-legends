# Phase16A scope

Phase16A is a static-only, read-only analysis of `libgcloud.so`. It starts at
the established `cu::CActionMgr+0x3b8` callback field and resolves only exact
field writes, the registration path, the assigned object's RTTI/vtable, slot
`+0x00`, and the naturally reached client boundary.

The existing Ghidra project was opened with `-noanalysis -readOnly`. No binary
was imported, no full analysis was run, and no device, emulator, Apex process,
network endpoint, or `libUE4.so` broad search was used.

Address model:

```text
GHIDRA_ADDRESS = ELF_VA + 0x100000
```

Raw Ghidra exports remain under the ignored `LocalInputs/Phase16A` tree.
