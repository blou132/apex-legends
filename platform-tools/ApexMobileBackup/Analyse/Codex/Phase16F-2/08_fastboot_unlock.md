# Fastboot unlock

After PotatoNV rebooted the phone, stock Android returned healthy. Read-only
ADB still reported 100 percent battery and 29 C. The host then requested stock
Fastboot/Rescue mode. Exactly one fastboot endpoint appeared; `oem
get-bootinfo` reported `locked`.

All planned pre-command checks passed. The private code was read from ignored
storage and the first `fastboot oem unlock` command was issued without printing
the code. Stock fastboot rejected it before any state change:

```text
Necessary to unlock FRP
Enable OEM unlock in Developer options
Command not allowed
```

No confirmation screen or wipe began. The hard-stop policy was applied and the
phone returned to healthy stock Android.

The owner subsequently enabled `OEM unlocking` through the normal Android UI.
Read-only ADB then reported `sys.oem_unlock_allowed=1`. The owner gave a new,
explicit authorization for one additional destructive fastboot command without
another testpoint or PotatoNV operation. Identity, health, battery, target
count, private-code format, and stock-fastboot lock state were revalidated.

The separately authorized command displayed the stock on-device unlock prompt.
The owner selected `Yes` and confirmed with Power. Fastboot reported that the
device would reboot and perform a factory reset, returned `OKAY`, and exited
with code zero. The private code was never printed or committed.

```text
STOCK_FASTBOOT_VISIBLE = YES_EXACTLY_ONE_ENDPOINT
PRECOMMAND_BOOTLOADER_STATE = LOCKED
DESTRUCTIVE_UNLOCK_READY = YES_AFTER_SEPARATE_REAUTHORIZATION
BOOTLOADER_UNLOCK_COMMAND_COUNT = 2
FIRST_UNLOCK_COMMAND = REJECTED_OEM_UNLOCK_NOT_ALLOWED
SECOND_UNLOCK_COMMAND = SEPARATELY_AUTHORIZED_ACCEPTED
BOOTLOADER_UNLOCK_COMMAND_EXECUTED = YES
BOOTLOADER_UNLOCK_COMMAND_ACCEPTED = YES
ON_DEVICE_CONFIRMATION = YES
FASTBOOT_FINAL_EXIT = 0
```
