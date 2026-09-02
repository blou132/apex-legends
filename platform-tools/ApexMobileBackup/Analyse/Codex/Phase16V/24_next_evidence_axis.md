# Next evidence axis

The exact persistent callback owner field is resolved, but its non-null writer
and source remain unknown. The next useful static axis is therefore:

```text
NEXT_EVIDENCE_AXIS = PC_ONLY_OWNER_STORAGE_ASSIGNMENT_PROVENANCE
```

It must start from the exact `DolphinCallback+0x08` field and the proven
callback object identity. It must not become a global `+0x08`, constructor,
vtable, or whole-callgraph scan.
