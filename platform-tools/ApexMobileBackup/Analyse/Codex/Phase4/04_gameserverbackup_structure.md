# Phase4 - GameServerBackupIpList

## Descripteurs

Chaine: `0x2280ba5` (ELF `0x2180ba5`). Relocations confirmees:

- `0xaf65f20`;
- `0xaf65f70`.

Le tableau de pointeurs de proprietes a `0xaf660b8..0xaf66170` reference les descripteurs avec un stride de pointeur `8`.

## Type et offset

**CONFIRMED** - le descripteur externe a `0xaf65f20` porte le flag genere bas `0x17`, coherent avec `EPropertyGenFlags::Array`, et l'offset encode `0x150`:

```text
0xaf65f20 name  -> GameServerBackupIpList
0xaf65f38 flags -> 0x4500000017
0xaf65f40 dims/offset -> 0x00000150_00000001
```

**PROBABLE** - l'element interne est de type `Name` (`0x15`), donc le type complet est probablement `TArray<FName>`.

## Owner

**PROBABLE** - le bloc de parametres de classe qui suit le tableau contient:

```text
0xaf66180 -> "Login"
0xaf66190 -> table de proprietes
0xaf661a0 -> table de fonctions
```

Le proprietaire est donc probablement la classe Unreal `ULogin` (nom genere `Login`).

## Readers et writers

**UNKNOWN**. Le scan trouve de nombreux acces ARM64 a `+0x150`, mais aucun n'est relie de facon unique a une instance `Login`. Aucun reader/writer n'est nomme sans cette preuve d'ownership runtime.

Le voisin `OpenServerList` appartient a la meme region de metadata, ce qui est structurellement pertinent mais ne prouve pas un flux de donnees.

Export: `output/gameserverbackup_structure.json`.
