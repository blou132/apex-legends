# Dolphin error domain

The exact network-unreachable branch constructs `0x0930002a`. Neighboring
failure branches use low-byte reason values and combine them with the constant
prefix `0x09300000`.

Proven structure:

- error-family base: `0x09300000`
- network-unreachable low reason: `0x2a`
- final code: `0x09300000 | 0x2a`

The connected functions do not name a module identifier or publish an explicit
domain mask. Assigning separate semantic meanings to `0x09` and `0x30` would
therefore be inference rather than proof.

DOLPHIN_ERROR_DOMAIN_MASK = UNKNOWN
DOLPHIN_ERROR_MODULE_ID = UNKNOWN
DOLPHIN_ERROR_REASON_ID = 0x2a
