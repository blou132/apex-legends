# ES2 fallback predicate

The fallback value is read by `FUN_04a93cf4` from:

```text
section = /Script/AndroidRuntimeSettings.AndroidRuntimeSettings
key = bBuildForES2
```
At Ghidra `0x59ef5b4-0x59ef5b8`, a nonzero value branches around the dialog and
continues with graphics state `1`. A zero value enters the diagnostic builder
and direct MessageBox call.

The Phase15L runtime message and this native branch agree that the analyzed
package has no ES2 fallback.

```text
ES2_FALLBACK_CHECK_SOURCE = AndroidRuntimeSettings.bBuildForES2
ES2_FALLBACK_AVAILABLE = CONFIRMED NO
```
