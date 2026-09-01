# W23 helper 2

`FUN_04a94aac` parses the resolved UTF-16 setting as a boolean. The bounded
comparisons include true spellings such as `True` and `Yes`, false spellings
such as `False`, `No`, and `Off`, additional engine-provided boolean aliases,
and a final base-10 numeric parse.

Its return bit is the parsed boolean value. No log or callback is tied to the
result.

```text
W23_HELPER2_FUNCTION = ENGINE_BOOLEAN_STRING_PARSER
W23_HELPER2_RETURN_BIT_MEANING = PARSED_B_USE_IFSFILE_BOOLEAN
W23_HELPER2_OBSERVABLE_MARKER = NONE
```
