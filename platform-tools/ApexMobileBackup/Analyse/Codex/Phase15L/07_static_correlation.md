# Static text correlation

The exact dialog text is not represented by an Android resource ID. Targeted
searches found the title and message fragments in the exact `libUE4.so` input:

- UTF-16 title at Ghidra address `0x0284437e`;
- UTF-16 package-requirement prefix at `0x025a5220` and `0x025c5512`;
- UTF-16 GLES 3.1 support line at `0x026222ae`;
- UTF-16 floating-point target line at `0x026830aa`;
- UTF-16 GLES 2 fallback sentence at `0x02564916` and `0x0256496a`;
- ASCII fallback fragment at `0x022ce8cd` and `0x02352acb`;
- JNI-facing method names `setCaption` at `0x0229e83f` and `addButton` at
  `0x02331e29`.

The existing read-only Ghidra program exposed no direct reference to these
undefined strings and no operand hit on their containing pages. The native text
owner and trigger caller therefore remain `UNKNOWN`; no broad native scan was
performed.

The exact DEX contains `com.epicgames.ue4.MessageBox01`, whose fields and
methods accept dynamic caption, text, and button values matching this native
interface and observed Android hierarchy. This is a probable creator
correlation, not a confirmed native caller edge.
