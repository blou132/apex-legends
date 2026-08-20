# Apex dialog

The SystemUI ANR window was already present before Apex launch. It is not the
Apex dialog.

At +5 and +15 seconds, WindowManager reported two full-screen Apex-owned Android
windows. At +30 seconds it reported a third Apex-owned `GameActivity` window
with wrap-content dialog geometry. The third window remained present through
+180 seconds. This independently reproduces the app dialog boundary seen in
Phase15H.

The permitted UI dump command completed but returned only its completion status
through the host capture, not hierarchy XML. The SystemUI overlay also remained
foreground, and logcat contains no dialog text. No button was clicked. The prior
visual `OK` action can be retained as a visual witness, but Phase15J does not
resolve the title or message.

No direct edge establishes that this dialog controls splash dismissal or is the
first bootstrap blocker.

```text
DIALOG_OWNER = APEX_GAMEACTIVITY_ANDROID_WINDOW
DIALOG_TITLE = UNKNOWN
DIALOG_MESSAGE = UNKNOWN
DIALOG_BUTTONS = OK_VISUAL_WITNESS_FROM_PHASE15H
APEX_OK_DIALOG_PRESENT = YES
APEX_OK_DIALOG_MESSAGE_RESOLVED = NO
APEX_OK_DIALOG_ROLE = UNKNOWN
```
