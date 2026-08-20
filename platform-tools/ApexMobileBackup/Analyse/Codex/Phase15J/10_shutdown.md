# Shutdown and restoration

At the +180 second bound, Apex was force-stopped and process absence was
confirmed. Airplane mode, Wi-Fi, and mobile data were restored exactly to their
recorded pre-run values. The AVD accepted the emulator shutdown request and the
ADB inventory became empty.

The initial post-run copy loop was interrupted by PowerShell treating an
`adb pull` progress line as an exception. This happened after all observations
and metadata snapshots. The cleanup `finally` block still completed correctly.

A later collection-only boot copied all eight public SDK files. Apex was not
launched during that boot. Network isolation was confirmed before collection,
then settings were restored and the AVD shut down. No emulator process or ADB
endpoint remains.

```text
NETWORK_RESTORED = YES
AVD_SHUTDOWN_CLEAN = YES
NO_ADB_ENDPOINT_REMAINING = YES
```
