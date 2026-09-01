# Non-null writer

Phase16S found no lifecycle-qualified non-null write to
`DolphinUpdater+0x1f0`.

`NON_NULL_WRITE_SITE = UNKNOWN`

`NON_NULL_WRITE_REACHABLE = UNKNOWN`

`NON_NULL_WRITE_SOURCE_REGISTER = UNKNOWN`

`NON_NULL_WRITE_VALUE_SOURCE = UNKNOWN`

The Phase16Q result therefore stands: the only proven `+0x1f0` write is the
reachable Shutdown clear, not the acquisition write.
