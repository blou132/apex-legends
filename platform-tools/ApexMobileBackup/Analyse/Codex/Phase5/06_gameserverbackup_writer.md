# Phase5 - GameServerBackupIpList

## Capture du callback

Le champ callback `+0x08` vient du premier argument objet de `RequestAvatarServerList`. Le handler l'utilise par `vtable+0x158`, puis navigue vers un objet de monde/contexte et un systeme d'evenements. Les metadonnees de la UFunction contiennent `WorldContextObject`.

**INVALIDATED dans ce callback**: rien ne permet d'identifier cette capture comme une instance `Login`; son utilisation est coherente avec un contexte de monde Unreal.

## Acces a +0x150

Le handler `FUN_06be3bdc` ne lit ni n'ecrit `captured_object+0x150`. Il ne parse pas le corps et le transmet directement a l'evenement `0x138`.

- ecriture `Login+0x150` dans ce callback: **INVALIDATED**;
- writer global de `GameServerBackupIpList`: **UNKNOWN**;
- lien event `0x138` vers ce writer: **UNKNOWN**;
- owner `Login` et offset `0x150`: respectivement **PROBABLE** et **CONFIRMED** depuis Phase4.

## TArray<FName>

La Phase5 ne rencontre aucune allocation, reallocation, operation `Num/Max`, construction `FName` ou insertion sur cette propriete. `TArray<FName>` reste donc **PROBABLE** uniquement sur la metadata Phase4; le contenu reel des elements reste **UNKNOWN**. Le nom `IpList` ne prouve pas des IP litterales.

Export: `output/gameserverbackup_writer.json`.
