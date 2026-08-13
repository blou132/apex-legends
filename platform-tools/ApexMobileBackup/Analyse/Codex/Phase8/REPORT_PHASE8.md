# REPORT PHASE8

Date: 2026-08-13. Read-only virtual-file mapping and manifest investigation.

## Result

Phase8 resolves important internal behavior but does not produce an exact EventSystem entry:

```text
probable EventSystem logical path
  -> .lua removed by native-to-Lua bridge
  -> unresolved Lua package searcher
  -> file candidate with case-insensitive prefix remap
  -> optional provider or probable Android asset/physical fallback
  -> unknown container and entry
```

The extracted OBB layer has no external manifest or PAK sidecar. The phone public cache has two OBB references but no virtual path list. Ghidra confirms no archive-name hash in the inspected loader path and identifies the fallback vtable/open behavior, but neither the effective provider nor a game mount point.

## Mandatory answers

1. **Does a readable manifest/index/cache exist outside the PAKs?** **CONFIRMED**: no readable entry manifest or index exists in the extracted OBB layer. A 127-byte public cache is readable but contains only two OBB references and timestamps, not entry mappings.
2. **Is there a sidecar associated with the PAKs?** **CONFIRMED no**: zero `sig`, `ucas`, `utoc`, `manifest`, `idx`, `index`, `paklist`, or `bin` sidecars.
3. **Is the concrete virtual backend identified?** **UNKNOWN** for the full virtual provider. The fallback is **PROBABLE** Android asset/physical file access; no PAK provider is identified.
4. **Which object/vtable implements it?** **CONFIRMED** fallback singleton `0xb7cf9b0`, factory `FUN_046355e8`, vtable `0xaf710e0`, OpenRead slot `+0xc0` to `0x49825b8/0x49825bc`. Facade vtable is `0xaf721e8`; optional provider global `0xb697528` has **UNKNOWN** type.
5. **Which mount points are proven?** **UNKNOWN / none**. `../../../%s/`, `/system/etc/`, `AClient/Content/Paks/`, `Client/`, and `Script/` are classified evidence but not proven runtime game mounts.
6. **What exact virtual path is used for ClientLaunch.lua?** **UNKNOWN**. The text `Client/Launch/ClientLaunch.lua` is **CONFIRMED** at raw offset `0x250517`, but not as an index entry or loader argument.
7. **Which path transform is applied?** **CONFIRMED partial**: `.LUA` is removed before Lua dispatch; the file loader performs case-insensitive base-prefix replacement and path cleanup. The Lua package search pattern and final extension are **UNKNOWN**.
8. **Is the mechanism validated by the ClientLaunch witness?** **INVALIDATED** as a validation claim. A body string does not establish the containing entry or lookup key.
9. **Can the probable EventSystem path be transformed with certainty?** **UNKNOWN / no**. The first eight code units and the module-to-file searcher remain unresolved.
10. **Which provider searches EventSystem?** **UNKNOWN**. The loader supports an optional provider and a fallback; the effective branch at EventSystem load time is not known.
11. **Which container holds it?** **UNKNOWN**.
12. **Which entry or ID?** **UNKNOWN**.
13. **What is the entry boundary?** **UNKNOWN**.
14. **Are its bytes accessible without bypass?** **UNKNOWN**. No mapping reaches bytes; no bypass was attempted.
15. **Which Lua content type is used?** **UNKNOWN** for EventSystem. The loader passes returned bytes unchanged after optional UTF-8 BOM removal, so it can accept a buffer but does not classify it here.
16. **Does a public phone cache contain a useful mapping?** **CONFIRMED no** among 94 public files and four safely inspected small candidates.
17. **Could EventSystem be outside the four PAKs?** **PROBABLE yes** architecturally: Android asset/physical paths and an optional provider exist. No alternate EventSystem copy is proven.
18. **Was the RequestAvatarServerList URL source narrowed?** **UNKNOWN / no new correlation**. It remains a caller-supplied native `FString`.
19. **Which decision gate applies?** **CONFIRMED E**: `VIRTUAL_PATH_MAPPING_STILL_UNKNOWN = YES`.
20. **What exact next step is justified?** **CONFIRMED**: obtain an authorized readable filename/index listing for this exact build, validate it first against ClientLaunch, then query EventSystem and stop if the entry remains protected.

## Decision gate

Exactly one gate is selected:

```text
A EVENTSYSTEM_ENTRY_RESOLVED = NO
B EVENTSYSTEM_CONTAINER_RESOLVED_ENTRY_BLOCKED = NO
C EVENTSYSTEM_PROVIDER_RESOLVED_CONTAINER_UNKNOWN = NO
D VIRTUAL_PATH_MAPPING_RESOLVED_BUT_EVENTSYSTEM_NOT_MAPPED = NO
E VIRTUAL_PATH_MAPPING_STILL_UNKNOWN = YES
```

## Reproducibility and safety

`ApexPhase8Export.java` was run against the existing Ghidra project with `-process libUE4.so -noanalysis -readOnly`. Its raw working export, public-cache copies, OBB/PAK files, and extracted assets remain under ignored `Analyse/LocalInputs/`. The public Phase8 tree contains only cleaned reports, hashes, addresses, classifications, and machine-readable summaries.
