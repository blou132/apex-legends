# Host and USB preflight

## Phone power

Three consecutive normal ADB reads succeeded. They reported 97 percent,
charging from USB, and 25.0 C. This exceeds the required 60 percent and the
preferred 70 percent threshold.

## Windows host

- Windows 11 Home Single Language, version/build `10.0.26200`, 64-bit.
- Portable host with its system battery at 100 percent.
- Two AMD USB 3.10 eXtensible Host Controller 1.10 devices are present.
- The connected PRA-LX1 and ADB interface both reported an OK PnP state.
- The current route terminates at a Windows USB Root Hub (USB 3.0) and then an
  AMD host controller; no external or generic hub was present in that route.
- Three consecutive ADB reads succeeded through the current data cable.

Software inventory cannot prove the number or mechanical condition of unused
physical ports. For any future physical phase, keep the host on AC power, use
the same known-good data cable and a direct root-hub port, disable sleep for the
bounded operation, and abort on any enumeration instability.

```text
BATTERY_READY = YES_97_PERCENT_CHARGING_25C
USB_HOST_STABLE = YES_THREE_ADB_READS_DIRECT_ROOT_HUB
HOST_POWER_READY = YES_SYSTEM_BATTERY_100_PERCENT
EXTERNAL_USB_HUB_IN_ACTIVE_ROUTE = NO
AVAILABLE_UNUSED_PORTS = NOT_OBSERVABLE_IN_SOFTWARE
```
