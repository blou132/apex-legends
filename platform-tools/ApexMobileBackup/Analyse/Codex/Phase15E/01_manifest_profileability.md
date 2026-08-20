# Manifest profileability

## Exact artifact

The local-only APK identity was reconfirmed before manifest analysis:

```text
SIZE = 96228800
SHA256 = 2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0
IDENTITY = MATCH
```

## Parsed manifest

`aapt2 dump xmltree` parsed the exact binary `AndroidManifest.xml` successfully.
The application element does not enable `android:debuggable`, so the manifest
default is false. A component-level `android:debuggable=false` value also
exists on `GameActivity`; it does not make the application debuggable.

The parsed tree contains no `<profileable>` element, no `android:shell`
attribute for such an element, and no `profileableByShell` field. Under the
required classification, an absent profileable element means shell
profileability is disabled. `android:extractNativeLibs=true` is explicit.

```text
PACKAGE_DEBUGGABLE = CONFIRMED NO
PROFILEABLE_ELEMENT_PRESENT = CONFIRMED NO
PROFILEABLE_SHELL = CONFIRMED NO (ELEMENT ABSENT)
PROFILEABLE_ENABLED = CONFIRMED NO (ELEMENT ABSENT)
PROFILEABLE_BY_SHELL = CONFIRMED NO
EXTRACT_NATIVE_LIBS = CONFIRMED YES
```

## Cross-checks

- `aapt2 dump badging` independently confirms the package, version, target SDK,
  and ARM64-only native code from the same APK.
- Phase15D package metadata matches package version `1.3.672.546`, code
  `64003140`, and primary ABI `arm64-v8a`.
- Phase14's runtime package audit classified the same production Apex package
  as non-debuggable.
- No retained Phase15D package dump exposes a separate
  `profileableByShell` field. No value is invented from that absence; the
  negative profileability decision comes from the authoritative exact manifest.

The package therefore does not authorize official shell profiling. This is
distinct from debugger attachment, which is also outside this phase.
