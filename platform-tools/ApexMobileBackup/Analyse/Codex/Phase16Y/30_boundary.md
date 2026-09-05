# Boundary

The stop conditions for the requested owner-reflection analysis were reached:

- no GameUpdateMgr class/property parameter block was recovered;
- no usable reflected property offsets were exposed;
- `+0x38` reflection status could not be established;
- native-registration and constructor targets were not coherently decodable;
- no exact reflected setter or owner assignment path was proven.

## Execution deviation

Review of the local helper code and outputs found that the earlier execution
was broader than authorized. ApexPhase16YExactAnchors included the common
PureClient package literal in its instruction scan and exported/decompiled
matching functions outside the two target classes. This also re-collected
known GameUpdateMgr lookup consumers. ApexPhase16YMetadataProbe enumerated
references to the shared registration helper and decompiled that helper.
It also invoked the decompiler on the generated constructor/registration
targets despite their opaque or absent instruction bodies.

These operations were static and read-only, but exceeded the requested
class-only and callback-classification limits. No further expansion was
performed after this review. Surplus function exports, shared-helper
decompilation, and bad-data callback pseudocode are excluded from the
accepted evidence. The raw files remain local-only; they are not published.

The accepted conclusions use the exact two class accessors and alias,
their literal arguments, direct superclass argument, and their bounded
compiled-in pointer records. These do not establish a property array,
native function table, setter, or assignment producer. Counts reported as
unknown must not be interpreted as zero.

## Remaining boundaries

No phone, ADB, Apex launch, runtime memory, ptrace, breakpoint, decryption,
patch, or writable Ghidra program operation occurred. Application data was
not inspected. Ghidra did perform normal host preference/log I/O, so
APPDATA_TOUCHED refers to application evidence, not all host AppData paths.
Analysis was offline; the authorized Git publication uses network access.

`FUTURE_PTRACE_TRACE_GATE = NO_GO`

`FINAL_GATE = E UNREAL_REFLECTION_OWNER_AXIS_EXHAUSTED`

This gate describes the usable owner evidence at the stop point, not a
certification that the earlier helper execution stayed strictly in scope.
