# Selector key references

The exact ARM64 materialization audit found 74 instruction sequences in 25
unique functions. Multiple sequences in one function are the inlined lazy
class initialization and reload paths, not independent semantic references.

Classification by function:

- 23 direct registry lookups through `FUN_04214458`;
- 1 lookup through the bounded generic class wrapper `FUN_045f0d64`;
- 1 lazy class accessor (`FUN_081b1348`), classified `UNKNOWN` for the
  lookup/insert/remove/update taxonomy;
- 0 inserts, removes, or updates of the source registry.

```text
SELECTOR_KEY_REFERENCE_COUNT = 25_UNIQUE_FUNCTIONS
SELECTOR_KEY_MATERIALIZATION_COUNT = 74
LOOKUP_REFERENCES = 24
INSERT_REFERENCES = 0
REMOVE_REFERENCES = 0
UPDATE_REFERENCES = 0
UNKNOWN_REFERENCES = 1_CLASS_ACCESSOR
```
