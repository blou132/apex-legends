# Callback owner identity

The callback forwards its `+0x08` pointer to owner methods. In particular,
`FUN_080f68e8` contains the exact identifier
`DolphinUpdater::OnNoticeInstallApk`, while `FUN_080f6f04` implements the
post-extraction owner continuation. This identifies the owner as
`DolphinUpdater` with high semantic confidence, but no RTTI or owner vtable was
recovered.

```text
DOLPHIN_CALLBACK_OWNER_MODULE = libUE4.so
DOLPHIN_CALLBACK_OWNER_CLASS = DolphinUpdater (PROBABLE)
DOLPHIN_CALLBACK_OWNER_VTABLE = UNKNOWN
```
