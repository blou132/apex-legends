# CPU and memory pressure

The lightweight and ANR snapshots show escalating guest CPU pressure:

| Snapshot | Total | Kernel | SurfaceFlinger | system_server | SystemUI |
| --- | ---: | ---: | ---: | ---: | ---: |
| about +5 s | 65% | 56% | 90% | 56% | 11% |
| about +15 s | 96% | 82% | 70% | 65% | 21% |
| ANR interval | 97% | 83% | 94% | 60% | 11% |

At the ANR, CPU PSI `some avg10` is `79.49`; the four-vCPU guest is under
severe scheduler contention. Kernel CPU dominates, and SurfaceFlinger
RenderEngine is the largest explicitly identified kernel consumer.

Memory is not the gate: memory PSI is zero, `MemAvailable` is approximately
1.47 GiB, only about 43 MiB of 2.18 GiB swap is used globally, SystemUI itself
has zero swap, and no OOM/LMK event is present. I/O PSI exists but is much lower
than CPU pressure and is not linked to the main-thread stack.

No direct watchdog, thermal-throttling, or CPU-frequency-throttling event is
present in the captured interval.

```text
TOTAL_GUEST_CPU_SATURATION = YES
KERNEL_CPU_DOMINANT = YES
TOP_KERNEL_CONSUMER = surfaceflinger/RenderEngine
MEMORY_PRESSURE = NO
SWAP_PRESSURE = NO
OOM_OR_LMK_RELATED = NO
WATCHDOG_EVENT = NO
THERMAL_THROTTLING = NO_DIRECT_EVIDENCE
CPU_FREQ_THROTTLING = NO_DIRECT_EVIDENCE
```
