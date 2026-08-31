# Stateful configuration candidates

The following sanitized structures persisted after the Phase16I warm-up:

| Candidate | Structural evidence | Apparent owner | Confidence as trigger lead |
|---|---|---|---|
| GCloud config pair: `shared_prefs/GCloudCoreSP.xml` key `GCloud.config` and external `cache/GCloud.config` | String key plus opaque 146-byte binary state | GCloudCore/GCloud | Medium lead, no causation |
| `shared_prefs/com.epicgames.ue4.GameActivity.xml`: `SPLASH_VIDEO_START` | Boolean added only after trace launch | Unreal activity | Excluded as warm-up cause |
| measurement keys `first_open_time`, `has_been_opened` | Generic first-open state | Google measurement | Low |
| Singular key `wasOpenedAfterInstall` | Generic install attribution state | Singular | Low |
| `tri_init` and TRI configuration keys | First-init marker/config structure | TRI SDK | Low |

Values were not copied into tracked output. The static target neighborhood
contains no direct read of any of these files or keys.

```text
STATEFUL_CONFIG_CANDIDATES = 5 CATEGORIES
```
