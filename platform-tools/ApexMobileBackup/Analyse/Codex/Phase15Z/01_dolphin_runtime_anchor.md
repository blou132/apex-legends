# Dolphin runtime anchor

Phase15Y established the runtime sequence:

`NormalConnectVersionSvr -> OnActionError -> ClientEvent/UpdateResult -> ProcessActionError`

The observed update result was decimal `154140714`, hexadecimal `0x0930002a`,
with result `-1`. It preceded the screenshot containing `I54140714` by about
1.7 seconds.

Phase15Z maps this runtime name to `FUN_00550ee4` in `libgcloud.so` and maps
the action RTTI to `dolphin::gcloud_version_action_imp`. The runtime timing is
correlation evidence only; it does not by itself prove visible-code ownership.
