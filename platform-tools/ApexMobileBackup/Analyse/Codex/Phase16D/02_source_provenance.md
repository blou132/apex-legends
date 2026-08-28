# Source provenance

The immutable local archive was obtained from the Android File Host listing.
The temporary signed mirror URL is intentionally not retained in the public
record. Android File Host is an established archive, but the uploader is a
community account and the original filename itself references `androidhost.ru`.

Three additional firmware catalogs independently preserve the same exact
model, build, CUST, and package identifier. This strengthens identity but does
not make the source official.

## Integrity and authorship evidence

- The local archive MD5 exactly matches the MD5 published by Android File Host.
- Both nested update ZIPs pass JAR verification when legacy SHA1 verification
  is enabled for static inspection.
- Their signer subject identifies Huawei AndroidTeam and both use certificate
  SHA256 fingerprint
  `16:77:84:F2:FD:80:97:8E:FC:D1:05:D5:CC:96:FC:E9:67:F7:91:1F:E3:6A:A2:4E:5B:73:EE:5F:75:26:E3:6D`.
- The certificate is self-signed from Java's perspective and has no trusted
  timestamp. The JAR manifest covers updater metadata and scripts, not the
  large `.APP` payload entries.
- All 43 payload records across the main and CUST `.APP` files independently
  pass their internal per-block CRC16 tables.
- The embedded `SHA256RSA` records were preserved but were not validated
  against a separately trusted Huawei public key.

The defensible conclusion is `REPUTABLE_ARCHIVE` provenance with `HIGH`
package identity confidence, not `OFFICIAL` source provenance.
