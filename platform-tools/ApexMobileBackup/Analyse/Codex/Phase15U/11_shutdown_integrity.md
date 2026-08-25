# Shutdown, restoration, and integrity

The decisive update-error frame was captured at `+120 s`. The local controller
did not perform visual classification during the run, so the state was
recognized during post-run inspection and the process continued only to the
authorized hard maximum of 180 seconds. No interaction occurred during that
interval.

At the hard limit, Apex was force-stopped. Its process then disappeared. No
temporary debug property was used. Airplane mode was disabled, mobile data was
restored to enabled, Wi-Fi was restored to enabled, and an active default
network/default route returned. Apex was not relaunched after restoration.

Post-run package metadata and both exact OBB names/sizes still match preflight.
No cache, data, preference, OBB, APK, or validation state was cleared or
modified by the controller.

```text
STOP_REASON = MAX_RUNTIME_180S; DECISIVE_UPDATE_ERROR_CAPTURED_AT_120S
APEX_PROCESS_PRESENT_AFTER_STOP = NO
NETWORK_RESTORED = YES
APEX_PACKAGE_INTACT_POSTRUN = YES
APEX_OBBS_INTACT_POSTRUN = YES
TEMPORARY_DEBUG_PROPERTY_USED = NO
APEX_RELAUNCHED_AFTER_NETWORK_RESTORE = NO
```
