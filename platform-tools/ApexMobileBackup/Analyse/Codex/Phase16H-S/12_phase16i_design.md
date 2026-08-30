# Future Phase16I design boundary

No Apex runtime work is authorized or ready. A future target would still need
the exact preserved `libgcloud.so` binary, a freshly validated module-relative
offset for `CVersionMgrImp::Init`, runtime load-bias correlation, and the exact
function signature/calling convention.

Static analysis indicates that an external client callback reaches
`CVersionMgrImp::Init` and is stored at object offset `0x18`. It does not yet
prove which ARM64 entry register carries the object or callback. That mapping
must not be hardcoded without revalidating the disassembly and runtime ABI.

Ptrace is per-thread. Any future method must identify the actual thread that
executes the target; attaching only to a process leader does not guarantee a
function hit.
