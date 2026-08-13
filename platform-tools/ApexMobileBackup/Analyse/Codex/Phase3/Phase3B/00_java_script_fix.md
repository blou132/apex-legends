# Phase3B - Correction execution Java Ghidra

Date locale: 2026-08-13

## Statut

CONFIRMED: l'execution de scripts Java Ghidra en headless fonctionne maintenant sur le projet existant `ApexMobileUE4`, programme `libUE4.so`.

## Contraintes respectees

- Pas d'installation PyGhidra.
- Pas de reimport de `libUE4.so`.
- Pas de relance de l'analyse automatique longue.
- Execution avec `-process libUE4.so -noanalysis -readOnly`.
- Aucun contact reseau avec les serveurs Apex.
- Les artefacts de jeu APK/OBB/PAK/SO/DEX n'ont pas ete modifies. Seuls scripts, exports, logs d'analyse et rapports Phase3B ont ete ecrits.

## Probleme initial

Le test minimal echouait avec l'erreur:

```text
Failed to get OSGi bundle containing script: ...\TestPhase3Script.java
```

L'erreur etait visible dans:

- `Phase3\output\phase3b_script_stdout_failed_osgi.txt`
- `Phase3\output\phase3b_script_log_failed_osgi.txt`
- `Phase3\output\phase3b_script_log.txt`

Ghidra 12.1.2 compile les scripts Java locaux dans un contexte OSGi. Un fichier Java invalide dans le repertoire de scripts peut bloquer le chargement du script demande. Le script historique `Phase3TargetExport.java` contenait des incompatibilites API avec Ghidra 12.1.2:

- conversion `ResourceFile` vers `File`;
- `getReferencesFrom(...)` retourne `Reference[]`, pas `ReferenceIterator`;
- `addNoWrap` / `subtractNoWrap` peuvent lever `AddressOverflowException`.

Ces points ont ete corriges dans `Phase3\ghidra_scripts\Phase3TargetExport.java`.

## Test minimal ajoute

Script:

- `Phase3\ghidra_scripts\TestPhase3Script.java`

Sorties attendues:

```text
PHASE3_SCRIPT_OK
libUE4.so
AARCH64:LE:64:v8A
```

Sorties observees:

- `Phase3\output\phase3b_script_stdout.txt`
- `Phase3\output\phase3b_script_log.txt`
- `Phase3\output\phase3b_script_scriptlog.txt`

Le scriptlog contient:

```text
2026-08-13 07:56:05 INFO  TestPhase3Script.java> PHASE3_SCRIPT_OK
2026-08-13 07:56:05 INFO  TestPhase3Script.java> libUE4.so
2026-08-13 07:56:05 INFO  TestPhase3Script.java> AARCH64:LE:64:v8A
```

## Commande validee

Forme de commande utilisee:

```powershell
& 'C:\Tools\ghidra_12.1.2_PUBLIC\support\analyzeHeadless.bat' `
  '<workspace>\ApexMobileBackup\Analyse\Codex\Phase3\ghidra_project' `
  'ApexMobileUE4' `
  -process 'libUE4.so' `
  -noanalysis `
  -readOnly `
  -scriptPath '<workspace>\ApexMobileBackup\Analyse\Codex\Phase3\ghidra_scripts' `
  -postScript 'TestPhase3Script.java'
```

## Export Phase3B

Nouveau script cible:

- `Phase3\ghidra_scripts\ApexPhase3TargetExport.java`

Exports confirmes par `PHASE3B_EXPORT_OK`:

- `Phase3\output\phase3b_export_lot1_scriptlog.txt`
- `Phase3\output\phase3b_export_lot2_scriptlog.txt`
- `Phase3\output\phase3b_export_lot3_scriptlog.txt`

Les 8 JSON finaux sont valides:

- `avatar_event_xrefs.json`
- `gameserver_backup_xrefs.json`
- `loginmgr_callgraph.json`
- `reconnect_callgraph.json`
- `request_avatar_metadata.json`
- `request_avatar_xrefs.json`
- `syncpayload_callgraph.json`
- `uedstoolkit_callgraph.json`
