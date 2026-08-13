# Phase3C - SyncPayloadToGameServer rebased

Statut modele d'adresses: CONFIRMED.

## Cibles rebased

| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |
|---|---|---:|---:|---|---|---:|---:|---:|
| SyncPayloadToGameServer string | string | `0x221f64a` | `0x231f64a` | `.rodata` | ascii:SyncPayloadToGameServer | 0 | 6 | 6 |
| SyncPayload metadata 1 | metadata | `0xab489d0` | `0xac489d0` | `.data.rel.ro` | u64:0x231f64a -> .rodata ascii:SyncPayloadToGameServer | 0 | 66 | 1 |
| SyncPayload metadata 2 | metadata | `0xab48ac8` | `0xac48ac8` | `.data.rel.ro` | u64:0x231f64a -> .rodata ascii:SyncPayloadToGameServer | 0 | 55 | 0 |
| SyncPayload metadata 3 | metadata | `0xab48c20` | `0xac48c20` | `.data.rel.ro` | u64:0x231f64a -> .rodata ascii:SyncPayloadToGameServer | 0 | 65 | 0 |
| SyncPayload metadata 4 | metadata | `0xad87bf8` | `0xae87bf8` | `.data.rel.ro` | u64:0x231f64a -> .rodata ascii:SyncPayloadToGameServer | 0 | 55 | 0 |
| SyncPayload metadata 5 | metadata | `0xad87c18` | `0xae87c18` | `.data.rel.ro` | u64:0x231f64a -> .rodata ascii:SyncPayloadToGameServer | 0 | 54 | 0 |
| SyncPayload metadata 6 | metadata | `0xad87c68` | `0xae87c68` | `.data.rel.ro` | u64:0x231f64a -> .rodata ascii:SyncPayloadToGameServer | 0 | 54 | 0 |

## Comparaison anciennes candidates

Les fonctions contenant les adresses rebased `0x7d...` / `0x7fb...` remplacent les candidates `0x7c...` / `0x7eb...` avant toute conclusion RPC.

## Fonctions corrigees

| Fonction | Entry | Body | Callers | Callees | Focus hits |
|---|---:|---|---:|---:|---|
| `FUN_07c14710` | `0x7c14710` | `0x7c14710-0x7c1473f` | 0 | 1 |  |
| `FUN_07c148b0` | `0x7c148b0` | `0x7c148b0-0x7c14927` | 0 | 2 |  |
| `FUN_07c14cac` | `0x7c14cac` | `0x7c14cac-0x7c14e43` | 0 | 2 |  |
| `FUN_07c14e44` | `0x7c14e44` | `0x7c14e44-0x7c14f5f` | 0 | 2 |  |
| `FUN_07eb80dc` | `0x7eb80dc` | `0x7eb80dc-0x7eb8223` | 0 | 1 |  |
| `FUN_07eb8224` | `0x7eb8224` | `0x7eb8224-0x7eb8363` | 0 | 1 |  |
| `FUN_07eb8364` | `0x7eb8364` | `0x7eb8364-0x7eb862b` | 0 | 1 |  |
| `FUN_07d1470c` | `0x7d1470c` | `0x7d1470c-0x7d1474b` | 0 | 1 |  |
| `FUN_07d14900` | `0x7d14900` | `0x7d14900-0x7d1493f` | 0 | 1 |  |
| `FUN_07d14cc8` | `0x7d14cc8` | `0x7d14cc8-0x7d14d83` | 0 | 1 |  |
| `FUN_07d14e08` | `0x7d14e08` | `0x7d14e08-0x7d14e47` | 0 | 1 |  |
| `FUN_07d14e88` | `0x7d14e88` | `0x7d14e88-0x7d14ec7` | 0 | 1 |  |
| `FUN_07fb813c` | `0x7fb813c` | `0x7fb813c-0x7fb817b` | 0 | 1 |  |
| `FUN_07fb81e0` | `0x7fb81e0` | `0x7fb81e0-0x7fb8297` | 0 | 1 |  |
| `FUN_07fb8384` | `0x7fb8384` | `0x7fb8384-0x7fb83c3` | 0 | 1 |  |

## Recherches texte

| Terme | Occurrences | Premieres adresses |
|---|---:|---|
| `SyncPayloadToGameServer` | 1 | 0x231f64a |
| `ProcessEvent` | 0 | - |
| `ProcessRemoteFunction` | 0 | - |
| `CallRemoteFunction` | 0 | - |
| `UNetDriver` | 0 | - |
| `UNetConnection` | 0 | - |
| `UChannel` | 0 | - |
| `ActorChannel` | 6 | 0x226bc13, 0x231d7ff, 0x2351d22, 0x23525e9, 0x2392e96, 0x23c1a75 |
| `SendBunch` | 0 | - |
| `ReplicateActor` | 0 | - |

JSON detaille: `output/syncpayload_rebased.json`.
