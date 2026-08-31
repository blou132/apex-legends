# Phase16K scope

Phase16K is a PC-only, static, read-only analysis of the exact preserved
`libgcloud.so` and `libUE4.so` inputs. Its bounded target is the client-side
ownership and bootstrap selection path for `CreateDolphin`.

No phone, ADB, application launch, AppData access, network operation,
breakpoint, process attachment, memory write, or binary modification was
used. Existing Ghidra projects were opened read-only with `-noanalysis`.

Raw Ghidra exports remain under the ignored `LocalInputs/Phase16K` tree. Only
sanitized findings and one target-specific helper are committed.
