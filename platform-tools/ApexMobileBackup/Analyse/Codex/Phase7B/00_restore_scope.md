# Phase7B - Restore scope

Date: 2026-08-13.

Phase7B is limited to locating the four original local-only PAK files from the historically analyzed Apex Mobile build, validating their identities, and rerunning the Phase7 raw scans only if all inputs are restored.

The search was non-destructive and filename-based in the authorized workspace, Downloads roots, Desktop root, and visible `ApexMobileBackup` directories. No file was downloaded, moved, modified, decrypted, decompressed, patched, or published. The phone and all network services were untouched.

Because no expected PAK or backup OBB was found, no `LocalInputs` working copy was created and no raw PAK scan was run. Phase7 remains authoritative for the native loader and EventSystem evidence.
