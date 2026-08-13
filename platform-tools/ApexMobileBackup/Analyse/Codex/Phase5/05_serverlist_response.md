# Phase5 - Format de reponse server-list

## Representation observee

**CONFIRMED**: le handler manipule le corps de reponse comme un `FString` et transmet a l'evenement:

```text
bool success
FString response_body
```

La methode `response vtable+0x48` fournit le code HTTP et `response vtable+0x50` copie le contenu dans la structure `FString` locale.

## Format et champs

- format wire JSON/protobuf/binaire: **UNKNOWN**;
- parser JSON, RapidJSON, protobuf ou deserialiseur specifique: **UNKNOWN**, aucun n'est appele par le callback;
- champs `server`, `host`, `port`, `region`, `backup` ou equivalents: **UNKNOWN**, aucun champ n'est lu ici;
- structure native `ServerInfo`: **UNKNOWN**, aucune structure serveur n'est construite dans ce chemin.

La conclusion importante est que le parsing se trouve necessairement chez un consommateur aval de l'evenement `0x138`, probablement dans une couche d'evenements/Lua/Blueprint, ou n'est pas present dans les fonctions natives inspectees.

Export: `output/serverlist_response.json`.
