# OBB installation

The package OBB directory was created through ordinary shell access. The exact
main and patch OBB files were copied without renaming beyond their authoritative
package/version filenames.

Post-copy inspection found exactly two files:

| File | Guest bytes | Expected bytes | Result |
| --- | ---: | ---: | --- |
| `main.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1942013346 | 1942013346 | MATCH |
| `patch.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1837582506 | 1837582506 | MATCH |

Host SHA256 verification was mandatory and passed before copying. A second
multi-gigabyte guest hash was not required. Scoped storage did not block the
operation.
