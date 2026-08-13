# Phase10 scope

Date: 2026-08-13

## Goal

Determine which observed bootstrap dependency actually prevents the client from progressing beyond the black screen. Chronological order is not treated as proof of a required dependency.

## Inputs

- The unchanged local `base.apk`, its DEX files, and selected native libraries.
- The local-only Phase9C Huawei log.
- Corrected Phase3C and later reports.
- Targeted Ghidra exports produced by `ApexPhase10BootstrapExport.java`.

## Boundaries

No client run, network request, DNS override, certificate, APK patch, runtime hook, authentication, or fabricated response was used. The preserved Samsung was not accessed. APK, DEX, SO, OBB, raw logs, device identifiers, request bodies, and local Ghidra projects remain excluded from Git.

All conclusions use `CONFIRMED`, `PROBABLE`, `UNKNOWN`, or `INVALIDATED`. Ghidra addresses use image base `0x100000`; an ELF virtual address is the listed Ghidra address minus `0x100000`.
