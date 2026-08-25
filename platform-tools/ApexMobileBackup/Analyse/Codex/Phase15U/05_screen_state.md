# Screen state

Screenshots at `+5`, `+15`, `+30`, and `+60` seconds are identical black
frames. They do not establish a crash because the Apex process and activity
remain present throughout those checkpoints.

At `+120` seconds, the client visibly renders its update interface. The
background shows update-module initialization at step `2/17`; a modal reports
that the update failed because the device has no network connection. The exact
displayed error code is `I54140714`.

No screen was touched and the visible quit action was not selected.

Exactly one compressed UIAutomator hierarchy was captured. It contains only
the Apex outer/content `FrameLayout` nodes and no client text or button nodes,
which confirms that this interface is UE4-rendered rather than exposed as
ordinary Android views.

```text
+5_S = UNKNOWN_BLACK_FRAME
+15_S = UNKNOWN_BLACK_FRAME
+30_S = UNKNOWN_BLACK_FRAME
+60_S = UNKNOWN_BLACK_FRAME
+120_S = UPDATE_ERROR_WITH_UPDATE_PROGRESS_2_OF_17
CLIENT_UI_NOT_EXPOSED_AS_ANDROID_VIEW = YES
UI_INTERACTION_PERFORMED = NO
```
