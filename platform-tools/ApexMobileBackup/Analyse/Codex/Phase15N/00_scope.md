# Phase15N scope

Date: 2026-08-20

Phase15N used a separate `ApexGraphicsProbe` AVD and an original application
with package `local.graphicsprobe`. The application contains no client code,
assets, strings, libraries, resources, or identifiers and requests no Internet
permission.

The initial repository-relative ADB path was absent, so that invocation did not
execute. The installed SDK ADB binary was then used for the required
`adb devices -l` preflight and reported no Android endpoint. All later guest
commands selected only the diagnostic emulator endpoint.

The phase did not boot or modify `ApexPhase9Lab`, install or launch Apex, use a
phone, access an account, or use root, hooks, debuggers, patching, spoofing,
proxies, certificates, DNS redirection, or a backend. Complete emulator output,
the generated APK, its signing key, and the local run harness remain ignored
under `Analyse/LocalInputs/Phase15N/`.

```text
PHYSICAL_DEVICE_PRESENT = NO
APEX_INSTALLED_OR_LAUNCHED = NO
PROBE_INTERNET_PERMISSION = NO
PROBE_AVD = ApexGraphicsProbe
PRESERVED_AVD = ApexPhase9Lab
```
