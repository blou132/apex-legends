# REPORT PHASE7C RESUME

Date: 2026-08-13. Read-only phone recovery and targeted local PAK scan.

## Final status

```text
STATUS = A / PHONE_CONNECTED_OBB_RECOVERED
PHONE_DETECTED = YES
PACKAGE_APEX_PRESENT = YES
PHONE_OBB_PRESENT = YES
OBB_RECOVERED = YES
PAKS_RECOVERED = 4/4
EVENTSYSTEM_FOUND_IN_RAW = NO
```

## Major result

The preserved phone exposed the original main and patch OBB files through public storage. Both were pulled read-only, matched historical sizes and partial hash witnesses, and yielded all four PAKs with historical sizes. Three historical Lua-path witnesses were confirmed, including `ClientLaunch.lua` at exactly `0x250517`. None of the eleven requested EventSystem/server-list terms appears as an exact ASCII/UTF-8 or UTF-16LE raw sequence.

## Mandatory answers

1. **Is the phone accessible through ADB?** **YES**, one redacted device in state `device`.
2. **Is Apex Mobile still installed?** **YES**, version `1.3.672.546`, code `64003140`.
3. **Are the OBB files still present?** **YES**, main and patch OBB.
4. **Can they be read without additional privilege?** **YES**, through the public OBB directory and `adb pull`.
5. **Do the recovered OBBs exactly match old hashes?** **UNKNOWN for full historical equality**. Sizes match exactly and new full SHA256 values match the known partial prefixes/suffixes; complete old hashes remain unavailable. Status: `SIZE_MATCH_HASH_PARTIAL_ONLY`.
6. **Were all four PAKs recovered?** **YES**, `4/4`.
7. **Do their sizes match?** **YES**, all four match historical sizes exactly.
8. **Were historical Lua witnesses found?** **YES**, all three; `ClientLaunch.lua` matches the historical PAK and offset.
9. **Was `EventSystem.lua` found?** **NO in raw exact-byte scans**.
10. **Was `PostCppEvent` found?** **NO in raw exact-byte scans**.
11. **Was `EVENTID_AVATARSERVERLIST_RETURN` found?** **NO in raw exact-byte scans**.
12. **In which PAK and at which offset?** **UNKNOWN / no target hit**.
13. **Can an entry boundary be identified?** **UNKNOWN / no EventSystem entry boundary is proven**.
14. **Can the subscriber be recovered without bypass?** **UNKNOWN with current evidence**. Raw scans do not expose it, and no bypass was attempted.
15. **What is the next step?** Obtain an authorized readable PAK manifest/index/cache or virtual-file mapping from the same build, correlate the probable module to a concrete entry, and stop if access requires decryption or bypass.

## Safety

The OBB, PAK, extracted assets, targeted-scan working JSON, and ADB output remain under ignored local storage. No serial, raw device log, game binary, proprietary Lua, credential, account/device identifier, or network request is published.
