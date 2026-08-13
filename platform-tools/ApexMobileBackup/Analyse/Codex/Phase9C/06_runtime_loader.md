# Runtime loader

## Result

```text
CLIENTLAUNCH_OBSERVED = NO
LUA_PACKAGE_SEARCHER_OBSERVED = NO
EFFECTIVE_PROVIDER = UNKNOWN
FINAL_OPEN_PATH = UNKNOWN
```

The cleaned first-run log was searched for:

- `Lua`
- `ClientLaunch`
- `EventSystem`
- `PostCppEvent`
- `Script/`
- `OpenRead`
- `Pak`
- `require`
- `module`

No target loader metadata was found. One generic occurrence of `require` had no demonstrated relationship to the game Lua package searcher and is not promoted as evidence.

Because `libUE4.so` load state and process mapping were inaccessible, no runtime base was established and none of the authoritative Phase9 ELF offsets was converted to a runtime address. No hook was attempted.
