# Phase2 - 05 - tdm.mgapex.com route

## Portee

Fichiers lus en lecture seule:

- `ApexMobileBackup\Analyse\APK\resources.arsc`
- `ApexMobileBackup\Analyse\APK\classes.dex`
- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libTDataMaster.so`
- autres `lib*.so` et assets pour recherche ciblee

Artefacts consultes:

- `ApexMobileBackup\Analyse\Codex\Phase2\_resources_tdm.json`
- `ApexMobileBackup\Analyse\Codex\Phase2\_dex_resource_xrefs.json`
- `ApexMobileBackup\Analyse\Codex\Phase2\_dex_static_resource_fields.json`
- `ApexMobileBackup\Analyse\Codex\Phase2\_libTDataMaster_tdm_context.json`

## Ressource Android

Ressource confirmee dans `resources.arsc`:

- Valeur: `https://tdm.mgapex.com:8013/tdm/v1/route`
- Offset brut chaine: `0x7af2`
- Pool global string index: `351`
- Package: `0x7f`, `com.ea.gp.apexlegendsmobilefps`
- Resource ID: `0x7f0e00b6`
- Type: `string`
- Nom: `router_address_formal`
- Offset entree: `0x7830c`
- Valeur simple: `TYPE_STRING`, data `351`

## References DEX

Champs statiques `R$string`:

- `classes.dex`: `Lcom/ea/gp/apexlegendsmobilefps/R$string;->router_address_formal:I = 0x7f0e00b6`, `static_values_off 0x3a5db8`, `value_offset 0x3a60e9`.
- `classes.dex`: `Lcom/gcore/tmgp/gcloudcore/R$string;->router_address_formal:I = 0x7f0e00b6`, `static_values_off 0x3ac4bf`, `value_offset 0x3ac4d4`.

Contexte methodes/chaines:

- Methode: `Lcom/tdatamaster/tdm/system/TDMUtils;->SaveConfigInfo(Landroid/content/Context;)V`
- `code_off`: `0x85398c`
- Chaines referencees:
  - `GCloud.TDM.AppId`
  - `GCloud.TDM.AppKey`
  - `GCloud.TDM.AppChannel`
  - `GCloud.TDM.Test`
  - `GCloud.TDM.EnableProxy`
  - `GCloud.TDM.Abroad`
  - `GCloud.TDM.LogLevel`
  - `GCloud.TDM.TGEMIT_ROUTER_ADDRESS_FORMAL`
  - `GCloud.TDM.TGEMIT_ROUTER_ADDRESS_TEST`
  - `routerAddressFormal = `
  - `routerAddressTest = `
  - `GCloud.TDM.BeaconAppId`
  - `GCloud.TDM.DisableDeviceInfo`
  - `GCloud.TDM.EncryptDeviceInfo`

Limite:

- L'ID brut `0x7f0e00b6` n'a ete trouve que dans les valeurs statiques `R$string`.
- Le parseur DEX minimal n'a pas identifie d'instruction methode lisant directement `R.string.router_address_formal`.
- Le lien entre `SaveConfigInfo` et la ressource est donc indirect/probable via les cles de configuration TDM, pas un appel direct prouve a l'ID ressource.

## libTDataMaster.so

Symboles/exports visibles:

- `Java_com_tdatamaster_tdm_TDataMaster_TDMInit`
- `TDMReportEvent`
- `TDMRealTimeReportEvent`
- `TDMReportLogin`
- `TDMReportBinary`
- `tdm_set_router_address`
- `tdm_initialize`
- `tdm_report_event`
- `tdm_real_time_report_kv_event`
- `tdm_real_time_report_bin_event`
- `tdm_upload_file`

Contexte HTTP:

- `Project/TDM/Source/HTTP/TDMHTTPManager.cpp`
- `TDMHTTPClient.cpp`
- `TDMHTTPManager is inited`
- `report real time %s`
- `VerifyParams`
- `http params error`
- `SendSynRequest`
- `SendReadRequest`
- `Content-Type`
- `application/json`
- `multipart/form-data; boundary=...TDMBoundary`
- `POST`
- `GET`

## Role du service

Elements confirmes:

- `tdm.mgapex.com:8013/tdm/v1/route` est une ressource Android nommee `router_address_formal`.
- Le namespace Java est `com.tdatamaster.tdm`.
- La librairie native expose des APIs de reporting: `TDMReportEvent`, `TDMReportLogin`, `TDMReportBinary`, `tdm_report_event`, `tdm_upload_file`.
- Les chaines HTTP montrent support JSON et multipart.

Interpretation prudente:

- Fonction reelle probable: routeur/configuration de reporting TDataMaster, pas mode Team Deathmatch.
- Methode HTTP probable: `POST` pour reporting JSON/multipart; `GET` est aussi present dans le client HTTP.
- Parametres envoyes exacts et schema de reponse: UNKNOWN.

## Conclusion

`/tdm/v1/route` appartient au chemin TDataMaster/GCloud TDM de telemetrie/reporting. Le lien ressource -> configuration Java/native est fortement probable, mais le callsite exact et le format de reponse ne sont pas reconstructibles avec les xrefs statiques disponibles.
