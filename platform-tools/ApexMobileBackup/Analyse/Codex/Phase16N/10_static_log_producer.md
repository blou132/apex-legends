# Static log-producer mapping

The exact early source/log anchor is in `libgcloudcore.so`, not `libgcloud.so`.
The containing exported function is:

`GCloud::Plugin::GCloudCoreReportSerivce::CreateEvent(int, int, char const*)`

The spelling `Serivce` is preserved from the binary symbol. Bounded static
disassembly confirms the source anchor `GCloudCoreReportService.cpp`, line 78,
and its call through the GCloud logging interface.

No direct native caller is resolved inside this module. The function is an
interface/service method and may execute on an arbitrary caller thread; it is
not itself established as a persistent worker entry.

- `EARLY_EVENT_MODULE = libgcloudcore.so`
- `EARLY_EVENT_FUNCTION = GCloud::Plugin::GCloudCoreReportSerivce::CreateEvent`
- `EARLY_EVENT_CALL_CONTEXT = INTERFACE_SERVICE_METHOD_ON_ARBITRARY_CALLER_THREAD`
