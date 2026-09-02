# +0x1f0 construction state

No constructor write to `+0x1f0` is proven. Phase16U therefore cannot
classify the construction value as NULL or non-NULL and cannot start a local
source slice.

- `CONSTRUCTION_ESTABLISHES_DOLPHIN_INTERFACE = UNKNOWN`
- `CONSTRUCTOR_DOLPHIN_INTERFACE_SOURCE = UNKNOWN`
- `POST_CONSTRUCTION_ACQUISITION_REQUIRED = UNKNOWN_NO_CONSTRUCTOR`
