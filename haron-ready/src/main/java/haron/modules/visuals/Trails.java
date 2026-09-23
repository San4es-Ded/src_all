package haron.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.core.BooleanCoercion;
import haron.events.WorldRenderPostEvent;
import haron.events.ClientTickEvent;
import haron.events.RenderTickEvent;
import haron.hud.core.HudServices;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.visuals.TrailPoint;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(a="Trails", b="Отображает след игрока", c=ModuleCategory.VISUALS)
public class Trails
extends HaronModule {
    private double previousX;
    private double previousY;
    private double previousZ;
    private final ModeSetting movementMode = new ModeSetting("Режим ходьбы", new String[]{"Только след", "Только частицы", "След + частицы"}, "Только след");
    private final BooleanSetting showInFirstPerson = new BooleanSetting("Показывать от первого лица", false);
    private final SettingGroup trailSettings = new SettingGroup("След").a(() -> {
        return this.movementMode.b("След + частицы");
    });
    private final ModeSetting trailStyle = new ModeSetting("Тип следа", new String[]{"Сплошной", "Пунктир", "Затухающий"}, "Сплошной").a(() -> {
        if (!this.movementMode.b("Только след") && !this.movementMode.b("След + частицы")) {
            return false;
        }
        return true;
    });
    private final NumberSetting trailLength = new NumberSetting("Длина следа", 5.0f, 1.0f, 20.0f, 1.0f).a(() -> this.movementMode.b("Только след") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting trailWidth = new NumberSetting("Ширина следа", 0.1f, 0.05f, 0.5f, 0.01f).a(() -> this.movementMode.b("Только след") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final BooleanSetting useClientTrailColor = new BooleanSetting("Цвет клиента", true).a(() -> this.movementMode.b("Только след") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final ColorSetting customTrailColor = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> (this.movementMode.b("Только след") || this.movementMode.b("След + частицы")) && !this.useClientTrailColor.a() ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final SettingGroup particleSettings = new SettingGroup("Частицы").a(() -> {
        return this.movementMode.b("След + частицы");
    });
    private final ModeSetting particleSpawnMode = new ModeSetting("Режим частиц", new String[]{"Под ногами", "По телу"}, "Под ногами").a(() -> this.movementMode.b("Только частицы") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final ModeSetting particleType = new ModeSetting("Тип частиц", new String[]{"Сердце", "Искра", "Снежинка", "Сияние", "Линия", "Доллар"}, "Сердце").a(() -> this.movementMode.b("Только частицы") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final ModeSetting particlePhysics = new ModeSetting("Режим физики", new String[]{"Реалистичная", "Без коллизий", "Без физики", "Притяжение"}, "Реалистичная").a(() -> this.movementMode.b("Только частицы") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting particleCount = new NumberSetting("Количество частиц", 4.0f, 1.0f, 20.0f, 1.0f).a(() -> this.movementMode.b("Только частицы") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting particleSize = new NumberSetting("Размер частиц", 0.3f, 0.1f, 0.75f, 0.05f).a(() -> this.movementMode.b("Только частицы") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting particleLifetimeTicks = new NumberSetting("Время жизни", 20.0f, 10.0f, 50.0f, 5.0f).a(() -> this.movementMode.b("Только частицы") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting particleSpread = new NumberSetting("Сила разлёта", 0.3f, 0.15f, 0.35f, 0.01f).a(() -> this.movementMode.b("Только частицы") || this.movementMode.b("След + частицы") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final BooleanSetting useClientParticleColor = new BooleanSetting("Цвет клиента", BooleanCoercion.from(-950111800)).a(() -> {
        if (!this.movementMode.b("Только частицы") && !this.movementMode.b("След + частицы")) {
            return false;
        }
        return true;
    });
    private final ColorSetting customParticleColor = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> (this.movementMode.b("Только частицы") || this.movementMode.b("След + частицы")) && !this.useClientParticleColor.a() ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final Deque<TrailPoint> trailPoints = new ArrayDeque<TrailPoint>();
    private boolean positionInitialized = false;

    private Color b(int n) {
        Color color = this.trailColor();
        Color color2 = this.trailColor();
        float f = Math.min(1.0f, (float)n / 20.0f);
        return new Color((int)((float)color.getRed() * (1.0f - f) + (float)color2.getRed() * f), (int)((float)color.getGreen() * (1.0f - f) + (float)color2.getGreen() * f), (int)((float)color.getBlue() * (1.0f - f) + (float)color2.getBlue() * f));
    }

    private void b(Matrix4f matrix4f, float f, List<TrailPoint> list, Vec3d vec3d) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
        int n = 0;
        while (n - -2 - 1 < list.size()) {
            this.a(bufferBuilder, matrix4f, list.get(n), list.get(n - -2 - 1), vec3d, f, n, list.size());
            n += 2;
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    private void c(Matrix4f matrix4f, float f, List<TrailPoint> list, Vec3d vec3d) {
        this.a(matrix4f, f, list, vec3d, true);
        this.a(matrix4f, list, vec3d, true);
        this.a(matrix4f, this.a(list, f), vec3d, true);
    }

    private Color trailColor() {
        return !this.useClientTrailColor.a() ? this.customTrailColor.a() : ModuleManager.CLIENT_COLOR.n();
    }

    @Override
    public void f() {
        super.f();
        this.trailPoints.clear();
        this.positionInitialized = false;
    }

    private void a(Vec3d vec3d, double d, int n) {
        int particleColor = ColorUtils.ensureOpaque(n);
        int particleCount = this.particleCount.b();
        int lifetime = this.particleLifetimeTicks.b();
        int minimumLifetime = Math.max(1, lifetime - 10);
        int maximumLifetime = lifetime + 10;
        float configuredSize = this.particleSize.a();
        float minimumSize = configuredSize - 0.05f;
        float maximumSize = configuredSize + 0.05f;
        double spreadStrength = this.particleSpread.a();
        String physicsMode = this.particlePhysics.d();
        for (int particleIndex = 0; particleIndex < particleCount; ++particleIndex) {
            int randomizedLifetime = ThreadLocalRandom.current().nextInt(minimumLifetime, maximumLifetime);
            float randomizedSize = minimumSize + ThreadLocalRandom.current().nextFloat() * (maximumSize - minimumSize);
            Vec3d velocity = new Vec3d(ThreadLocalRandom.current().nextDouble(-d, d), ThreadLocalRandom.current().nextDouble(0.0, d), ThreadLocalRandom.current().nextDouble(-d, d));
            if (velocity.lengthSquared() > 0.0) {
                velocity = velocity.normalize().multiply(ThreadLocalRandom.current().nextDouble(0.005, spreadStrength));
            }
            HudServices.PARTICLES.spawn(vec3d, velocity, randomizedLifetime, randomizedSize, this.particleTexture(), particleColor, physicsMode);
        }
    }

    private void a(Matrix4f matrix4f, float f, List<TrailPoint> list, Vec3d vec3d) {
        this.a(matrix4f, f, list, vec3d, false);
        this.a(matrix4f, list, vec3d, false);
        this.a(matrix4f, this.a(list, f), vec3d, false);
    }

    private void a(Matrix4f matrix4f, List<TrailPoint> list, Vec3d vec3d, boolean bl) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < list.size(); ++i) {
            this.a(bufferBuilder, matrix4f, list.get(i).position(), vec3d, this.b(i), this.a(i, list.size(), bl));
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    private void a(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, Vec3d vec3d, Vec3d vec3d2, Color color, float f2) {
        this.a(bufferBuilder, matrix4f, vec3d, vec3d2, color, f2);
        this.a(bufferBuilder, matrix4f, vec3d.add(0.0, (double)f, 0.0), vec3d2, color, f2);
    }

    @EventHandler
    public void a(WorldRenderPostEvent kvprd92) {
        String string = this.movementMode.d();
        if (!string.equals("Только след") && !string.equals("След + частицы") || Trails.c.world == null || Trails.c.player == null || this.trailPoints.size() < 2) {
            return;
        }
        if (!Trails.c.options.getPerspective().isFirstPerson() || ((Boolean)this.showInFirstPerson.k()).booleanValue()) {
            MatrixStack matrixStack = kvprd92.a();
            matrixStack.push();
            Vec3d vec3d = Trails.c.gameRenderer.getCamera().getPos();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask((boolean)false);
            RenderSystem.lineWidth((float)(this.trailWidth.a() * 3.0f));
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            ArrayList<TrailPoint> arrayList = new ArrayList<TrailPoint>(this.trailPoints);
            String string2 = this.trailStyle.d();
            if ("Пунктир".equals(string2)) {
                this.b(matrix4f, Trails.c.player.getHeight(), arrayList, vec3d);
            } else if ("Затухающий".equals(string2)) {
                this.c(matrix4f, Trails.c.player.getHeight(), arrayList, vec3d);
            } else {
                this.a(matrix4f, Trails.c.player.getHeight(), arrayList, vec3d);
            }
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.depthMask((boolean)true);
            RenderSystem.lineWidth((float)1.0f);
            matrixStack.pop();
        }
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        String string = this.movementMode.d();
        if ((string.equals("Только частицы") || string.equals("След + частицы")) && Trails.c.player != null && (!Trails.c.options.getPerspective().isFirstPerson() || ((Boolean)this.showInFirstPerson.k()).booleanValue())) {
            double d = Trails.c.player.getX();
            double d2 = Trails.c.player.getY();
            double d3 = Trails.c.player.getZ();
            if (!this.positionInitialized) {
                this.previousX = d;
                this.previousY = d2;
                this.previousZ = d3;
                this.positionInitialized = true;
                return;
            }
            double d4 = d - this.previousX;
            double d5 = d2 - this.previousY;
            double d6 = d3 - this.previousZ;
            double d7 = Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
            this.previousX = d;
            this.previousY = d2;
            this.previousZ = d3;
            if (d7 > 0.01) {
                String string2 = this.particleSpawnMode.d();
                if (string2.equals("Под ногами")) {
                    this.a(Trails.c.player.getPos(), 0.1, this.particleColor().getRGB());
                    return;
                }
                if (string2.equals("По телу")) {
                    double d8 = Trails.c.player.getHeight();
                    for (int i = 0; i < 5; ++i) {
                        this.a(Trails.c.player.getPos().add(0.0, d8 / 4.0 * (double)i, 0.0), 0.08, this.particleColor().getRGB());
                    }
                }
            }
        }
    }

    @EventHandler
    public void a(RenderTickEvent zv7i6c2) {
        ClientPlayerEntity clientPlayerEntity;
        String string = this.movementMode.d();
        if (!string.equals("Только след") && !string.equals("След + частицы") || (clientPlayerEntity = Trails.c.player) == null || Trails.c.world == null) {
            return;
        }
        float f = zv7i6c2.a();
        this.trailPoints.removeIf(point -> point.isOlderThan(500));
        Vec3d vec3d = new Vec3d(MathHelper.lerp((double)f, (double)clientPlayerEntity.prevX, (double)clientPlayerEntity.getX()), MathHelper.lerp((double)f, (double)clientPlayerEntity.prevY, (double)clientPlayerEntity.getY()), MathHelper.lerp((double)f, (double)clientPlayerEntity.prevZ, (double)clientPlayerEntity.getZ()));
        if (this.trailPoints.isEmpty() || this.trailPoints.getLast().position().squaredDistanceTo(vec3d) > 1.0E-4) {
            this.trailPoints.addLast(new TrailPoint(vec3d));
            while ((float)this.trailPoints.size() > this.trailLength.a() * 10.0f) {
                this.trailPoints.pollFirst();
            }
        }
    }

    private void a(BufferBuilder bufferBuilder, Matrix4f matrix4f, Vec3d vec3d, Vec3d vec3d2, Color color, float f) {
        bufferBuilder.vertex(matrix4f, (float)(vec3d.x - vec3d2.x), (float)(vec3d.y - vec3d2.y), (float)(vec3d.z - vec3d2.z)).color(color.getRed(), color.getGreen(), color.getBlue(), (int)(f * 255.0f));
    }

    private float a(int n, int n2, boolean bl) {
        float f = n2 <= 0 ? 0.0f : (float)n / (float)n2;
        return !bl ? f : f * f;
    }

    private List<TrailPoint> a(List<TrailPoint> list, float f) {
        ArrayList<TrailPoint> arrayList = new ArrayList<TrailPoint>(list.size());
        list.forEach(point -> arrayList.add(new TrailPoint(point.position().add(0.0, f, 0.0), point.createdAtMs())));
        return arrayList;
    }

    private void a(BufferBuilder bufferBuilder, Matrix4f matrix4f, TrailPoint firstPoint, TrailPoint secondPoint, Vec3d vec3d, float f, int n, int n2) {
        float f2 = this.a(n, n2, false);
        Color color = this.b(n);
        this.a(bufferBuilder, matrix4f, firstPoint.position(), vec3d, color, f2);
        this.a(bufferBuilder, matrix4f, secondPoint.position(), vec3d, color, f2);
        this.a(bufferBuilder, matrix4f, firstPoint.position().add(0.0, f, 0.0), vec3d, color, f2);
        this.a(bufferBuilder, matrix4f, secondPoint.position().add(0.0, f, 0.0), vec3d, color, f2);
    }

    private void a(Matrix4f matrix4f, float f, List<TrailPoint> list, Vec3d vec3d, boolean bl) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < list.size(); ++i) {
            this.a(bufferBuilder, matrix4f, f, list.get(i).position(), vec3d, this.b(i), this.a(i, list.size(), bl));
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    private Color particleColor() {
        return !this.useClientParticleColor.a() ? this.customParticleColor.a() : ModuleManager.CLIENT_COLOR.n();
    }

    private Identifier particleTexture() {
        String textureName = switch (this.particleType.d()) {
            case "Искра" -> "sparkle";
            case "Снежинка" -> "snowflake";
            case "Сияние" -> "glow";
            case "Линия" -> "line";
            case "Доллар" -> "dollar";
            default -> "heart";
        };
        return Identifier.of("haron", "textures/particle/" + textureName + ".png");
    }
}
