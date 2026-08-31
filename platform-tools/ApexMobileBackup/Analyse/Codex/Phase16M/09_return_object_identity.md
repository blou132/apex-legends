# Return object identity

No returned pointer existed to validate. No heap or object memory was read.
The static `GCloudDolphinImp` primary vptr candidate remains untested at
runtime.

```text
RETURN_POINTER_MAPPING_VALID = UNKNOWN
RETURN_OBJECT_VPTR_CAPTURED = NO
RETURN_OBJECT_VPTR_MODULE = UNKNOWN
RETURN_OBJECT_IS_GCLOUDDOLPHINIMP = UNKNOWN
OBJECT_MEMORY_BYTES_READ = 0
```
