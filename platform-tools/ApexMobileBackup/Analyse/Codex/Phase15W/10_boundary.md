# Boundary and gate

Phase15W resolves the dynamic callback that stopped Phase15V. The
`ProcessResult` callback has a concrete in-library implementation:
`GCloud::GCloudPufferImp` implementing `cu::IPufferCallBack` through secondary
vtable `0x009785f8`.

That implementation is a forwarder. Its field `+0x18` contains a separately
supplied client callback whose vtable slot `+0x10` is invoked at
`0x00503040`. The concrete external callback remains unresolved because:

- `libUE4` has no exact `CreatePufferCallBack` symbol/string reference;
- the exact `CreatePuffer` reference is only a facade factory call;
- all five exact Puffer error constants have zero `libUE4` hits;
- following the external virtual slot without an anchor would require a global
  class/vtable scan prohibited by the scope.

```text
CURRENT_BLOCKER = OPAQUE_DYNAMIC_CALLBACK_BOUNDARY_AT_GCLOUDPUFFERIMP_CLIENT_CALLBACK
NEXT_RUNTIME_LAUNCH_REQUIRED = NO
FINAL_GATE = D CONCRETE_DYNAMIC_CALLBACK_IMPLEMENTATION_RESOLVED
```
