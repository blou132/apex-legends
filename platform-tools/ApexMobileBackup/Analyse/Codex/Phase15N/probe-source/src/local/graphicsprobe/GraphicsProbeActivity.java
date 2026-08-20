package local.graphicsprobe;

import android.app.Activity;
import android.graphics.Color;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GraphicsProbeActivity extends Activity {
    private static final String TAG = "LocalGraphicsProbe";
    private static final String RESULT_FILE = "graphics_probe_result.json";
    private static final int EGL_OPENGL_ES3_BIT_KHR = 0x0040;
    private static final int EGL_CONTEXT_MINOR_VERSION_KHR = 0x30FB;
    private static final int GL_RGBA16F = 0x881A;

    private TextView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statusView = new TextView(this);
        statusView.setBackgroundColor(Color.rgb(20, 24, 28));
        statusView.setTextColor(Color.WHITE);
        statusView.setGravity(Gravity.CENTER);
        statusView.setTextSize(18.0f);
        statusView.setText("Running local EGL/GLES capability probe...");
        setContentView(statusView);

        Thread worker = new Thread(this::runProbe, "graphics-probe");
        worker.start();
    }

    private void runProbe() {
        JSONObject result = new JSONObject();
        EGLDisplay display = EGL14.EGL_NO_DISPLAY;
        EGLContext context = EGL14.EGL_NO_CONTEXT;
        EGLSurface surface = EGL14.EGL_NO_SURFACE;

        try {
            result.put("PROBE_RAN", true);
            result.put("PACKAGE", getPackageName());

            display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
            require(display != EGL14.EGL_NO_DISPLAY, "eglGetDisplay failed");

            int[] eglVersion = new int[2];
            require(EGL14.eglInitialize(display, eglVersion, 0, eglVersion, 1),
                    "eglInitialize failed: " + eglError());
            result.put("EGL_INITIALIZED", true);
            result.put("EGL_VERSION", eglVersion[0] + "." + eglVersion[1]);
            result.put("EGL_VENDOR", nullToEmpty(EGL14.eglQueryString(display, EGL14.EGL_VENDOR)));

            require(EGL14.eglBindAPI(EGL14.EGL_OPENGL_ES_API),
                    "eglBindAPI failed: " + eglError());

            int[] configAttributes = {
                    EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                    EGL14.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES3_BIT_KHR,
                    EGL14.EGL_RED_SIZE, 8,
                    EGL14.EGL_GREEN_SIZE, 8,
                    EGL14.EGL_BLUE_SIZE, 8,
                    EGL14.EGL_ALPHA_SIZE, 8,
                    EGL14.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] configCount = new int[1];
            require(EGL14.eglChooseConfig(
                            display, configAttributes, 0, configs, 0, configs.length, configCount, 0)
                            && configCount[0] > 0,
                    "eglChooseConfig failed: " + eglError());

            int[] pbufferAttributes = {
                    EGL14.EGL_WIDTH, 4,
                    EGL14.EGL_HEIGHT, 4,
                    EGL14.EGL_NONE
            };
            surface = EGL14.eglCreatePbufferSurface(display, configs[0], pbufferAttributes, 0);
            require(surface != EGL14.EGL_NO_SURFACE,
                    "eglCreatePbufferSurface failed: " + eglError());

            int[] exactContextAttributes = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 3,
                    EGL_CONTEXT_MINOR_VERSION_KHR, 1,
                    EGL14.EGL_NONE
            };
            context = EGL14.eglCreateContext(
                    display, configs[0], EGL14.EGL_NO_CONTEXT, exactContextAttributes, 0);
            String contextRequest = "OpenGL ES 3.1 exact";

            if (context == EGL14.EGL_NO_CONTEXT) {
                int exactRequestError = EGL14.eglGetError();
                int[] es3ContextAttributes = {
                        EGL14.EGL_CONTEXT_CLIENT_VERSION, 3,
                        EGL14.EGL_NONE
                };
                context = EGL14.eglCreateContext(
                        display, configs[0], EGL14.EGL_NO_CONTEXT, es3ContextAttributes, 0);
                contextRequest = "OpenGL ES 3.x fallback after exact request "
                        + hex(exactRequestError);
            }
            result.put("CONTEXT_REQUEST", contextRequest);
            require(context != EGL14.EGL_NO_CONTEXT,
                    "eglCreateContext failed: " + eglError());
            require(EGL14.eglMakeCurrent(display, surface, surface, context),
                    "eglMakeCurrent failed: " + eglError());
            result.put("EGL_MAKE_CURRENT", true);

            String glVersion = nullToEmpty(GLES20.glGetString(GLES20.GL_VERSION));
            String glVendor = nullToEmpty(GLES20.glGetString(GLES20.GL_VENDOR));
            String glRenderer = nullToEmpty(GLES20.glGetString(GLES20.GL_RENDERER));
            int[] parsedVersion = parseGlesVersion(glVersion);
            int major = parsedVersion[0];
            int minor = parsedVersion[1];
            boolean gles31 = major == 3 && minor >= 1;

            Set<String> extensions = readExtensions(major);
            boolean halfFloat = extensions.contains("GL_EXT_color_buffer_half_float");
            boolean floatColor = extensions.contains("GL_EXT_color_buffer_float");
            boolean floatPredicate = halfFloat || (major == 3 && floatColor);

            result.put("GL_VENDOR", glVendor);
            result.put("GL_RENDERER", glRenderer);
            result.put("GL_VERSION_RAW", glVersion);
            result.put("GLES_MAJOR", major);
            result.put("GLES_MINOR", minor);
            result.put("GLES31_PREDICATE", gles31 ? "PASS" : "FAIL");
            result.put("EXT_COLOR_BUFFER_HALF_FLOAT", halfFloat);
            result.put("EXT_COLOR_BUFFER_FLOAT", floatColor);
            result.put("FLOAT_RT_DIAGNOSTIC_PREDICATE",
                    floatPredicate ? "PASS" : "FAIL");

            FloatFboResult fbo = testFloatFramebuffer(floatPredicate);
            result.put("FLOAT_COLOR_ATTACHMENT_CREATED", fbo.attachmentCreated);
            result.put("FBO_STATUS", hex(fbo.framebufferStatus));
            result.put("FBO_COMPLETE", fbo.framebufferComplete);
            result.put("GL_ERROR", fbo.glError);
            result.put("PROBE_SUCCESS", true);
        } catch (Throwable error) {
            Log.e(TAG, "Probe failed", error);
            putSafely(result, "PROBE_SUCCESS", false);
            putSafely(result, "ERROR", error.getClass().getSimpleName() + ": " + error.getMessage());
        } finally {
            if (display != EGL14.EGL_NO_DISPLAY) {
                EGL14.eglMakeCurrent(
                        display, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
                if (surface != EGL14.EGL_NO_SURFACE) {
                    EGL14.eglDestroySurface(display, surface);
                }
                if (context != EGL14.EGL_NO_CONTEXT) {
                    EGL14.eglDestroyContext(display, context);
                }
                EGL14.eglTerminate(display);
            }
        }

        String json = result.toString();
        writeResult(json);
        Log.i(TAG, "RESULT_JSON=" + json);
        boolean passed = "PASS".equals(result.optString("GLES31_PREDICATE"));
        runOnUiThread(() -> statusView.setText(
                passed ? "GLES 3.1 capability: PASS" : "GLES 3.1 capability: FAIL"));
    }

    private static Set<String> readExtensions(int major) {
        Set<String> extensions = new HashSet<>();
        if (major >= 3) {
            int[] count = new int[1];
            GLES30.glGetIntegerv(GLES30.GL_NUM_EXTENSIONS, count, 0);
            if (GLES30.glGetError() == GLES30.GL_NO_ERROR) {
                for (int index = 0; index < count[0]; index++) {
                    String extension = GLES30.glGetStringi(GLES30.GL_EXTENSIONS, index);
                    if (extension != null && !extension.isEmpty()) {
                        extensions.add(extension);
                    }
                }
            }
        }

        if (extensions.isEmpty()) {
            String rawExtensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
            if (rawExtensions != null) {
                for (String extension : rawExtensions.trim().split("\\s+")) {
                    if (!extension.isEmpty()) {
                        extensions.add(extension);
                    }
                }
            }
        }
        return extensions;
    }

    private static FloatFboResult testFloatFramebuffer(boolean permitted) {
        FloatFboResult result = new FloatFboResult();
        if (!permitted) {
            result.glError = "NOT_ATTEMPTED_EXTENSION_PREDICATE_FAILED";
            return result;
        }

        clearGlErrors();
        int[] renderbuffer = new int[1];
        int[] framebuffer = new int[1];
        GLES30.glGenRenderbuffers(1, renderbuffer, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderbuffer[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GL_RGBA16F, 4, 4);
        int storageError = GLES30.glGetError();
        result.attachmentCreated = renderbuffer[0] != 0 && storageError == GLES30.GL_NO_ERROR;

        if (result.attachmentCreated) {
            GLES30.glGenFramebuffers(1, framebuffer, 0);
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, framebuffer[0]);
            GLES30.glFramebufferRenderbuffer(
                    GLES30.GL_FRAMEBUFFER,
                    GLES30.GL_COLOR_ATTACHMENT0,
                    GLES30.GL_RENDERBUFFER,
                    renderbuffer[0]);
            result.framebufferStatus = GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);
            result.framebufferComplete = result.framebufferStatus == GLES30.GL_FRAMEBUFFER_COMPLETE;
        }

        int finalError = GLES30.glGetError();
        result.glError = storageError == GLES30.GL_NO_ERROR
                ? glErrorName(finalError)
                : "STORAGE_" + glErrorName(storageError);

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, 0);
        if (framebuffer[0] != 0) {
            GLES30.glDeleteFramebuffers(1, framebuffer, 0);
        }
        if (renderbuffer[0] != 0) {
            GLES30.glDeleteRenderbuffers(1, renderbuffer, 0);
        }
        return result;
    }

    private void writeResult(String json) {
        File output = new File(getFilesDir(), RESULT_FILE);
        try (FileOutputStream stream = new FileOutputStream(output, false)) {
            stream.write(json.getBytes(StandardCharsets.UTF_8));
            stream.write('\n');
        } catch (Exception error) {
            Log.e(TAG, "Unable to write result", error);
        }
    }

    private static int[] parseGlesVersion(String rawVersion) {
        Matcher matcher = Pattern.compile("OpenGL ES\\s+(\\d+)\\.(\\d+)").matcher(rawVersion);
        if (!matcher.find()) {
            return new int[]{-1, -1};
        }
        return new int[]{Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))};
    }

    private static void clearGlErrors() {
        for (int count = 0; count < 32 && GLES30.glGetError() != GLES30.GL_NO_ERROR; count++) {
            // Bound stale-error draining before the isolated FBO test.
        }
    }

    private static String eglError() {
        return hex(EGL14.eglGetError());
    }

    private static String glErrorName(int error) {
        if (error == GLES30.GL_NO_ERROR) {
            return "GL_NO_ERROR (0x0000)";
        }
        return String.format(Locale.US, "0x%04X", error);
    }

    private static String hex(int value) {
        return String.format(Locale.US, "0x%04X", value);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private static void putSafely(JSONObject target, String key, Object value) {
        try {
            target.put(key, value);
        } catch (Exception ignored) {
            Log.e(TAG, "Unable to add result field " + key);
        }
    }

    private static final class FloatFboResult {
        boolean attachmentCreated;
        boolean framebufferComplete;
        int framebufferStatus;
        String glError = "GL_NO_ERROR (0x0000)";
    }
}
