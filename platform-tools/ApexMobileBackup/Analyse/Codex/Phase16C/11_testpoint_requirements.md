# Testpoint requirements

PotatoNV upstream requires physical motherboard testpoint access and device
disassembly. The tested-device table maps PRA to `Kirin 65x (A)`, and an
upstream community report describes a successful PRA-LX1 operation. However,
the upstream manual provides only a generic testpoint procedure, not a
maintainer-verified PRA-LX1 board photograph.

Search results contain third-party PRA-LA1/PRA-LX1 testpoint images, but their
provenance and board-revision coverage are insufficient for an execution gate.
No image was downloaded and no phone was opened.

```text
TESTPOINT_REQUIRED = YES
TESTPOINT_REFERENCE_CONFIDENCE = LOW
PHONE_DISASSEMBLY_REQUIRED = YES
```

No operator should short a point based on this report. A future phase must
first validate an exact motherboard/reference image against the physical board
revision and document the cable, USB COM driver, battery, and recovery plan.

Current non-destructive preconditions:

- Battery was comfortably charged (`96%`).
- The current USB cable is proven for normal ADB only.
- The Windows PC is stable and host platform tools are present.
- Huawei USB COM/testpoint drivers are not verified.
- Exact recovery images are not prepared.
