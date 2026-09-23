package haron.effects;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.effects.JumpWave;
import haron.effects.AnimatedTextureCircle;
import haron.effects.BoxFadeSample;
import haron.events.LivingEntityJumpEvent;
import haron.events.WorldRenderPostEvent;
import haron.events.ClientTickEvent;
import haron.hud.core.HudServices;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.hud.ClientColor;
import haron.render.shader.DefaultShaders;
import haron.render.shader.ShaderProgram;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

@ModuleInfo(a="Jump Circles", b="Отображает круги при прыжке игрока", c=ModuleCategory.VISUALS)
public class JumpCircles
extends HaronModule {
    private static final String MODE_PARTICLES = "Только частицы";
    private static final String MODE_BLOCK_WAVE = "Блоковая волна";
    private static final String MODE_CIRCLE = "Круг";
    private static final String CIRCLE_JUMP = "Jump";
    private static final String CIRCLE_JUMP2 = "Jump2";
    private static final String FILL_NORMAL = "Обычная";
    private static final String FILL_SHADER = "Шейдер";
    private static final String SHADER_NEBULA = "Небула";
    private static final String SHADER_STARS = "Звёзды";
    private static final String SHADER_WEB = "Паутина";
    private static final String SHADER_PLASMA = "Плазма";
    private final ModeSetting a = new ModeSetting("Режим прыжка", new String[]{"Только частицы", "Блоковая волна", "Круг"}, "Только частицы");
    private final BooleanSetting b = new BooleanSetting("Показывать от первого лица", true);
    private final SettingGroup j = new SettingGroup("Частицы").a(() -> {
        return this.a.b("Только частицы");
    });
    private final ModeSetting k = new ModeSetting("Тип частиц", new String[]{"Сердце", "Искра", "Снежинка", "Сияние", "Линия", "Доллар", "Лепестки", "Звёздочка", "Блюм"}, "Сердце").a(() -> {
        return this.a.b("Только частицы");
    });
    private final ModeSetting l = new ModeSetting("Режим физики", new String[]{"Реалистичная", "Без коллизий", "Без физики", "Притяжение"}, "Реалистичная").a(() -> {
        return this.a.b("Только частицы");
    });
    private final NumberSetting m = new NumberSetting("Количество частиц", 10.0f, 1.0f, 20.0f, 1.0f).a(() -> {
        return this.a.b("Только частицы");
    });
    private final NumberSetting n = new NumberSetting("Размер частиц", 0.5f, 0.1f, 1.0f, 0.05f).a(() -> {
        return this.a.b("Только частицы");
    });
    private final NumberSetting o = new NumberSetting("Время жизни", 30.0f, 10.0f, 60.0f, 5.0f).a(() -> {
        return this.a.b("Только частицы");
    });
    private final NumberSetting p = new NumberSetting("Сила разлёта", 0.4f, 0.15f, 0.6f, 0.01f).a(() -> {
        return this.a.b("Только частицы");
    });
    private final BooleanSetting q = new BooleanSetting("Цвет клиента", true).a(() -> {
        return this.a.b("Только частицы");
    });
    private final ColorSetting r = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> this.a.b("Только частицы") && !this.q.a());
    private final SettingGroup s = new SettingGroup("Настройки кольца").a(() -> {
        return this.a.b("Блоковая волна");
    });
    private final NumberSetting t = new NumberSetting("Радиус волны", 15.0f, 6.0f, 35.0f, 1.0f).a(() -> {
        return this.a.b("Блоковая волна");
    });
    private final NumberSetting u = new NumberSetting("Скорость волны", 6.0f, 1.0f, 25.0f, 0.5f).a(() -> {
        return this.a.b("Блоковая волна");
    });
    private final NumberSetting v = new NumberSetting("Толщина кольца", 2.5f, 0.5f, 4.0f, 0.1f).a(() -> {
        return this.a.b("Блоковая волна");
    });
    private final BooleanSetting w = new BooleanSetting("Обводка", true).a(() -> {
        return this.a.b("Блоковая волна");
    });
    private final NumberSetting x = new NumberSetting("Толщина линий", 3.0f, 1.0f, 5.0f, 0.5f).a(() -> this.a.b("Блоковая волна") && this.w.a());
    private final BooleanSetting y = new BooleanSetting("Заливка", true).a(() -> {
        int n = 586;
        return this.a.b("Блоковая волна");
    });
    private final ModeSetting z = new ModeSetting("Тип заливки", new String[]{"Обычная", "Шейдер"}, "Обычная").a(() -> this.a.b("Блоковая волна") && this.y.a());
    private final NumberSetting A = new NumberSetting("Прозрачность заливки", 0.8f, 0.1f, 1.0f, 0.05f).a(() -> this.a.b("Блоковая волна") && this.y.a() && this.z.b("Обычная"));
    private final ModeSetting B = new ModeSetting("Шейдер", new String[]{"Небула", "Звёзды", "Паутина", "Плазма"}, "Небула").a(() -> this.a.b("Блоковая волна") && this.y.a() && this.z.b("Шейдер"));
    private final NumberSetting C = new NumberSetting("Скорость шейдера", 1.0f, 0.1f, 3.0f, 0.1f).a(() -> this.a.b("Блоковая волна") && this.y.a() && this.z.b("Шейдер"));
    private final NumberSetting D = new NumberSetting("Прозрачность шейдера", 1.0f, 0.1f, 1.0f, 0.05f).a(() -> this.a.b("Блоковая волна") && this.y.a() && this.z.b("Шейдер"));
    private final BooleanSetting E = new BooleanSetting("Цвет клиента", true).a(() -> {
        return this.a.b("Блоковая волна");
    });
    private final ColorSetting F = new ColorSetting("Кастомный цвет", new Color(255, 0, 0, 255)).a(() -> this.a.b("Блоковая волна") && !this.E.a());
    private final SettingGroup G = new SettingGroup("Настройки круга").a(() -> {
        int n = 887;
        return this.a.b("Круг");
    });
    private final ModeSetting H = new ModeSetting("Текстура", new String[]{"Jump", "Jump2"}, "Jump").a(() -> {
        return this.a.b("Круг");
    });
    private final NumberSetting I = new NumberSetting("Радиус круга", 3.0f, 1.0f, 8.0f, 0.5f).a(() -> {
        return this.a.b("Круг");
    });
    private final NumberSetting J = new NumberSetting("Длительность", 1.5f, 0.5f, 4.0f, 0.1f).a(() -> {
        return this.a.b("Круг");
    });
    private final BooleanSetting K = new BooleanSetting("Цвет клиента", true).a(() -> {
        return this.a.b("Круг");
    });
    private final ColorSetting L = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> this.a.b("Круг") && !this.K.a());
    private final long shaderStartTime = System.currentTimeMillis();
    private final Queue<JumpWave> blockWaves = new LinkedBlockingQueue<JumpWave>();
    private final Queue<AnimatedTextureCircle> textureCircles = new LinkedBlockingQueue<AnimatedTextureCircle>();
    private boolean wasOnGround = true;

    @EventHandler
    public void onTick(ClientTickEvent q8krcw2) {
        if (JumpCircles.c.player == null) {
            return;
        }
        boolean bl = JumpCircles.c.player.isOnGround();
        if (this.wasOnGround && !bl) {
            String string = this.a.d();
            if (JumpCircles.c.options.getPerspective().isFirstPerson() && !this.b.a()) {
                this.wasOnGround = bl;
                return;
            }
            Vec3d vec3d = JumpCircles.c.player.getPos();
            if (string.equals("Только частицы")) {
                this.spawnJumpParticles(vec3d.add(0.0, 0.1, 0.0));
            } else if (string.equals("Блоковая волна")) {
                this.addBlockWave(vec3d);
            } else if (string.equals("Круг")) {
                BlockPos blockPos = BlockPos.ofFloored((double)vec3d.x, (double)(vec3d.y - 0.5), (double)vec3d.z);
                double d = (double)blockPos.getY() + 1.0;
                this.spawnTextureCircle(new Vec3d(vec3d.x, d, vec3d.z));
            }
        }
        this.wasOnGround = bl;
    }

    private Color waveColor() {
        if (this.E.a()) {
            ClientColor byzlib2 = ModuleManager.CLIENT_COLOR;
            return byzlib2.n();
        }
        return this.F.a();
    }

    private void shaderQuad(Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.shaderTriangle(matrix4f, f, f2, f3, 0.0f, 0.0f, f4, f5, f6, 1.0f, 0.0f, f7, f8, f9, 1.0f, 1.0f);
        this.shaderTriangle(matrix4f, f, f2, f3, 0.0f, 0.0f, f7, f8, f9, 1.0f, 1.0f, f10, f11, f12, 0.0f, 1.0f);
    }

    private static void addLine(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        bufferBuilder.vertex(matrix4f, f, f2, f3).color(f7, f8, f9, f10);
        bufferBuilder.vertex(matrix4f, f4, f5, f6).color(f7, f8, f9, f10);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.blockWaves.clear();
        this.textureCircles.clear();
    }

    private String shaderName() {
        switch (this.B.d()) {
            case "Звёзды": {
                return "block_starfield";
            }
            case "Паутина": {
                return "block_cobweb";
            }
            case "Плазма": {
                return "block_plasma";
            }
        }
        return "block_nebula";
    }

    @EventHandler
    public void onJump(LivingEntityJumpEvent casljc2) {
        if (JumpCircles.c.player == null || JumpCircles.c.world == null) {
            return;
        }
        if (casljc2 == null || casljc2.a() != JumpCircles.c.player) {
            return;
        }
        if (JumpCircles.c.options.getPerspective().isFirstPerson() && !this.b.a()) {
            return;
        }
        Vec3d vec3d = JumpCircles.c.player.getPos();
        String string = this.a.d();
        if (string.equals("Только частицы")) {
            this.spawnJumpParticles(vec3d.add(0.0, 0.1, 0.0));
        } else if (string.equals("Блоковая волна")) {
            this.addBlockWave(vec3d);
        } else if (string.equals("Круг")) {
            Vec3d vec3d2 = JumpCircles.c.player.getPos();
            BlockPos blockPos = BlockPos.ofFloored((double)vec3d2.x, (double)(vec3d2.y - 0.5), (double)vec3d2.z);
            double d = (double)blockPos.getY() + 1.0;
            this.spawnTextureCircle(new Vec3d(vec3d2.x, d, vec3d2.z));
        }
    }

    private boolean isTopVisible(BlockPos blockPos) {
        BlockState blockState = JumpCircles.c.world.getBlockState(blockPos.up());
        return blockState.isAir() || !blockState.isOpaqueFullCube();
    }

    private void renderTextureCircles(WorldRenderPostEvent kvprd92) {
        if (this.textureCircles.isEmpty()) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        Vec3d vec3d = JumpCircles.c.getEntityRenderDispatcher().camera.getPos();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        this.textureCircles.removeIf(ozu7bc2 -> {
            ozu7bc2.update();
            if (ozu7bc2.isFinished()) {
                return true;
            }
            float f = ozu7bc2.getAlpha();
            if (f < 0.01f) {
                return true;
            }
            float f2 = (float)(ozu7bc2.position.x - vec3d.x);
            float f3 = (float)(ozu7bc2.position.y - vec3d.y) + 0.05f;
            float f4 = (float)(ozu7bc2.position.z - vec3d.z);
            float f5 = ozu7bc2.size / 2.0f;
            RenderSystem.setShaderTexture((int)0, (Identifier)ozu7bc2.texture);
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            float f6 = (float)ozu7bc2.tintColor.getRed() / 255.0f;
            float f7 = (float)ozu7bc2.tintColor.getGreen() / 255.0f;
            float f8 = (float)ozu7bc2.tintColor.getBlue() / 255.0f;
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(matrix4f, f2 - f5, f3, f4 - f5).texture(0.0f, 0.0f).color(f6, f7, f8, f);
            bufferBuilder.vertex(matrix4f, f2 - f5, f3, f4 + f5).texture(0.0f, 1.0f).color(f6, f7, f8, f);
            bufferBuilder.vertex(matrix4f, f2 + f5, f3, f4 + f5).texture(1.0f, 1.0f).color(f6, f7, f8, f);
            bufferBuilder.vertex(matrix4f, f2 + f5, f3, f4 - f5).texture(1.0f, 0.0f).color(f6, f7, f8, f);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
            return false;
        });
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc((int)513);
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void drawShaderFill(MatrixStack matrixStack, Box box, Color color, float f) {
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find(this.shaderName());
        if (!optional.isPresent() || !optional.get().b()) {
            RenderSystem.disableCull();
            RenderSystem.depthMask((boolean)false);
            this.drawTopFill(matrixStack, box, color, Math.min(1.0f, this.A.a() * f));
            RenderSystem.depthMask((boolean)true);
            RenderSystem.enableCull();
            return;
        }
        ShaderProgram s5pbng2 = optional.get();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float f2 = (float)((System.currentTimeMillis() - this.shaderStartTime) % 100000L) / 1000.0f * this.C.a();
        float f3 = Math.min(1.0f, this.D.a() * f * 1.5f);
        RenderSystem.disableCull();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.blendFunc((int)770, (int)1);
        s5pbng2.d();
        try {
            s5pbng2.a("time", f2);
            s5pbng2.a("screenSize", (float)c.getWindow().getFramebufferWidth(), (float)c.getWindow().getFramebufferHeight());
            s5pbng2.a("baseColo྄", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, 1.0f);
            s5pbng2.a("alpha", f3);
            float f4 = (float)box.minX;
            float f5 = (float)box.minY;
            float f6 = (float)box.minZ;
            float f7 = (float)box.maxX;
            float f8 = (float)box.maxY;
            float f9 = (float)box.maxZ;
            this.shaderQuad(matrix4f, f4, f5, f9, f7, f5, f9, f7, f8, f9, f4, f8, f9);
            this.shaderQuad(matrix4f, f7, f5, f6, f4, f5, f6, f4, f8, f6, f7, f8, f6);
            this.shaderQuad(matrix4f, f4, f5, f6, f4, f5, f9, f4, f8, f9, f4, f8, f6);
            this.shaderQuad(matrix4f, f7, f5, f9, f7, f5, f6, f7, f8, f6, f7, f8, f9);
            this.shaderQuad(matrix4f, f4, f8, f9, f7, f8, f9, f7, f8, f6, f4, f8, f6);
            this.shaderQuad(matrix4f, f4, f5, f6, f7, f5, f6, f7, f5, f9, f4, f5, f9);
        }
        finally {
            s5pbng2.e();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask((boolean)true);
            RenderSystem.enableCull();
        }
    }

    @EventHandler
    public void onWorldRender(WorldRenderPostEvent kvprd92) {
        if (JumpCircles.c.world == null) {
            return;
        }
        this.renderBlockWave(kvprd92);
        this.renderTextureCircles(kvprd92);
    }

    private static void appendTopOutline(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        JumpCircles.addLine(bufferBuilder, matrix4f, f, f5, f3, f4, f5, f3, f7, f8, f9, f10);
        JumpCircles.addLine(bufferBuilder, matrix4f, f4, f5, f3, f4, f5, f6, f7, f8, f9, f10);
        JumpCircles.addLine(bufferBuilder, matrix4f, f4, f5, f6, f, f5, f6, f7, f8, f9, f10);
        JumpCircles.addLine(bufferBuilder, matrix4f, f, f5, f6, f, f5, f3, f7, f8, f9, f10);
    }

    private void spawnJumpParticles(Vec3d vec3d) {
        int n = ColorUtils.a(this.particleColor().getRGB());
        int n2 = Math.max(1, this.m.b());
        int n3 = Math.max(1, this.o.b());
        int n4 = Math.max(1, n3 / 5);
        float f = Math.max(0.01f, this.n.a());
        double d = Math.max(0.005, (double)this.p.a());
        Identifier identifier = this.particleTexture();
        String string = this.l.d();
        for (int i = 0; i < n2; ++i) {
            Vec3d vec3d2 = new Vec3d(ThreadLocalRandom.current().nextDouble(-1.0, 1.0), ThreadLocalRandom.current().nextDouble(0.15, 1.0), ThreadLocalRandom.current().nextDouble(-1.0, 1.0));
            if (vec3d2.lengthSquared() > 0.0) {
                vec3d2 = vec3d2.normalize().multiply(ThreadLocalRandom.current().nextDouble(0.005, d));
            }
            int n5 = Math.max(1, n3 + ThreadLocalRandom.current().nextInt(-n4, n4 + 1));
            float f2 = Math.max(0.01f, f + (ThreadLocalRandom.current().nextFloat() - 0.5f) * 0.08f);
            HudServices.PARTICLES.spawn(vec3d, vec3d2, n5, f2, identifier, n, string);
        }
    }

    private void shaderTriangle(Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15) {
        Vector4f vector4f = matrix4f.transform(new Vector4f(f, f2, f3, 1.0f));
        Vector4f vector4f2 = matrix4f.transform(new Vector4f(f6, f7, f8, 1.0f));
        Vector4f vector4f3 = matrix4f.transform(new Vector4f(f11, f12, f13, 1.0f));
        float[] fArray = new float[]{vector4f.x, vector4f.y, vector4f.z, f4, f5, vector4f2.x, vector4f2.y, vector4f2.z, f9, f10, vector4f3.x, vector4f3.y, vector4f3.z, f14, f15};
        ShaderProgram.a(fArray, 3);
    }

    private static void appendTopQuad(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        bufferBuilder.vertex(matrix4f, f, f5, f3).color(f7, f8, f9, f10);
        bufferBuilder.vertex(matrix4f, f, f5, f6).color(f7, f8, f9, f10);
        bufferBuilder.vertex(matrix4f, f4, f5, f6).color(f7, f8, f9, f10);
        bufferBuilder.vertex(matrix4f, f4, f5, f3).color(f7, f8, f9, f10);
    }

    private void addBlockWave(Vec3d vec3d) {
        float f = this.t.a();
        float f2 = f / Math.max(0.05f, this.u.a());
        this.blockWaves.add(new JumpWave(this, vec3d, f, f2));
    }

    private void renderBlockWave(WorldRenderPostEvent kvprd92) {
        if (JumpCircles.c.world == null || this.blockWaves.isEmpty()) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        Vec3d vec3d = JumpCircles.c.getEntityRenderDispatcher().camera.getPos();
        Color color = this.waveColor();
        float f = (float)color.getRed() / 255.0f;
        float f2 = (float)color.getGreen() / 255.0f;
        float f3 = (float)color.getBlue() / 255.0f;
        float f4 = this.v.a();
        float f5 = this.A.a();
        boolean bl = this.y.a() && this.z.b("Обычная");
        boolean bl2 = this.y.a() && this.z.b("Шейдер");
        boolean bl3 = this.w.a();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc((int)513);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        this.blockWaves.removeIf(k82gnv2 -> {
            k82gnv2.update();
            if (k82gnv2.isFinished()) {
                return true;
            }
            float f6 = k82gnv2.radius();
            if (f6 < 0.5f) {
                return false;
            }
            float f7 = Math.max(0.0f, f6 - f4);
            float f8 = f7 * f7;
            float f9 = f6 * f6;
            float f10 = Math.min(1.0f, k82gnv2.alpha() * 1.5f);
            BlockPos blockPos = BlockPos.ofFloored((Position)k82gnv2.position);
            int n = (int)Math.ceil(f6) + 2;
            ArrayList<float[]> arrayList = bl ? new ArrayList<float[]>() : null;
            ArrayList<float[]> arrayList2 = bl3 ? new ArrayList<float[]>() : null;
            ArrayList<BoxFadeSample> arrayList3 = bl2 ? new ArrayList<BoxFadeSample>() : null;
            for (int i = -n; i <= n; ++i) {
                for (int j = -n; j <= n; ++j) {
                    double d;
                    double d2 = (double)(blockPos.getX() + i) + 0.5 - k82gnv2.position.x;
                    double d3 = d2 * d2 + (d = (double)(blockPos.getZ() + j) + 0.5 - k82gnv2.position.z) * d;
                    if (d3 > (double)f9) continue;
                    double d4 = Math.sqrt(Math.max(0.0, (double)f9 - d3));
                    int n2 = (int)Math.floor(-d4);
                    int n3 = (int)Math.ceil(d4);
                    for (int k = n2; k <= n3; ++k) {
                        BlockPos blockPos2;
                        BlockState blockState;
                        double d5 = (double)(blockPos.getY() + k) + 0.5 - k82gnv2.position.y;
                        double d6 = d3 + d5 * d5;
                        if (d6 < (double)f8 || d6 > (double)f9 || (blockState = JumpCircles.c.world.getBlockState(blockPos2 = blockPos.add(i, k, j))).isAir() || !blockState.isOpaqueFullCube() || !this.isTopVisible(blockPos2)) continue;
                        try {
                            VoxelShape voxelShape = blockState.getOutlineShape((BlockView)JumpCircles.c.world, blockPos2);
                            if (voxelShape.isEmpty()) continue;
                            Box box = voxelShape.getBoundingBox().expand(0.01).offset((double)blockPos2.getX() - vec3d.x, (double)blockPos2.getY() - vec3d.y, (double)blockPos2.getZ() - vec3d.z);
                            float f11 = f4 > 0.0f ? (float)((Math.sqrt(d6) - (double)f7) / (double)f4) : 0.5f;
                            float f12 = Math.max(0.3f, f10 * (1.0f - Math.abs(f11 - 0.5f) * 1.5f));
                            if (arrayList != null) {
                                arrayList.add(new float[]{(float)box.minX, (float)box.minY, (float)box.minZ, (float)box.maxX, (float)box.maxY, (float)box.maxZ, f5 * f12});
                            }
                            if (arrayList2 != null) {
                                arrayList2.add(new float[]{(float)box.minX, (float)box.minY, (float)box.minZ, (float)box.maxX, (float)box.maxY, (float)box.maxZ, f12 * 1.2f});
                            }
                            if (arrayList3 == null) continue;
                            arrayList3.add(new BoxFadeSample(box, f12));
                            continue;
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                }
            }
            if (arrayList != null && !arrayList.isEmpty()) {
                RenderSystem.disableCull();
                RenderSystem.depthMask((boolean)false);
                RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
                BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                for (float[] fArray : arrayList) {
                    JumpCircles.appendTopQuad(bufferBuilder, matrix4f, fArray[0], fArray[1], fArray[2], fArray[3], fArray[4], fArray[5], f, f2, f3, fArray[6]);
                }
                BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
                RenderSystem.depthMask((boolean)true);
                RenderSystem.enableCull();
            }
            if (arrayList3 != null && !arrayList3.isEmpty()) {
                for (BoxFadeSample qvbvlj2 : arrayList3) {
                    this.drawShaderFill(matrixStack, qvbvlj2.box, color, qvbvlj2.alpha);
                }
            }
            if (arrayList2 != null && !arrayList2.isEmpty()) {
                RenderSystem.lineWidth((float)this.x.a());
                RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
                for (float[] fArray : arrayList2) {
                    JumpCircles.appendTopOutline(bufferBuilder, matrix4f, fArray[0], fArray[1], fArray[2], fArray[3], fArray[4], fArray[5], f, f2, f3, fArray[6]);
                }
                BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
                GL11.glDisable((int)2848);
                RenderSystem.lineWidth((float)1.0f);
            }
            return false;
        });
        RenderSystem.depthFunc((int)513);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    private Color particleColor() {
        if (this.q.a()) {
            ClientColor byzlib2 = ModuleManager.CLIENT_COLOR;
            return byzlib2.n();
        }
        return this.r.a();
    }

    private void spawnTextureCircle(Vec3d vec3d) {
        float f = this.I.a();
        Identifier identifier = this.circleTexture();
        Color color = this.circleColor();
        this.textureCircles.add(new AnimatedTextureCircle(vec3d, identifier, f, 0.0f, color));
    }

    private Identifier particleTexture() {
        switch (this.k.d()) {
            case "Искра": {
                return Identifier.of((String)"haron", (String)"textures/particle/sparkle.png");
            }
            case "Снежинка": {
                return Identifier.of((String)"haron", (String)"textures/particle/snowflake.png");
            }
            case "Сияние": {
                return Identifier.of((String)"haron", (String)"textures/particle/glow.png");
            }
            case "Линия": {
                return Identifier.of((String)"haron", (String)"textu༅es/particle/line.png");
            }
            case "Доллар": {
                return Identifier.of((String)"haron", (String)"textures/particle/dollar.png");
            }
            case "Лепестки": {
                return Identifier.of((String)"haron", (String)"textures/particle/petals.png");
            }
            case "Звёздочка": {
                return Identifier.of((String)"haron", (String)"textures/star.png");
            }
            case "Блюм": {
                return Identifier.of((String)"haron", (String)"textures/bloom.png");
            }
        }
        return Identifier.of((String)"haron", (String)"textures/particle/heart.png");
    }

    private Identifier circleTexture() {
        if (this.H.b("Jump2")) {
            return Identifier.of((String)"haron", (String)"textures/jump2.png");
        }
        return Identifier.of((String)"haron", (String)"texturྒྷs/jump.png");
    }

    private Color circleColor() {
        if (this.K.a()) {
            ClientColor byzlib2 = ModuleManager.CLIENT_COLOR;
            return byzlib2.n();
        }
        return this.L.a();
    }

    private void drawTopFill(MatrixStack matrixStack, Box box, Color color, float f) {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        JumpCircles.appendTopQuad(bufferBuilder, matrix4f, (float)box.minX, (float)box.minY, (float)box.minZ, (float)box.maxX, (float)box.maxY, (float)box.maxZ, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, f);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    @Override
    public void f() {
        super.f();
        this.blockWaves.clear();
        this.textureCircles.clear();
        this.wasOnGround = true;
    }
}

