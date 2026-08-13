# GameActivity lookup

No unique `JNI_OnLoad` function or delegated registration helper was reached.
Phase12 therefore cannot attribute a `FindClass` call to either of these class
name forms:

- `com/epicgames/ue4/GameActivity`
- `com.epicgames.ue4.GameActivity`

```text
GAMEACTIVITY_CLASS_LOOKUP = UNKNOWN
CLASS_NAME = UNKNOWN
CALL_SITE = UNKNOWN
RETURN_VALUE_STORAGE = UNKNOWN
DOWNSTREAM_CALLS = UNKNOWN
```

This is not evidence that GameActivity is never looked up. It records only that
the lookup is not reachable from a confirmed Phase12 root.
