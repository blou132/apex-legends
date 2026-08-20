# Phase15H scope

Date: 2026-08-20

Phase15H observed one offline launch of the unchanged Apex installation in the
existing `ApexPhase9Lab` AVD. The validated OBB cache from Phase15G was kept
unchanged. The observation began at process creation, established a separate
clock when `GameActivity` resumed after `DownloaderActivity`, and ended at
`+300 s` on that post-resume clock.

The run used only lifecycle logs, targeted activity/window state, three local
screenshots, process-alive checks, and a non-privileged thread-name inventory.
It did not use a phone, account, network response, memory map, stack, register,
profiler, debugger, root, hook, patch, proxy, certificate, or backend.

Raw logs, dumps, screenshots, process identifiers, emulator endpoint details,
and the run script remain local-only and ignored. Published evidence contains
only relative times and cleaned component/state metadata.

```text
QUESTION = WHAT_HAPPENS_AFTER_GAMEACTIVITY_RESUMES_WITH_VALIDATED_OBB_DATA
PROCESS_START_CLOCK = USED_FOR_DOWNLOADER_CORRELATION
POST_RESUME_T0 = GAME_ON_RESUME_BODY_COMPLETED_WITH_HAS_ALL_FILES_TRUE
OBSERVATION_LIMIT = +300S_POST_RESUME
LAUNCH_COUNT = 1
```
