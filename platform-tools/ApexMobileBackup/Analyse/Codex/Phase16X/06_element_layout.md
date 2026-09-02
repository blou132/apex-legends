# Element layout

The source registry element layout is confirmed from both the lookup helper
and `SkipAppUpdate`:

| Offset | Width | Proven use |
| --- | ---: | --- |
| `+0x00` | 8 | `UClass*` key |
| `+0x08` | 8 | selected UObject pointer |
| `+0x10` | 4 | integer chain field read by the lookup helper |

The remaining bytes in the `0x18` stride were not assigned semantics.

```text
COLLECTION_ELEMENT_SIZE = 0x18
COLLECTION_ELEMENT_KEY_OFFSET = 0x00
COLLECTION_ELEMENT_VALUE_OFFSET = 0x08
COLLECTION_ELEMENT_THIRD_FIELD_OFFSET = 0x10
COLLECTION_ELEMENT_LAYOUT_CONFIDENCE = CONFIRMED
```
