# Evidence table

| Claim | Evidence | Result |
| --- | --- | --- |
| Inputs unchanged | Fresh SHA256 checks | All match |
| Correct phone and health | Normal read-only ADB | PRA-LX1/C33/hi6250, 100 percent, normal temperature |
| Board/testpoint match | Current local-only photo plus prior references | Confirmed |
| One attempt only | Operator sequence and host observation | Count 1/1 |
| Bootrom live binding | Present PnP properties | `12D1:3609`, `oem14`, status OK, problem 0 |
| D00D functional binding | PotatoNV exact D00D libusb path plus stored devnode | `oem77`/WinUSB functional |
| Previous timeout passed | Private PotatoNV log | Connection and device info reached |
| Code operation complete | Private PotatoNV log/code file | Obtained, local-only |
| Stock fastboot locked | `oem get-bootinfo` | Locked |
| First unlock rejected | First authorized fastboot command | OEM unlock/FRP permission missing; no state change |
| Normal policy enabled | Read-only ADB after owner UI action | OEM unlock allowed value 1 |
| Separate authorization | Owner confirmation | One new command; wipe accepted; no new physical entry |
| Unlock accepted | Stock prompt plus fastboot completion | On-device Yes, `OKAY`, exit 0 |
| Wipe complete | Fastboot message plus initial setup | Factory reset and language selector |
| Permanent unlock | Post-wipe `oem get-bootinfo` | Unlocked, `OKAY`, exit 0 |
| Healthy stock boot | Display/touch and stable USB/MTP | Confirmed |

Raw logs, identifiers, photo, and code are not tracked.
