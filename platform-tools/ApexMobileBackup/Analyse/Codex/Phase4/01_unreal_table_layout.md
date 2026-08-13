# Phase4 - Layout des tables Unreal

Modele d'adresses: `GHIDRA = ELF VA + 0x100000`.

## Slot `0xa9db6c8`

**CONFIRMED** - table alternee de stride `0x10`:

```text
[0x00] const char *name
[0x08] native function pointer
```

L'entree cible est:

```text
0xa9db6c8 -> 0x22c409f "RequestAvatarServerList"
0xa9db6d0 -> 0x7a31858 FUN_07a31858
```

Chaque slot possede une relocation `R_AARCH64_RELATIVE`. La repetition `[STRING_PTR][TEXT_PTR]` et les thunks voisins correspondent a un tableau **FNameNativePtrPair-like**.

Temoins du meme tableau:

| Nom | Slot nom | Pointeur fonction |
|---|---:|---:|
| PrintStringWarning | `0xa9db5c8` | `0x7a285f0` |
| PropsWeaponTypeToItemId | `0xa9db5d8` | `0x7a37dc0` |
| PushEvent | `0xa9db5e8` | `0x59478dc` |
| ReleaseCharacter | `0xa9db658` | `0x7a33d48` |
| RemoveAttachedObject | `0xa9db688` | `0x7a3817c` |
| RequestAvatarServerList | `0xa9db6c8` | `0x7a31858` |
| RequestCharacter | `0xa9db6d8` | `0x51dba88` |
| RestorePawnProxyMat | `0xa9db708` | `0x7a2e710` |

## Slot `0xa9e7b70`

**PROBABLE** - champ `NameUTF8` d'un descripteur de parametres de `UFunction`, dont la base est `0xa9e7b60`.

Indices:

- `FUN_07a41d4c` passe exactement `0xa9e7b60` a `FUN_041b178c`;
- `0xa9e7b70` contient le nom cible;
- les champs suivants contiennent flags, chainage de proprietes et `ReturnValue`;
- les descripteurs voisins ont le meme layout et des noms de parametres tels que `WorldContextObject`.

Ce slot n'est pas un couple nom/native direct.

## Slot `0xa9ecb10`

**CONFIRMED** - tableau de fonctions de construction Unreal de stride `0x10`, mais la paire logique est decalee:

```text
[0x00] UFunction constructor
[0x08] function name
```

La preuve vient des descripteurs charges par chaque constructeur:

```text
0xa9ecb08 -> FUN_07a41d4c -> descriptor 0xa9e7b60 -> RequestAvatarServerList
0xa9ecb10 -> "RequestAvatarServerList"
0xa9ecb18 -> FUN_07a41d8c -> descriptor 0xa9e7cd8 -> fonction suivante
```

Lire ce tableau comme `[name][next function]` attribuerait a tort `FUN_07a41d8c` a la cible.

## Conclusion

- `0xa9db6c8`: native registration pair, **CONFIRMED**.
- `0xa9e7b70`: metadata de parametres/UFunction, **PROBABLE**.
- `0xa9ecb10`: function-info/constructeur UFunction, **CONFIRMED**.
- `FUN_07941d10`: lien cible toujours **INVALIDATED**.

Export complet: `output/requestavatar_table_layout.json`.
