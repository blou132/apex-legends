# Lab network audit

## Required policy

```text
HUAWEI_TO_PC_LOCAL = REQUIRED
HUAWEI_TO_INTERNET = BLOCKED
```

## Available capabilities

- Huawei: production, non-root, non-debuggable Android build.
- Huawei tools: `ndc` and `iptables` binaries exist, but modifying routing/firewall without root is not an authorized path.
- ADB reverse: available for TCP only; it cannot carry ordinary UDP DNS.
- Host: no installed Python, mitmproxy, dnsmasq, named, tcpdump, tshark, or Wireshark command.
- Windows hosted network: reported unsupported.

PowerShell could implement a small local listener, but the missing isolated route and DNS path means the Huawei could not be allowed to reach that listener while proving all other Internet paths blocked.

## Decision

No lab listener, DNS service, proxy, firewall rule, hotspot, or route was created. The Huawei network remained in the state restored after Phase9C.

```text
STRICT_LOCAL_LAB_NETWORK_AVAILABLE = NO
SECOND_RUN_PERFORMED = NO
```
