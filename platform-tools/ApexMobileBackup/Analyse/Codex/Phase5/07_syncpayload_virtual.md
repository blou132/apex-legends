# Phase5 - SyncPayloadToGameServer

## Thunk confirme

```text
SyncPayloadToGameServer
  -> FUN_0a220f70 (Ghidra 0xa220f70, ELF 0xa120f70)
  -> receiver vtable+0xa58
```

Le thunk avance le curseur du frame Unreal a `param_2+0x20`, charge le vptr du receiver dans `x8`, charge l'entree `+0xa58` dans `x1`, puis effectue `BR x1` a `0xa220f88`.

## Resolution

- classe concrete du receiver: **UNKNOWN**;
- vtable concrete atteignant `+0xa58`: **UNKNOWN**;
- cible native unique: **UNKNOWN**;
- nature RPC/reseau: **UNKNOWN**.

La table de registration confirme le nom et le thunk, mais le receiver arrive dynamiquement comme `this` Unreal. Aucun constructeur ou vptr unique n'est relie a ce thunk par les references existantes. Attribuer une cible a partir du nom seul serait incorrect.

Export: `output/syncpayload_virtual.json`.
