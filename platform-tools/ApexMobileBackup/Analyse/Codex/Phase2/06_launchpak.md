# Phase2 - 06 - launch.pak

## Portee

Fichier lu en lecture seule:

- `ApexMobileBackup\Analyse\MAIN\AClient\Content\Paks\launch.pak`

Artefact consulte:

- `ApexMobileBackup\Analyse\Codex\Phase2\_launchpak_windows.json`

Taille du fichier:

- `140867491` octets

## Client/Launch/ClientLaunch.lua

Preuves:

- Occurrence du chemin: `Client/Launch/ClientLaunch.lua` a l'offset `0x250517`.
- `ClientLaunch` a `0x250525`.
- `Launch.lua` a `0x25052b`.
- `.lua` a `0x250531`.
- Marqueur de compression proche: `zlib_789c` a `0x2513f8`, delta `+3809`.

Fenetre inspectee:

- Fenetre autour de l'offset: `+/- 8192` octets.
- Entropie 4 KB avant: `7.700`.
- Entropie 4 KB apres: `7.287`.
- Fragments imprimables courts: `276`.
- Aucun contenu Lua lisible proche: pas de `function`, `local`, `require`, `return` relie au chemin dans cette fenetre.

Conclusion:

- Le nom de fichier est present: CONFIRMED.
- Le contenu Lua non compresse n'a pas ete retrouve autour du nom: UNKNOWN/NO.
- Une compression ou un bloc binaire proche est plausible, mais le header d'entree PAK complet n'est pas recupere sans index.

## sgaf_config.json

Occurrence 1:

- Offset: `0x37f731f`.
- Entropie 4 KB avant: `6.856`.
- Entropie 4 KB apres: `7.464`.
- Fragments imprimables courts: `244`.
- Marqueurs proches:
  - `zlib_7801` a `0x37f7f04`, delta `+3045`.
  - `zlib_789c` a `0x37f8099`, delta `+3450`.
- Aucun JSON lisible proche.

Occurrence 2:

- Offset: `0x38067eb`.
- Entropie 4 KB avant: `7.918`.
- Entropie 4 KB apres: `7.538`.
- Fragments imprimables courts: `242`.
- Marqueur proche:
  - `zlib_7801` a `0x38072cd`, delta `+2786`.
- Aucun JSON lisible proche.

## Comptages cibles dans launch.pak

| Terme | Nombre |
|---|---:|
| `ClientLaunch` | `1` |
| `sgaf_config` | `2` |
| `.lua` | `1` |
| `.json` | `2` |
| `function ` | `7` |
| `local ` | `0` |
| `require(` | `0` |
| `require ` | `0` |
| `return ` | `0` |

Les occurrences `function ` sont loin des chemins cibles et ne prouvent pas une copie lisible de `ClientLaunch.lua`.

## Conclusion

`Client/Launch/ClientLaunch.lua` et `sgaf_config.json` sont visibles comme noms/references dans `launch.pak`, mais leurs contenus ne sont pas extractibles depuis les fenetres inspectees sans lire l'index PAK et sans connaitre le contexte compression/chiffrement exact. Aucun octet du PAK n'a ete modifie.
