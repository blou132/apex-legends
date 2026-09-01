# Plus 0x1f8 relation

No owner construction or pre-CheckUpdate initializer was recovered.

`PLUS_0X1F8_CONSTRUCTION_SOURCE = UNKNOWN`

`PLUS_0X1F0_AND_0X1F8_INITIALIZED_TOGETHER = UNKNOWN`

Shutdown still reads both fields in related control flow, which preserves the
prior `+0x1f8` probable-related-interface classification but does not prove a
paired acquisition bundle.
