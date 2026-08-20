# WHPX state

The requested `Get-WindowsOptionalFeature -Online -FeatureName
HypervisorPlatform` query was attempted read-only. This non-elevated session
returned the Windows elevation-required error `0x800702E4`; no elevation was
requested.

The read-only `Win32_OptionalFeature` provider independently returned:

```text
FEATURE_NAME = HypervisorPlatform
INSTALL_STATE_CODE = 1
WHPX_FEATURE_STATE = CONFIRMED ENABLED
```

Windows also reports a currently present hypervisor. This differs from
Phase15A, where install-state code `2` meant disabled and no hypervisor was
present.
