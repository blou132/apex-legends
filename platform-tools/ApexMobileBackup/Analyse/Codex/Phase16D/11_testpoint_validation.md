# Testpoint validation

No phone was opened and no contact was made. Reference photos and video
thumbnails remain local-only.

## Visual cross-check

Three independent visual references agree on the PRA-LX1 motherboard zone and
contact: a large exposed circular pad on the lower edge of the mainboard,
immediately beside the shield and the small pad row, above the battery flex.
The clear close-up exposes the `HL2PRAM ... RF VER.D` board marking and enough
surrounding geometry to preserve orientation.

| Reference | Exact family | Orientation/contact quality | Result |
| --- | --- | --- | --- |
| [PinOut PRA-LX1 reference](https://pinout.tembelpanci.com/2022/08/huawei-pra-lx1-test-point.html?m=1) | PRA-LX1 | Clear close-up and unambiguous marked pad | MATCH |
| [StiveMaroc PRA-LX1 demonstration](https://www.youtube.com/watch?v=jGbI9lmEBW8) | PRA-LX1 | Full device plus probe in the same board zone | MATCH |
| [Gsm Rashid Ali PRA-LX1 demonstration](https://www.youtube.com/watch?v=LUYAwGXbwD8) | PRA-LX1 | Board marking, shield, screw, pad row, and probe visible | MATCH |

An additional [DC-Unlocker exact-device case](https://forum.dc-unlocker.com/forum/modems-and-phones/huawei/169167-huawei-p8-lite-2017-brick)
reports that its attached PRA-LX1 point produced `USB SER`, became
`HUAWEI USB COM`, and was accepted by forum support after successful recovery.
This is functional corroboration but is not counted as a fourth visual match
because the attachment could not be retrieved for local comparison.

## Limit

The actual phone's silkscreen revision remains unknown until a future,
separately authorized physical inspection. The reference is therefore
`VERIFIED_ENOUGH` for planning at medium confidence, not a high-confidence
operator instruction.

```text
TESTPOINT_REFERENCE_FOUND = YES
TESTPOINT_REFERENCE_CONFIDENCE = VERIFIED_ENOUGH_MEDIUM
TESTPOINT_CROSSCHECK_COUNT = 3
PHONE_OPENED = NO
```
