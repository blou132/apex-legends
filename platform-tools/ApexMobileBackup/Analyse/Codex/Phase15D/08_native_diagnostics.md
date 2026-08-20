# Native diagnostics

After the process remained stable through +60 seconds, each permitted native
diagnostic was attempted once.

## showmap

`showmap` could not parse the target process `smaps` file. This is a process-map
permission boundary on the non-debuggable guest/package.

```text
SHOWMAP_APEX = NO_PERMISSION
```

## debuggerd

The single `debuggerd -b` request was refused because root is required in this
guest configuration. No frame was produced.

```text
DEBUGGERD_APEX = NO_PERMISSION
```

Neither command was retried or escalated. No root, debug property, ptrace,
debugger attach, hook, patch, or tombstone request was used.
