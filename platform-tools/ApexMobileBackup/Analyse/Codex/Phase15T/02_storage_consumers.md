# Storage consumers

The Phase15T baseline was `442952 KiB` free on `/data` and `422472 KiB` free
on shared storage. Android reported only about 3.8 MiB of aggregate application
cache, empty Downloads, and negligible disposable media. Apex accounted for
about 3.84 GiB of application/OBB storage and was fully protected.

The largest non-Apex consumer was Google Play Services, followed by updated
consumer applications. Play Services, Play Store, Google Services Framework,
Huawei account/framework services, Huawei System Manager, the active keyboard,
and all Android platform components were excluded from cleanup.

Removing an immutable factory APK under `/system`, `/cust`, or Huawei's factory
image would not reclaim `/data`. Cleanup therefore targeted only supported
Package Manager removal of updated applications and their user state. No raw
path under `/data`, `/system`, `/vendor`, or shared storage was deleted.

The final filesystem delta is larger than some individual APK sizes because
Package Manager also reclaims associated compiled artifacts and package state.
Small background-storage fluctuations are included in the observed deltas.
