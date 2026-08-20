# Dialog role and blocking status

The complete message explicitly states that the application requires OpenGL ES
3.1 and floating-point render-target support, while the translated AVD reports
neither requirement and exposes only OpenGL ES 2 for this path.

- role: `DEVICE_COMPATIBILITY` (`CONFIRMED` from runtime text);
- trigger condition: GLES 3.1 and floating-point render-target requirements not
  met, with no packaged ES2 fallback (`PROBABLE`, native caller unknown);
- user acknowledgement required: `YES` (`CONFIRMED` for the matching
  `MessageBox01.show()` mechanism);
- blocks bootstrap: `PROBABLE`;
- controls splash dismissal: `NO_EVIDENCE`;
- native splash-dismiss trigger: `UNKNOWN`;
- first blocking dependency: `OPENGL_ES_3_1_DEVICE_COMPATIBILITY_GATE`.

The final gate is `B APEX_DIALOG_TEXT_RESOLVED_TRIGGER_UNKNOWN`: the complete
text and role are resolved, but no direct native owner/caller reference was
established.
