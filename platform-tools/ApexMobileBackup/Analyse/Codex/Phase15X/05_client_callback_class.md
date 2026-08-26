# Client callback class

No downstream client callback object is resolved. Consequently there is no
anchored allocation, constructor, vtable assignment, RTTI, source string,
destructor relationship, or class metadata to inspect in `libUE4`.

The known `cu::IPufferCallBack` implementation from Phase15W is
`GCloud::GCloudPufferImp`; it is the SDK forwarder, not the downstream client
callback requested by Phase15X.

```text
CLIENT_CALLBACK_INTERFACE_NAME = UNKNOWN
CLIENT_CALLBACK_CONCRETE_CLASS = UNKNOWN
CLIENT_CALLBACK_CONSTRUCTOR = UNKNOWN
CLIENT_CALLBACK_CONSTRUCTOR_ADDRESS = UNKNOWN
CLIENT_CALLBACK_VTABLE = UNKNOWN
```
