# Shutdown and restoration

After the hierarchy attempt, Apex was force-stopped. The guest network was
restored exactly to its initial state:

- airplane mode: disabled;
- Wi-Fi: enabled;
- mobile data: enabled.

The AVD shutdown was requested and completed. Final host checks found no ADB
endpoint and no emulator process. No physical device was used at any point.
