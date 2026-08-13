# Endpoints and network indicators

Scope: static strings, APK assets/resources, selected native libraries, DEX strings, four PAK files, OBB exact-string checks, and phone logs. Values that look like account IDs, device IDs, auth codes, SDK keys, client IDs, API keys, sessions or tokens are redacted or omitted.

## Confirmed endpoints

| Indicator | Evidence | Likely role | Notes |
| --- | --- | --- | --- |
| `https://cloudctrl.mgapex.com/cfgpush/getConfig?...` | `Android-data/cache/GCloudSDKLog/GCloudCore/GCloudCore_2026081217.log:12`; base URL at lines 14, 75, 93, 111 | client config/update | Runtime log shows `RemoteConfig` request for module `MSDK`. Query contains `did` and `oid`, both treated as sensitive and redacted. Requests failed with `UnknownHost`. |
| `https://cloudctrl.mgapex.com` | `Analyse/APK/resources.arsc@0x7ad5`; GCloudCore logs | client config/update | Also embedded in compiled resources near the TDM route. |
| `https://tdm.mgapex.com:8013/tdm/v1/route` | `Analyse/APK/resources.arsc@0x7af4` | telemetry / route service | Confirmed resource string. Not evidence for game server traffic. |
| `https://itop.mgapex.com` | `Analyse/APK/assets/MSDKConfig.ini:11` | MSDK prod/base endpoint | Main config only. Dev/test use `msdk-test.intlgame.com`. |
| `https://msdk-test.intlgame.com` | `MSDKConfig_Dev.ini:11`, `MSDKConfig_Test.ini:11` | MSDK dev/test endpoint | Environment-specific. |
| `https://cotest.msdkpass.com` | `MSDKConfig.ini:48`, `MSDKConfig_Dev.ini:48`, `MSDKConfig_Test.ini:48` | MSDK account service | The adjacent comment also mentions formal `https://us.msdkpass.com`; the stored value is `cotest` in all three files. SDK key/app id values exist but are redacted. |
| `https://accounts.ea.com/connect/auth?...client_id=<REDACTED>...` | `MSDKConfig.ini:63` | EA auth/login | Prod EA OAuth-style login URL. |
| `https://accounts.int.ea.com/connect/auth?...client_id=<REDACTED>...` | `MSDKConfig_Dev.ini:63`, `MSDKConfig_Test.ini:63`; short resource string in `resources.arsc@0x7a88` | EA auth/login dev/test | Dev/test equivalent. |
| `https://eagw1.mgapex.com/web_auth_redirect` | `MSDKConfig.ini:64`; `Android-data/.../MSDK_2026081217.xlog:1-3` | EA auth redirect | Runtime xlog contains the redirect URI in mmap text. |
| `https://test.apgame.qq.com:7000/apgame_account_auth` | `MSDKConfig_Dev.ini:64`, `MSDKConfig_Test.ini:64` | EA/auth redirect dev/test | Host:port confirmed. |
| `puffer.4.707369824.dmp.mgapex.com` | `GCloud_2026081217.log:25,47,62-67` | GCloud Puffer resource/update service | Runtime DNS attempt failed: `getaddrinfo(...), port:0 failed`. Not a game server. |
| `download.2.707369824.dmp.mgapex.com` | `GCloud_2026081217.log:527,568,588-596` | GCloud/Dolphin version or update service | Runtime version-check URL. DNS failed later (`getaddrinfo failed`). |
| `speedtest.mgapex.com`, port `17000` | `GPMSDK_2026081217.log:98,105` | GPMSDK performance/telemetry | Appears in report config as `sip=speedtest.mgapex.com, sport=17000`. |
| `https://bugly-android.mgapex.com/rqd/pb/async` | `MSDKConfig.ini:114` | crash/bug report | Prod Android Bugly endpoint. |
| `https://bugly-ios.mgapex.com/rqd/pb/sync` | `MSDKConfig.ini:115` | crash/bug report | iOS config present in Android asset. |
| `https://astat.bugly.qcloud.com/rqd/pb/async` | `MSDKConfig_Dev.ini:113`, `MSDKConfig_Test.ini:113` | crash/bug report dev/test | Dev/test Android Bugly endpoint. |
| `https://ios.bugly.qcloud.com/rqd/pb/sync` | `MSDKConfig_Dev.ini:114`, `MSDKConfig_Test.ini:114` | crash/bug report dev/test | Dev/test iOS Bugly endpoint. |
| `pandora.mgapex.com` | `libUE4-strings.txt:11373`; `libUE4.so@0x213c7d1` | Pandora SDK/service | Nearby strings include `PandoraCallGame`, `PandoraLogoutSignal`, socket handling. |
| `https://pdrlog.game.qq.com/?c=PandoraSDKLogUpload&a=batch` | `libUE4-strings.txt:29434`; `libUE4.so@0x21c21e2` | Pandora log upload | Telemetry/logging, not gameplay. |
| `ire.csoversea.mgapex.com` | `Analyse/APK/lib/arm64-v8a/libanort.so@0x228250` | security/reporting, likely anti-cheat related | Appears near `dl.listdl.com` and `libanogs.so`. Keep role as probable/unknown. |
| `pay.mgapex.com` | `centauri_oversea_singapore/centauri_oversea_cp.cfg:6-8` | payment | Centauri Singapore release domain/reportdomain/iplist. |
| `na1.centauriglobal.com` | `centauri_oversea_na/centauri_oversea_cp.cfg:6-8` | payment | NA Centauri release config. |
| `sandbox.centauriglobal.com` | `centauri_oversea_*/*.cfg` | payment test/sandbox | Test mode in Centauri configs. |
| `dev.api.unipay.qq.com` | `centauri_oversea_local/centauri_oversea_cp.cfg:21` | payment dev | Local dev mode. |
| `szmg.qq.com` | `centauri_oversea_local/centauri_oversea_cp.cfg:7,23` | payment report | Local release/dev reportdomain. |
| `https://apgame-client-1258344700.cos.ap-shanghai.myqcloud.com/ConfigHotFix` | `libUE4-strings.txt:62029`; `libUE4.so@0x22b5540` | config hotfix / TGPA report area | Nearby source path is `PureClient/Report/TGPAReport.cpp`. |
| `https://apgame-test-1258344700.cos.ap-shanghai.myqcloud.com/app/` | `MSDKConfig*.ini:74` | WeChat universal link test asset | Present in prod/dev/test configs. |
| `https://cdn-msdk-twitter.mgapex.com/v5/eu/jssdk/twitterlogin.html` | `MSDKConfig*.ini` | Twitter login webview | Same in prod/dev/test. |
| `https://apex-15483282.firebaseio.com` | `resources.arsc@0x7ace` | Firebase/unknown | Embedded resource string; no runtime log evidence found. |

## Voice endpoints

`Analyse/APK/assets/GCloudVoice/config.json:2464-2470` contains:

| Endpoint | Role |
| --- | --- |
| `https://idcconfig.gcloudsdk.com:443` | voice remote config |
| `https://tglog.gcloudsdk.com:443` | voice log reporting |
| `https://harmony.voice.gcloudsdk.com:443` | voice report |
| `https://api.translator.voice.gcloudsdk.com:443` | voice translation |
| `udp://cn.voice.gcloudcs.com:10001` | voice UDP |
| `udp://cdn.cn.gcloudcs.com:18301` | voice/file quality or CDN UDP |
| `udp://qosidc.gcloudsdk.com:8011` | QoS report |

The requested `voice-sg.mgapex.com` was not found.

## IPs and ports

| Indicator | Evidence | Interpretation |
| --- | --- | --- |
| `101.32.133.41` | Centauri test configs | Payment sandbox/test IP. |
| `183.61.41.148` | `centauri_oversea_local` release/dev configs | Payment local release/dev IP. |
| `198.18.53.53` | `GPMSDK_2026081217.log:135` | Reported as `ldns`; likely local/resolver/test network, not backend. |
| `1.1.1.1:8080`, `1.1.1.1:8081` | `GPMSDK_2026081217.log:98,105` | GPMSDK speed list values. Not game server proof. |
| `test.apgame.qq.com:7000` | MSDK dev/test configs | Auth redirect dev/test. |
| `tdm.mgapex.com:8013` | `resources.arsc` | TDM route service. |
| `8085`, `8080` | `GCloud_2026081217.log:4` as `RpcAddressSvrPortList[8085|8080]` | GCloud RPC address-service ports, no concrete host in that line. |
| `0.0.0.0`, `127.0.0.1:1000`, `10.0.0.1`, `10.0.0.108` | DEX/native string scans | Placeholder/private/local strings. Not backend evidence. |

## Requested domains not confirmed

No direct occurrence was found for `lobby.mgapex.com`, `file.mgapex.com`, `voice-sg.mgapex.com`, or `tgpa.mgapex.com` in the scanned APK assets/resources, logs, `libUE4` strings/native targets, PAK exact searches, or OBB exact searches.

`tdm.mgapex.com` is confirmed only as `https://tdm.mgapex.com:8013/tdm/v1/route`. `cloudctrl.mgapex.com`, `itop.mgapex.com`, `pay.mgapex.com`, `bugly-android.mgapex.com`, `pandora.mgapex.com`, `eagw1.mgapex.com`, `msdkpass.com`, `apgame.qq.com`, `myqcloud.com`, and `game.qq.com` are confirmed as listed above.
