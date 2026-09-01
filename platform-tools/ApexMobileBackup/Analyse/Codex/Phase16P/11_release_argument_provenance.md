# Release argument provenance

No exact reachable `ReleaseDolphin` call exists from which to slice `X0`.

```text
RELEASEDOLPHIN_ARGUMENT_SOURCES = NONE_NO_EXACT_DIRECT_CALLSITE
```

The release-like sequence in `DolphinUpdater::Shutdown` is documented
separately and is not relabeled as `ReleaseDolphin`.
