# Boundary

Phase16Q did not touch any Android device and did not execute the target
application. It did not access AppData or the network, modify an APK/OBB/SO,
interact with GameProtector, or use ptrace or breakpoints.

Analysis used exact local hashes, ELF unwind metadata, a read-only existing
Ghidra project, one exact direct-caller scan, direct CFG edges inside the
coherent `CheckUpdate` continuation, and field-specific accesses in already
proven owner methods.

Raw bytes, disassembly, scripts, and Ghidra logs remain under ignored
`Analyse/LocalInputs/Phase16Q`. Only cleaned metadata and conclusions are
committed.
