# Phase4 - Native RequestAvatarServerList

## Registration

**CONFIRMED**:

```text
name slot     0xa9db6c8 (ELF 0xa8db6c8)
function slot 0xa9db6d0 (ELF 0xa8db6d0)
function      FUN_07a31858 (ELF 0x7931858)
relocations   R_AARCH64_RELATIVE
```

`FUN_07a31858` est un thunk natif Unreal: il extrait les arguments depuis le frame d'appel, construit la valeur de retour et appelle directement `FUN_06bc68e8`.

## Implementation runtime

**CONFIRMED**:

```text
FUN_07a31858 -> FUN_06bc68e8
FUN_06bc68e8: Ghidra 0x6bc68e8, ELF 0x6ac68e8
```

`FUN_06bc68e8`:

- obtient un objet manager par `FUN_04d6bb5c`;
- ecrit le verbe UTF-16 `GET` via un appel virtuel;
- alloue un objet callback/contexte;
- soumet l'objet requete par appels virtuels;
- appelle `FUN_06bc6ca0` pendant la configuration de la requete.

Cela confirme une implementation de requete asynchrone. L'URL, le callback de reponse final et la conversion vers la liste de serveurs ne sont pas encore identifies.

## Constructeur reflection

**CONFIRMED**:

```text
FUN_07a41d4c
  ADRP/ADD a 0x7a41d6c/0x7a41d70
  => 0xa9e7b60
  => FUN_041b178c(0xb7132a8, 0xa9e7b60)
```

`FUN_07a41d0c` charge `0xa9e7ab0`, le descripteur precedent (`ReplacePawnProxyMat`). Il n'est pas la native RequestAvatar.

Exports: `output/requestavatar_native.json` et `output/requestavatar_runtime_probe.json`.
