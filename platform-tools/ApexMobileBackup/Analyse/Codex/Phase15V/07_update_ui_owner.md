# Update UI owner

No direct static edge from `CPufferInitActionResult::ProcessResult` reaches a
UE4 widget, UMG class, Lua event, Android dialog, or GCloud-rendered view. The
chain ends at a dynamic manager callback in `libgcloud.so`.

Phase15U observed that UIAutomator did not expose the rendered client text.
That is consistent with an engine-rendered surface, but it is not sufficient to
distinguish UE4 native UI, UMG, Lua-driven UI, or another texture-rendered
layer. The earlier shorthand that called the modal UE4-rendered is therefore
not used as direct ownership evidence.

No bounded Puffer resource key or user-facing update-error label was found.
The exact French text may be localized or built dynamically, but neither is
proven.

```text
UPDATE_UI_OWNER = UNKNOWN
UPDATE_UI_OWNER_CONFIDENCE = LOW
UPDATE_UI_TEXT_KEY = UNKNOWN
```
