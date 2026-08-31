# Source and string anchors

References bounded to the target function identify a post-extraction path
bundle:

- `GCloudDolphin FetchPath UpdateInfoPath1 %s`
- `GCloudDolphin FetchPath UpdateInfoPath2 %s`
- `GCloudDolphin FetchPath CurApkPath%s`
- `GCloudDolphin FetchPath Paks2 %s`
- `Paks/`

The bounded upper function carries the function identifier
`DolphinCallback::OnDolphinFirstExtractSuccess` and source basename
`DolphinUpdaterCallback.cpp`. No private absolute build path is retained in
the committed outputs.

```text
TARGET_FUNCTION_SOURCE_ANCHORS = UPDATEINFOPATH1; UPDATEINFOPATH2; CURAPKPATH; PAKS2; PAKS
TARGET_FUNCTION_SEMANTIC_ROLE = POST_EXTRACTION_PATH_BUNDLE_CONSTRUCTION
```
