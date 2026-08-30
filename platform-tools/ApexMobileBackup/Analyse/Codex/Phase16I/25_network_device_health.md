# Network and device health

Post-trace checks confirmed airplane mode enabled, Wi-Fi and mobile data
disabled, empty IPv4/IPv6 route tables, and no Android default network.

The PRA-LX1 remained booted and responsive on USB ADB. Root still returned
`uid=0`, SELinux remained `Enforcing`, the launcher was responsive, battery was
healthy, and no Apex or helper process remained. Apex stays installed with the
verified OBBs while the device remains offline.

```text
NO_EXTERNAL_BACKEND_CONTACT_CONFIRMED_BY_ISOLATION = YES
NETWORK_ISOLATION_STILL_ACTIVE = YES
ROOT_STILL_HEALTHY = YES
SELINUX_STILL_ENFORCING = YES
POST_PHASE16I_DEVICE_HEALTH_OK = YES
```
