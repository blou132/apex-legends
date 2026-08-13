# Phase5 - Construction de la requete

## Entree native

**CONFIRMED**:

```text
RequestAvatarServerList
  -> FUN_07a31858  (Ghidra 0x7a31858, ELF 0x7931858)
  -> FUN_06bc68e8  (Ghidra 0x6bc68e8, ELF 0x6ac68e8)
```

Le thunk decode un objet de contexte et un `FString`, copie ce dernier, puis appelle `FUN_06bc68e8(context, &string)`. Dans l'implementation, ce `FString` est passe a la methode virtuelle `request+0x58`. Il s'agit donc de la source de l'URL, fournie dynamiquement par l'appelant Unreal.

## Ordre runtime

| Site | Operation | Statut |
|---:|---|---|
| `0x6bc6908` | `FUN_04d6bb5c()` obtient le manager HTTP | PROBABLE pour le type exact |
| `0x6bc6918` | `manager vtable+0x50`, cree/retourne une requete partagee | CONFIRMED |
| `0x6bc69a0` | `request vtable+0x58(request, url_fstring)` | CONFIRMED |
| `0x6bc69b0..0x6bc69e0` | construit le `FString` UTF-16 `GET` | CONFIRMED |
| `0x6bc69f4` | `request vtable+0x50(request, "GET")` | CONFIRMED |
| `0x6bc6a28` | `request vtable+0x98`, accede au delegate de completion | CONFIRMED |
| `0x6bc6a64` | allocation de `0x30` octets pour le callback | CONFIRMED |
| `0x6bc6bf8` | `FUN_06bc6ca0(callback, delegate_storage)` | CONFIRMED |
| `0x6bc6b74` / `0x6bc6bd4` | `request vtable+0x90(request)`, soumission | CONFIRMED |

Aucun appel distinct de configuration d'en-tete n'est visible dans cette fonction. Les autres `BLR` concernent l'allocateur partage, les compteurs de references et la destruction du callback temporaire. La liste exhaustive des 28 instructions `BL`/`BLR`, leurs sites et le desassemblage ARM64 sont dans `output/request_build.json`.

## Contexte callback

Disposition observee de l'allocation de `0x30` octets:

```text
+0x00 vptr: 0xa732390 pendant la construction,
             puis 0xa732320 pour l'objet actif
+0x08 objet de contexte capture
+0x10 requete HTTP
+0x18 controleur du pointeur partage de requete
+0x20 etat de delegate copie par FUN_06bc6ca0
+0x28 identifiant atomique de delegate
```

Le type concret du manager et les noms exacts des methodes virtuelles restent `PROBABLE`; leur role fonctionnel est confirme par les arguments, l'ordre et le callback de completion.

Export: `output/request_build.json`.
