# Phase3B - Native registration

## Sources

- `Phase3\Phase3B\output\request_avatar_xrefs.json`
- `Phase3\Phase3B\output\request_avatar_metadata.json`

## Resultat

UNKNOWN: aucune chaine de registration native Unreal n'est confirmee pour `RequestAvatarServerList`.

## Termes recherches

L'export cible a recherche:

- `FNativeFunctionRegistrar`
- `RegisterFunction`
- `NativeFunc`
- `ProcessEvent`
- `UFunction`
- `StaticRegisterNatives`
- `exec`

Resultat dans les exports:

- symboles: 0 pour ces termes;
- occurrences brutes: 0 pour ces termes;
- aucune fonction racine ou body hit rattache a ces termes.

## Metadata candidates

Les trois adresses metadata `0xa8db6c8`, `0xa8e7b70`, `0xa8ecb10` sont dans `.data.rel.ro` et n'ont pas de xref directe. Les fenetres exportees contiennent des pointeurs de fonctions, mais sans label ou signature permettant de les identifier comme:

- `FNativeFunctionRegistrar::RegisterFunction`;
- `UFunction::Func`;
- pointeur `NativeFunc`;
- wrapper `execRequestAvatarServerList`.

## Impact

La Phase3 precedente pouvait seulement poser une hypothese a partir des adresses Phase2. Phase3B ne fournit pas de preuve supplementaire de registration native. La bonne conclusion est donc:

- fonction native de `RequestAvatarServerList`: UNKNOWN;
- pointeur natif associe: UNKNOWN;
- registration `FNativeFunctionRegistrar`: UNKNOWN.
