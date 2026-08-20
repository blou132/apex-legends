# AVD and host resources

The disposable AVD retains its existing Android 36.1 Google Play x86_64 image.
No configuration was edited.

| Property | Value |
| --- | --- |
| System image | `system-images;android-36.1;google_apis_playstore;x86_64` |
| Guest vCPUs | 4 |
| Guest RAM | 3072 MB |
| VM heap | 512 MB |
| Display | 1080 x 2400 at density 420 |
| Default GPU configuration | enabled, `auto` |
| Host logical CPUs | 8 |
| Host physical RAM | 21.92 GiB |

These values describe capacity only. The starvation classification comes from
the runtime scheduler and CPU evidence, not from the configured values alone.
