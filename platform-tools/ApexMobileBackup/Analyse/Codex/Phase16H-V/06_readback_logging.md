# Request and readback logging

Before SETREGSET, the tracer records:

- `REQUESTED_SLOT`;
- `REQUESTED_ADDRESS`;
- `REQUESTED_CONTROL`;
- `SETREGSET_IOV_LENGTH`.

Immediately after GETREGSET, it records:

- `RETURNED_ADDRESS`;
- `RETURNED_CONTROL`;
- `RETURNED_REGSET_LENGTH`;
- `ADDRESS_READBACK_MATCH`.

This closes the H-U evidence gap: a future failure will retain both sides of
the exact request/readback comparison. Runtime absolute addresses must remain
in local-only raw output and must not be committed to the cleaned report.

The active readback gate accepts only requested `0x000001e5` normalized to
cached `0x000041e4` with an exact address match.

Result: `EXACT_REQUEST_RETURN_ADDRESS_LOGGING_ADDED = YES`.
