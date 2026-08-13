# Phase3B - SyncPayloadToGameServer RPC

## Source

- `Phase3\Phase3B\output\syncpayload_callgraph.json`

## Resultat

UNKNOWN: Phase3B ne confirme pas que `SyncPayloadToGameServer` passe par le chemin RPC Unreal standard.

UNKNOWN: la fonction native exacte de `SyncPayloadToGameServer` n'est pas confirmee.

PROBABLE: les fonctions racines exportees autour de `0x7c14710` / `0x7eb80dc` sont liees a de la construction ou serialization de metadata, mais le lien avec un appel RPC runtime reste non prouve.

## Elements confirmes

- Cible `SyncPayloadToGameServer_string @ 0x221f64a`, bloc `.rodata`, aucune xref.
- Occurrence brute: `0x231f64a`, preview `SyncPayloadToGameServer`, aucune xref.
- Metadata candidates:
  - `0xab489d0`
  - `0xab48ac8`
  - `0xab48c20`
  - `0xad87bf8`
  - `0xad87c18`
  - `0xad87c68`
- Toutes ces cibles ont 0 xref directe dans l'export.

## Fonctions racines

Fonctions exportees comme candidates:

- `FUN_07c14710 @ 0x7c14710`
- `FUN_07c148b0 @ 0x7c148b0`
- `FUN_07c14cac @ 0x7c14cac`
- `FUN_07c14e44 @ 0x7c14e44`
- `FUN_07eb80dc @ 0x7eb80dc`
- `FUN_07eb8224 @ 0x7eb8224`
- `FUN_07eb8364 @ 0x7eb8364`

Extrait de `FUN_07c14710`:

```c
void FUN_07c14710(void)
{
  undefined8 *puVar1;

  puVar1 = (undefined8 *)FUN_045f577c(0x10);
  *puVar1 = &UNK_0ab74288;
  puVar1[1] = 0x8000000e8;
  return;
}
```

Les fonctions `FUN_07c14cac`, `FUN_07c14e44`, `FUN_07eb80dc`, `FUN_07eb8224` et `FUN_07eb8364` manipulent des champs autour de `param_2 + 0x20`, `0x38`, `0x78`, `0x80` et appellent `FUN_04ba8da8` ou un appel virtuel `+0xd0`, ce qui ressemble a une logique de serialization/deserialization.

## Recherche RPC standard

Termes sans hit:

- `ProcessEvent`
- `UNetDriver`
- `UNetConnection`
- `UChannel`
- `CallRemoteFunction`
- `InternalProcessRemoteFunction`
- `ProcessRemoteFunction`
- `ReplicateActor`
- `SendBunch`

Le terme `ActorChannel` apparait en chaines `.rodata` sans xref vers les fonctions candidates.

## Conclusion

Le nom `SyncPayloadToGameServer` suggere une intention reseau, mais Phase3B ne confirme ni le chemin RPC Unreal standard, ni la native exacte. Le statut technique reste UNKNOWN.
