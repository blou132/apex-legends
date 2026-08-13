# Phase8 - Mount points

No effective runtime mount point is proven.

The loader obtains a current base path from `FUN_045a4384`, ensures path separators with `FUN_04165f24`, and reads an alternate runtime prefix from globals `0xb7c7020/0xb7c7028`. `FUN_04166f64` then performs a case-insensitive prefix replacement in the requested path. Static initialization sets the alternate prefix to an empty string; its runtime value was not recovered.

The following strings are evidence about path handling, not proven mounts:

| Value | Source | Classification |
| --- | --- | --- |
| `../../../%s/` | constant `0x28752a4` | confirmed formatting pattern; semantic use not tied to EventSystem |
| `/system/etc/` | constant `0x2633dc8` in path normalization | confirmed path exception/check; not a game mount |
| `AClient/Content/Paks/` | recovered OBB layout | confirmed physical ZIP layout; virtual prefix unknown |
| `Client/` | raw Lua witness | confirmed text in PAK body; not a mount proof |
| `Script/` | probable EventSystem logical name | probable logical prefix only |

```text
PROVEN_GAME_MOUNT_POINTS = 0
RUNTIME_PREFIX = UNKNOWN
```
