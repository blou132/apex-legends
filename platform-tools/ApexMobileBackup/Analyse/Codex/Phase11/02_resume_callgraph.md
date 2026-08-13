# Resume callgraph

The requested depth was five. No root function was resolved, so traversing
callees would require selecting an arbitrary stripped function. Phase11 does
not do that.

```text
ROOT = UNKNOWN
REQUESTED_DEPTH = 5
EXPORTED_NODES = 0
EXPORTED_EDGES = 0
CALLGRAPH_STATUS = BLOCKED_BY_UNRESOLVED_JNI_ENTRY
```

Consequently, the absence of an edge to Lua, ClientLaunch, Login, rendering,
or a wait is not evidence that such an edge does not exist. Their reachability
from this JNI method remains `UNKNOWN`.
