# GLES 3.1 predicate

`FUN_05db077c` (Ghidra `0x5db077c`, ELF `0x5cb077c`) initializes the Android
OpenGL capability object. It obtains strings using the call whose arguments
are `GL_EXTENSIONS (0x1F03)`, `GL_RENDERER (0x1F01)`, and
`GL_VERSION (0x1F02)`. Although the imported function is mislabeled in the
existing Ghidra database, the enum values, character-pointer return, and uses
identify the operation as `glGetString`.

`FUN_059ef114` parses the stored `GL_VERSION` text into integer major and minor
values. The exact version capability predicate is:

```text
supports_es31 = (major == 3) && (minor >= 1)
```
The primary ES31 path additionally requires:

```text
bBuildForES31 == true
r.Android.DisableOpenGLES31Support == 0
```

No use of `ro.opengles.version` was found on this direct path.

```text
GLES31_CHECK_SOURCE = glGetString(GL_VERSION) -> parsed major/minor
GLES31_REQUIRED_VALUE = OpenGL ES 3.1 or newer minor in major 3
GLES31_PREDICATE = major == 3 && minor >= 1
```
