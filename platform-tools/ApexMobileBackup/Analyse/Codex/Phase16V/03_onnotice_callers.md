# OnNotice callers

One exact direct caller reaches `DolphinUpdater::OnNoticeInstallApk`:

| Source ELF | Target ELF | Caller | Exact identifier |
|---|---|---|---|
| `0x07ff87c8` | `0x07ff68e8` | `FUN_080f8754` | `DolphinCallback::OnDolphinNoticeInstallApk` |

The callback loads `X0` from `[callback + 0x08]`, null-tests it, moves the
original callback argument 1 into `X1`, and performs the direct `BL`.

```text
ONNOTICE_DIRECT_BRANCH_COUNT = 1
```
