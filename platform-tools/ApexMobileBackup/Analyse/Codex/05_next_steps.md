# Next steps

Most useful next step: capture a clean launch where DNS/network is working, then collect `logcat`, app logs, and a local packet capture if available. The current logs show `UnknownHost` on `cloudctrl.mgapex.com` and failed DNS for Puffer/version hosts, so they stop before login/server-list/lobby/matchmaking.

Recommended order:

1. Re-run the client in a controlled environment with network/DNS available and preserve logs immediately after launch. Focus on the interval after `cloudctrl.mgapex.com` succeeds.
2. Look for the first successful MSDK/EA auth flow after `EA_LOGIN_URL` and `EA_REDIRECT_URI`. Redact auth codes, tokens, cookies, open IDs, account IDs and device IDs.
3. Search fresh logs for `RequestAvatarServerList`, `EVENTID_AVATARSERVERLIST_RETURN`, `OpenServerList`, `ServerListName`, `GameServerBackupIpList`, `ServerIP`, `ServerPort`, `Gateway`, and `Reconnect`.
4. If legally available, inspect the remote config payload returned by `cloudctrl.mgapex.com/cfgpush/getConfig`, because it may map update/config routing that is not hardcoded.
5. Use an Unreal PAK-aware reader only if approved. Current PAK indexes appear encrypted/unreadable; raw strings are not enough for a complete file list.
6. If doing static native analysis, prioritize `libUE4.so` around the source-path clusters `PureClient/Login/LoginMgr.cpp`, `LoginMgrWrapper.cpp`, `UEDSToolkit/DSControllerComponent.cpp`, and `socket_http.cpp`. The goal is to connect symbols to the transport layer, not to decompile unrelated UE4 code.
7. Inspect `resources.arsc` strings with a proper Android resource decoder if approved; it contains `cloudctrl`, `tdm`, Firebase and EA resource strings.

What is still missing:

- no concrete lobby host
- no concrete matchmaking endpoint
- no concrete game server IP/port
- no populated server list
- no successful auth/login request/response log
- no evidence that a private server is feasible

Do not classify `puffer.*`, `download.*`, `speedtest.*`, TDM, Bugly, Pandora, payment, or GCloudVoice endpoints as game servers without a direct gameplay connection trace.
