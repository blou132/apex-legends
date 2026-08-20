# Phase15C scope

Date: 2026-08-20

## Objective

Boot the unchanged `ApexPhase9Lab` AVD under WHPX and validate its guest ARM64
translation configuration without installing or launching Apex.

## Controlled boundary

- The mandatory initial ADB inventory was empty: no physical device and no
  pre-existing emulator endpoint.
- WHPX and the existing AVD configuration were revalidated before launch.
- The AVD used normal acceleration with `-no-snapshot-load`,
  `-no-snapshot-save`, and no wipe.
- Guest commands were read-only and always selected the discovered emulator.
- No APK, OBB, PAK, SO, account data, or Apex package was installed or run.
- No Huawei or Samsung command was issued.
- The AVD was shut down through the emulator console command.

Complete emulator output remains ignored under `LocalInputs/Phase15C/`.
Published output contains only cleaned metadata and no endpoint port.
