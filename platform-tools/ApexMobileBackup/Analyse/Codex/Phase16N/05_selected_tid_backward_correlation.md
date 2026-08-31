# Selected instance backward correlation

The same-run Puffer caller record maps to `THREAD10_A`; the corresponding task
record has comm name `Thread-10`. Searching backward by the local numeric TID,
before anonymization, gives:

1. T0 + 3.302 s: `mali_winsys` window-surface event.
2. T0 + 4.063 s: GCloudCore report-service `CreateEvent`.
3. T0 + 9.550 s: Puffer manager caller witness.

The first item is the literal earliest event but is not role-specific. The
second is the earliest useful role discriminator.

`SELECTED_THREAD10_INSTANCE = THREAD10_A`

`SELECTION_SAME_RUN_CORRELATION = PUFFER_CALLER_TO_TASK_COMM`
