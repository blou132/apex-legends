# Wait timeline

All times below are relative to the Phase15H post-resume clock unless noted.

| Time | Observation | Classification |
| --- | --- | --- |
| Before T0 | SystemUI ANR dialog is drawn | Android system overlay; predates Apex splash |
| About -4.457 s | Apex Java splash-start message | Splash creation path |
| T0 | `GameActivity` resumed with local files present | Confirmed application transition |
| +0.109 s | First TDM POST attempt | Offline failure; no direct splash edge |
| +0.340 s | Downloader destroyed | Downloader no longer controls screen |
| +7.726 to +28.271 s | Bounded GCloud retries | Stop after early window; no direct edge |
| +15.246 to +260.479 s | Repeating TDM retries | Continue while process remains alive |
| +16.305 s | EGL initialization | Process-scoped graphics stage |
| +17.127 s | Vulkan discovery | Process-scoped graphics stage |
| About +25.269 s | Apex-owned Android alert window appears | Message obscured; role unknown |
| +30 s | Lightspeed logo and Android overlays visible | Rendered APK splash |
| +120 s | Same pixels as +30 s | No visible progress |
| +300 s | Apex still alive, foreground, visible, and drawn | Splash/dialog transition unresolved |

The Java logger is suppressed in Shipping after its callback registration, and
the registered callback is a no-op. Missing prepared/completed/dismiss messages
therefore do not prove those callbacks were absent. Persistent splash visibility
does prove that final dismissal had not occurred by the retained observations.

```text
TDM_WAIT_DEPENDENCY = NO_EVIDENCE
GCLOUD_WAIT_DEPENDENCY = NO_EVIDENCE
```
