# Interface-presence proxy

The exact client dispatch loads `DolphinUpdater+0x1f0` and invokes slot
`+0x10`. A retained exact Init-failure marker would directly prove that this
dispatch returned and therefore that the interface was present. None was
observed.

Phase15U's `version_mgr_imp.cpp` Init witness proves execution inside
`GCloudDolphinImp::Init`, but the dynamic target and exclusive provenance from
the exact client field remain probable rather than confirmed. It is therefore
an indirect provider-level proxy, not a direct field proof.

```text
INTERFACE_PRESENCE_OBSERVABLE_PROXY = DIRECT_IF_INIT_FAILURE_LOG_OBSERVED; VERSIONMGR_INIT_IS_INDIRECT_PROVIDER_PROXY
PHASE15U_INTERFACE_PRESENT = PROBABLE
PHASE16I_INTERFACE_PRESENT = NOT_OBSERVED
```
