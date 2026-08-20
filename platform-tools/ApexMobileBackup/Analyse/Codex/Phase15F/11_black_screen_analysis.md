# Black-screen correlation

The lifecycle timing establishes which activity owns the foreground window:
`DownloaderActivity` is displayed at +15.864 s and the `GameActivity` window is
hidden at +17.064 s. The observed post-launch screen therefore coincides with
the downloader's local-validation interval rather than a visible UE4 gameplay
window.

Static resources do not support an empty-layout or transparent-theme theory.
The downloader inflates a concrete progress layout with a bitmap background,
status, progress, and action widgets. A surface-sync timeout occurred before
Android subsequently reported the activity displayed, so that timeout alone
is not a demonstrated rendering failure.

The log also rejects several stronger claims for this interval:

- no missing-OBB branch was selected;
- no OBB outer-size mismatch was reported;
- no network service or network-wait state was selected;
- no storage denial or validator failure was logged.

There is no screenshot or framebuffer capture, so the actual pixels remain
unknown. It is probable that the user-visible black interval belongs to the
foreground downloader activity, but current evidence does not prove whether
its background/progress UI rendered incorrectly, was visually dark, or was
simply not recognized by the observer. It is not justified as the final
black-screen cause.

```text
BLACK_SCREEN_DOWNLOADER_LINK = PROBABLE_TEMPORAL_AND_FOREGROUND_OWNERSHIP_LINK
EMPTY_DOWNLOADER_LAYOUT = NO
TRANSPARENT_DOWNLOADER_THEME = NO_EVIDENCE
NETWORK_WAIT = CONFIRMED_NO_FOR_OBSERVED_BRANCH
OBB_MISMATCH = NO_EVIDENCE
FINAL_BLACK_SCREEN_CAUSE = UNKNOWN
```
