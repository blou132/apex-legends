# Alternate system-image decision

The local SDK contains only the current image:

```text
system-images;android-36.1;google_apis_playstore;x86_64
```

There is no locally installed alternate image. No package was downloaded and
no AVD was created in Phase15R.

An alternate Android system-image test is technically justified because all
four renderer/userdata matrix quadrants fail before any client launch, the
exact SystemUI trace shows scheduler starvation during platform startup, and
no Apex-specific cause exists in this phase. A future phase should use a new
disposable AVD and preserve the existing AVDs.

```text
ALTERNATE_LOCAL_IMAGES = []
ALTERNATE_SYSTEM_IMAGE_TEST_JUSTIFIED = YES
```
