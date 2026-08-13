# Candidate library classification

Classification uses the strongest target-relevant evidence for each library.

| Class | Libraries | Reason |
|---|---|---|
| A `EXACT_FULL_JNI_EXPORT` | none | exact full export absent in all 17 |
| B `SHORT_NATIVE_NAME_AND_REGISTRATION_EVIDENCE` | none | short name absent outside the libUE4 string witness; no target row |
| C `JNI_ONLOAD_EXPORTED` | `libCrashSight.so`, `libGPM.so`, `libGVoice.so`, `libINTLTAB.so`, `libMSDKCore.so`, `libTDataMaster.so`, `libanogs.so`, `libanort.so`, `libgcloud.so`, `libgcloudcore.so`, `libmmkv.so`, `libtgpa.so` | authoritative global function exports |
| D `GAMEACTIVITY_STRING_EVIDENCE_ONLY` | `libUE4.so` | full name, short name, slash class, and signature; no target symbol/table |
| E `NO_RELEVANT_EVIDENCE` | `libgnustl_shared.so`, `libtransceiver.so`, `libzip.so`, `libzlib.so` | no target identity or JNI root |

No Class C library contains either GameActivity class spelling or either target
method spelling. Therefore its `JNI_OnLoad` cannot be linked to the requested
ordinary static `JNINativeMethod` row.

`libUE4.so` is the strongest contextual candidate because Java and manifest
loading point to UE4 and its binary carries all target strings. Its evidence
remains `STRING_ONLY`; ownership is not promoted to probable or confirmed.
