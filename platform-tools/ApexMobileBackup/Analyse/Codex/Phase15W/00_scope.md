# Phase15W scope

Date: 2026-08-26

Phase15W is a local, read-only static analysis of the dynamic result callback
reached from `CPufferInitActionResult::ProcessResult`. It uses only the existing
`libgcloud.so` and `libUE4.so` Ghidra projects with `-noanalysis -readOnly`.

The bounded work covered:

- exact result/action/manager functions named by the Phase15W request;
- concrete vtables and RTTI reached from those functions;
- the exact `CreatePuffer` and `CreatePufferCallBack` registration anchors;
- an exact scalar scan in `libUE4.so` for `0x0430002e` through `0x04300032`.

No device, ADB, emulator, client launch, network operation, reimport, full auto
analysis, binary modification, or runtime instrumentation was used. Raw Ghidra
exports remain local-only under the gitignored `LocalInputs/Phase15W` path.

The stop rule applies after the resolved `GCloud::GCloudPufferImp` forwarder:
the downstream client callback is externally supplied, has no exact constructor
or registration anchor in `libUE4`, and none of the five exact error constants
has a `libUE4` hit.
