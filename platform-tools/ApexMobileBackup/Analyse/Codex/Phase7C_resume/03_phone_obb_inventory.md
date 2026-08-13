# Phase7C Resume - Phone OBB inventory

The public package directory was readable through both `ls` and `find`:

```text
/sdcard/Android/obb/com.ea.gp.apexlegendsmobilefps/
```

| File | Present | Pulled read-only |
| --- | --- | --- |
| `main.64003140.com.ea.gp.apexlegendsmobilefps.obb` | yes | yes |
| `patch.64003140.com.ea.gp.apexlegendsmobilefps.obb` | yes | yes |

```text
PHONE_OBB_ACCESS = ALLOWED
PHONE_OBB_PRESENT = YES
OBB_PULL_METHOD = adb pull
```

The copies are stored only under ignored `Analyse/LocalInputs/OBB`. No source file on the phone was changed.
