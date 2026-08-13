# Phase3 - 00 - Installation

## Java / JDK

Commandes executees:

- `java -version`
- `javac -version`
- `where.exe java`
- `where.exe javac`

Resultat:

- Java: `java version "21.0.10" 2026-01-20 LTS`
- Runtime: `Java(TM) SE Runtime Environment (build 21.0.10+8-LTS-217)`
- VM: `Java HotSpot(TM) 64-Bit Server VM`
- `javac`: `javac 21.0.10`
- `java.exe` trouve dans:
  - `C:\Program Files\Common Files\Oracle\Java\javapath\java.exe`
  - `C:\Program Files\Java\jdk-21.0.10\bin\java.exe`
- `javac.exe` trouve dans:
  - `C:\Program Files\Common Files\Oracle\Java\javapath\javac.exe`
  - `C:\Program Files\Java\jdk-21.0.10\bin\javac.exe`

Decision: JDK 21 x64 deja installe. Aucun JDK supplementaire installe.

## Ghidra

Source officielle utilisee:

- GitHub officiel `NationalSecurityAgency/ghidra`
- Release latest stable detectee: `Ghidra 12.1.2`
- Tag: `Ghidra_12.1.2_build`
- Asset: `ghidra_12.1.2_PUBLIC_20260605.zip`
- URL: `https://github.com/NationalSecurityAgency/ghidra/releases/download/Ghidra_12.1.2_build/ghidra_12.1.2_PUBLIC_20260605.zip`

Telechargement:

- Archive: `C:\Tools\Downloads\ghidra_12.1.2_PUBLIC_20260605.zip`
- Taille: `572803866` octets
- SHA-256 calcule:
  - `B62E81A0390618466C019C60D8C2F796CED2509C4C1AEA4A37644A77272CF99D`
- SHA-256 attendu par la consigne pour 12.1.2:
  - `b62e81a0390618466c019c60d8c2f796ced2509c4c1aea4a37644a77272cf99d`

Resultat: hash conforme. Extraction autorisee.

Extraction:

- Dossier: `C:\Tools\ghidra_12.1.2_PUBLIC`
- `ghidraRun.bat`: present
- `support\analyzeHeadless.bat`: present
- `Ghidra\application.properties`:
  - `application.version=12.1.2`
  - `application.release.name=PUBLIC`

Test:

- `support\analyzeHeadless.bat` affiche l'aide headless.
- Le code de sortie du test d'aide est non-zero parce que la commande sans arguments affiche l'usage, mais le binaire headless demarre correctement.

## Dossiers Phase3

Dossiers crees:

- `ApexMobileBackup\Analyse\Codex\Phase3\`
- `ApexMobileBackup\Analyse\Codex\Phase3\ghidra_project\`
- `ApexMobileBackup\Analyse\Codex\Phase3\ghidra_scripts\`
- `ApexMobileBackup\Analyse\Codex\Phase3\output\`

## Scripts crees

Scripts reproductibles crees sous `Phase3\ghidra_scripts\`:

- `Phase3TargetExport.java`
- `phase3_target_export.py`

Etat:

- Le script Java local est visible par `-scriptPath`, mais Ghidra 12.1.2 refuse de le charger: `Failed to get OSGi bundle containing script`.
- Le script Python local necessite un lancement PyGhidra; `analyzeHeadless.bat` retourne: `Ghidra was not started with PyGhidra. Python is not available`.
- `pyghidraRun.bat -H` demande l'installation interactive de PyGhidra. Cette installation n'a pas ete faite, car la consigne autorisait explicitement Ghidra et le JDK necessaire, pas une installation Python supplementaire.

Scripts Ghidra integres utilises:

- `ExportFunctionInfoScript.java`

Sortie:

- `Phase3\output\ghidra_export_functions.json`

## Contraintes respectees

- Aucun patch du client.
- Aucune modification de `libUE4.so`, APK, OBB, PAK, DEX ou logs originaux.
- Aucun appel vers les anciens serveurs Apex.
- Aucun token/cookie/identifiant utilise.
- Les projets, scripts, sorties et rapports nouveaux sont sous `ApexMobileBackup\Analyse\Codex\Phase3\`, sauf Ghidra installe sous `C:\Tools\` et l'archive officielle sous `C:\Tools\Downloads\`.
