# AppData structural diff

Snapshot 0 contained no package-owned regular file. Snapshot 1 contained the
first-run package state created by the warm-up. Snapshot 2 retained that
state and changed a bounded set of files during the trace launch.

## Snapshot 1 to 2

- Rotated two zero-length CrashSight log placeholders.
- Created one zero-length GCloud log placeholder plus GCloudCore and MSDK log
  files.
- Modified, without size growth, `databases/crashSight_db_`,
  `databases/google_app_measurement_local.db`, `files/MSDK.mmap3`,
  `shared_prefs/GCloudCoreSP.xml`, and `shared_prefs/itop.xml`.
- Modified `shared_prefs/com.epicgames.ue4.GameActivity.xml` from 65 to 125
  bytes by adding the boolean key `SPLASH_VIDEO_START`.
- Modified the Google measurement preferences by one byte; only key/type
  structure is published.
- Modified external `cache/GCloud.config` while retaining its 146-byte size.

Archive copies, inventories, hashes, mtimes, raw values, and opaque file
contents remain local-only. Structural change alone does not prove a trigger.
