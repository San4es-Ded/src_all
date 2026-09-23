package haron.render;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SkyShaderProgram {
    private static final SkyShaderProgram INSTANCE = new SkyShaderProgram();
    private int programId = -1;
    private int vaoId = -1;
    private int vboId = -1;
    private boolean initFailed = false;
    private int uTime;
    private int uSpeed;
    private int uDensity;
    private int uScale;
    private int uSkyColor;
    private int uScreenSize;
    private int uCamYaw;
    private int uCamPitch;
    private int uCamFov;
    private int uSkyMode;
    private final float startTime = (float)System.nanoTime() / 1.0E9f;

    public void render(float f, float f2, float f3, Color color, float f4, float f5, float f6, int n) {
        this.init();
        if (!this.initFailed && this.programId != -1) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            int n2 = minecraftClient.getWindow().getFramebufferWidth();
            int n3 = minecraftClient.getWindow().getFramebufferHeight();
            boolean bl = GL11.glIsEnabled((int)2929);
            boolean bl2 = GL11.glIsEnabled((int)3042);
            boolean bl3 = GL11.glGetBoolean((int)2930);
            int n4 = GL11.glGetInteger((int)2932);
            int n5 = GL11.glGetInteger((int)35725);
            GL11.glEnable((int)2929);
            GL11.glDepthFunc((int)515);
            GL11.glDepthMask((boolean)false);
            GL11.glDisable((int)3042);
            GL20.glUseProgram((int)this.programId);
            float f7 = (float)System.nanoTime() / 1.0E9f - this.startTime;
            GL20.glUniform1f((int)this.uTime, (float)f7);
            GL20.glUniform1f((int)this.uSpeed, (float)f);
            GL20.glUniform1f((int)this.uDensity, (float)f2);
            GL20.glUniform1f((int)this.uScale, (float)f3);
            GL20.glUniform3f((int)this.uSkyColor, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f));
            GL20.glUniform2f((int)this.uScreenSize, (float)n2, (float)n3);
            GL20.glUniform1f((int)this.uCamYaw, (float)((float)Math.toRadians(f4)));
            GL20.glUniform1f((int)this.uCamPitch, (float)((float)Math.toRadians(f5)));
            GL20.glUniform1f((int)this.uCamFov, (float)((float)Math.toRadians(f6)));
            GL20.glUniform1i((int)this.uSkyMode, (int)n);
            GL30.glBindVertexArray((int)this.vaoId);
            GL11.glDrawArrays((int)4, (int)0, (int)3);
            GL30.glBindVertexArray((int)0);
            GL20.glUseProgram((int)n5);
            GL11.glDepthFunc((int)n4);
            GL11.glDepthMask((boolean)bl3);
            if (!bl) {
                GL11.glDisable((int)2929);
            }
            if (bl2) {
                GL11.glEnable((int)3042);
            }
        }
    }

    private int compileShader(int n, String string) {
        int n2 = GL20.glCreateShader((int)n);
        GL20.glShaderSource((int)n2, (CharSequence)this.readShaderSource(string));
        GL20.glCompileShader((int)n2);
        if (GL20.glGetShaderi((int)n2, (int)35713) == 0) {
            System.err.println(SkyShaderProgram.$sf$2(string, GL20.glGetShaderInfoLog((int)n2)));
        }
        return n2;
    }

    private String readShaderSource(String string) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SkyShaderProgram.class.getResourceAsStream(SkyShaderProgram.$sf$3(string)))));
            String string2 = bufferedReader.lines().collect(Collectors.joining("\n"));
            bufferedReader.close();
            return string2;
        }
        catch (IOException iOException) {
            throw new RuntimeException(SkyShaderProgram.$sf$4(string), iOException);
        }
    }

    public static SkyShaderProgram get() {
        return INSTANCE;
    }

    private void init() {
        if (this.programId == -1 && !this.initFailed) {
            try {
                int n = this.compileShader(35633, "sky_shader.vert");
                int n2 = this.compileShader(35632, "sky_shader.frag");
                this.programId = GL20.glCreateProgram();
                GL20.glAttachShader((int)this.programId, (int)n);
                GL20.glAttachShader((int)this.programId, (int)n2);
                GL20.glLinkProgram((int)this.programId);
                if (GL20.glGetProgrami((int)this.programId, (int)35714) == 0) {
                    System.err.println(SkyShaderProgram.$sf$0(GL20.glGetProgramInfoLog((int)this.programId)));
                    this.initFailed = true;
                    return;
                }
                GL20.glDeleteShader((int)n);
                GL20.glDeleteShader((int)n2);
                this.uTime = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"Time");
                this.uSpeed = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"Speed");
                this.uDensity = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"Density");
                this.uScale = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"Scale");
                this.uSkyColor = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"SkyColor");
                this.uScreenSize = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"ScreenSize");
                this.uCamYaw = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"CamYaw");
                this.uCamPitch = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"CamPitch");
                this.uCamFov = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"CamFovRad");
                this.uSkyMode = GL20.glGetUniformLocation((int)this.programId, (CharSequence)"SkyMode");
                float[] fArray = new float[]{-1.0f, -1.0f, 3.0f, -1.0f, -1.0f, 3.0f};
                this.vaoId = GL30.glGenVertexArrays();
                GL30.glBindVertexArray((int)this.vaoId);
                this.vboId = GL15.glGenBuffers();
                GL15.glBindBuffer((int)34962, (int)this.vboId);
                FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)fArray.length);
                floatBuffer.put(fArray).flip();
                GL15.glBufferData((int)34962, (FloatBuffer)floatBuffer, (int)35044);
                GL20.glVertexAttribPointer((int)0, (int)2, (int)5126, (boolean)false, (int)0, (long)0L);
                GL20.glEnableVertexAttribArray((int)0);
                GL30.glBindVertexArray((int)0);
                GL15.glBindBuffer((int)34962, (int)0);
            }
            catch (Exception exception) {
                System.err.println(SkyShaderProgram.$sf$1(String.valueOf(exception)));
                this.initFailed = true;
            }
        }
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "[Haron] SkyShader link failed: " + string;
    }

    private static /* synthetic */ String $sf$4(String string) {
        return "Failed to load shader source: " + string;
    }

    private static /* synthetic */ String $sf$3(String string) {
        return "/assets/haron/shaders/" + string;
    }

    private static /* synthetic */ String $sf$1(String string) {
        return "[Haron] SkyShader init failed: " + string;
    }

    private static /* synthetic */ String $sf$2(String string, String string2) {
        return "[Haron] SkyShader compile failed (" + string + "): " + string2;
    }
}
