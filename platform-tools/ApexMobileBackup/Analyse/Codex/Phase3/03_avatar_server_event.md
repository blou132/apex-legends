# Phase3 - 03 - EVENTID_AVATARSERVERLIST_RETURN

## Cible

Chaine:

- `EVENTID_AVATARSERVERLIST_RETURN`
- Offset `.rodata`: `0x217dbf5`
- Contexte Phase2: `ELuaCppEventType::EVENTID_AVATARSERVERLIST_RETURN`

## Resultat Ghidra

L'import Ghidra partiel ne fournit pas, via les exports disponibles, de reference directe ou de fonction contenant cette chaine.

Phase2 avait deja etabli:

- aucune relocation directe detectee;
- aucune reference code directe detectee par le scan AArch64 minimal;
- contexte enum-like avec d'autres `ELuaCppEventType`.

Phase3 n'a pas ajoute de handler confirme.

## Valeur numerique de l'enum

Objectif:

- retrouver la valeur numerique de `ELuaCppEventType::EVENTID_AVATARSERVERLIST_RETURN`;
- identifier d'eventuels `switch`/comparaisons.

Resultat:

- Valeur numerique: UNKNOWN.
- Table d'enum: UNKNOWN.
- `switch` handler: UNKNOWN.
- Comparaison de valeur numerique: UNKNOWN.

Raison:

- Le script local prevu pour exporter references directes/indirectes et pseudo-code n'a pas pu etre execute dans Ghidra headless.
- L'export integre `ExportFunctionInfoScript.java` liste les fonctions, mais ne contient pas les references data/code ni les corps de fonctions.

## Lua/C++ bridge

Termes recherches conceptuellement:

- `ELuaCppEventType`
- `LuaCppEvent`
- `DispatchEvent`
- `SendEvent`
- `OnEvent`
- `TriggerEvent`
- `Lua`
- `CppEvent`

Resultat:

- Aucun handler rattache a `EVENTID_AVATARSERVERLIST_RETURN` n'est confirme.
- Le lien `RequestAvatarServerList -> EVENTID_AVATARSERVERLIST_RETURN` reste UNKNOWN.

## Conclusion

Ghidra confirme l'import ARM64/ELF et le projet partiel, mais ne permet pas encore d'identifier la valeur numerique de l'evenement ni son handler. Le statut reste UNKNOWN.
