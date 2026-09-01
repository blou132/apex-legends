# Acquisition and Init order

The readable continuation proves selector checks followed by the existing
interface Init dispatch, but no acquisition write appears in that region. It
is therefore impossible to choose between owner-construction acquisition,
earlier `CheckUpdate` acquisition, or external injection.

```text
ACQUISITION_INIT_ORDER = UNKNOWN
```
