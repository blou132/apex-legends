# Invocation model

CheckUpdate is reached from multiple explicit lifecycle transitions:
StartUpdate, source-update completion, update-success handling, and a
SkipAppUpdate request. None of the four bounded callsites forms a local tight
retry or tick loop around CheckUpdate.

The newly recovered exact identifiers do not occur in retained Phase15U or
Phase16I sanitized evidence, so they do not create a new observed divergence.

```text
CHECKUPDATE_INVOCATION_MODEL = EVENT_DRIVEN
CHECKUPDATE_CALLER_GATE_RUNTIME_RELEVANCE = LOW
```
