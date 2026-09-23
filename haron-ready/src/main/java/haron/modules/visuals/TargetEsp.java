package haron.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.entity.EntityInterpolation;
import haron.events.WorldRenderPostEvent;
import haron.hud.core.HudServices;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.hud.ClientColor;
import haron.render.shader.DefaultShaders;
import haron.render.shader.ShaderProgram;
import haron.render.world.WorldRenderUtils;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

@ModuleInfo(a="Target ESP", b="Отображает ESP вокруг цели", c=ModuleCategory.VISUALS)
public class TargetEsp
extends HaronModule {
    private final ModeSetting mode = new ModeSetting("Режим", new String[]{"Призраки", "Круг", "Квадратик", "Орбита"}, "Орбита");
    private final SettingGroup ghostSettings = new SettingGroup("Призраки").a(() -> {
        return this.mode.b("Призраки");
    });
    private final NumberSetting ghostSpeed = new NumberSetting("Скорость анимации", 1.5f, 0.5f, 5.0f, 0.1f).a(() -> {
        return this.mode.b("Призраки");
    });
    private final NumberSetting ghostParticleSize = new NumberSetting("Размер частиц", 0.25f, 0.05f, 0.5f, 0.01f).a(() -> {
        return this.mode.b("Призраки");
    });
    private final NumberSetting ghostCount = new NumberSetting("Количество призраков", 4.0f, 2.0f, 6.0f, 1.0f).a(() -> {
        return this.mode.b("Призраки");
    });
    private final SettingGroup circleSettings = new SettingGroup("Круг").a(() -> {
        return this.mode.b("Круг");
    });
    private final NumberSetting circleSpeed = new NumberSetting("Скорость анимации", 1.5f, 0.5f, 5.0f, 0.1f).a(() -> {
        return this.mode.b("Круг");
    });
    private final SettingGroup squareSettings = new SettingGroup("Квадратик").a(() -> {
        return this.mode.b("Квадратик");
    });
    private final NumberSetting squareSpeed = new NumberSetting("Скорость анимации", 2.5f, 0.5f, 5.0f, 0.1f).a(() -> {
        return this.mode.b("Квадратик");
    });
    private final NumberSetting squareSize = new NumberSetting("Размер квадратика", 1.4f, 0.5f, 2.0f, 0.1f).a(() -> {
        return this.mode.b("Квадратик");
    });
    private final SettingGroup orbitSettings = new SettingGroup("Орбита").a(() -> {
        int n = 422;
        return this.mode.b("Орбита");
    });
    private final ModeSetting orbitShape = new ModeSetting("Форма", new String[]{"Стрелки", "Ромбы", "Кубы"}, "Стрелки").a(() -> {
        return this.mode.b("Орбита");
    });
    private final NumberSetting orbitSpeed = new NumberSetting("Скорость анимации", 1.5f, 0.5f, 5.0f, 0.1f).a(() -> {
        return this.mode.b("Орбита");
    });
    private final NumberSetting figuresPerRing = new NumberSetting("Фигур по кругу", 3.0f, 2.0f, 8.0f, 1.0f).a(() -> {
        return this.mode.b("Орбита");
    });
    private final NumberSetting orbitLayerCount = new NumberSetting("Слоёв по высоте", 3.0f, 2.0f, 5.0f, 1.0f).a(() -> {
        return this.mode.b("Орбита");
    });
    private final NumberSetting orbitVerticalSpread = new NumberSetting("Отступ между слоями", 1.0f, 0.3f, 2.0f, 0.05f).a(() -> {
        int n = 249;
        return this.mode.b("Орбита");
    });
    private final NumberSetting orbitRadius = new NumberSetting("Дистанция", 0.9f, 0.5f, 2.0f, 0.05f).a(() -> {
        return this.mode.b("Орбита");
    });
    private final NumberSetting orbitFigureSize = new NumberSetting("Размер фигур", 0.2f, 0.08f, 0.4f, 0.01f).a(() -> {
        return this.mode.b("Орбита");
    });
    private final BooleanSetting orbitRotationEnabled = new BooleanSetting("Вращение", true).a(() -> {
        return this.mode.b("Орбита");
    });
    private final BooleanSetting orbitShaderEnabled = new BooleanSetting("Шейдер", true).a(() -> {
        return this.mode.b("Орбита");
    });
    private final ModeSetting orbitShaderType = new ModeSetting("Тип шейдера", new String[]{"Небула", "Звёзды", "Паутина", "Плазма"}, "Небула").a(() -> this.mode.b("Орбита") && this.orbitShaderEnabled.a());
    private final BooleanSetting orbitGlowEnabled = new BooleanSetting("Свечение", true).a(() -> {
        return this.mode.b("Орбита");
    });
    private final NumberSetting orbitGlowSize = new NumberSetting("Размер свечения", 0.85f, 0.1f, 1.0f, 0.05f).a(() -> BooleanCoercion.from(this.mode.b("Орбита") && this.orbitGlowEnabled.a() ? 1 : 0));
    private final NumberSetting orbitGlowOpacity = new NumberSetting("Прозрачность свечения", 0.25f, 0.1f, 1.0f, 0.05f).a(() -> BooleanCoercion.from(this.mode.b("Орбита") && this.orbitGlowEnabled.a() ? 1 : 0));
    private final SettingGroup colorSettings = new SettingGroup("Цвет");
    private final BooleanSetting useClientColor = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting customColor = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> {
        return BooleanCoercion.from(this.useClientColor.a() ? 0 : 1);
    });
    private final SettingGroup hitSettings = new SettingGroup("При ударе");
    private final BooleanSetting hitEffectEnabled = new BooleanSetting("Включить", BooleanCoercion.from(-1673835725));
    private final BooleanSetting hitColorEnabled = new BooleanSetting("Изменять цвет", true).a(() -> {
        return this.hitEffectEnabled.a();
    });
    private final ColorSetting hitColor = new ColorSetting("Цвет урона", new Color(255, 50, 50)).a(() -> BooleanCoercion.from(this.hitEffectEnabled.a() && this.hitColorEnabled.a() ? 1 : 0));
    private final BooleanSetting hitSpeedEnabled = new BooleanSetting("Ускорять анимацию", true).a(() -> {
        int n = 978;
        return this.hitEffectEnabled.a();
    });
    private final NumberSetting hitSpeedMultiplier = new NumberSetting("Множитель ускорения", 3.0f, 1.2f, 5.0f, 0.1f).a(() -> BooleanCoercion.from(this.hitEffectEnabled.a() && this.hitSpeedEnabled.a() ? 1 : 0));
    private final NumberSetting hitEffectDuration = new NumberSetting("Длительность эффекта", 0.8f, 0.3f, 2.0f, 0.1f).a(() -> BooleanCoercion.from(this.hitEffectEnabled.a() && this.hitSpeedEnabled.a() ? 1 : 0));
    private final Map<LivingEntity, AnimatedValue> visibilityAnimations = new HashMap<LivingEntity, AnimatedValue>();
    private final Identifier targetTexture = Identifier.of((String)"haron", (String)"textures/target.png");
    private final BooleanSetting targetPlayers = new BooleanSetting("Игроки", true);
    private final BooleanSetting targetMobs = new BooleanSetting("Мобы", true);
    private final Map<LivingEntity, Integer> previousHurtTimes = new HashMap<LivingEntity, Integer>();
    private final Map<LivingEntity, Long> hitTimestamps = new HashMap<LivingEntity, Long>();
    private final Map<LivingEntity, Double> rotationDegrees = new HashMap<LivingEntity, Double>();
    private final Map<LivingEntity, Double> animationPhase = new HashMap<LivingEntity, Double>();
    private long lastFrameTimeMs = System.currentTimeMillis();

    private boolean matchesTargetType(LivingEntity livingEntity) {
        if (livingEntity == null || livingEntity == TargetEsp.c.player) {
            return false;
        }
        return livingEntity instanceof PlayerEntity ? this.targetPlayers.a() : this.targetMobs.a();
    }

    private Color getRenderColor(LivingEntity livingEntity) {
        if (this.hitEffectEnabled.a() && this.hitColorEnabled.a() && livingEntity != null && livingEntity.hurtTime > 0) {
            float f = (float)livingEntity.hurtTime / 10.0f;
            Color color = this.baseColor();
            Color color2 = this.hitColor.a();
            int n = (int)((float)color.getRed() + (float)(color2.getRed() - color.getRed()) * f);
            int n2 = (int)((float)color.getGreen() + (float)(color2.getGreen() - color.getGreen()) * f);
            int n3 = (int)((float)color.getBlue() + (float)(color2.getBlue() - color.getBlue()) * f);
            return new Color(Math.max(0, Math.min(255, n)), Math.max(0, Math.min(255, n2)), Math.max(0, Math.min(255, n3)));
        }
        return this.baseColor();
    }

    private void renderGhosts(WorldRenderPostEvent kvprd92, LivingEntity livingEntity, float f) {
        if (!this.isRenderableTarget(livingEntity) || f <= 0.0f || !TargetEsp.c.player.canSee((Entity)livingEntity)) {
            return;
        }
        Vec3d vec3d = EntityInterpolation.b((Entity)livingEntity, kvprd92.b());
        if (vec3d == null) {
            vec3d = livingEntity.getPos();
        }
        if (vec3d != null) {
            double d = Math.toRadians(50.0) / 15.0;
            int n = this.ghostCount.b();
            double d2 = this.animationPhase.getOrDefault(livingEntity, 0.0);
            double d3 = (double)Math.max(livingEntity.getWidth(), 0.5f) + 0.3;
            double d4 = vec3d.y + (double)(Math.max(livingEntity.getHeight(), 0.5f) / 2.0f);
            double d5 = f;
            Vec3d[] vec3dArray = new Vec3d[]{new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(1.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(-1.0, -1.0, -1.0)};
            for (int i = 0; i < n; ++i) {
                Vec3d vec3d2;
                double d6 = d2 + (double)i * Math.PI / 2.0;
                Vec3d vec3d3 = vec3dArray[i];
                double d7 = Math.sqrt(vec3d3.x * vec3d3.x + vec3d3.y * vec3d3.y + vec3d3.z * vec3d3.z);
                Vec3d vec3d4 = new Vec3d(vec3d3.x / d7, vec3d3.y / d7, vec3d3.z / d7);
                if (Math.abs(vec3d4.dotProduct(vec3d2 = new Vec3d(0.0, 1.0, 0.0))) > 0.99) {
                    vec3d2 = new Vec3d(1.0, 0.0, 0.0);
                }
                Vec3d vec3d5 = vec3d4.crossProduct(vec3d2).normalize();
                Vec3d vec3d6 = vec3d4.crossProduct(vec3d5).normalize();
                for (int j = 0; j < 15; ++j) {
                    double d8 = (double)j * d + d6;
                    double d9 = Math.cos(d8);
                    double d10 = Math.sin(d8);
                    Vec3d vec3d7 = vec3d5.multiply(d9).add(vec3d6.multiply(d10)).multiply(d3);
                    double d11 = vec3d.x + vec3d7.x;
                    double d12 = d4 + vec3d7.y;
                    double d13 = vec3d.z + vec3d7.z;
                    try {
                        WorldRenderUtils.a(kvprd92.a(), new Vec3d(d11, d12, d13), this.ghostParticleSize.a() * (1.0f + (float)j / 15.0f), ColorUtils.a(ColorUtils.a(this.getRenderColor(livingEntity).getRGB()), Math.max(1, (int)(d5 * 150.0))));
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }
    }

    private void b(Matrix4f matrix4f, Vec3d vec3d, Color color, boolean bl, float f, float f2, float f3, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, float f4, int n) {
        double d10 = d5 * d9 - d6 * d8;
        double d11 = d6 * d7 - d4 * d9;
        double d12 = d4 * d8 - d5 * d7;
        float f5 = f4;
        float f6 = (float)((double)f + d * (double)f5 * 1.2 - vec3d.x);
        float f7 = (float)((double)f2 + d2 * (double)f5 * 1.2 - vec3d.y);
        float f8 = (float)((double)f3 + d3 * (double)f5 * 1.2 - vec3d.z);
        float f9 = (float)((double)f - d * (double)f5 * 1.2 - vec3d.x);
        float f10 = (float)((double)f2 - d2 * (double)f5 * 1.2 - vec3d.y);
        float f11 = (float)((double)f3 - d3 * (double)f5 * 1.2 - vec3d.z);
        float f12 = f5 * 0.6f;
        float f13 = (float)((double)f + d7 * (double)f12 + d10 * (double)f12 - vec3d.x);
        float f14 = (float)((double)f2 + d8 * (double)f12 + d11 * (double)f12 - vec3d.y);
        float f15 = (float)((double)f3 + d9 * (double)f12 + d12 * (double)f12 - vec3d.z);
        float f16 = (float)((double)f + d7 * (double)f12 - d10 * (double)f12 - vec3d.x);
        float f17 = (float)((double)f2 + d8 * (double)f12 - d11 * (double)f12 - vec3d.y);
        float f18 = (float)((double)f3 + d9 * (double)f12 - d12 * (double)f12 - vec3d.z);
        float f19 = (float)((double)f - d7 * (double)f12 - d10 * (double)f12 - vec3d.x);
        float f20 = (float)((double)f2 - d8 * (double)f12 - d11 * (double)f12 - vec3d.y);
        float f21 = (float)((double)f3 - d9 * (double)f12 - d12 * (double)f12 - vec3d.z);
        float f22 = (float)((double)f - d7 * (double)f12 + d10 * (double)f12 - vec3d.x);
        float f23 = (float)((double)f2 - d8 * (double)f12 + d11 * (double)f12 - vec3d.y);
        float f24 = (float)((double)f3 - d9 * (double)f12 + d12 * (double)f12 - vec3d.z);
        this.a(matrix4f, color, bl, f6, f7, f8, f13, f14, f15, f16, f17, f18, n);
        this.a(matrix4f, color, bl, f6, f7, f8, f16, f17, f18, f19, f20, f21, n);
        this.a(matrix4f, color, bl, f6, f7, f8, f19, f20, f21, f22, f23, f24, n);
        this.a(matrix4f, color, bl, f6, f7, f8, f22, f23, f24, f13, f14, f15, n);
        this.a(matrix4f, color, bl, f9, f10, f11, f16, f17, f18, f13, f14, f15, n);
        this.a(matrix4f, color, bl, f9, f10, f11, f19, f20, f21, f16, f17, f18, n);
        this.a(matrix4f, color, bl, f9, f10, f11, f22, f23, f24, f19, f20, f21, n);
        this.a(matrix4f, color, bl, f9, f10, f11, f13, f14, f15, f22, f23, f24, n);
    }

    private void c(Matrix4f matrix4f, Vec3d vec3d, Color color, boolean bl, float f, float f2, float f3, double d, double d2, double d3, double d4, double d5, double d6, float f4, int n) {
        int[][] nArrayArray;
        float f5 = f4 * 0.5f;
        double d7 = d2 * d6 - d3 * d5;
        double d8 = d3 * d4 - d * d6;
        double d9 = d * d5 - d2 * d4;
        float[][] fArray = new float[8][3];
        int n2 = 0;
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                for (int k = -1; k <= 1; k += 2) {
                    fArray[n2++] = new float[]{(float)((double)f + d * (double)f5 * (double)i + d4 * (double)f5 * (double)j + d7 * (double)f5 * (double)k - vec3d.x), (float)((double)f2 + d2 * (double)f5 * (double)i + d5 * (double)f5 * (double)j + d8 * (double)f5 * (double)k - vec3d.y), (float)((double)f3 + d3 * (double)f5 * (double)i + d6 * (double)f5 * (double)j + d9 * (double)f5 * (double)k - vec3d.z)};
                }
            }
        }
        for (int[] nArray : nArrayArray = new int[][]{{0, 1, 3, 2}, {4, 6, 7, 5}, {0, 4, 5, 1}, {2, 3, 7, 6}, {0, 2, 6, 4}, {1, 5, 7, 3}}) {
            this.a(matrix4f, color, bl, fArray[nArray[0]][0], fArray[nArray[0]][1], fArray[nArray[0]][2], fArray[nArray[1]][0], fArray[nArray[1]][1], fArray[nArray[1]][2], fArray[nArray[2]][0], fArray[nArray[2]][1], fArray[nArray[2]][2], fArray[nArray[3]][0], fArray[nArray[3]][1], fArray[nArray[3]][2], n);
        }
    }

    private float animationSpeed(LivingEntity livingEntity) {
        float hitStrength;
        float f2;
        Long l;
        float f3 = this.baseAnimationSpeed();
        if (!this.hitEffectEnabled.a() || !this.hitSpeedEnabled.a() || livingEntity == null) {
            return f3;
        }
        int n = livingEntity.hurtTime;
        int n2 = this.previousHurtTimes.getOrDefault(livingEntity, 0);
        this.previousHurtTimes.put(livingEntity, n);
        if (n >= 9 && n2 < 9) {
            this.hitTimestamps.put(livingEntity, System.currentTimeMillis());
        }
        if ((l = this.hitTimestamps.get(livingEntity)) == null) {
            return f3;
        }
        float f4 = (float)(System.currentTimeMillis() - l) / 1000.0f;
        if (f4 > (f2 = this.hitEffectDuration.a())) {
            return f3;
        }
        float f5 = f4 / f2;
        if (f5 >= 0.15f) {
            float f6 = (f5 - 0.15f) / 0.85f;
            hitStrength = 1.0f - f6 * f6 * (3.0f - 2.0f * f6);
        } else {
            float f7 = f5 / 0.15f;
            hitStrength = f7 * f7 * (3.0f - 2.0f * f7);
        }
        return f3 * (1.0f + (this.hitSpeedMultiplier.a() - 1.0f) * hitStrength);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Handled unverifiable bytecode (illegal stack merge).
     */
    private void renderCircle(WorldRenderPostEvent kvprd92, LivingEntity livingEntity, float f) {
        if (f <= 0.0f || livingEntity == null || !this.isRenderableTarget(livingEntity) || !TargetEsp.c.player.canSee((Entity)livingEntity)) {
            return;
        }
        float f2 = livingEntity.getWidth() * 0.7f;
        Vec3d vec3d = EntityInterpolation.b((Entity)livingEntity, kvprd92.b());
        if (vec3d == null) {
            vec3d = livingEntity.getPos();
        }
        double d = this.animationPhase.getOrDefault(livingEntity, 0.0) % (Math.PI * 2);
        double d2 = Math.PI;
        boolean bl = d > Math.PI;
        double d3 = d / Math.PI;
        double d4 = bl ? d3 - 1.0 : 1.0 - d3;
        double d5 = d4 >= 0.5 ? 1.0 - Math.pow(-2.0 * d4 + 2.0, 2.0) / 2.0 : 2.0 * d4 * d4;
        double d6 = (double)(livingEntity.getHeight() / 2.0f) * (d5 <= 0.5 ? d5 : 1.0 - d5) * (bl ? -1 : (int)1.0);
        Tessellator tessellator = Tessellator.getInstance();
        MatrixStack matrixStack = kvprd92.a();
        Vec3d vec3d2 = TargetEsp.c.gameRenderer.getCamera().getPos();
        Color color = this.getRenderColor(livingEntity);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        matrixStack.push();
        try {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.depthMask((boolean)false);
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderSystem.lineWidth((float)3.0f);
            BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
            for (int i = 0; i <= 360; ++i) {
                double d7 = Math.toRadians(i);
                double d8 = Math.cos(d7);
                double d9 = Math.sin(d7);
                float f3 = (float)(vec3d.x + d8 * (double)f2 - vec3d2.x);
                float f4 = (float)(vec3d.y + (double)livingEntity.getHeight() * d5 - vec3d2.y);
                float f5 = (float)(vec3d.z + d9 * (double)f2 - vec3d2.z);
                bufferBuilder.vertex(matrix4f, f3, f4, f5).color(color.getRed(), color.getGreen(), color.getBlue(), (int)(100.0f * f));
                float f6 = (float)(vec3d.y + (double)livingEntity.getHeight() * d5 + d6 - vec3d2.y);
                bufferBuilder.vertex(matrix4f, f3, f6, f5).color(color.getRed(), color.getGreen(), color.getBlue(), 0);
            }
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
            BufferBuilder bufferBuilder2 = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
            for (int i = 0; i <= 360; ++i) {
                double d10 = Math.toRadians(i);
                float f7 = (float)(vec3d.x + Math.cos(d10) * (double)f2 - vec3d2.x);
                float f8 = (float)(vec3d.y + (double)livingEntity.getHeight() * d5 - vec3d2.y);
                float f9 = (float)(vec3d.z + Math.sin(d10) * (double)f2 - vec3d2.z);
                bufferBuilder2.vertex(matrix4f, f7, f8, f9).color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255.0f * f));
            }
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder2.end());
        }
        catch (Exception exception) {
        }
        finally {
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.depthMask((boolean)true);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glDisable((int)2848);
            matrixStack.pop();
        }
    }

    private Color baseColor() {
        if (this.useClientColor.a()) {
            ClientColor byzlib2 = ModuleManager.CLIENT_COLOR;
            return byzlib2.n();
        }
        return this.customColor.a();
    }

    @Override
    public void f() {
        super.f();
        this.visibilityAnimations.clear();
        this.previousHurtTimes.clear();
        this.hitTimestamps.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void renderOrbit(WorldRenderPostEvent event, LivingEntity target, float alpha) {
        if (alpha <= 0.0f || !isRenderableTarget(target) || !TargetEsp.c.player.canSee(target)) {
            return;
        }

        Vec3d entityPosition = EntityInterpolation.interpolatedPosition(target, event.b());
        if (entityPosition == null) {
            entityPosition = target.getPos();
        }
        if (entityPosition == null) {
            return;
        }

        MatrixStack matrices = event.a();
        Vec3d cameraPosition = TargetEsp.c.gameRenderer.getCamera().getPos();
        Color color = getRenderColor(target);
        int figuresPerRing = this.figuresPerRing.b();
        int layerCount = this.orbitLayerCount.b();
        float entityHeight = target.getHeight();
        float verticalSpan = entityHeight * this.orbitVerticalSpread.a();
        float lowerOffset = (entityHeight - verticalSpan) / 2.0f;
        float baseRadius = this.orbitRadius.a() + target.getWidth() / 2.0f;
        float figureSize = this.orbitFigureSize.a() * alpha;
        Vec3d orbitCenter = entityPosition.add(0.0, entityHeight / 2.0f, 0.0);
        double rotationDegrees = this.orbitRotationEnabled.a()
                ? this.rotationDegrees.getOrDefault(target, 0.0)
                : 0.0;
        double animationPhase = this.animationPhase.getOrDefault(target, 0.0);
        float animatedRadius = (float)(baseRadius + Math.sin(animationPhase) * 0.08);
        String shape = this.orbitShape.d();
        Optional<ShaderProgram> shader = resolveOrbitShader();

        if (this.orbitGlowEnabled.a()) {
            renderOrbitGlow(matrices, entityPosition, orbitCenter, color, alpha, figuresPerRing,
                    layerCount, verticalSpan, lowerOffset, animatedRadius, figureSize, rotationDegrees, shape);
        }

        renderOrbitFigures(matrices, cameraPosition, entityPosition, orbitCenter, color, alpha,
                figuresPerRing, layerCount, verticalSpan, lowerOffset, animatedRadius,
                figureSize, rotationDegrees, animationPhase, shape, shader);
    }

    private Optional<ShaderProgram> resolveOrbitShader() {
        if (!this.orbitShaderEnabled.a()) {
            return Optional.empty();
        }
        return DefaultShaders.getRegistry()
                .find(shaderName(this.orbitShaderType))
                .filter(ShaderProgram::b);
    }

    private void renderOrbitGlow(
            MatrixStack matrices,
            Vec3d entityPosition,
            Vec3d orbitCenter,
            Color color,
            float alpha,
            int figuresPerRing,
            int layerCount,
            float verticalSpan,
            float lowerOffset,
            float animatedRadius,
            float figureSize,
            double rotationDegrees,
            String shape) {
        float glowSize = this.orbitGlowSize.a();
        int glowColor = ColorUtils.withAlpha(
                ColorUtils.ensureOpaque(color.getRGB()),
                (int)(alpha * 255.0f * this.orbitGlowOpacity.a()));

        for (int layerIndex = 0; layerIndex < layerCount; layerIndex++) {
            float layerY = layerY(entityPosition.y, lowerOffset, verticalSpan, layerIndex, layerCount);
            float layerRadius = layerRadius(animatedRadius, layerIndex, layerCount);
            double angularOffset = alternatingAngularOffset(layerIndex, figuresPerRing);

            for (int figureIndex = 0; figureIndex < figuresPerRing; figureIndex++) {
                double angle = Math.toRadians(rotationDegrees + angularOffset
                        + 360.0 * figureIndex / figuresPerRing);
                Vec3d figurePosition = new Vec3d(
                        orbitCenter.x + layerRadius * Math.cos(angle),
                        layerY,
                        orbitCenter.z + layerRadius * Math.sin(angle));

                if (shape.equals("Стрелки")) {
                    Vec3d towardCenter = orbitCenter.subtract(figurePosition);
                    if (towardCenter.lengthSquared() > 1.0E-6) {
                        figurePosition = figurePosition.add(towardCenter.normalize().multiply(figureSize * 0.75f));
                    }
                }
                WorldRenderUtils.drawBillboardGlow(matrices, figurePosition, glowSize, glowColor);
            }
        }
    }

    private void renderOrbitFigures(
            MatrixStack matrices,
            Vec3d cameraPosition,
            Vec3d entityPosition,
            Vec3d orbitCenter,
            Color color,
            float alpha,
            int figuresPerRing,
            int layerCount,
            float verticalSpan,
            float lowerOffset,
            float animatedRadius,
            float figureSize,
            double rotationDegrees,
            double animationPhase,
            String shape,
            Optional<ShaderProgram> shader) {
        matrices.push();
        try {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
            int shapeType = switch (shape) {
                case "Стрелки" -> 0;
                case "Ромбы" -> 1;
                case "Кубы" -> 2;
                default -> -1;
            };
            int renderPassCount = shader.isPresent() ? 2 : 1;

            for (int passIndex = 0; passIndex < renderPassCount; passIndex++) {
                boolean shaderPass = passIndex == 1;
                try {
                    if (shaderPass) {
                        ShaderProgram program = shader.orElseThrow();
                        program.d();
                        program.a("time", (float)(animationPhase * 0.4));
                        program.a("screenSize", (float)c.getWindow().getFramebufferWidth(),
                                (float)c.getWindow().getFramebufferHeight());
                        program.a("baseColor", color.getRed() / 255.0f, color.getGreen() / 255.0f,
                                color.getBlue() / 255.0f, 1.0f);
                        program.a("alpha", alpha * 1.5f);
                        RenderSystem.blendFunc(770, 1);
                    }

                    for (int layerIndex = 0; layerIndex < layerCount; layerIndex++) {
                        float layerY = layerY(entityPosition.y, lowerOffset, verticalSpan, layerIndex, layerCount);
                        float radius = layerRadius(animatedRadius, layerIndex, layerCount);
                        double angularOffset = alternatingAngularOffset(layerIndex, figuresPerRing);

                        for (int figureIndex = 0; figureIndex < figuresPerRing; figureIndex++) {
                            double angle = Math.toRadians(rotationDegrees + angularOffset
                                    + 360.0 * figureIndex / figuresPerRing);
                            float figureX = (float)(orbitCenter.x + radius * Math.cos(angle));
                            float figureZ = (float)(orbitCenter.z + radius * Math.sin(angle));
                            Vec3d forward = orbitCenter.subtract(figureX, layerY, figureZ).normalize();
                            Vec3d side = new Vec3d(-forward.z, 0.0, forward.x).normalize();
                            Vec3d up = side.crossProduct(forward);
                            int opacity = (int)(alpha * 220.0f);

                            switch (shapeType) {
                                case 0 -> this.a(positionMatrix, cameraPosition, color, shaderPass,
                                        figureX, layerY, figureZ,
                                        forward.x, forward.y, forward.z,
                                        up.x, up.y, up.z,
                                        side.x, side.y, side.z,
                                        figureSize, opacity);
                                case 1 -> this.b(positionMatrix, cameraPosition, color, shaderPass,
                                        figureX, layerY, figureZ,
                                        forward.x, forward.y, forward.z,
                                        up.x, up.y, up.z,
                                        side.x, side.y, side.z,
                                        figureSize, opacity);
                                case 2 -> this.c(positionMatrix, cameraPosition, color, shaderPass,
                                        figureX, layerY, figureZ,
                                        up.x, up.y, up.z,
                                        side.x, side.y, side.z,
                                        figureSize, opacity);
                                default -> { }
                            }
                        }
                    }
                } finally {
                    if (shaderPass) {
                        shader.ifPresent(program -> {
                            try {
                                program.e();
                            } catch (Exception ignored) {
                            }
                        });
                        RenderSystem.defaultBlendFunc();
                    }
                }
            }
        } finally {
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            matrices.pop();
        }
    }

    private static float layerY(double entityY, float lowerOffset, float verticalSpan, int layerIndex, int layerCount) {
        int intervals = Math.max(1, layerCount - 1);
        return (float)(entityY + lowerOffset + verticalSpan * layerIndex / intervals);
    }

    private static float layerRadius(float baseRadius, int layerIndex, int layerCount) {
        float centerLayer = (layerCount - 1) / 2.0f;
        return baseRadius * (1.0f - Math.abs(layerIndex - centerLayer)
                / Math.max(centerLayer, 0.001f) * 0.3f);
    }

    private static double alternatingAngularOffset(int layerIndex, int figuresPerRing) {
        return 180.0 / figuresPerRing * (layerIndex % 2);
    }

    private void renderSquare(WorldRenderPostEvent kvprd92, LivingEntity livingEntity, float f) {
        if (f <= 0.0f || livingEntity == null || !this.isRenderableTarget(livingEntity) || !TargetEsp.c.player.canSee((Entity)livingEntity)) {
            return;
        }
        Vec3d vec3d = EntityInterpolation.b((Entity)livingEntity, kvprd92.b());
        if (vec3d == null) {
            vec3d = livingEntity.getPos();
        }
        if (vec3d != null) {
            double d = this.rotationDegrees.getOrDefault(livingEntity, 0.0);
            double d2 = this.animationPhase.getOrDefault(livingEntity, 0.0);
            float f2 = (float)((double)this.squareSize.a() * (1.0 + 0.05 * Math.sin(d2))) * f;
            WorldRenderUtils.drawTexturedBillboard(kvprd92.a(), new Vec3d(vec3d.x, vec3d.y + (double)(livingEntity.getHeight() / 2.0f), vec3d.z), f2, ColorUtils.withAlpha(ColorUtils.ensureOpaque(this.getRenderColor(livingEntity).getRGB()), Math.max(1, (int)(f * 255.0f))), this.targetTexture, (float)d, true);
        }
    }

    private void a(Matrix4f matrix4f, Vec3d vec3d, Color color, boolean bl, float f, float f2, float f3, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, float f4, int n) {
        float f5 = f4 * 0.5f;
        this.a(matrix4f, color, bl, (float)((double)f + d * (double)f4 * 1.5 - vec3d.x), (float)((double)f2 + d2 * (double)f4 * 1.5 - vec3d.y), (float)((double)f3 + d3 * (double)f4 * 1.5 - vec3d.z), (float)((double)f + d4 * (double)f5 - vec3d.x), (float)((double)f2 + d5 * (double)f5 - vec3d.y), (float)((double)f3 + d6 * (double)f5 - vec3d.z), (float)((double)f - d4 * (double)f5 - vec3d.x), (float)((double)f2 - d5 * (double)f5 - vec3d.y), (float)((double)f3 - d6 * (double)f5 - vec3d.z), (float)((double)f + d7 * (double)f5 - vec3d.x), (float)((double)f2 + d8 * (double)f5 - vec3d.y), (float)((double)f3 + d9 * (double)f5 - vec3d.z), n);
    }

    private boolean isRenderableTarget(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return false;
        }
        if (!this.matchesTargetType(livingEntity)) {
            return false;
        }
        if (!livingEntity.isAlive() || livingEntity.getWorld() != TargetEsp.c.world) {
            return false;
        }
        if (livingEntity.getWidth() <= 0.0f || livingEntity.getHeight() <= 0.0f) {
            return false;
        }
        if (livingEntity.squaredDistanceTo((Entity)TargetEsp.c.player) > 25.0) {
            return false;
        }
        if (!TargetEsp.c.player.canSee((Entity)livingEntity)) {
            return false;
        }
        if (livingEntity.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            boolean bl = !livingEntity.getMainHandStack().isEmpty() || !livingEntity.getOffHandStack().isEmpty();
            boolean bl2 = false;
            for (ItemStack itemStack : livingEntity.getArmorItems()) {
                if (itemStack.isEmpty()) continue;
                bl2 = true;
                break;
            }
            if (!bl && !bl2) {
                return false;
            }
        }
        return true;
    }

    private void updateAnimationPhase(LivingEntity livingEntity, float f) {
        float f2 = this.animationSpeed(livingEntity);
        this.rotationDegrees.put(livingEntity, (this.rotationDegrees.getOrDefault(livingEntity, 0.0) + (double)(f * f2) * 50.0) % 360.0);
        this.animationPhase.put(livingEntity, this.animationPhase.getOrDefault(livingEntity, 0.0) + (double)(f * f2) * 2.5);
    }

    @EventHandler
    public void onWorldRender(WorldRenderPostEvent kvprd92) {
        long l = this.lastFrameTimeMs;
        this.lastFrameTimeMs = System.currentTimeMillis();
        float f = Math.min((float)(this.lastFrameTimeMs - l) / 1000.0f, 0.1f);
        LivingEntity livingEntity2 = this.findTarget();
        if (livingEntity2 != null && this.isRenderableTarget(livingEntity2)) {
            this.visibilityAnimations.computeIfAbsent(livingEntity2, livingEntity -> {
                int n = 95;
                return new AnimatedValue();
            }).a(1.0, 0.5, Easings.f, true);
        }
        HashSet<LivingEntity> hashSet = new HashSet<LivingEntity>();
        for (Map.Entry<LivingEntity, AnimatedValue> livingEntity3 : this.visibilityAnimations.entrySet()) {
            LivingEntity livingEntity4 = livingEntity3.getKey();
            if (this.isRenderableTarget(livingEntity4)) {
                AnimatedValue urhlup2 = livingEntity3.getValue();
                if (livingEntity4 != livingEntity2) {
                    urhlup2.a(0.0, 0.5, Easings.f, true);
                }
                urhlup2.a();
                if (!urhlup2.d() || urhlup2.i() != 0.0) {
                    this.updateAnimationPhase(livingEntity4, f);
                    switch (this.mode.d()) {
                        case "Призраки": {
                            this.renderGhosts(kvprd92, livingEntity4, (float)urhlup2.j());
                            break;
                        }
                        case "Круг": {
                            this.renderCircle(kvprd92, livingEntity4, (float)urhlup2.j());
                            break;
                        }
                        case "Квадратик": {
                            this.renderSquare(kvprd92, livingEntity4, (float)urhlup2.j());
                            break;
                        }
                        case "Орбита": {
                            this.renderOrbit(kvprd92, livingEntity4, (float)urhlup2.j());
                        }
                    }
                    continue;
                }
                hashSet.add(livingEntity4);
                continue;
            }
            hashSet.add(livingEntity4);
        }
        for (LivingEntity livingEntity5 : hashSet) {
            this.visibilityAnimations.remove(livingEntity5);
            this.rotationDegrees.remove(livingEntity5);
            this.animationPhase.remove(livingEntity5);
        }
    }

    private void a(Matrix4f matrix4f, Color color, boolean bl, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int n) {
        if (!bl) {
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(matrix4f, f, f2, f3).color(color.getRed(), color.getGreen(), color.getBlue(), n);
            bufferBuilder.vertex(matrix4f, f4, f5, f6).color(color.getRed(), color.getGreen(), color.getBlue(), n * 3 / 4);
            bufferBuilder.vertex(matrix4f, f7, f8, f9).color(color.getRed(), color.getGreen(), color.getBlue(), n * 3 / 4);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
            return;
        }
        Vector4f vector4f = matrix4f.transform(new Vector4f(f, f2, f3, 1.0f));
        Vector4f vector4f2 = matrix4f.transform(new Vector4f(f4, f5, f6, 1.0f));
        Vector4f vector4f3 = matrix4f.transform(new Vector4f(f7, f8, f9, 1.0f));
        float[] fArray = new float[]{vector4f.x, vector4f.y, vector4f.z, 0.5f, 0.0f, vector4f2.x, vector4f2.y, vector4f2.z, 0.0f, 1.0f, vector4f3.x, vector4f3.y, vector4f3.z, 1.0f, 1.0f};
        ShaderProgram.a(fArray, 3);
    }

    private void a(Matrix4f matrix4f, Color color, boolean bl, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, int n) {
        this.a(matrix4f, color, bl, f, f2, f3, f4, f5, f6, f7, f8, f9, n);
        this.a(matrix4f, color, bl, f, f2, f3, f7, f8, f9, f10, f11, f12, n);
    }

    private String shaderName(ModeSetting s82syr2) {
        switch (s82syr2.d()) {
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

    private float baseAnimationSpeed() {
        switch (this.mode.d()) {
            case "Призраки": {
                return this.ghostSpeed.a();
            }
            case "Круг": {
                return this.circleSpeed.a();
            }
            case "Квадратик": {
                return this.squareSpeed.a();
            }
            case "Орбита": {
                return this.orbitSpeed.a();
            }
        }
        return 1.5f;
    }

    private LivingEntity findTarget() {
        LivingEntity livingEntity;
        if (TargetEsp.c.player == null || TargetEsp.c.world == null) {
            return null;
        }
        LivingEntity livingEntity2 = HudServices.TARGETS.h();
        if (this.isRenderableTarget(livingEntity2)) {
            return livingEntity2;
        }
        Entity entity = TargetEsp.c.targetedEntity;
        if (entity instanceof LivingEntity && this.isRenderableTarget(livingEntity = (LivingEntity)entity)) {
            return livingEntity;
        }
        return null;
    }
}
