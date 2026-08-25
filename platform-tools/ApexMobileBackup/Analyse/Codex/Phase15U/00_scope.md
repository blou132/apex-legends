# Phase15U scope

Date: 2026-08-25

Phase15U performed one bounded, offline Apex launch on the physical Huawei
PRA-LX1. The objective was to observe the first client stage beyond the known
native/graphics frontier without answering any request or interacting with the
client UI.

The run used only ADB process/activity inspection, logcat, screenshots, one
UIAutomator dump, storage checks, and package/OBB metadata checks. No account,
touch, button press, dialog acknowledgement, backend, response, redirection,
proxy, debugger, profiler, root operation, package reinstall, or data/cache
clear was used.

Raw screenshots, logcat, UI hierarchy, full activity/window dumps, and the
local controller remain under the gitignored `LocalInputs/Phase15U` boundary.
Only cleaned metadata and conclusions are published here.

One local controller preflight initially stopped on a parser error before
logcat startup and before the launch command. Network state was restored, the
parser was corrected, and the launch count was revalidated as zero before the
single actual run. Exactly one Apex launch occurred.

```text
TARGET = Huawei PRA-LX1 only
MODE = OFFLINE_READ_ONLY_OBSERVATION
APEX_LAUNCH_COUNT = 1
RAW_EVIDENCE_PUBLISHED = NO
SENSITIVE_IDENTIFIERS_REDACTED = YES
```
