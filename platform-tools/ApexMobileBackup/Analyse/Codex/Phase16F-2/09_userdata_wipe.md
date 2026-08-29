# Userdata wipe

The first command was rejected before an unlock or confirmation stage. After
the owner enabled the normal OEM-unlock control and separately authorized one
new command, the stock prompt was confirmed. Fastboot explicitly announced a
factory reset and returned success. Android then booted to the initial language
selection instead of the prior user environment. No preservation bypass,
restore, or account sign-in was used.

```text
FULL_USERDATA_WIPE_ACCEPTED = YES
DEVICE_WIPED = YES
WIPE_STARTED = YES_FASTBOOT_CONFIRMED
FIRST_BOOT_STATE = INITIAL_SETUP_LANGUAGE_SELECTION
```
