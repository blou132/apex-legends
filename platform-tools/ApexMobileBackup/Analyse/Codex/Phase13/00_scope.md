# Phase13 scope

Date: 2026-08-13

## Objective

Identify which of the exact base APK's 17 ARM64 libraries exports or statically
registers `GameActivity.nativeResumeMainInit()V`.

## Evidence boundary

- APK: 96,228,800 bytes, SHA256
  `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0`.
- Inputs: only `lib/arm64-v8a/*.so`, the two local DEX files, and previously
  decoded manifest evidence from that APK.
- ELF work: in-memory ZIP reads, authoritative dynamic tables, exact
  null-terminated strings, and loader metadata.
- Ghidra work: the 12 libraries exporting `JNI_OnLoad`, imported locally and
  opened with `-noanalysis -readOnly`; only `JNI_OnLoad` was decompiled.
- No runtime launch, phone access, hook, patch, network change, backend work, or
  authentication work occurred.

The APK, native libraries, DEX, raw output, and Ghidra project remain ignored
under `LocalInputs`. Public outputs contain only compact metadata and findings.

## Stop rule

If no exact export and no target `JNINativeMethod` row is demonstrated across
the 17 libraries, Phase13 stops at static ownership exhaustion. A string alone
is never promoted to a function or owner.
