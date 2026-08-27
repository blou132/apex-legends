# Bootloader state

## Normal-boot evidence

| Property/check | Observed value | Meaning |
| --- | --- | --- |
| `ro.boot.flash.locked` | `1` | Bootloader locked |
| `ro.boot.verifiedbootstate` | `GREEN` | Verified boot accepted current images |
| `ro.boot.vbmeta.device_state` | `locked` | Device state locked |
| `ro.boot.veritymode` | `enforcing` | Verity enforcing |
| `ro.bootmode` | `normal` | No fastboot session entered |
| `ro.oem_unlock_supported` | `1` | Platform advertises unlock support |
| `sys.oem_unlock_allowed` | `0` | Unlock not currently allowed by this property |
| global OEM settings | `null` | No reliable UI toggle state exposed here |

## Required result

```text
BOOTLOADER_LOCKED = YES
OEM_UNLOCK_TOGGLE_AVAILABLE = UNKNOWN
OEM_UNLOCK_TOGGLE_ENABLED = NO_BY_SYS_PROPERTY
FRP_STATE = UNKNOWN
FBLOCK_STATE = UNKNOWN_NO_FASTBOOT_SESSION
FASTBOOT_AVAILABLE = HOST_TOOL_PRESENT_DEVICE_FASTBOOT_NOT_ENTERED
```

The existence of the `frp` partition path is not an FRP-state reading. Likewise,
FBLOCK cannot be classified from normal Android properties. The device was not
rebooted to fastboot because this phase is read-only and normal-boot only.

Android documents that a locked device rejects untrusted software and that a
transition to unlocked state wipes data partitions:
[Android verified-boot device state](https://source.android.com/docs/security/features/verifiedboot/device-state).
