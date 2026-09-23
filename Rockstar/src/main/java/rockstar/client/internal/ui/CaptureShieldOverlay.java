package rockstar.client.internal.ui;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLCapabilities;
import rockstar.client.internal.core.WindowCaptureUtils;
import rockstar.client.RockstarClient;

public final class CaptureShieldOverlay {
    private static final String internalField0248 = "#version 150\nin vec2 Position;\nout vec2 uv;\nvoid main() {\n    uv = Position * 0.5 + 0.5;\n    gl_Position = vec4(Position, 0.0, 1.0);\n}\n";
    private static final String internalField0247 = "#version 150\nuniform sampler2D Sampler;\nin vec2 uv;\nout vec4 fragColor;\nvoid main() {\n    fragColor = texture(Sampler, uv);\n}\n";
    private long internalField0229;
    private GLCapabilities internalField0174;
    private GLCapabilities internalField0175;
    private int internalField0227;
    private int internalField0228;
    private int internalField1053;
    private int internalField1055 = -1;
    private boolean internalField0277;
    private boolean internalField0276;
    private int internalField1056 = Integer.MIN_VALUE;
    private int internalField1054 = Integer.MIN_VALUE;
    private int internalField1464 = -1;
    private int internalField1470 = -1;

    public boolean internalMethod02338() {
        return this.internalField0229 != 0L;
    }

    public boolean internalMethod02343() {
        return this.internalField0276;
    }

    public boolean internalMethod08566() {
        if (this.internalField0229 != 0L) {
            return true;
        }
        if (this.internalField0276) {
            return false;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        long l = minecraftClient.getWindow().getHandle();
        try {
            this.internalField0175 = GL.getCapabilities();
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint((int)139266, (int)3);
            GLFW.glfwWindowHint((int)139267, (int)2);
            GLFW.glfwWindowHint((int)139272, (int)204801);
            GLFW.glfwWindowHint((int)131076, (int)0);
            GLFW.glfwWindowHint((int)131077, (int)0);
            GLFW.glfwWindowHint((int)131079, (int)1);
            GLFW.glfwWindowHint((int)131075, (int)0);
            GLFW.glfwWindowHint((int)131073, (int)0);
            GLFW.glfwWindowHint((int)131084, (int)0);
            GLFW.glfwWindowHint((int)131085, (int)1);
            GLFW.glfwWindowHint((int)131082, (int)1);
            GLFW.glfwWindowHint((int)135172, (int)8);
            GLFW.glfwWindowHint((int)135173, (int)0);
            GLFW.glfwWindowHint((int)135174, (int)0);
            this.internalField0229 = GLFW.glfwCreateWindow((int)Math.max(1, minecraftClient.getWindow().getWidth()), (int)Math.max(1, minecraftClient.getWindow().getHeight()), (CharSequence)"", (long)0L, (long)l);
            GLFW.glfwDefaultWindowHints();
            if (this.internalField0229 == 0L) {
                this.internalField0276 = true;
                return false;
            }
            if (GLFW.glfwGetWindowAttrib((long)this.internalField0229, (int)131082) != 1) {
                this.internalMethod02342();
                this.internalField0276 = true;
                return false;
            }
            long l2 = GLFWNativeWin32.glfwGetWin32Window((long)this.internalField0229);
            if (l2 == 0L || !WindowCaptureUtils.excludeFromCapture(l2)) {
                this.internalMethod02342();
                this.internalField0276 = true;
                return false;
            }
            WindowCaptureUtils.makeGhost(l2);
            GLFW.glfwMakeContextCurrent((long)this.internalField0229);
            this.internalField0174 = GL.createCapabilities();
            GLFW.glfwSwapInterval((int)0);
            this.internalMethod08565();
            GLFW.glfwMakeContextCurrent((long)l);
            GL.setCapabilities((GLCapabilities)this.internalField0175);
            return true;
        }
        catch (Throwable throwable) {
            RockstarClient.internalField0572.error("[CaptureShield] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0437\u0434\u0430\u0442\u044c \u043e\u043a\u043d\u043e \u043f\u0440\u0438\u0432\u0430\u0442\u043d\u043e\u0433\u043e \u0441\u043b\u043e\u044f", throwable);
            try {
                GLFW.glfwMakeContextCurrent((long)l);
                if (this.internalField0175 != null) {
                    GL.setCapabilities((GLCapabilities)this.internalField0175);
                }
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
            this.internalMethod02342();
            this.internalField0276 = true;
            return false;
        }
    }

    private void internalMethod08565() {
        int n = this.internalMethod02944(35633, internalField0248);
        int n2 = this.internalMethod02944(35632, internalField0247);
        this.internalField0227 = GL20.glCreateProgram();
        GL20.glAttachShader((int)this.internalField0227, (int)n);
        GL20.glAttachShader((int)this.internalField0227, (int)n2);
        GL20.glBindAttribLocation((int)this.internalField0227, (int)0, (CharSequence)"Position");
        GL20.glLinkProgram((int)this.internalField0227);
        if (GL20.glGetProgrami((int)this.internalField0227, (int)35714) == 0) {
            throw new IllegalStateException("link: " + GL20.glGetProgramInfoLog((int)this.internalField0227));
        }
        GL20.glDeleteShader((int)n);
        GL20.glDeleteShader((int)n2);
        this.internalField1055 = GL20.glGetUniformLocation((int)this.internalField0227, (CharSequence)"Sampler");
        this.internalField0228 = GL30.glGenVertexArrays();
        this.internalField1053 = GL15.glGenBuffers();
        GL30.glBindVertexArray((int)this.internalField0228);
        GL15.glBindBuffer((int)34962, (int)this.internalField1053);
        GL15.glBufferData((int)34962, (float[])new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f}, (int)35044);
        GL20.glEnableVertexAttribArray((int)0);
        GL20.glVertexAttribPointer((int)0, (int)2, (int)5126, (boolean)false, (int)8, (long)0L);
        GL30.glBindVertexArray((int)0);
        GL15.glBindBuffer((int)34962, (int)0);
    }

    private int internalMethod02944(int n, String string) {
        int n2 = GL20.glCreateShader((int)n);
        GL20.glShaderSource((int)n2, (CharSequence)string);
        GL20.glCompileShader((int)n2);
        if (GL20.glGetShaderi((int)n2, (int)35713) == 0) {
            throw new IllegalStateException("compile: " + GL20.glGetShaderInfoLog((int)n2));
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void internalMethod05460(int n, int[] nArray, int n2, int n3, int n4) {
        if (this.internalField0229 == 0L) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        long l = minecraftClient.getWindow().getHandle();
        if (!this.internalMethod04679(l)) {
            this.internalMethod02337();
            return;
        }
        try {
            GL11.glFlush();
            GLFW.glfwMakeContextCurrent((long)this.internalField0229);
            GL.setCapabilities((GLCapabilities)this.internalField0174);
            int[] nArray2 = new int[1];
            int[] nArray3 = new int[1];
            GLFW.glfwGetFramebufferSize((long)this.internalField0229, (int[])nArray2, (int[])nArray3);
            GL11.glViewport((int)0, (int)0, (int)nArray2[0], (int)nArray3[0]);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)3089);
            GL11.glDisable((int)2884);
            GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
            GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
            GL11.glClear((int)16384);
            if (n != 0 && n2 > 0 && n3 > 0 && n4 > 0) {
                GL20.glUseProgram((int)this.internalField0227);
                if (this.internalField1055 >= 0) {
                    GL20.glUniform1i((int)this.internalField1055, (int)0);
                }
                GL13.glActiveTexture((int)33984);
                GL11.glBindTexture((int)3553, (int)n);
                GL30.glBindVertexArray((int)this.internalField0228);
                float f = (float)nArray2[0] / (float)n3;
                float f2 = (float)nArray3[0] / (float)n4;
                GL11.glEnable((int)3089);
                for (int i = 0; i < n2; ++i) {
                    int n5 = i * 4;
                    GL11.glScissor((int)((int)Math.floor((float)nArray[n5] * f)), (int)((int)Math.floor((float)nArray[n5 + 1] * f2)), (int)((int)Math.ceil((float)nArray[n5 + 2] * f)), (int)((int)Math.ceil((float)nArray[n5 + 3] * f2)));
                    GL11.glDrawArrays((int)5, (int)0, (int)4);
                }
                GL11.glDisable((int)3089);
                GL30.glBindVertexArray((int)0);
                GL11.glBindTexture((int)3553, (int)0);
                GL20.glUseProgram((int)0);
            }
            GLFW.glfwSwapBuffers((long)this.internalField0229);
        }
        catch (Throwable throwable) {
            RockstarClient.internalField0572.error("[CaptureShield] \u043e\u0448\u0438\u0431\u043a\u0430 \u0432\u044b\u0432\u043e\u0434\u0430 \u043f\u0440\u0438\u0432\u0430\u0442\u043d\u043e\u0433\u043e \u0441\u043b\u043e\u044f", throwable);
            this.internalField0276 = true;
        }
        finally {
            GLFW.glfwMakeContextCurrent((long)l);
            GL.setCapabilities((GLCapabilities)this.internalField0175);
        }
        if (!this.internalField0277) {
            GLFW.glfwShowWindow((long)this.internalField0229);
            this.internalField0277 = true;
        }
    }

    private boolean internalMethod04679(long l) {
        if (GLFW.glfwGetWindowAttrib((long)l, (int)131074) == 1) {
            return false;
        }
        if (GLFW.glfwGetWindowAttrib((long)l, (int)131073) != 1) {
            return false;
        }
        int[] nArray = new int[1];
        int[] nArray2 = new int[1];
        int[] nArray3 = new int[1];
        int[] nArray4 = new int[1];
        GLFW.glfwGetWindowPos((long)l, (int[])nArray, (int[])nArray2);
        GLFW.glfwGetWindowSize((long)l, (int[])nArray3, (int[])nArray4);
        if (nArray3[0] <= 0 || nArray4[0] <= 0) {
            return false;
        }
        if (nArray3[0] != this.internalField1464 || nArray4[0] != this.internalField1470) {
            GLFW.glfwSetWindowSize((long)this.internalField0229, (int)nArray3[0], (int)nArray4[0]);
            this.internalField1464 = nArray3[0];
            this.internalField1470 = nArray4[0];
        }
        if (nArray[0] != this.internalField1056 || nArray2[0] != this.internalField1054) {
            GLFW.glfwSetWindowPos((long)this.internalField0229, (int)nArray[0], (int)nArray2[0]);
            this.internalField1056 = nArray[0];
            this.internalField1054 = nArray2[0];
        }
        return true;
    }

    public void internalMethod02337() {
        if (this.internalField0229 != 0L && this.internalField0277) {
            GLFW.glfwHideWindow((long)this.internalField0229);
            this.internalField0277 = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void internalMethod02342() {
        if (this.internalField0229 == 0L) {
            return;
        }
        long l = MinecraftClient.getInstance().getWindow().getHandle();
        try {
            GLFW.glfwMakeContextCurrent((long)this.internalField0229);
            GL.setCapabilities((GLCapabilities)this.internalField0174);
            if (this.internalField1053 != 0) {
                GL15.glDeleteBuffers((int)this.internalField1053);
            }
            if (this.internalField0228 != 0) {
                GL30.glDeleteVertexArrays((int)this.internalField0228);
            }
            if (this.internalField0227 != 0) {
                GL20.glDeleteProgram((int)this.internalField0227);
            }
        }
        catch (Throwable throwable) {
        }
        finally {
            GLFW.glfwMakeContextCurrent((long)l);
            if (this.internalField0175 != null) {
                GL.setCapabilities((GLCapabilities)this.internalField0175);
            }
        }
        GLFW.glfwDestroyWindow((long)this.internalField0229);
        this.internalField0229 = 0L;
        this.internalField0228 = 0;
        this.internalField1053 = 0;
        this.internalField0227 = 0;
        this.internalField0277 = false;
        this.internalField1056 = Integer.MIN_VALUE;
        this.internalField1054 = Integer.MIN_VALUE;
        this.internalField1464 = -1;
        this.internalField1470 = -1;
    }
}

