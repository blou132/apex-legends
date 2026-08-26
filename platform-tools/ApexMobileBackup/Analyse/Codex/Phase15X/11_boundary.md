# Boundary and gate

The exact `CreatePuffer` wrapper is resolved, including its tail-return
semantics, but the receiving call site is not present in the static callgraph.
The wrapper is referenced only through non-code-owned indirection/data entries.

This satisfies Phase15X stop condition B: the facade Init invocation cannot be
tied to the returned object by direct dataflow. It also prevents a defensible
trace of Init `x2`, the downstream client callback class, or its slot `+0x10`.

Following generic Unreal dispatch tables or searching all candidate
constructors/vtables would violate the strict boundary. The static Puffer client
boundary is therefore exhausted for this analysis axis.

```text
CURRENT_BLOCKER = CREATEPUFFER_RETURN_CONSUMER_HIDDEN_BEHIND_UNRESOLVED_INDIRECT_DISPATCH
STATIC_PUFFER_CLIENT_BOUNDARY_EXHAUSTED = YES
NEXT_RUNTIME_LAUNCH_REQUIRED = NO
FINAL_GATE = G CREATEPUFFER_OBJECT_FLOW_OPAQUE
```
