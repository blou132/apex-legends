# Puffer configuration and request path

`GCloudPufferImp::Init` is identified at Ghidra `0x50323c` by its source/log
strings. It builds the internal configuration from fields including:

- `puffer_product_id`;
- `puffer_group_mark_id`;
- `puffer_game_id`;
- `update_type`;
- `dolphin_product_id`;
- application and resource versions;
- resource directory and update policy flags.

No values or credentials are published.

`FUN_004ffd44` reads the GCloud singleton returned by `FUN_004d3fa8`, uses its
field at `+0x48`, and constructs service name `PufferUpdateService`. The exact
runtime host is not a literal in the coordinator. This supports a runtime
configuration source, while lower SDK host-selection details remain opaque.

`FUN_004fd278` creates the client/callback object, registers
`FUN_004fc7c0` (`ResUpdateCallBack`), and initiates the asynchronous request.
Generated RPC code contains method `PufferUpdateService.GetUpdateInfo`;
`FUN_004ed038` is a generated asynchronous request function for that method.
The coordinator-to-concrete transport edge is virtual and is not promoted to a
direct call.

```text
PUFFER_REQUEST_COORDINATOR = FUN_004ffd44 / CPufferInitAction::MakeSureGetUrlFromServer
PUFFER_TRANSPORT_FUNCTION = FUN_004ed038 PufferUpdateService.GetUpdateInfo (generated path; caller edge dynamic)
PUFFER_CALLBACK_FUNCTION = FUN_004fc7c0 / ResUpdateCallBack
PUFFER_HOST_SOURCE = RUNTIME_CONFIG
```
