# UI capture capability

Before the SystemUI ANR appeared, `uiautomator dump --compressed` successfully
wrote a guest XML file. A read-only pull produced a well-formed hierarchy with
four nodes. This proves the guest-to-host hierarchy capture mechanism works
when the foreground UI is unobstructed.

The raw XML remains local-only. It contains only the launcher baseline and was
not used as Apex evidence.

```text
UI_DUMP_BASELINE_WORKS = YES
BASELINE_XML_WELL_FORMED = YES
BASELINE_NODE_COUNT = 4
APEX_DIALOG_CAPTURE_ATTEMPTED = NO_PREFLIGHT_GATE
```
