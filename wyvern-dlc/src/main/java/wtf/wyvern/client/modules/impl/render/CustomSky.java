package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.GlProgram;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "CustomSky",
        category = Category.RENDER,
        description = "Меняет вид неба"
)
public class CustomSky extends Module implements IMinecraft {

    public static final CustomSky INSTANCE = new CustomSky();
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();

    public final ModeSetting mode = new ModeSetting("Режим", "Аврора", "Сакура", "Плазма", "Плазма 2", "Северное сияние", "Ночной", "Летнее небо");

    // Время суток для «Летнего неба» — виден только когда выбран этот режим
    public final ModeSetting summerTime = new ModeSetting("Время суток", () -> mode.is("Летнее небо"), "Дневное", "Ночное");

    public final SliderSetting speed = new SliderSetting("Скорость", 1.0f, 0.1f, 5.0f, 0.01f);
    public final SliderSetting scale = new SliderSetting("Размер", 5.0f, 1.0f, 20.0f, 0.01f);
    public final SliderSetting intensity = new SliderSetting("Интенсивность", 0.1f, 0.1f, 1f, 0.01f);
    public final SliderSetting alpha = new SliderSetting("Прозрачность", 1.0f, 0.3f, 1.0f, 0.05f);
    public final BooleanSetting useThemeColor = new BooleanSetting("В цвет темы", false);

    private static final GlProgram AURORA_2_SHADER = new GlProgram(Wyvern.id("skyshader/sky_aurora"), VertexFormats.POSITION);
    private static final GlProgram SAKURA_2_SHADER = new GlProgram(Wyvern.id("skyshader/sky_sakura"), VertexFormats.POSITION);
    private static final GlProgram PLASMA_SHADER = new GlProgram(Wyvern.id("skyshader/sky_plasma"), VertexFormats.POSITION);
    private static final GlProgram PLASMA_2_SHADER = new GlProgram(Wyvern.id("skyshader/plasma2"), VertexFormats.POSITION);
    private static final GlProgram CAUSTIC_SHADER = new GlProgram(Wyvern.id("skyshader/caustic"), VertexFormats.POSITION);
    private static final GlProgram AURORA_SHADER = new GlProgram(Wyvern.id("skyshader/aurora"), VertexFormats.POSITION);
    private static final GlProgram NIGHT_SHADER = new GlProgram(Wyvern.id("skyshader/night"), VertexFormats.POSITION);
    private static final GlProgram SUMMER_SHADER = new GlProgram(Wyvern.id("skyshader/summer"), VertexFormats.POSITION);

    private long startMillis = -1;

    private CustomSky() {
        mode.addMode("Каустик");
    }

    @FastNative
    @Override
    public void onEnable() {
        startMillis = System.currentTimeMillis();
        super.onEnable();
    }

    @FastNative
    @Override
    public void onDisable() {
        startMillis = -1;
        super.onDisable();
    }

    public void renderSkyShader() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (startMillis < 0) {
            startMillis = System.currentTimeMillis();
        }

        float time = (System.currentTimeMillis() - startMillis) / 1000.0f;
        float fw = mc.getWindow().getFramebufferWidth();
        float fh = mc.getWindow().getFramebufferHeight();

        ColorRGBA themeColor = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
        ColorRGBA secondColor = Wyvern.getInstance().getThemeManager().getCurrentTheme().getSecondColor();
        float cr = themeColor.getRed() / 255.0f;
        float cg = themeColor.getGreen() / 255.0f;
        float cb = themeColor.getBlue() / 255.0f;
        float cr2 = secondColor.getRed() / 255.0f;
        float cg2 = secondColor.getGreen() / 255.0f;
        float cb2 = secondColor.getBlue() / 255.0f;
        float shaderR = useThemeColor.isEnabled() ? cr : 1.0F;
        float shaderG = useThemeColor.isEnabled() ? cg : 1.0F;
        float shaderB = useThemeColor.isEnabled() ? cb : 1.0F;
        float shaderR2 = useThemeColor.isEnabled() ? cr2 : 1.0F;
        float shaderG2 = useThemeColor.isEnabled() ? cg2 : 1.0F;
        float shaderB2 = useThemeColor.isEnabled() ? cb2 : 1.0F;

        GlProgram activeShader;
        if (mode.is("Плазма")) {
            activeShader = PLASMA_SHADER;
        } else if (mode.is("Плазма 2")) {
            activeShader = PLASMA_2_SHADER;
        } else if (mode.is("Каустик")) {
            activeShader = CAUSTIC_SHADER;
        } else if (mode.is("Северное сияние")) {
            activeShader = AURORA_SHADER;
        } else if (mode.is("Ночной")) {
            activeShader = NIGHT_SHADER;
        } else if (mode.is("Летнее небо")) {
            activeShader = SUMMER_SHADER;
        } else if (mode.is("Сакура")) {
            activeShader = SAKURA_2_SHADER;
        } else if (mode.is("Аврора")) {
            activeShader = AURORA_2_SHADER;
        } else {
            activeShader = AURORA_2_SHADER;
        }

        if (activeShader == null || !activeShader.isLoaded()) {
            return;
        }

        activeShader.use();

        Camera cam = mc.gameRenderer.getCamera();
        float yawRad = (float) Math.toRadians(-cam.getYaw());
        float pitchRad = (float) Math.toRadians(cam.getPitch());
        float fovDeg = (float) mc.options.getFov().getValue().intValue();

        if (mode.is("Плазма")) {
            applyPlasmaUniforms(activeShader, 1.0F);
        } else if (mode.is("Плазма 2")) {
            setUniform(activeShader, "u_Resolution", fw, fh);
            setUniform(activeShader, "u_Time", time * speed.getCurrent());
            setUniform(activeShader, "u_Fov", fovDeg);
            setUniform(activeShader, "u_CameraDir", yawRad, pitchRad);
            setUniform(activeShader, "u_Scale", scale.getCurrent());
            setUniform(activeShader, "u_Intensity", intensity.getCurrent());
            setUniform(activeShader, "u_Alpha", alpha.getCurrent());
            // Plasma 2 mirrors MainMenu, so it always follows the selected
            // theme instead of falling back to a white palette.
            setUniform(activeShader, "u_Color", cr, cg, cb);
            setUniform(activeShader, "u_Color2", cr2, cg2, cb2);
        } else if (mode.is("Сакура")) {
            long dayTime = Math.floorMod(mc.world.getTimeOfDay(), 24000L);
            float night = dayTime >= 13000L && dayTime <= 23000L ? 1.0F : 0.0F;
            setUniform(activeShader, "u_Color", shaderR, shaderG, shaderB, alpha.getCurrent());
            setUniform(activeShader, "u_Resolution", fw, fh);
            setUniform(activeShader, "u_Scale", scale.getCurrent());
            setUniform(activeShader, "u_Time", time * speed.getCurrent());
            setUniform(activeShader, "u_Night", night);
            setUniform(activeShader, "u_Fov", fovDeg);
            setUniform(activeShader, "u_CameraDir", yawRad, pitchRad);
        } else if (mode.is("Аврора")) {
            setUniform(activeShader, "u_Color", shaderR, shaderG, shaderB, alpha.getCurrent());
            setUniform(activeShader, "u_Resolution", fw, fh);
            setUniform(activeShader, "u_Scale", scale.getCurrent());
            setUniform(activeShader, "u_Time", time * speed.getCurrent());
            setUniform(activeShader, "u_Fov", fovDeg);
            setUniform(activeShader, "u_CameraDir", yawRad, pitchRad);
        } else {
            setUniform(activeShader, "uTime", time);
            setUniform(activeShader, "uResolution", fw, fh);
            setUniform(activeShader, "uColor", cr, cg, cb);
            setUniform(activeShader, "uAlpha", alpha.getCurrent());
            setUniform(activeShader, "uSpeed", speed.getCurrent());
            setUniform(activeShader, "uScale", scale.getCurrent());
            setUniform(activeShader, "uIntensity", intensity.getCurrent());
            setUniform(activeShader, "uCameraDir", yawRad, pitchRad);
            setUniform(activeShader, "uFov", fovDeg);
            setUniform(activeShader, "uColor2", cr2, cg2, cb2);
            setUniform(activeShader, "uRain", mc.world.getRainGradient(1.0f));
            setUniform(activeShader, "uThunder", mc.world.getThunderGradient(1.0f));
            setUniform(activeShader, "uNight", summerTime.is("Ночное") ? 1.0f : 0.0f);
            // «Ночной» всегда следует палитре клиента; у остальных режимов
            // это по-прежнему управляется настройкой «В цвет темы».
            setUniform(activeShader, "uThemeMix",
                    mode.is("Ночной") || useThemeColor.isEnabled() ? 1.0f : 0.0f);
        }

        Matrix4f savedProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        RenderSystem.setProjectionMatrix(new Matrix4f(), com.mojang.blaze3d.systems.ProjectionType.ORTHOGRAPHIC);

        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().identity();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(org.lwjgl.opengl.GL11.GL_LEQUAL);
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        BufferBuilder buf = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        buf.vertex(IDENTITY_MATRIX, -1f, -1f, 0f);
        buf.vertex(IDENTITY_MATRIX, 1f, -1f, 0f);
        buf.vertex(IDENTITY_MATRIX, 1f, 1f, 0f);
        buf.vertex(IDENTITY_MATRIX, -1f, 1f, 0f);
        BufferRenderer.drawWithGlobalProgram(buf.end());

        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthFunc(org.lwjgl.opengl.GL11.GL_LEQUAL);
        RenderSystem.setProjectionMatrix(savedProj, com.mojang.blaze3d.systems.ProjectionType.PERSPECTIVE);
        // Не оставляем fullscreen-программу активной для следующего прохода рендера.
        RenderSystem.setShader(ShaderProgramKeys.POSITION);
    }

    /**
     * Shared Plasma setup used by the CustomSky plasma mode.
     */
    @FastNative
    public void applyPlasmaUniforms(GlProgram program, float alphaMultiplier) {
        applyPlasmaUniforms(program, alphaMultiplier, speed.getCurrent());
    }

    @FastNative
    public void applyPlasmaUniforms(GlProgram program, float alphaMultiplier, float animationSpeed) {
        if (program == null || !program.isLoaded() || mc.player == null || mc.world == null) {
            return;
        }
        if (startMillis < 0) {
            startMillis = System.currentTimeMillis();
        }

        ColorRGBA first = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
        ColorRGBA second = Wyvern.getInstance().getThemeManager().getCurrentTheme().getSecondColor();
        float firstR = useThemeColor.isEnabled() ? first.getRed() / 255.0F : 1.0F;
        float firstG = useThemeColor.isEnabled() ? first.getGreen() / 255.0F : 1.0F;
        float firstB = useThemeColor.isEnabled() ? first.getBlue() / 255.0F : 1.0F;
        float secondR = useThemeColor.isEnabled() ? second.getRed() / 255.0F : 1.0F;
        float secondG = useThemeColor.isEnabled() ? second.getGreen() / 255.0F : 1.0F;
        float secondB = useThemeColor.isEnabled() ? second.getBlue() / 255.0F : 1.0F;
        float shaderAlpha = Math.max(0.0F, Math.min(1.0F, alpha.getCurrent() * alphaMultiplier));

        Camera camera = mc.gameRenderer.getCamera();
        setUniform(program, "u_Color", firstR, firstG, firstB, shaderAlpha);
        setUniform(program, "u_Color2", secondR, secondG, secondB, shaderAlpha);
        setUniform(program, "u_Resolution",
                mc.getWindow().getFramebufferWidth(),
                mc.getWindow().getFramebufferHeight());
        setUniform(program, "u_Scale", scale.getCurrent());
        setUniform(program, "u_Time",
                (System.currentTimeMillis() - startMillis) / 1000.0F * animationSpeed);
        setUniform(program, "u_Fov", (float) mc.options.getFov().getValue().intValue());
        setUniform(program, "u_CameraDir",
                (float) Math.toRadians(-camera.getYaw()),
                (float) Math.toRadians(camera.getPitch()));
    }

    @FastNative
    private void setUniform(GlProgram program, String name, float value) {
        net.minecraft.client.gl.GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(value);
        }
    }

    @FastNative
    private void setUniform(GlProgram program, String name, float x, float y) {
        net.minecraft.client.gl.GlUniform uniform = program.findUniform(name);
        if (uniform != null) uniform.set(x, y);
    }

    @FastNative
    private void setUniform(GlProgram program, String name, float x, float y, float z) {
        net.minecraft.client.gl.GlUniform uniform = program.findUniform(name);
        if (uniform != null) uniform.set(x, y, z);
    }

    @FastNative
    private void setUniform(GlProgram program, String name, float x, float y, float z, float w) {
        net.minecraft.client.gl.GlUniform uniform = program.findUniform(name);
        if (uniform != null) uniform.set(x, y, z, w);
    }
}
