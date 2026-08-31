# Init versus version check

The manager vtable separates object initialization from update execution:

- `0x009787d0` -> `FUN_0050690c` -> `CVersionMgrImp::Init`.
- `0x009787e8` -> `FUN_0050691c` -> `CVersionMgrImp::CheckAppUpdate`.
- Init builds and stores instance-owned callback/action/strategy state.
- CheckAppUpdate requires the strategy at manager `+0x10`, calls a strategy
  readiness predicate, and then invokes the update operation.
- `NormalConnectVersionSvr` belongs to the later Dolphin network-action path.
- Puffer initialization is a neighboring bootstrap path, not the direct
  CVersionMgr Init caller.

```text
INIT_AND_VERSION_CHECK_SAME_TRIGGER = NO
CVERSIONMGR_INIT_LIFETIME = PER_MANAGER_INSTANCE
```

No static once guard proves process-wide uniqueness; the classification is
limited to the manager-owned state directly visible in the analyzed code.
