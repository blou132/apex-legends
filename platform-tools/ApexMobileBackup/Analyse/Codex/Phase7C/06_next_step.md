# Phase7C - Next step

The original phone must first be physically connected and visible as `device` in `adb devices`. The serial must remain redacted in all retained output.

After that prerequisite is satisfied, rerun only the approved read-only sequence:

1. query `pm path` and `dumpsys package` for the Apex package;
2. list its public OBB directory;
3. use `adb pull` only if the expected OBB files are present and readable;
4. hash the local copies and compare against complete historical hashes if those hashes can be recovered;
5. extract only local copies, validate PAK sizes/hashes, and then run the targeted raw scans.

If the OBB directory returns permission denied, stop. Do not use root, unlock, private `/data` access, reinstall, or any bypass.
