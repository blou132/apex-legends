# Next step

Phase9D already confirms the earliest application request without contacting its destination. The safe next step is infrastructure, not protocol emulation:

1. provide a genuinely isolated network where the Huawei can reach one local PC address and no Internet route;
2. provide a controlled DNS responder for only `tdm.mgapex.com` on that isolated link;
3. run a local listener on port `8013` and record only connection metadata;
4. if a TLS ClientHello arrives, record port, SNI if present, and observable TLS version, then stop at the certificate boundary;
5. do not install a CA, bypass pinning, patch the APK, or emulate authentication;
6. if no TLS arrives, document transport behavior without inventing a protocol;
7. keep `RequestAvatarServerList` separate unless a later request is directly tied to its confirmed native chain.

No complete fake backend or response schema is justified.
