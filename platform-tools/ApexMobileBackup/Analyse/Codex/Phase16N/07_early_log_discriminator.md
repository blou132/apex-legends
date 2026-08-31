# Early log discriminator

The earliest useful event is the GCloudCore report-service `CreateEvent` at
T0 + 4.063 s. Among the five same-name tasks in the retained Phase16M run,
only `THREAD10_A` emits this exact external role sequence followed by the
Puffer caller witness.

This is a deterministic non-ptrace discriminator for the retained run. It is
not proven to occur before GameProtector3 ownership because no tracer sample
exists at that instant.

- `EARLY_LOG_DISCRIMINATOR = GCLOUDCORE_CREATEEVENT`
- `EARLY_LOG_DISCRIMINATOR_UNIQUE = YES_RETAINED_PHASE16M_RUN`
- `EARLY_LOG_DISCRIMINATOR_BEFORE_PROTECTOR = UNKNOWN`
