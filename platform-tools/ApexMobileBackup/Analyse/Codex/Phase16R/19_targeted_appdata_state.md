# Targeted AppData state

The retained extracted files were searched only for exact known component
names and keys.

- One unique logical GCloud log artifact contains Dolphin app/product/resource
  version configuration keys. It contains no `version_mgr_imp.cpp` witness.
- The persistent version-state candidates comprise that GCloud log plus the
  opaque GCloud configuration pair: a `GCloud.config` preference key and the
  associated fixed-size external config artifact.
- Puffer errors also appear in GCloud and Unreal logs, but are runtime logs,
  not direct persistent selector values.

Raw preference values, paths, identifiers, and log timestamps remain local.

```text
APPDATA_DOLPHIN_RELATED_ARTIFACT_COUNT = 1
APPDATA_VERSION_STATE_ARTIFACT_COUNT = 3
```
