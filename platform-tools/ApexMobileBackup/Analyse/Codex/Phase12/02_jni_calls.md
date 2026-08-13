# JNI calls

The Phase12 stop rule prevents selecting an unrelated stripped function and
interpreting arbitrary C++ vtable calls as JNI. No resolved `JNI_OnLoad` body
exists from which to attribute these JNI operations:

| JNI operation | Expected interface slot | Reached from JNI_OnLoad |
|---|---:|---|
| JavaVM `GetEnv` | `0x30` | `UNKNOWN` |
| `FindClass` | JNIEnv index 6 | `UNKNOWN` |
| `GetMethodID` | JNIEnv index 33 | `UNKNOWN` |
| `GetStaticMethodID` | JNIEnv index 113 | `UNKNOWN` |
| `RegisterNatives` | JNIEnv index 215 | `UNKNOWN` |

No indirect call was assigned one of these names solely from a common vtable
offset.
