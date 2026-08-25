# Puffer owner

`libgcloud.so` owns the observed Puffer update implementation. Among the
bounded GCloud/MSDK candidate libraries, the exact `CPufferInitAction` and
`MakeSureGetUrlFromServer` evidence occurs in `libgcloud.so`.

The function-name string `MakeSureGetUrlFromServer` is stored at Ghidra
`0x79aef0` / ELF VA and file offset `0x69aef0`. Functions using the same source
file and function-name argument identify `FUN_004ffd44` as the implementation.
It constructs service name `PufferUpdateService`, starts the RPC client, waits
for connection, initiates version retrieval, and extracts primary/spare URLs.

```text
PUFFER_OWNER_LIBRARY = libgcloud.so
PUFFER_SYMBOL_LOCATION = function-name data at Ghidra 0x79aef0
PUFFER_FUNCTION_ADDRESS_GHIDRA = 0x004ffd44
PUFFER_FUNCTION_ADDRESS_ELF = 0x003ffd44
PUFFER_FUNCTION_FILE_OFFSET = 0x003ffd44
PUFFER_OWNER_CONFIDENCE = CONFIRMED
```

This promotes the runtime symbol from Phase15U to a static function owner. It
does not prove that Puffer constructs the rendered `I54140714` value.
