# REPORT PHASE7B

Date: 2026-08-13. Non-destructive local filename search only.

## Result

No historical PAK and no backup OBB was found in the authorized local scopes. Nothing was copied, extracted, hashed, or scanned. This phase records the failed input-restoration prerequisite without changing Phase7 conclusions.

## Mandatory answers

1. **Were all four original PAKs found?** **UNKNOWN / no local candidate found**.
2. **Where?** **UNKNOWN**. No matching file exists in the searched scopes.
3. **Do their sizes match historical observations?** **UNKNOWN** because no candidate size is available.
4. **What are their SHA256 values?** **UNKNOWN**. No candidate was available to hash.
5. **Were they copied into ignored `LocalInputs` storage?** **CONFIRMED no**. No input existed to copy; no `LocalInputs` directory was created.
6. **Were the historical Lua witnesses found again?** **UNKNOWN**. They were not rescanned without PAK inputs.
7. **Does `EventSystem.lua` appear in raw data?** **UNKNOWN**. The raw scan did not run.
8. **Does `PostCppEvent` appear?** **UNKNOWN**. The raw scan did not run.
9. **Does `EVENTID_AVATARSERVERLIST_RETURN` appear?** **UNKNOWN**. The raw scan did not run.
10. **In which PAK?** **UNKNOWN**.
11. **At which offset?** **UNKNOWN**.
12. **Is an entry boundary proven?** **UNKNOWN / no boundary proven**.
13. **Does the entry content appear to be text, bytecode, compressed, encrypted, or unknown?** **UNKNOWN** because no entry or bytes were available.
14. **Can the `0x138` subscriber be recovered without bypass?** **UNKNOWN with current inputs**. No bypass was attempted.
15. **What is the exact next step?** Locate the four original local-only PAKs or their preserved OBB source from the same build, validate size and SHA256, restore ignored working copies, and rerun only the targeted raw scans. Stop if access requires decryption or protection bypass.

## Safety and Git

The existing public-repository policy already ignores `*.pak`, `*.obb`, and all `Analyse` content except the cleaned `Codex` tree. No game binary, original asset, private path, credential, account/device data, or raw proprietary context is included.
