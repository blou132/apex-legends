# Thread-name inventory

The permitted non-privileged `ps -T` inventory succeeded at approximately
`+5 s` and `+120 s`. Android returned 66 and 76 Apex thread rows respectively,
excluding the header.

Every visible `NAME` field was collapsed to the package process name. No
individual `GameThread`, `RenderThread`, `RHIThread`, `TaskGraph`, `Audio`,
`UE4`, or `Lua` name was exposed. The command was not denied, but its output
cannot support thread-role attribution. No stack, register, map, or memory data
was read.

```text
THREAD_INVENTORY = AVAILABLE_BUT_THREAD_NAMES_COLLAPSED
UE4_GAME_THREAD_NAME_VISIBLE = NO
UE4_RENDER_THREAD_NAME_VISIBLE = NO
OTHER_RELEVANT_THREAD_NAMES = NONE_VISIBLE
THREAD_FUNCTION_EXECUTION_PROVEN = NO
```
