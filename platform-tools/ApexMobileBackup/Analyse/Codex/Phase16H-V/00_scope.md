# Phase16H-V scope

Date: 2026-08-30

This phase is a PC-only redesign of the disposable ARM64 hardware-breakpoint
test. It does not authorize or perform a new runtime attempt.

## Allowed work

- Reconcile the exact Phase16H-U regset submission with Phase16H-S evidence.
- Resolve kernel ordering and disabled-versus-active control semantics.
- Replace the disabled pre-step with one atomic active slot-0 submission.
- Strengthen logging, disable behavior, and failure cleanup.
- Build and inspect the new sources with the official NDK r27d.

## Excluded work

- No ADB or physical device access.
- No tracee or tracer execution.
- No Apex installation or launch.
- No root, system, boot, kernel, SELinux, or Magisk change.

Any Phase16H-W runtime attempt requires separate explicit authorization.
