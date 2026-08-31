# Immediate predecessor calls

Within the bounded precursor window, the calls relevant to the raw target
region are:

| Site | Actual target | Bounded role |
|---:|---|---|
| `0x05a313d4` | `FUN_0417a4ac` | String/buffer sizing or setup helper |
| `0x05a31420` | `FUN_0417a69c` | String/buffer copy or construction helper |
| `0x05a3144c` | `std::__throw_length_error(char const*)` | Noreturn reachability barrier |
| `0x05a31470` | optional `BLR` | Temporary release through manager slot `+0x28` |
| `0x05a314b4` | `FUN_0450f738` | Optional level-5 diagnostic log |
| `0x05a314c4` | `gettimeofday` | Actual imported symbol; register shape is adjacent-`strncpy`-like |

Only the first three rows are on an entry-reachable path through the first
throw. Calls after `0x05a3144c` are raw post-noreturn fallthrough.

```text
CREATEDOLPHIN_IMMEDIATE_PREDECESSOR_CALLS = STRING_BUFFER_HELPERS; THROW_LENGTH_ERROR_BARRIER; OPTIONAL_RELEASE_BLR; LOG_HELPER; GETTIMEOFDAY
```
