# Provider callback use

Init stores `X5` to `GCloudDolphinImp+0x10`. The exact secondary provider
interface installed at provider `+0x08` later loads this field, null-tests it,
and dispatches virtual callback methods.

Observed forwarding slots include `+0x10`, `+0x18`, `+0x20`, `+0x28`, `+0x30`,
and `+0x48`.

```text
PROVIDER_CALLBACK_PERSISTED_OR_DISPATCHED = YES
```
