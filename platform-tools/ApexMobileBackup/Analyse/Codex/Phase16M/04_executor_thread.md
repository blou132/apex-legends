# Executor thread

The historical Phase15U/Phase16I/Phase16J evidence associated the native
GCloud/Dolphin caller role with `Thread-10`. The fresh launch exposed multiple
threads sharing that truncated name, so name-only selection was rejected.

A same-run GCloud log identified the exact Puffer manager caller TID. That TID
mapped to one live `Thread-10`, providing the required unique selection when
combined with the retained Dolphin correlation. Confidence is MEDIUM rather
than HIGH because no direct `CreateDolphin` trap was obtained.

Before active programming, `/proc` reported that this selected TID was already
traced by `GameProtector3`. A pre-active read-only attach returned `EPERM`; the
final watcher therefore stopped without trying another TID.

```text
CREATEDOLPHIN_EXECUTOR_THREAD_NAME = Thread-10
CREATEDOLPHIN_EXECUTOR_CONFIDENCE = MEDIUM
TARGET_TID_RESOLVED = YES_LOCAL_ONLY
SELECTED_TID_ALREADY_TRACED = YES_GAMEPROTECTOR3
CURRENT_BLOCKER = SELECTED_CREATEDOLPHIN_EXECUTOR_ALREADY_PTRACED_BY_GAMEPROTECTOR3
```
