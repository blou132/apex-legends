# MessageBox native bridge

The exact DEX exposes `com.epicgames.ue4.MessageBox01` with:

| Method | DEX address | Signature | Parameter order |
| --- | ---: | --- | --- |
| constructor | `0x5a15fc` | `()V` | none |
| `setCaption` | `0x5a18a0` | `(Ljava/lang/String;)V` | caption |
| `setText` | `0x5a18b8` | `(Ljava/lang/String;)V` | message |
| `addButton` | `0x5a164c` | `(Ljava/lang/String;)V` | button label |
| `show` | `0x5a18d0` | `()I` | returns selected button |

The native lookup-cache initializer `FUN_082480a8` (ELF `0x81480a8`) resolves
a constructor with `()V`, string setters at structure offsets `+0x10`, `+0x28`
and `+0x40`, a void method at `+0x58`, and `show()I` at `+0x70`.
`setCaption`, `addButton`, `(Ljava/lang/String;)V`, `show`, and `()I` are exact
plaintext constants. The class and two method names are encoded in the static
image, preventing an exact native-to-class string edge.

`FUN_059ef114` directly calls Ghidra `0x81dea3c` with arguments
`(0, message, title)`. That target lies in protected/non-disassemblable code in
the Ghidra image, so its internal calls to the lookup cache cannot be proven.

```text
MESSAGEBOX_NATIVE_BRIDGE = REFLECTION_LOOKUP (PROBABLE)
NATIVE_MESSAGEBOX_FUNCTION_GHIDRA = 0x81dea3c (DIRECT TARGET)
NATIVE_MESSAGEBOX_FUNCTION_ELF = 0x80dea3c
CONFIDENCE = PROBABLE_MESSAGEBOX_SEMANTICS; CONFIRMED_CALL_TARGET
```

There is no evidence for a static JNI export or a `RegisterNatives` row for
this helper.
