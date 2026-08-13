# Phase3 - Rapport de synthese

## Resume technique

Ghidra 12.1.2 a ete installe depuis la release officielle `NationalSecurityAgency/ghidra`, avec SHA-256 conforme:

- `B62E81A0390618466C019C60D8C2F796CED2509C4C1AEA4A37644A77272CF99D`

Java/JDK:

- `java version "21.0.10" 2026-01-20 LTS`
- `javac 21.0.10`

Import:

- Loader: ELF
- Langage: `AARCH64:LE:64:v8A:default`
- ELF: `ELF64`, `ET_DYN`, AArch64, little-endian
- Entry point: `0x395a3e0`
- LOAD min vaddr: `0x0`
- Projet: `Phase3\ghidra_project\ApexMobileUE4`
- Programme: `/libUE4.so`

Analyse:

- Temps Ghidra: `7199 secs`
- Timeout: `7200 secs`
- Resultat: import sauvegarde et reussi, analyse automatique incomplete
- Fonctions exportees par script integre: `467079`

Limite majeure:

- Les scripts locaux de xrefs/decompilation sous `Phase3\ghidra_scripts` n'ont pas pu etre executes par `analyzeHeadless.bat`.
- Java: `Failed to get OSGi bundle containing script`.
- Python: `Ghidra was not started with PyGhidra. Python is not available`.
- PyGhidra demanderait une installation Python interactive, non effectuee.

## 14 questions

### 1. Quelle fonction native correspond a RequestAvatarServerList ?

UNKNOWN.

Preuves:

- Chaine: `0x21c409f`.
- Metadata: `0xa8db6c8`, `0xa8e7b70`, `0xa8ecb10`.
- References code candidates Phase2: `0x7941d2c`, `0x7941d6c`.
- Ghidra export fonctions: les deux adresses tombent lineairement dans l'intervalle `FUN_07941d10` -> `FUN_07941e84`.

Conclusion: `FUN_07941d10` est une fonction candidate de metadata/reflection, pas la native C++ confirmee.

### 2. Quel code appelle cette fonction ?

UNKNOWN.

Preuve:

- Aucun export callers/callees n'a ete obtenu pour `FUN_07941d10`.
- Aucun pointeur `NativeFunc`/`FNativeFuncPtr` associe n'a ete confirme.

### 3. Quelle valeur numerique correspond a EVENTID_AVATARSERVERLIST_RETURN ?

UNKNOWN.

Preuve:

- Chaine `.rodata`: `0x217dbf5`.
- Aucun export Ghidra de table enum, switch ou comparaison numerique.

### 4. Quel handler traite cet evenement ?

UNKNOWN.

Preuve:

- Aucune xref directe Phase2.
- Aucun handler Ghidra exporte Phase3.

### 5. LoginMgr appelle-t-il reellement RequestAvatarServerList ?

UNKNOWN.

Preuve:

- `PureClient/Login/LoginMgr.cpp`: `0x21cab3d`.
- `ULoginMgrWrapper`: `0x221434f`.
- `LoginMgrWrapper.cpp`: `0x22d375d`.
- Aucune xref `LoginMgr -> RequestAvatarServerList` exportee.

### 6. Quelle classe possede GameServerBackupIpList ?

UNKNOWN.

Preuve:

- `GameServerBackupIpList`: `0x2180ba5`.
- Metadata: `0xae65f20`, `0xae65f70`.
- Contexte: `AuthFailed...`, `ChannelList`.
- Classe proprietaire non exportee.

### 7. Quel code remplit GameServerBackupIpList ?

UNKNOWN.

Preuve:

- Aucun site d'ecriture/remplissage Ghidra obtenu.
- Aucun lien vers `inet_addr`, `inet_pton`, `getaddrinfo`, `sockaddr` confirme.

### 8. Quel est son type exact ?

UNKNOWN.

Preuve:

- Metadata/propriete confirmee, mais aucun type `FString`, `TArray<FString>`, map ou autre n'a ete exporte.

### 9. SyncPayloadToGameServer est-il un RPC Unreal ?

PROBABLE.

Preuves:

- Chaine: `0x221f64a`.
- Metadata multiples: `0xab489d0`, `0xab48ac8`, `0xab48c20`, `0xad87bf8`, `0xad87c18`, `0xad87c68`.
- Champs proches Phase2: `TeamId`, `ReturnValue`, `LevelCount`, `bSyncEarlyLoadLevel`, `UnitLevelSize`, `LevelOriginLocation`.

Limite: RPC Unreal exact non confirme par pseudo-code.

### 10. Quelle fonction native lui correspond ?

UNKNOWN.

Preuves:

- Fonctions candidates autour des references code:
  - `FUN_07c14710`
  - `FUN_07c148b0`
  - `FUN_07c14cac`
  - `FUN_07c14e44`
  - `FUN_07eb80dc`
  - `FUN_07eb8224`
  - `FUN_07eb8364`
- Ces fonctions sont candidates par intervalle d'entrees, pas des natives confirmees.

### 11. Quel transport UEDSToolkit utilise-t-il ?

UNKNOWN.

Preuves:

- `/Script/UEDSToolkit`: `0x226211a`.
- `DSControllerComponent.cpp`: `0x21c0147`.
- `socket_http.cpp`: `0x2235be7`.
- `RegisterDSControllerComponent`: `0x2120940`.
- Aucun lien exporte vers `socket`, `connect`, `getaddrinfo`, `send`, `recv`, `curl_easy_*` ou HTTP.

### 12. Le code de reconnexion revele-t-il IP/port ou une structure serveur ?

UNKNOWN.

Preuves:

- `ReconnectSyncData` candidates: `FUN_079c3420`.
- `OnServerAboutToReconnect` candidates: `FUN_07e9ffe4`, `FUN_07ea03fc`, `FUN_07ea059c`, `FUN_07ea05dc`, `FUN_07ea0694`.
- `OnPreReconnectOnServer` candidate: `FUN_079c90bc`.
- `ClientNotifyReconnectedSuccessfully` candidate: `FUN_07a1a7c8`.
- Aucun champ IP/port ou destination reconnect exporte.

### 13. A-t-on enfin identifie un chemin server list -> IP/port -> connexion game server ?

UNKNOWN.

Preuve:

- Server-list metadata confirmee.
- `GameServerBackupIpList` metadata confirmee.
- Fonctions candidates Ghidra proches de metadata confirmees.
- Aucun lien runtime complet, aucune structure IP/port et aucune connexion socket rattachee.

### 14. Quel est le prochain obstacle ?

CONFIRMED.

Obstacle:

- Obtenir un export Ghidra cible avec xrefs, ranges de fonctions, callers/callees et decompilation.

Options possibles:

- installer/configurer PyGhidra avec accord explicite;
- transformer le script Java Phase3 en extension Ghidra chargeable sans copier de script hors `Phase3`;
- ouvrir le projet Ghidra en GUI et lancer les scripts depuis un environnement ou le provider Java accepte les scripts locaux;
- relancer une analyse plus ciblee/desactivee des analyseurs couteux pour eviter le timeout global.

## Conclusion

La Phase3 a installe Ghidra officiellement, importe `libUE4.so`, produit un projet Ghidra sauvegarde et exporte `467079` fonctions. Elle ajoute des candidats `FUN_...` autour des adresses code Phase2, mais ne resout pas encore les questions critiques de native function pointer, handler d'evenement, type/remplissage `GameServerBackupIpList`, transport UEDSToolkit ou chemin game server IP/port.
