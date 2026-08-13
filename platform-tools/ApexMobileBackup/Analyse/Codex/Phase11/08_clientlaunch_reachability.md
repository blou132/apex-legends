# ClientLaunch reachability

Phase7C Resume confirms a raw body witness for
`Client/Launch/ClientLaunch.lua` in local-only PAK data. Phase8 confirms that
this witness is not a resolved virtual path or loader edge.

Phase11 found no callgraph rooted at `nativeResumeMainInit`; therefore it cannot
connect the resume method to the Lua loader, module resolution, `require`, or
ClientLaunch execution.

```text
CLIENTLAUNCH_REACHABLE_FROM_NATIVE_RESUME = UNKNOWN
```
