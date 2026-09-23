package rockstar.client.esp;






import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.render.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import net.minecraft.block.BlockState;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ColorSetting;
import rockstar.client.setting.GradientColorSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.esp.EspFeature;
import rockstar.client.esp.ItemTargetType;
import rockstar.client.ui.ThemeColors;
import rockstar.client.esp.EntityTargetType;
import rockstar.client.internal.render.EspGlowCaptureRenderer;
import rockstar.client.ui.WindowAccess;
import rockstar.client.render.ManagedFramebuffer;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.render.GlowBloomRenderer;
import rockstar.client.internal.render.HeatHazeRenderer;

public class FlameEspFeature
extends EspFeature
implements WindowAccess {
    public static boolean internalField0277;
    private static final ColorRGBA internalField0777;
    private final BooleanSetting internalField0650 = this.internalMethod02236("esp.flame");
    private final SliderSetting internalField0383 = new SliderSetting(this, "esp.flame.strength").internalMethod05900(1.0f).internalMethod02732(5.0f).internalMethod08673(1.0f).internalMethod08074(3.0f);
    private final SliderSetting internalField0382 = new SliderSetting(this, "esp.flame.rise_speed").internalMethod05900(0.5f).internalMethod02732(4.0f).internalMethod08673(0.1f).internalMethod08074(2.5f);
    private final SliderSetting internalField1142 = new SliderSetting(this, "esp.flame.wobble").internalMethod05900(0.0f).internalMethod02732(2.0f).internalMethod08673(0.1f).internalMethod08074(2.5f);
    private final SliderSetting internalField1140 = new SliderSetting(this, "esp.flame.fade_rate").internalMethod05900(0.0f).internalMethod02732(80.0f).internalMethod08673(1.0f).internalMethod08074(50.0f);
    private final SliderSetting internalField1141 = new SliderSetting(this, "esp.flame.intensity").internalMethod05900(0.5f).internalMethod02732(4.0f).internalMethod08673(0.1f).internalMethod08074(2.0f);
    private final BooleanSetting internalField0651 = new BooleanSetting(this, "esp.flame.distortion");
    private final SliderSetting internalField1143 = new SliderSetting((SettingOwner)this, "esp.flame.distortion_strength", () -> !this.internalField0651.internalMethod04496()).internalMethod05900(0.1f).internalMethod02732(3.0f).internalMethod08673(0.1f).internalMethod08074(1.0f);
    private final BooleanSetting internalField1261 = new BooleanSetting(this, "esp.flame.item_color");
    private final BooleanSetting internalField1263 = new BooleanSetting((SettingOwner)this, "theme.sync", this.internalField1261::internalMethod04496);
    private final BooleanSetting internalField1262 = new BooleanSetting((SettingOwner)this, "esp.flame.gradient", () -> this.internalField1261.internalMethod04496() || this.internalField1263.internalMethod04496());
    private final GradientColorSetting internalField0666 = new GradientColorSetting(this, "esp.flame.gradient_color", () -> this.internalField1261.internalMethod04496() || this.internalField1263.internalMethod04496() || !this.internalField1262.internalMethod04496()).internalMethod00046(new ColorRGBA(255.0f, 220.0f, 60.0f, 255.0f), new ColorRGBA(255.0f, 60.0f, 0.0f, 255.0f));
    private final ColorSetting internalField0665 = new ColorSetting(this, "esp.flame.color", () -> this.internalField1261.internalMethod04496() || this.internalField1263.internalMethod04496() || this.internalField1262.internalMethod04496()).internalMethod04886(new ColorRGBA(255.0f, 110.0f, 30.0f, 255.0f));
    private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0f);
    private final ManagedFramebuffer internalField0770 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0f);
    private final ManagedFramebuffer internalField1308 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0f);
    private final ManagedFramebuffer internalField1307 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0f);
    private final GlowBloomRenderer internalField0320 = GlowBloomRenderer.internalMethod06776();
    private final HeatHazeRenderer internalField0322 = new HeatHazeRenderer();
    private boolean internalField0276 = false;
    private boolean internalField1099 = false;
    private final EventListener<Render3DEvent> internalField0157 = render3DEvent -> {
        boolean bl;
        this.internalField0276 = false;
        boolean bl2 = bl = FlameEspFeature.internalField0149.options != null && FlameEspFeature.internalField0149.options.getPerspective() != null && FlameEspFeature.internalField0149.options.getPerspective().isFirstPerson();
        if (!this.internalMethod02927(ItemTargetType.internalField0012) || !bl) {
            if (this.internalField1099) {
                this.internalField0770.internalMethod02227(true);
                this.internalField0770.internalMethod03248();
                this.internalField1099 = false;
            }
            return;
        }
        this.internalField0769.internalMethod02227(true);
        this.internalField0769.internalMethod03248();
    };
    private final EventListener<PreHudRenderEvent> internalField0158 = preHudRenderEvent -> {
        ManagedFramebuffer typedValue245;
        int n;
        float f;
        float f2;
        if (!this.internalMethod02927(ItemTargetType.internalField0012)) {
            return;
        }
        if (this.internalField1099) {
            this.internalMethod02631();
            f2 = Math.max(0.0f, Math.min(1.0f, this.internalField1140.internalMethod08576() / 100.0f));
            f = 0.94f + f2 * 0.055f;
            if (!this.internalField0276) {
                f += (1.0f - f) * 0.5f;
            }
            this.internalMethod05531(f);
        }
        if (this.internalField0276) {
            if (this.internalField1262.internalMethod04496() && !this.internalField1261.internalMethod04496() && !this.internalField1263.internalMethod04496()) {
                this.internalMethod00643(internalField0777);
            } else {
                ColorRGBA colorRGBA = this.internalMethod00087();
                if (colorRGBA != null) {
                    this.internalMethod00643(colorRGBA);
                }
            }
            this.internalMethod08751();
            this.internalField1099 = true;
        }
        if (!this.internalField1099) {
            return;
        }
        if (this.internalField0651.internalMethod04496() && (n = rockstar.client.render.FramebufferCompat.glId(this.internalField0770.getColorAttachment())) != 0) {
            f = (float)internalField0267.getScaledWidth() / (float)Math.max(1, internalField0267.getScaledHeight());
            float f3 = (float)(System.currentTimeMillis() % 100000L) / 1000.0f;
            this.internalField0322.internalMethod04104(n, f, this.internalField1143.internalMethod08576(), f3);
        }
        f2 = this.internalField1141.internalMethod08576();
        if (this.internalField0276) {
            this.internalMethod08753();
            typedValue245 = this.internalField1307;
        } else {
            typedValue245 = this.internalField0770;
        }
        if (f2 <= 0.01f) {
            return;
        }
        this.internalField0320.internalMethod06580(Math.max(1, (int)(this.internalField0383.internalMethod08576() / 2.0f)));
        this.internalField0320.internalMethod05694(typedValue245);
        EspGlowCaptureRenderer.internalMethod04936(this.internalField0320.internalMethod06210());
        int n2 = this.internalField0320.internalMethod03125();
        if (n2 != 0) {
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE);
            RenderSystem.setShaderTexture((int)0, (int)n2);
            RenderSystem.setShaderColor((float)f2, (float)f2, (float)f2, (float)1.0f);
            ColorRGBA colorRGBA = this.internalField0666.internalMethod05319();
            ColorRGBA colorRGBA2 = this.internalField0666.internalMethod01482();
            if (this.internalField1262.internalMethod04496() && !this.internalField1261.internalMethod04496() && !this.internalField1263.internalMethod04496() && colorRGBA != null && colorRGBA2 != null) {
                FlameEspFeature.internalMethod04521(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight(), FlameEspFeature.internalMethod04348(colorRGBA), FlameEspFeature.internalMethod04348(colorRGBA2));
            } else {
                RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
            }
        }
        RenderSystem.depthMask((boolean)true);
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.defaultBlendFunc();
    };
    public FlameEspFeature() {
        super("flame", new ItemTargetType[]{ItemTargetType.internalField0012}, EntityTargetType.internalField0964);
        this.internalField0320.internalMethod03093();
        this.internalField0320.internalMethod06504(3);
        this.internalField0320.internalMethod06503(2.2f);
        this.internalField0322.internalMethod01553();
    }

    public ColorRGBA internalMethod00087() {
        if (this.internalField1261.internalMethod04496()) {
            return null;
        }
        return this.internalField1263.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
    }

    public void internalMethod00220(HeldItemRenderer heldItemRenderer, AbstractClientPlayerEntity abstractClientPlayerEntity, ItemStack itemStack, ItemDisplayContext modelTransformationMode, boolean bl, MatrixStack matrixStack, int n) {
        if (!this.internalMethod02927(ItemTargetType.internalField0012)) {
            return;
        }
        if (itemStack == null || itemStack.isEmpty()) {
            return;
        }
        internalField0277 = true;
        this.internalField0769.internalMethod02227(false);
        VertexConsumerProvider.Immediate immediate = internalField0149.getBufferBuilders().getEntityVertexConsumers();
        try {
            rockstar.client.render.LegacyRenderCompat.renderItem((LivingEntity)abstractClientPlayerEntity, itemStack, modelTransformationMode, bl, matrixStack, immediate, 0xF000F0);
            immediate.draw();
            this.internalField0276 = true;
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.internalField0769.internalMethod03248();
        internalField0277 = false;
    }

    public void internalMethod00240(BlockRenderManager blockRenderManager, BlockState blockState, MatrixStack matrixStack, int n) {
        if (!this.internalMethod02927(ItemTargetType.internalField0012)) {
            return;
        }
        if (blockState == null) {
            return;
        }
        internalField0277 = true;
        this.internalField0769.internalMethod02227(false);
        VertexConsumerProvider.Immediate immediate = internalField0149.getBufferBuilders().getEntityVertexConsumers();
        try {
            blockRenderManager.renderBlockAsEntity(blockState, matrixStack, (VertexConsumerProvider)immediate, 0xF000F0, n);
            immediate.draw();
            this.internalField0276 = true;
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.internalField0769.internalMethod03248();
        internalField0277 = false;
    }

    private void internalMethod02631() {
        float f = internalField0267.getScaledWidth();
        float f2 = internalField0267.getScaledHeight();
        float f3 = this.internalField0382.internalMethod08576();
        float f4 = this.internalField1142.internalMethod08576();
        float f5 = (float)(System.currentTimeMillis() % 100000L) / 1000.0f;
        this.internalField1308.internalMethod02227(true);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, this.internalField0770.getColorAttachmentView());
        for (int i = 0; i < 24; ++i) {
            float f6 = (float)i / 24.0f;
            float f7 = (float)(i + 1) / 24.0f;
            float f8 = f6 * f2 - f3;
            float f9 = f7 * f2 - f3;
            float f10 = 1.0f - f6;
            float f11 = 1.0f - f7;
            float f12 = (float)i * 0.45f;
            float f13 = (float)Math.sin(f5 * 4.5f + f12) * f4 + (float)Math.sin(f5 * 1.7f + f12 * 2.1f) * (f4 * 0.5f) + (float)Math.sin(f5 * 7.3f + f12 * 3.7f) * (f4 * 0.32f) + (float)Math.sin(f5 * 2.3f + f12 * 1.3f) * (f4 * 0.45f) + (float)Math.sin(f5 * 0.61f + f12 * 0.7f) * (f4 * 0.35f);
            BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(f13, f8, 0.0f).texture(0.0f, f10).color(-1);
            bufferBuilder.vertex(f13, f9, 0.0f).texture(0.0f, f11).color(-1);
            bufferBuilder.vertex(f + f13, f9, 0.0f).texture(1.0f, f11).color(-1);
            bufferBuilder.vertex(f + f13, f8, 0.0f).texture(1.0f, f10).color(-1);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        }
        RenderSystem.setShaderTexture((int)0, (int)0);
        this.internalField0770.internalMethod02227(true);
        RenderSystem.disableBlend();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, this.internalField1308.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, f, f2);
        RenderSystem.setShaderTexture((int)0, (int)0);
        rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
    }

    private void internalMethod05531(float f) {
        this.internalField0770.internalMethod02227(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.ZERO, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int n = Math.round(f * 255.0f) & 0xFF;
        int n2 = n << 24;
        float f2 = internalField0267.getScaledWidth();
        float f3 = internalField0267.getScaledHeight();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(0.0f, 0.0f, 0.0f).color(n2);
        bufferBuilder.vertex(0.0f, f3, 0.0f).color(n2);
        bufferBuilder.vertex(f2, f3, 0.0f).color(n2);
        bufferBuilder.vertex(f2, 0.0f, 0.0f).color(n2);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.defaultBlendFunc();
        rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
    }

    private void internalMethod08751() {
        this.internalField0770.internalMethod02227(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 771);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, this.internalField0769.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.defaultBlendFunc();
        rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
    }

    private void internalMethod08753() {
        this.internalField1307.internalMethod02227(true);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, this.internalField0770.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
        if (this.internalField0276) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.ZERO, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA, (com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.ZERO, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)0.45f);
            RenderSystem.setShaderTexture(0, this.internalField0769.getColorAttachmentView());
            RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
            RenderSystem.defaultBlendFunc();
        }
        RenderSystem.setShaderTexture((int)0, (int)0);
        rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
    }

    private void internalMethod00643(ColorRGBA colorRGBA) {
        this.internalField0769.beginWrite(true);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(772, 0, 0, 1);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int n = FlameEspFeature.internalMethod00642(colorRGBA);
        float f = internalField0267.getScaledWidth();
        float f2 = internalField0267.getScaledHeight();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(0.0f, 0.0f, 0.0f).color(n);
        bufferBuilder.vertex(0.0f, f2, 0.0f).color(n);
        bufferBuilder.vertex(f, f2, 0.0f).color(n);
        bufferBuilder.vertex(f, 0.0f, 0.0f).color(n);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.defaultBlendFunc();
        rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
    }

    private static void internalMethod04521(float f, float f2, float f3, float f4, int n, int n2) {
        float f5 = f4 / 4.0f;
        float f6 = 2.0f * f5;
        float f7 = (float)(System.currentTimeMillis() % 1000000L) / 1000.0f;
        float f8 = f7 * 80.0f % f6;
        int n3 = 2;
        BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        for (int i = -n3; i < 4 + n3; ++i) {
            boolean bl = Math.floorMod(i, 2) == 0;
            int n4 = bl ? n : n2;
            int n5 = bl ? n2 : n;
            float f9 = f2 + (float)i * f5 - f8;
            float f10 = f9 + f5;
            float f11 = 1.0f - (f9 - f2) / f4;
            float f12 = 1.0f - (f10 - f2) / f4;
            bufferBuilder.vertex(f, f9, 0.0f).texture(0.0f, f11).color(n4);
            bufferBuilder.vertex(f, f10, 0.0f).texture(0.0f, f12).color(n5);
            bufferBuilder.vertex(f + f3, f10, 0.0f).texture(1.0f, f12).color(n5);
            bufferBuilder.vertex(f + f3, f9, 0.0f).texture(1.0f, f11).color(n4);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    private static int internalMethod00642(ColorRGBA colorRGBA) {
        float f = Math.max(0.0f, Math.min(1.0f, colorRGBA.getAlpha() / 255.0f));
        int n = Math.round(colorRGBA.getRed() * f) & 0xFF;
        int n2 = Math.round(colorRGBA.getGreen() * f) & 0xFF;
        int n3 = Math.round(colorRGBA.getBlue() * f) & 0xFF;
        return 0xFF000000 | n << 16 | n2 << 8 | n3;
    }

    private static int internalMethod04348(ColorRGBA colorRGBA) {
        int n = Math.round(colorRGBA.getRed()) & 0xFF;
        int n2 = Math.round(colorRGBA.getGreen()) & 0xFF;
        int n3 = Math.round(colorRGBA.getBlue()) & 0xFF;
        return 0xFF000000 | n << 16 | n2 << 8 | n3;
    }

    @Generated
    public BooleanSetting internalMethod04095() {
        return this.internalField0650;
    }

    @Generated
    public SliderSetting internalMethod01865() {
        return this.internalField0383;
    }

    @Generated
    public SliderSetting internalMethod02532() {
        return this.internalField0382;
    }

    @Generated
    public SliderSetting internalMethod08826() {
        return this.internalField1142;
    }

    @Generated
    public SliderSetting internalMethod08945() {
        return this.internalField1140;
    }

    @Generated
    public SliderSetting internalMethod08214() {
        return this.internalField1141;
    }

    @Generated
    public BooleanSetting internalMethod04842() {
        return this.internalField0651;
    }

    @Generated
    public SliderSetting internalMethod08345() {
        return this.internalField1143;
    }

    @Generated
    public BooleanSetting internalMethod08953() {
        return this.internalField1261;
    }

    @Generated
    public BooleanSetting internalMethod07837() {
        return this.internalField1263;
    }

    @Generated
    public BooleanSetting internalMethod08643() {
        return this.internalField1262;
    }

    @Generated
    public GradientColorSetting internalMethod04151() {
        return this.internalField0666;
    }

    @Generated
    public ColorSetting internalMethod04150() {
        return this.internalField0665;
    }

    @Generated
    public ManagedFramebuffer internalMethod06732() {
        return this.internalField0769;
    }

    @Generated
    public ManagedFramebuffer internalMethod07387() {
        return this.internalField0770;
    }

    @Generated
    public ManagedFramebuffer internalMethod08192() {
        return this.internalField1308;
    }

    @Generated
    public ManagedFramebuffer internalMethod08324() {
        return this.internalField1307;
    }

    @Generated
    public GlowBloomRenderer internalMethod06465() {
        return this.internalField0320;
    }

    @Generated
    public HeatHazeRenderer internalMethod06466() {
        return this.internalField0322;
    }

    @Generated
    public boolean internalMethod02632() {
        return this.internalField0276;
    }

    @Generated
    public boolean internalMethod08752() {
        return this.internalField1099;
    }

    @Generated
    public EventListener<Render3DEvent> internalMethod07468() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<PreHudRenderEvent> internalMethod00711() {
        return this.internalField0158;
    }

    static {
        internalField0777 = new ColorRGBA(255.0f, 255.0f, 255.0f, 255.0f);
    }
}

