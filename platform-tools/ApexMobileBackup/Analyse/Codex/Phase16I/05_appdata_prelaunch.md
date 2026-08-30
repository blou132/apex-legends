# Prelaunch AppData

Before the first Apex launch, a local-only archive and structural inventory were
captured for the package-private and package-owned external directories only.
No unrelated `/data` content was collected.

The private package scope contained only empty startup cache directories and no
regular files. Raw archives, modes, owners, hashes, and any later values remain
gitignored.

```text
APPDATA_PRELAUNCH_SNAPSHOT = SUCCESS
PRELAUNCH_PRIVATE_REGULAR_FILES = 0
```
