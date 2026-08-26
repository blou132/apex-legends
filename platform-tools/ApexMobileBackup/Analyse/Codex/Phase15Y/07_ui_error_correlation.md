# UI error correlation

The `+120 s` screenshot remains the only exact `I54140714` witness. Earlier
screenshots through `+60 s` are black, so `+120 s` is the first captured visible
update state.

The bounded log does not contain `I54140714`. It does contain Dolphin
`UpdateResult` code `154140714` about 1.7 seconds before the screenshot. The
rendered digits are a suffix match, and the failure class and ordering are
consistent, but no formatter, mapper, UI event, or direct call edge is exposed.

Puffer's exact `70254639` result occurs about 39 seconds after the screenshot.
This ordering weakens the earlier timing-only Puffer attribution and supports
the Dolphin result as a better candidate, but it still does not prove
construction or ownership of `I54140714`.

```text
I54140714_LOGCAT_ANCHOR = NOT_FOUND
I54140714_CONSTRUCTION_CONFIRMED = NO
PUFFER_TO_UI_TEMPORAL_ORDER = DOLPHIN_RESULT_BEFORE_UI; PUFFER_FINAL_RESULT_AFTER_UI
```
