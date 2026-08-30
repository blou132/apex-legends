# Phase16H-U tracer review

The exact H-U source used a compatibility state containing an 8-byte header
and 16-byte hardware-debug slots. Its SETREGSET iovec ended at slot 1, so its
length was 24 bytes: the complete header plus the complete slot 0.

The H-U first submission therefore contained both:

- slot-0 target address;
- slot-0 control value `0x00000000`.

It was not address-only at the ABI level. The accurate classification is
`ATOMIC_ADDRESS_PLUS_DISABLED_CONTROL`.

H-U observed successful SETREGSET followed by an address mismatch. It did not
log the returned address, did not submit active control, did not continue the
tracee, and safely terminated the disposable process. The exact returned H-U
address remains unknown.

The redesign removes this disabled pre-step entirely. It must not be used as a
prerequisite for active programming.
