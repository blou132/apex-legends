# Phase16H-W scope

Date: 2026-08-30

This phase consumed the single authorized runtime attempt on the rooted
PRA-LX1. It used only the committed Phase16H-V disposable tracee and atomic
ARM64 hardware-breakpoint tracer.

Authorized work was limited to one `NT_ARM_HW_BREAK` slot-0 test, register
capture, explicit disable, no-retrap proof, safe detach, process termination,
and cleanup. No software breakpoint, memory patch, injection, Apex operation,
network work, policy change, module, or second attempt occurred.

Raw logs, runtime addresses, maps, binaries, and device identifiers remain in
gitignored local storage. This directory contains cleaned results only.
