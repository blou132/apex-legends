# Phase4 - EVENTID_AVATARSERVERLIST_RETURN

## Table enum

La chaine qualifiee est:

```text
0x227dbe3 (ELF 0x217dbe3)
ELuaCppEventType::EVENTID_AVATARSERVERLIST_RETURN
```

**CONFIRMED** - relocation vers `0xad25078`. Le tableau est constitue de paires de stride `0x10`:

```text
[0x00] const char *qualified_event_name
[0x08] uint64 numeric_value
```

Entree cible:

```text
0xad25078 -> 0x227dbe3
0xad25080 -> 0x138
```

Valeur numerique: **`0x138` / `312` CONFIRMED**.

Temoins: `EVENTID_FORBIDDENZONETIME_ONREP=0x137`, `EVENTID_MSDK_NOTICE_NOTICEINFO=0x14a`, puis les evenements Puffer `0x14c..0x150`.

## Handler

**UNKNOWN**. Le scan ARM64 trouve de nombreux `MOV #0x138`, sans `CMP` unique ni relation avec le slot enum. La constante seule est trop commune pour attribuer un handler.

La prochaine recherche doit partir du callback configure par `FUN_06bc68e8`/`FUN_06bc6ca0`, puis confirmer l'emission de `0x138` dans son callgraph.

Export: `output/avatar_event_table.json`.
