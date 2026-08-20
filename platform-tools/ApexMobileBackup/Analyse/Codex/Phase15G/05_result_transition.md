# Result transition

The runtime success chain is explicit:

```text
full local validation
  -> DownloaderActivity onPostExecute true
  -> DownloaderActivity result 1 and finish
  -> GameActivity onActivityResult, HasAllFiles=true
  -> GameActivity onResume
  -> onResumeBody, HasAllFiles=true
```

The foreground activity changed from `DownloaderActivity` back to
`GameActivity`. The process then remained alive for the authorized additional
30 seconds, and the downloader did not immediately return.

No line names `nativeResumeMainInit` in this log. Its absence does not weaken
the confirmed Android/downloader success chain; it remains no new runtime
witness for that native method in this observation.

```text
DOWNLOADER_RESULT = 1
GAMEACTIVITY_RESUMED = YES
NATIVE_RESUME_MAIN_INIT_WITNESS = NO_NEW_EVIDENCE
PROCESS_STABLE_AFTER_SUCCESS = YES_30S
DOWNLOADER_RETURNED_AFTER_SUCCESS = NO
```
