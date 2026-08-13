# REPORT PHASE5

Date: 2026-08-13. Analyse statique Ghidra `-process libUE4.so -noanalysis -readOnly`.

## Reponses obligatoires

1. **Role de FUN_06bc6ca0** - **CONFIRMED**: clone/copy-constructor du delegate callback de `0x30` octets, Ghidra `0x6bc6ca0`, ELF `0x6ac6ca0`; ce n'est pas le handler.
2. **Vtable du callback** - **CONFIRMED**: active `0xa732320` (ELF `0xa632320`), temporaire/base `0xa732390` (ELF `0xa632390`).
3. **Fonction au retour HTTP** - **CONFIRMED**: adaptateur `FUN_06be413c` (ELF `0x6ae413c`) vers handler `FUN_06be3bdc` (ELF `0x6ae3bdc`).
4. **URL du GET** - **UNKNOWN** pour la valeur; **CONFIRMED** comme `FString` fourni par l'appelant Unreal, passe a `request+0x58` a `0x6bc69a0`.
5. **Format de reponse** - **UNKNOWN** pour le wire format; representation callback `FString response_body` **CONFIRMED**.
6. **Champs server-list parses** - **UNKNOWN**; aucun champ n'est parse par le callback.
7. **Emission de l'evenement 0x138** - **CONFIRMED** au site `0x6be3e90..0x6be3e94`.
8. **Fonction emettrice** - **CONFIRMED**: `FUN_06be3f4c` (Ghidra `0x6be3f4c`, ELF `0x6ae3f4c`), appelee par `FUN_06be3bdc`.
9. **Instance Login capturee** - **INVALIDATED pour ce callback**: la capture `+0x08` est utilisee comme contexte Unreal/monde, sans preuve de type `Login`.
10. **Ecriture GameServerBackupIpList +0x150** - **INVALIDATED dans le callback observe**; **UNKNOWN** dans un consommateur aval.
11. **Code writer** - **UNKNOWN**; aucun writer n'est atteint.
12. **TArray<FName> confirme** - **PROBABLE**, inchange depuis la metadata Phase4; aucune manipulation runtime Phase5 ne le confirme.
13. **Contenu des elements** - **UNKNOWN**; ni IP, hostname, nom logique, ID ni region ne sont prouves.
14. **Autre ServerInfo** - **UNKNOWN**; aucune structure serveur n'est construite par le callback.
15. **OpenServerList apres callback** - **UNKNOWN**; aucune liaison n'apparait dans le callgraph cible.
16. **Cible de SyncPayload vtable+0xa58** - **UNKNOWN**; thunk `FUN_0a220f70` confirme, receiver concret non resolu.
17. **Cible reseau/RPC** - **UNKNOWN**; aucun primitive RPC/reseau n'est relie a une cible dynamique prouvee.
18. **Chaine complete** - **CONFIRMED** jusqu'a `GET -> response FString -> event 0x138`; **UNKNOWN** pour `parsed server-list -> stored server data`.
19. **Adresse/host/port game server** - **UNKNOWN**; aucune destination game server n'est obtenue.
20. **Prochaine inconnue bloquante** - **CONFIRMED comme priorite d'analyse**: consommateur Lua/Blueprint/event de `0x138`, ou l'appelant dynamique qui fournit l'URL.

## Chemin majeur

```text
RequestAvatarServerList
  -> FUN_07a31858
  -> FUN_06bc68e8
  -> HTTP GET
  -> callback vtable 0xa732320 +0x58
  -> FUN_06be413c
  -> FUN_06be3bdc
  -> FUN_06be3f4c(event 0x138, success, response FString)
```

Ce chemin satisfait le critere de succes superieur de Phase5. Il montre aussi que le parsing et le stockage ne sont pas dans le callback natif.

## Artefacts

Les six JSON demandes sont sous `output/`. `callback_vtables.json` contient les dumps `-0x20..+0x200`, les fonctions executables, leurs callers/callees, desassemblages et pseudo-code. `request_build.json` contient les 28 `BL`/`BLR` de l'implementation. L'exporteur reproductible est `Phase3/ghidra_scripts/ApexPhase5Export.java`.

## Limites et securite

Aucune analyse globale, requete reseau, authentification, instrumentation, modification du binaire ou contact avec un ancien serveur n'a ete effectue. Les conclusions n'exposent aucun secret ni endpoint invente.
