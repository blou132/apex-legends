# Phase15P scope

Date: 2026-08-20

Phase15P authorized one final bounded, offline Apex launch on the preserved
`ApexPhase9Lab` AVD with the command-line-only `-gpu host` override. Launch was
strictly gated behind a 120-second SystemUI stability window.

No physical Android device was present. The AVD booted read-only without
snapshot load/save or wipe. The `0 s` checkpoint was clear, but the `30 s`
checkpoint exposed a visible `Application Not Responding:
com.android.systemui` window. The hard stop was applied immediately.

Apex was not launched, network state was not changed, and the ANR was not
interacted with. Raw emulator and Android dumps remain ignored under
`Analyse/LocalInputs/Phase15P/`; only cleaned metadata is published here.
