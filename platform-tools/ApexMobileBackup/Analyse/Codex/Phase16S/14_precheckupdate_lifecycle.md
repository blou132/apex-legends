# Pre-CheckUpdate lifecycle

No proven CheckUpdate caller or owner dispatch sequence was recovered.
Accordingly, there is no bounded lifecycle function in which to identify a
previous owner method.

`PRE_CHECKUPDATE_OWNER_METHOD = UNKNOWN`

`PRECHECKUPDATE_INITIALIZER_CANDIDATES = NONE_PROVEN`

This is the exact boundary before a forbidden generic indirect-call or
whole-library lifecycle search would be required.
