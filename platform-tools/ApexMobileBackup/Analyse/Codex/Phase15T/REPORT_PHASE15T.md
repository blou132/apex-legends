# Phase15T - Controlled PRA-LX1 storage cleanup

Date: 2026-08-25

## Executive result

Phase15T inventoried all 166 installed packages and identified updated consumer
applications that consumed real `/data` space. Eight applications were proven
nonessential on this dedicated lab and removed one at a time using supported
Package Manager operations. Stability and Apex integrity were checked after
every package.

Free `/data` space rose from `442952 KiB` (432.6 MiB) to a final recorded
`2121612 KiB` (2.023 GiB), reclaiming approximately 1.601 GiB. Cleanup stopped
immediately after crossing 2 GiB. No cache was separately cleared and no raw
filesystem deletion occurred.

Apex remained installed, stopped, and unchanged at version `1.3.672.546`, code
`64003140`; both OBB names and exact byte sizes still match Phase15S. All
protected Android, Google, Huawei, input, networking, storage, and UI packages
remained installed. ADB, SystemUI, window policy, Settings, and the Huawei
launcher passed active post-cleanup checks.

## Required result

```text
FREE_SPACE_BEFORE = /data 442952 KiB (432.6 MiB); shared 422472 KiB (412.6 MiB)
FREE_SPACE_AFTER = /data 2121612 KiB (2.023 GiB); shared 2101132 KiB (2.004 GiB)
TOTAL_SPACE_RECLAIMED = 1678660 KiB (1639.3 MiB; 1.601 GiB)

PACKAGES_ANALYZED = 166
PACKAGES_REMOVED = 8
CACHES_CLEARED = 0

TOP_SPACE_GAINS = Docs editor 568536 KiB; Google Search 493764 KiB; Photos 146720 KiB; Sheets 127244 KiB

MICROSD_PRESENT = NO
ADOPTABLE_STORAGE_SUPPORTED = NO

APEX_PACKAGE_INTACT = YES
APEX_OBBS_INTACT = YES

PHONE_STORAGE_READY = YES
RUNTIME_TEST_POSSIBLE_WITH_STORAGE_CAUTION = YES

SAFE_CLEANUP_EXHAUSTED = NO_STOP_GATE_REACHED

APEX_LAUNCHED = NO
APEX_DATA_CLEARED = NO
FACTORY_RESET_PERFORMED = NO

SENSITIVE_IDENTIFIERS_REDACTED = YES

FINAL_GATE = A FREE_SPACE_2GIB_REACHED
COMMIT = Reclaim PRA-LX1 lab storage Phase15T
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Removed packages

`com.google.android.googlequicksearchbox`, `com.google.android.apps.photos`,
`com.google.android.apps.maps`, `com.google.android.gm`,
`com.google.android.apps.docs.editors.docs`,
`com.google.android.apps.docs.editors.sheets`,
`com.google.android.apps.tachyon`, and `com.google.android.youtube`.

## Evidence boundary

This phase establishes storage readiness and post-cleanup platform stability. It
does not establish Apex runtime, graphics initialization, backend, login, Lua,
or gameplay behavior because Apex was never launched.
