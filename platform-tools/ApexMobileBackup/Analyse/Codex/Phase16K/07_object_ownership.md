# Object ownership

The factory return leaves `libgcloud.so` in `x0`; the provider does not retain
the newly constructed object before returning it. The only exact client call
decoded in `libUE4.so` does not store the pointer in an owner field, global,
service object, or wrapper. Instead, its incoherent PLT sequence moves the
value into an argument register for an incompatible adjacent helper.

Consequently there is no defensible store site, owner base, or field offset.
The exact static module boundary is confirmed, but returned-object ownership
is not.

```text
DOLPHIN_OBJECT_STORAGE = NOT_PROVEN
DOLPHIN_OWNER_OBJECT = UNKNOWN
DOLPHIN_OWNER_FIELD = UNKNOWN
```
