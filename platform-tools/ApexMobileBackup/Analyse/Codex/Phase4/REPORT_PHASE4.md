# REPORT PHASE4

Date: 2026-08-13. Analyse statique Ghidra `-noanalysis -readOnly`.

## Reponses obligatoires

1. **Types des trois slots** - `0xa9db6c8`: FNameNativePtrPair-like, CONFIRMED; `0xa9e7b70`: metadata de parametres UFunction, PROBABLE; `0xa9ecb10`: nom dans une table function-info/constructeur UFunction, CONFIRMED.
2. **Couple nom -> fonction** - Oui, CONFIRMED: `0xa9db6c8` / `0xa9db6d0`.
3. **Fonction** - thunk `FUN_07a31858` (ELF `0x7931858`), implementation directe `FUN_06bc68e8` (ELF `0x6ac68e8`).
4. **FUN_07a41d0c / FUN_07a41d4c** - constructeurs reflection. Le premier charge `0xa9e7ab0` (`ReplacePawnProxyMat`); le second `0xa9e7b60` (`RequestAvatarServerList`). CONFIRMED.
5. **Callee commun** - `FUN_041b178c`, helper `ConstructUFunction-like`. CONFIRMED par comportement et repetition.
6. **LoginMgr relie a RequestAvatar** - UNKNOWN. La classe `Login` est structurellement proche, mais aucun callgraph commun n'est prouve.
7. **Valeur EVENTID_AVATARSERVERLIST_RETURN** - `0x138` / `312`, CONFIRMED a `0xad25078..80`.
8. **Handler de l'evenement** - UNKNOWN; les immediats `0x138` ne discriminent pas un handler.
9. **Classe de GameServerBackupIpList** - probablement `ULogin` (metadata `Login` a `0xaf66180`), PROBABLE.
10. **Type** - Array CONFIRMED; `TArray<FName>` PROBABLE via flags `0x17`/`0x15`.
11. **Offset** - `0x150`, CONFIRMED par le descripteur `0xaf65f20`.
12. **Writers** - UNKNOWN; les acces generiques `+0x150` ne sont pas relies a une instance `Login`.
13. **SyncPayload a un pointeur fonction** - Oui, CONFIRMED a `0xac489d8`.
14. **Fonction SyncPayload** - `FUN_0a220f70` (ELF `0xa120f70`), thunk virtuel.
15. **RPC/reseau reel** - UNKNOWN; aucune liaison confirmee vers le pipeline RPC UE.
16. **Chaine complete obtenue** - Non. Request et requete HTTP GET sont confirmees; reponse, stockage et connexion restent non relies.
17. **Inconnue bloquante** - callback de reponse de `FUN_06bc68e8`, notamment `FUN_06bc6ca0` et ses vtables.

## Resultats majeurs

- `FUN_07941d10` reste INVALIDATED pour la cible.
- Le faux appariement `RequestAvatarServerList -> FUN_07a41d8c` est evite: ce dernier construit la fonction suivante.
- `FUN_07a31858 -> FUN_06bc68e8` etablit le premier chemin runtime fiable.
- `FUN_06bc68e8` construit une requete asynchrone avec verbe `GET`.
- L'enum `ELuaCppEventType` est un tableau nom/valeur de stride `0x10`.
- `GameServerBackupIpList` est une propriete Array a l'offset `0x150` de la classe generee `Login`.
- `SyncPayloadToGameServer -> FUN_0a220f70` est confirme, mais son dispatch reste virtuel.

## Artefacts

Les sept JSON demandes sont sous `output/`; `requestavatar_runtime_probe.json` ajoute le callee runtime. L'exporteur reproductible est `Phase3/ghidra_scripts/ApexPhase4Export.java`.
