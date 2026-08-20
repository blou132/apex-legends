# Downloader UI

`DownloaderActivity.onCreate` inflates `layout/downloader_progress`. Portrait
and landscape resource variants provide the same functional dashboard. The
root uses the `downloadimageh` bitmap background rather than an empty view.

Relevant widgets are limited to:

- status text;
- progress bar;
- fraction and percent labels;
- speed and remaining-time labels;
- dashboard container;
- pause/resume and cancel controls;
- Wi-Fi settings and cellular-approval controls for download-service states.

In the Phase15D local-validation branch, `onPreExecute` should show the short
status `Verifying Download`, expose validation progress, retain a cancel action,
and hide controls that only apply to network download. On a bad CRC it shows
`XAPK File Validation Failed.`; on success it finishes without requiring user
interaction.

The activity has no explicit manifest theme and therefore inherits the
platform default. `SplashTheme` belongs to `GameActivity`, whose window
background is black; it is not declared for `DownloaderActivity`.

```text
DOWNLOADER_LAYOUT = layout/downloader_progress
DOWNLOADER_STATUS_WIDGETS = STATUS, PROGRESS, FRACTION, PERCENT, SPEED, REMAINING, ACTIONS
EXPECTED_VISIBLE_STATE = DOWNLOAD_BACKGROUND_WITH_VERIFYING_DOWNLOAD_PROGRESS_UI
```
