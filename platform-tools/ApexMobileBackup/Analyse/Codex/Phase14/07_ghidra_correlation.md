# Ghidra correlation

Targeted Ghidra resolution was correctly skipped because Phase14 produced no
validated `libUE4.so` PC and the debuggerd PC model remains unknown. No global
analysis was repeated.

Runtime reachability for the established Phase6-8 boundaries remains:

| Function | Runtime reachability |
| --- | --- |
| `FUN_049a8b54` | `NO_EVIDENCE` |
| `FUN_049a9694` | `NO_EVIDENCE` |
| `FUN_048ab4d0` | `NO_EVIDENCE` |
| `FUN_046355e8` | `NO_EVIDENCE` |
| `FUN_06be427c` | `NO_EVIDENCE` |
| `FUN_06be3f4c` | `NO_EVIDENCE` |

`NO_EVIDENCE` does not mean that a function was not reached.
