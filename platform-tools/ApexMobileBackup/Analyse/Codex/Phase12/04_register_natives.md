# RegisterNatives

No GameActivity class value or registration helper is reachable from a
confirmed `JNI_OnLoad` root. Consequently no indirect JNIEnv call can be
confirmed as GameActivity `RegisterNatives`.

```text
REGISTER_SITE = UNKNOWN
CLASS_SOURCE = UNKNOWN
TABLE_POINTER = UNKNOWN
COUNT = UNKNOWN
STATIC_OR_DYNAMIC_TABLE = UNKNOWN
```

Phase11's orphaned full JNI name in `.dynstr` is not forced into a registration
table and does not establish static or dynamic registration.
