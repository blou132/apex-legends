# Flash preflight

Immediately before flash, Android was healthy at 100 percent battery and 29 C.
Exactly one PRA-LX1 was connected and no Samsung endpoint existed. Stock and
patched hashes matched their recorded values, the stock recovery materials
remained present, and Huawei Fastboot again reported `unlocked`.

The only proposed rollback command was documented, not executed:

`fastboot flash ramdisk <EXACT_STOCK_RAMDISK>`

```text
ROLLBACK_MATERIAL_READY = YES
MAGISK_FLASH_READY = YES
FLASH_TARGET_COUNT = 1
SAMSUNG_ENDPOINT_COUNT = 0
```
