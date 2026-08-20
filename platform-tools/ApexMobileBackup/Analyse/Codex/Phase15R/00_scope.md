# Phase15R scope

Phase15R investigates why `com.android.systemui` exceeds the approximately
20-second Android service-execution timeout on the disposable emulator. It does
not investigate Apex runtime behavior.

The mandatory device inventory was empty and WHPX acceleration was usable. The
only booted AVD was `ApexGraphicsProbe`, using the missing matrix quadrant:
`-gpu auto`, writable userdata, and snapshot load/save disabled. No application
was installed or launched, and no guest network state was changed.

Exactly one bugreport was captured after a visible SystemUI ANR. Raw logs,
dumps, the bugreport archive, and the capture harness remain local-only under
the ignored `Analyse/LocalInputs/Phase15R/` tree. This report publishes only
sanitized technical findings.
