# Factory registration

No factory, plugin, service, or semantic function-pointer registration entry
containing `CreateDolphin` was found in the bounded provider or consumer
evidence. `libgcloud.so` exposes a normal dynamic factory export and
`libUE4.so` imports it through a normal PLT/GOT edge.

The import relocation is not a client ownership registry. The isolated
reference to the preceding `IGCloud::GetInstance` PLT entry lies in malformed
or data-like code with no caller and provides no valid registration consumer.

```text
CREATEDOLPHIN_REGISTRATION_KIND = NONE_FOUND
CREATEDOLPHIN_REGISTRATION_OWNER = NONE
CREATEDOLPHIN_FACTORY_CONSUMER = UNKNOWN
```
