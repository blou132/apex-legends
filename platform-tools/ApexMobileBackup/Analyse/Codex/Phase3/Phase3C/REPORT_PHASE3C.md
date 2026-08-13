# Phase3C - Rapport final

Date locale: 2026-08-13

Projet: `Phase3\ghidra_project\ApexMobileUE4`, programme `libUE4.so`, mode `-process libUE4.so -noanalysis -readOnly`.

## Reponse courte

CONFIRMED: le modele `GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000` est confirme par les controles `.rodata`, `.text` et `.data.rel.ro`.

INVALIDATED: les conclusions Phase3B qui utilisaient directement des VA Phase2 comme adresses Ghidra doivent etre rejetees, notamment pour les anciennes fonctions `FUN_07941d10` et candidates `0x7c...` / `0x7eb...`.

UNKNOWN: les chemins reseau et RPC restent non demontres si les liens `STRING -> RELOCATION/RAW_POINTER -> METADATA -> CODE` ne sont pas presents dans les sorties corrigees.

## Questions finales

1. CONFIRMED - `+0x100000` est la transformation ELF VA -> Ghidra VA dans ce projet.
2. INVALIDATED - toute conclusion basee sur une ancienne adresse Phase2 non rebased; voir `09_phase3b_invalidations.md`.
3. INVALIDATED - `FUN_07941d10` n'est valide que si elle correspond a la fonction contenant `0x7a41d2c`/`0x7a41d6c`.
4. PROBABLE - fonctions contenant les code-xrefs corrigees: FUN_07a41d0c @ 0x7a41d0c ; FUN_07a41d4c @ 0x7a41d4c.
5. PROBABLE - references brutes/relocations vers la chaine presentes; verifier le JSON pour relier aux metadata.
6. UNKNOWN - aucune native `RequestAvatarServerList` n'est confirmee sans slot NativeFunc relie.
7. UNKNOWN - `LoginMgr -> RequestAvatarServerList` doit venir du callgraph corrige; aucun lien ne doit etre repris de Phase3B sans preuve.
8. UNKNOWN - la valeur/handler `EVENTID_AVATARSERVERLIST_RETURN` reste a confirmer par table enum ou references brutes.
9. UNKNOWN - classe/type/writer `GameServerBackupIpList` non confirme tant qu'une metadata rebased n'est pas reliee a la chaine.
10. UNKNOWN - `SyncPayloadToGameServer` n'est pas classe RPC sans chemin vers `ProcessRemoteFunction`/`UNet*`/`SendBunch`.
11. UNKNOWN - native `SyncPayloadToGameServer` non confirmee sans registration rebased.
12. UNKNOWN - reconnect ne revele pas Host/IP/Port sans lien corrige vers `Host`, `Port`, `RemoteAddr`, `FURL` ou socket.
13. UNKNOWN - transport UEDSToolkit non confirme sans callgraph vers `socket/connect/send/recv/getaddrinfo`.
14. UNKNOWN - aucun chemin complet server list -> adresse -> game server n'est etabli par defaut.
15. UNKNOWN - restent a prouver: native exacte, handlers d'evenements, writers de metadata, valeurs enum et transport effectif.

## Fichiers produits

- `00_address_model.md`
- `01_requestavatar_rebased.md`
- `02_native_registration_rebased.md`
- `03_avatar_event_rebased.md`
- `04_loginmgr_rebased.md`
- `05_gameserverbackup_rebased.md`
- `06_syncpayload_rebased.md`
- `07_reconnect_rebased.md`
- `08_uedstoolkit_rebased.md`
- `09_phase3b_invalidations.md`
- JSON dans `output\`
