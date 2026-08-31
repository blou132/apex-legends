# CreateDolphin block

The block containing the factory call is:

```text
0x05a314c8  strb wzr, [x19, #0x1ff]
0x05a314cc  bl   CreateDolphin@plt
```

It starts at `0x05a314c8`, ends at `0x05a314cc`, and has one structural direct
predecessor, block `0x05a314b8`. Its normal-return successor is block
`0x05a314d0`.

The predecessor itself is not entry-reachable in the noreturn-aware graph.
Ghidra reports no reference directly entering `0x05b314c8` or `0x05b314cc`.
Therefore the structural predecessor count is one, while the count of
legitimate entry-reachable predecessors is zero.

```text
CREATEDOLPHIN_BLOCK_START = 0x05a314c8
CREATEDOLPHIN_BLOCK_PREDECESSOR_COUNT = 1_RAW / 0_LEGITIMATE_SEMANTIC
```
