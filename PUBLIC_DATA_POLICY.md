# Public repository data policy

This repository is public. Every staged file must be reviewed as publishable material before it is pushed.

## Allowed content

- cleaned Markdown analysis reports
- Ghidra and Codex analysis scripts
- sanitized, reasonably sized JSON analysis exports
- methodology and reproducibility notes
- summaries that use `<REDACTED>` for sensitive values

## Local-only content

- APK, OBB, PAK, SO, DEX, XAPK, DLL, EXE, and original game media
- Android-data, Phone-storage, complete backups, and private `/data` content
- extracted original game directories such as `Analyse/APK`, `Analyse/MAIN`, and `Analyse/PATCH`
- raw phone, Ghidra, import, and execution logs
- local Ghidra projects, databases, caches, locks, and compiled Java classes
- bulk temporary exports and backup archives
- environment files, keys, certificates, keystores, tokens, cookies, OAuth values, account identifiers, and device identifiers

These files may remain in the local workspace when needed for analysis. They must stay ignored and must not be staged.

## Security review

Before publishing, scan all tracked candidates for at least:

```text
token access_token refresh_token authorization bearer cookie openid oauth
client_secret password device_id android_id imei serial
```

Occurrences are acceptable only when they are static symbol names, documented search terms, format strings, or explicitly replaced with `<REDACTED>`. Real credential or identifier values must be removed from the public copy.

The security review performed on 2026-08-13 found only redacted placeholders, static binary strings, and analysis terminology. It found no private key, bearer credential, JWT, GitHub token, password value, account identifier, or device identifier in the cleaned publication set.
