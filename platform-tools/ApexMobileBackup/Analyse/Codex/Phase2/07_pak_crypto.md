# Phase2 - 07 - PAK crypto indicators

## Portee

Fichiers lus en lecture seule:

- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libUE4.so`
- autres `lib*.so` extraites de l'APK
- `classes*.dex`
- `resources.arsc`

Artefact consulte:

- `ApexMobileBackup\Analyse\Codex\Phase2\_pak_crypto_indicators.json`

Termes recherches:

- `AES`
- `EncryptionKey`
- `PakEncryptionKey`
- `FPakPlatformFile`
- `FPakFile`
- `FAES`
- `Decrypt`
- `RegisterEncryptionKey`
- `EncryptionKeyGuid`
- `PakSigning`
- `PakFile`
- `PakPlatformFile`
- `EncryptedIndex`
- `bEncryptedIndex`

## Resultats

Termes PAK specifiques non identifies de facon utile:

- `EncryptionKey`
- `PakEncryptionKey`
- `FPakPlatformFile`
- `FPakFile`
- `FAES`
- `RegisterEncryptionKey`
- `EncryptionKeyGuid`
- `PakSigning`
- `EncryptedIndex`
- `bEncryptedIndex`

Occurrences `PakFile`:

- Quelques occurrences generiques existent dans `libUE4.so`, dont `bIsLoadingFromPakFiles` autour de `0x210a072` et un contexte `PackageReader.cpp` autour de `0x22333aa`.
- Ces occurrences prouvent seulement des chemins Unreal generiques lies aux PAK/packages.
- Elles ne prouvent ni cle, ni routine de decrypt PAK, ni enregistrement de cle.

Occurrences `AES` et `Decrypt`:

- Nombreuses occurrences dans des contextes TLS/OpenSSL/GCloud, par exemple suites de chiffrement `AES` pour TLS.
- Ces occurrences ne sont pas specifiques aux PAK Unreal.
- Les chaines `Decrypt` observees appartiennent a des contextes crypto/reseau generiques, pas a un callsite PAK confirme.

## Cle candidate

`AES_KEY_CANDIDATE_FOUND = NO` pour une preuve PAK-specifique.

Precision:

- Un scan heuristique large a trouve des valeurs hex/base64-like pres de chaines crypto generiques.
- Ces valeurs ne sont pas reliees a `FPakFile`, `FPakPlatformFile`, `FAES`, `RegisterEncryptionKey`, `EncryptedIndex` ou a une routine de chargement PAK.
- Aucune valeur candidate n'est publiee, conformement a la consigne de redaction.

## Hypotheses possibles

| Hypothese | Statut | Raison |
|---|---|---|
| cle PAK embarquee dans le client | UNKNOWN/NO | aucune preuve PAK-specifique trouvee |
| cle fournie dynamiquement | UNKNOWN | aucune preuve positive dans les logs/statique; absence de cle statique ne suffit pas |
| index PAK chiffre ou illisible | PROBABLE | rapports Phase1 et fenetres Phase2 montrent noms visibles mais contenu/index non exploitable avec les outils actuels |

## Prochain besoin technique

Pour aller plus loin sans modifier les fichiers, il faudrait un outil deja approuve/installe capable de:

- reconstruire les fonctions ARM64 et les xrefs complets (`Ghidra`, `IDA`, `rizin`, `radare2`);
- analyser les structures Unreal PAK avec index, compression et chiffrement (`UnrealPak`, `FModel`, `UE Viewer` ou equivalent).

Aucun de ces outils n'a ete installe pendant cette phase.
