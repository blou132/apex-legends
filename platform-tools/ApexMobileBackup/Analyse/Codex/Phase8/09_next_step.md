# Phase8 - Next justified step

Obtain an authorized readable file list, manifest, or PAK index export for the exact preserved build. It must expose filenames and container/entry metadata without decryption work performed in this project.

Then:

1. locate `Client/Launch/ClientLaunch.lua` in that listing and verify its container, mount prefix, entry boundary, and relation to the raw witness;
2. use the same proven naming convention to query the probable EventSystem logical path and its extensionless variant;
3. record provider, container, offset, size, compression/encryption flags, and stop if the entry remains protected or unreadable;
4. only if bytes are legitimately accessible, classify them locally and publish hashes/summary rather than the Lua asset.

Further raw-string scans are not justified: all four PAKs and both encodings have already been covered. Network contact, runtime hooking, key search, and index decryption remain out of scope.
