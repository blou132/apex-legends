# Client consumer

The first concrete consumer beyond `CPufferInitActionResult::ProcessResult` is
`GCloud::GCloudPufferImp` in `libgcloud.so`:

```text
ProcessResult
-> cu::IPufferCallBack slot +0x10 / FUN_00503050
-> FUN_00503024
-> external client callback slot +0x10
```

`FUN_00503024` is a raw forwarding boundary. It loads the external callback
from `GCloudPufferImp+0x18` and invokes its slot `+0x10` with the success flag
and error code unchanged.

No exact `libUE4` registration site, constructor, internal-code hit, or direct
consumer is available. Resolving the next target would require an unanchored
global vtable/class scan, so the Phase15W stop rule applies.

```text
CLIENT_PUFFER_CONSUMER = UNKNOWN_BEYOND_GCLOUDPUFFERIMP_FORWARDER
CLIENT_ERROR_MAPPING_FUNCTION = UNKNOWN
CLIENT_ERROR_MAPPING_STYLE = UNKNOWN; libgcloud boundary is RAW_PASS_THROUGH
LOGIN_DIRECT_EDGE_FROM_UPDATE = NO_NEW_EDGE
SERVER_LIST_DIRECT_EDGE_FROM_UPDATE = NO_NEW_EDGE
```
