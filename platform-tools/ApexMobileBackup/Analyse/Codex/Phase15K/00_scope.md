# Phase15K scope

Phase15K was authorized as one bounded, read-only attempt to resolve the exact
Apex-owned Android dialog. It targeted only the existing `ApexPhase9Lab` AVD.

The mandatory ADB inventory contained no Android endpoint before boot, so no
physical phone was present. WHPX was usable and the existing AVD was booted
without wipe, reinstall, APK/OBB modification, cache deletion, root, or
snapshot load/save.

The run was stopped at the SystemUI preflight gate. Apex was never launched,
no dialog button or system overlay was touched, and no account or network
service was used.

Raw window/activity dumps, the baseline hierarchy XML, and emulator output are
local-only and ignored by Git.
