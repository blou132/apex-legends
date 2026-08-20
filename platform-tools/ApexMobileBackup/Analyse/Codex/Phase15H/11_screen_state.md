# Screen state

Three screenshots were captured locally and remain ignored. They contain an
Android immersive-mode tutorial overlay above the Apex window.

| Post-resume target | Classification | Visible application content beneath overlay |
| ---: | --- | --- |
| `+5 s` | `OTHER` | dark game surface and partial wait UI |
| `+30 s` | `OTHER` | Lightspeed Studios splash/logo and wait/OK UI |
| `+120 s` | `OTHER` | same Lightspeed splash/logo and wait/OK UI |

The screenshots are not pixel-black, so the prior black-screen description is
not confirmed for this cached post-resume run. The system overlay prevents a
clean application-only screen classification, and the cause of the underlying
wait state is not established.

```text
SCREEN_STATE_5S = OTHER
SCREEN_STATE_30S = OTHER
SCREEN_STATE_120S = OTHER
SCREEN_DETAIL_5S = SYSTEM_IMMERSIVE_OVERLAY_ABOVE_GAME_WAIT_UI
SCREEN_DETAIL_30S = SYSTEM_IMMERSIVE_OVERLAY_ABOVE_LIGHTSPEED_SPLASH_WAIT_UI
SCREEN_DETAIL_120S = SYSTEM_IMMERSIVE_OVERLAY_ABOVE_LIGHTSPEED_SPLASH_WAIT_UI
BLACK_SCREEN_CONFIRMED_BY_PIXEL_CAPTURE = NO
BLACK_SCREEN_CAUSE = NOT_APPLICABLE_SCREEN_NOT_BLACK
WAIT_STATE_CAUSE = UNKNOWN
```
