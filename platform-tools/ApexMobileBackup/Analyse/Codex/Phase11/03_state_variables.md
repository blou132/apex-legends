# State variables

No function body can be attributed to `nativeResumeMainInit`, so Phase11 cannot
identify a global or field read/write as belonging to this entrypoint.

| Address | Initial value | Write site | Read site | Semantic candidate |
|---|---|---|---|---|
| `UNKNOWN` | `UNKNOWN` | `UNKNOWN` | `UNKNOWN` | `UNKNOWN` |

No variable was renamed as `resumed`, `engine ready`, `window ready`, `Lua
ready`, or `login ready` without a proven root and data flow.
