package rockstar.client.esp;






import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.render.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
import java.util.Arrays;
import lombok.Generated;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.Framebuffer;
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
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ColorSetting;
import rockstar.client.setting.ModeSetting;
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
import rockstar.client.internal.script.ItemCenterShader;

public class FillEspFeature
extends EspFeature
implements WindowAccess {
    public static boolean internalField0277;
    private final BooleanSetting internalField0650 = this.internalMethod02236("esp.fill");
    private final ModeSetting internalField0668 = new ModeSetting(this, "esp.fill.mode");
    private final ModeSetting.InternalType0088 internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "esp.fill.mode.mirror");
    private final ModeSetting.InternalType0088 internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "esp.fill.mode.shader");
    private final BooleanSetting internalField0651 = new BooleanSetting(this, "theme.sync");
    private final ColorSetting internalField0665 = new ColorSetting(this, "esp.fill.color", this.internalField0651::internalMethod04496).internalMethod04886(new ColorRGBA(255.0f, 60.0f, 60.0f, 255.0f));
    private final BooleanSetting internalField1261 = new BooleanSetting((SettingOwner)this, "esp.fill.mirror_flat", () -> this.internalField0668.internalMethod07418() != this.internalField0237);
    private final SliderSetting internalField0383 = new SliderSetting((SettingOwner)this, "esp.fill.mirror_alpha", () -> this.internalField0668.internalMethod07418() != this.internalField0237).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(100.0f);
    private final SliderSetting internalField0382 = new SliderSetting((SettingOwner)this, "esp.fill.mirror_blur", () -> this.internalField0668.internalMethod07418() != this.internalField0237).internalMethod05900(0.0f).internalMethod02732(10.0f).internalMethod08673(1.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField1142 = new SliderSetting((SettingOwner)this, "esp.fill.mirror_ambient", () -> this.internalField0668.internalMethod07418() != this.internalField0237 || this.internalField1261.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(35.0f);
    private final ModeSetting internalField0669 = new ModeSetting((SettingOwner)this, "esp.fill.shader", () -> this.internalField0668.internalMethod07418() != this.internalField0238);
    private final ModeSetting.InternalType0088 internalField1066 = new ModeSetting.InternalType0088(this.internalField0669, "esp.fill.shader.caustic");
    private final ModeSetting.InternalType0088 internalField1067 = new ModeSetting.InternalType0088(this.internalField0669, "esp.fill.shader.plasma");
    private final ModeSetting.InternalType0088 internalField1068 = new ModeSetting.InternalType0088(this.internalField0669, "esp.fill.shader.lava");
    private final SliderSetting internalField1140 = new SliderSetting((SettingOwner)this, "esp.fill.shader_speed", () -> this.internalField0668.internalMethod07418() != this.internalField0238).internalMethod05900(0.0f).internalMethod02732(0.5f).internalMethod08673(0.01f).internalMethod08074(0.25f);
    private final SliderSetting internalField1141 = new SliderSetting((SettingOwner)this, "esp.fill.shader_alpha", () -> this.internalField0668.internalMethod07418() != this.internalField0238).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(100.0f);
    private final ManagedFramebuffer[] internalField0553 = new ManagedFramebuffer[]{new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0f), new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0f)};
    private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013().internalMethod03472(0.5f);
    private final ManagedFramebuffer internalField0770 = new ManagedFramebuffer(false).internalMethod06013().internalMethod03472(1.0f);
    private boolean internalField0276 = false;
    private final GlowBloomRenderer internalField0320 = GlowBloomRenderer.internalMethod06776();
    private final boolean[] internalField0637 = new boolean[2];
    private final boolean[] internalField0636 = new boolean[2];
    private final float[] internalField0615 = new float[]{0.5f, 0.5f};
    private final float[] internalField0616 = new float[]{0.5f, 0.5f};
    private final EventListener<Render3DEvent> internalField0157 = render3DEvent -> {
        boolean bl;
        Arrays.fill(this.internalField0637, false);
        Arrays.fill(this.internalField0636, false);
        this.internalField0276 = false;
        boolean bl2 = bl = FillEspFeature.internalField0149.options != null && FillEspFeature.internalField0149.options.getPerspective() != null && FillEspFeature.internalField0149.options.getPerspective().isFirstPerson();
        if (!this.internalMethod02927(ItemTargetType.internalField0012) || !bl) {
            return;
        }
        if (this.internalField0668.internalMethod07418() == this.internalField0237) {
            this.internalMethod04998();
        }
    };
    private final EventListener<PreHudRenderEvent> internalField0158 = preHudRenderEvent -> {
        if (!this.internalMethod02927(ItemTargetType.internalField0012)) {
            return;
        }
        boolean bl = this.internalField0668.internalMethod07418() == this.internalField0238;
        for (int i = 0; i < 2; ++i) {
            if (!this.internalField0637[i]) continue;
            EspGlowCaptureRenderer.internalMethod04936(this.internalField0553[i]);
            if (bl) {
                this.internalMethod04065(i);
                continue;
            }
            this.internalMethod04012(i);
        }
    };

    public FillEspFeature() {
        super("fill", new ItemTargetType[]{ItemTargetType.internalField0012}, EntityTargetType.internalField0964);
        this.internalField0237.select();
        this.internalField1066.select();
        for (ManagedFramebuffer typedValue245 : this.internalField0553) {
            typedValue245.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
        this.internalField0320.internalMethod03093();
        this.internalField0320.internalMethod06580(1);
        this.internalField0320.internalMethod06503(2.0f);
    }

    public ColorRGBA internalMethod03135() {
        return this.internalField0651.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
    }

    public boolean internalMethod04999() {
        return false;
    }

    public void internalMethod01398(HeldItemRenderer heldItemRenderer, AbstractClientPlayerEntity abstractClientPlayerEntity, ItemStack itemStack, ItemDisplayContext modelTransformationMode, boolean bl, MatrixStack matrixStack, int n) {
        if (!this.internalMethod02927(ItemTargetType.internalField0012)) {
            return;
        }
        if (itemStack == null || itemStack.isEmpty()) {
            return;
        }
        int n2 = this.internalMethod04013(bl);
        this.internalMethod05182(n2, matrixStack);
        internalField0277 = true;
        ManagedFramebuffer typedValue245 = this.internalMethod01464(n2);
        VertexConsumerProvider.Immediate immediate = internalField0149.getBufferBuilders().getEntityVertexConsumers();
        try {
            rockstar.client.render.LegacyRenderCompat.renderItem((LivingEntity)abstractClientPlayerEntity, itemStack, modelTransformationMode, bl, matrixStack, immediate, 0xF000F0);
            immediate.draw();
            this.internalField0637[n2] = true;
        }
        catch (Exception exception) {
            // empty catch block
        }
        typedValue245.internalMethod03248();
        internalField0277 = false;
    }

    public void internalMethod06931(BlockRenderManager blockRenderManager, BlockState blockState, MatrixStack matrixStack, int n) {
        if (!this.internalMethod02927(ItemTargetType.internalField0012)) {
            return;
        }
        if (blockState == null) {
            return;
        }
        int n2 = this.internalMethod04013(EspGlowCaptureRenderer.internalField1099);
        this.internalMethod05182(n2, matrixStack);
        internalField0277 = true;
        ManagedFramebuffer typedValue245 = this.internalMethod01464(n2);
        VertexConsumerProvider.Immediate immediate = internalField0149.getBufferBuilders().getEntityVertexConsumers();
        try {
            blockRenderManager.renderBlockAsEntity(blockState, matrixStack, (VertexConsumerProvider)immediate, 0xF000F0, n);
            immediate.draw();
            this.internalField0637[n2] = true;
        }
        catch (Exception exception) {
            // empty catch block
        }
        typedValue245.internalMethod03248();
        internalField0277 = false;
    }

    private int internalMethod04013(boolean bl) {
        return this.internalField0668.internalMethod07418() == this.internalField0238 && bl ? 1 : 0;
    }

    private ManagedFramebuffer internalMethod01464(int n) {
        ManagedFramebuffer typedValue245 = this.internalField0553[n];
        typedValue245.internalMethod02227(!this.internalField0636[n]);
        this.internalField0636[n] = true;
        return typedValue245;
    }

    private void internalMethod05182(int n, MatrixStack matrixStack) {
        try {
            Matrix4f matrix4f = new Matrix4f((Matrix4fc)RenderSystem.getProjectionMatrix()).mul((Matrix4fc)RenderSystem.getModelViewMatrix()).mul((Matrix4fc)matrixStack.peek().getPositionMatrix());
            Vector4f vector4f = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
            matrix4f.transform(vector4f);
            if (vector4f.w > 1.0E-4f) {
                this.internalField0615[n] = vector4f.x / vector4f.w * 0.5f + 0.5f;
                this.internalField0616[n] = vector4f.y / vector4f.w * 0.5f + 0.5f;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void internalMethod04012(int n) {
        int n2;
        float f = FillEspFeature.internalMethod04011(this.internalField0383.internalMethod08576() / 100.0f);
        if (f <= 0.001f) {
            return;
        }
        int n3 = rockstar.client.render.FramebufferCompat.glId(this.internalField0276 ? this.internalField0770.getColorAttachment() : internalField0149.getFramebuffer().getColorAttachment());
        int n4 = (int)this.internalField0382.internalMethod08576();
        if (n4 > 0) {
            this.internalMethod09154(n3);
            this.internalField0320.internalMethod06504(n4);
            this.internalField0320.internalMethod05694(this.internalField0769);
            n2 = this.internalField0320.internalMethod03125();
            if (n2 == 0) {
                n2 = n3;
            }
        } else {
            n2 = n3;
        }
        this.internalMethod06778(n, n2);
        ColorRGBA colorRGBA = this.internalMethod03135();
        float f2 = colorRGBA.getRed() / 255.0f * 2.4f;
        float f3 = colorRGBA.getGreen() / 255.0f * 2.4f;
        float f4 = colorRGBA.getBlue() / 255.0f * 2.4f;
        this.internalMethod04322(rockstar.client.render.FramebufferCompat.glId(this.internalField0553[n].getColorAttachment()), f2, f3, f4, f);
    }

    private void internalMethod04065(int n) {
        float f = FillEspFeature.internalMethod04011(this.internalField1141.internalMethod08576() / 100.0f);
        if (f <= 0.001f) {
            return;
        }
        ModeSetting.InternalType0088 nestedValue0042 = this.internalField0669.internalMethod07418();
        ItemCenterShader internalValue0012 = nestedValue0042 == this.internalField1067 ? RenderPipeline.internalField0337 : (nestedValue0042 == this.internalField1068 ? RenderPipeline.internalField1128 : RenderPipeline.internalField0336);
        if (internalValue0012 == null) {
            return;
        }
        float f2 = this.internalField1140.internalMethod08576();
        float f3 = (float)((double)(System.currentTimeMillis() % 1000000L) / 1000.0 * (double)f2);
        ColorRGBA colorRGBA = this.internalMethod03135();
        internalValue0012.internalMethod01220();
        internalValue0012.internalMethod03819(f3, colorRGBA, this.internalField0615[n], this.internalField0616[n]);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, this.internalField0553[n].getColorAttachmentView());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f);
        RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
    }

    private void internalMethod04322(int n, float f, float f2, float f3, float f4) {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture((int)0, (int)n);
        RenderSystem.setShaderColor((float)f, (float)f2, (float)f3, (float)f4);
        RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
    }

    private void internalMethod09154(int n) {
        this.internalField0769.internalMethod02227(true);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (int)n);
        RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
        RenderSystem.setShaderTexture((int)0, (int)0);
        this.internalField0769.internalMethod03248();
    }

    private void internalMethod04998() {
        Framebuffer framebuffer = internalField0149.getFramebuffer();
        if (framebuffer == null) {
            return;
        }
        int n = internalField0267.getScaledWidth();
        int n2 = internalField0267.getScaledHeight();
        RenderSystem.backupProjectionMatrix();
        Matrix4f matrix4f = new Matrix4f().setOrtho(0.0f, (float)n, (float)n2, 0.0f, 1000.0f, 21000.0f);
        RenderSystem.setProjectionMatrix((Matrix4f)matrix4f, (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.identity().translate(0.0f, 0.0f, -11000.0f);
        this.internalField0770.internalMethod02227(true);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, framebuffer.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
        RenderSystem.setShaderTexture((int)0, (int)0);
        this.internalField0770.internalMethod03248();
        matrix4fStack.popMatrix();
        RenderSystem.restoreProjectionMatrix();
        this.internalField0276 = true;
    }

    private void internalMethod06778(int n, int n2) {
        this.internalField0553[n].beginWrite(true);
        RenderSystem.enableBlend();
        boolean bl = this.internalField1261.internalMethod04496();
        if (bl) {
            RenderSystem.blendFuncSeparate(772, 0, 0, 1);
        } else {
            RenderSystem.blendFuncSeparate(774, 0, 0, 1);
        }
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        if (!bl && RenderPipeline.internalField0343 != null) {
            RenderPipeline.internalField0343.internalMethod01220();
            RenderPipeline.internalField0343.internalMethod02391(FillEspFeature.internalMethod04011(this.internalField1142.internalMethod08576() / 100.0f));
        } else {
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        }
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (int)n2);
        float f = internalField0267.getScaledWidth();
        float f2 = internalField0267.getScaledHeight();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(0.0f, 0.0f, 0.0f).texture(1.0f, 1.0f).color(-1);
        bufferBuilder.vertex(0.0f, f2, 0.0f).texture(1.0f, 0.0f).color(-1);
        bufferBuilder.vertex(f, f2, 0.0f).texture(0.0f, 0.0f).color(-1);
        bufferBuilder.vertex(f, 0.0f, 0.0f).texture(0.0f, 1.0f).color(-1);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.defaultBlendFunc();
        rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
    }

    private static float internalMethod04011(float f) {
        return f < 0.0f ? 0.0f : (f > 1.0f ? 1.0f : f);
    }

    @Generated
    public BooleanSetting internalMethod06337() {
        return this.internalField0650;
    }

    @Generated
    public ModeSetting internalMethod06396() {
        return this.internalField0668;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod01424() {
        return this.internalField0237;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod01637() {
        return this.internalField0238;
    }

    @Generated
    public BooleanSetting internalMethod07014() {
        return this.internalField0651;
    }

    @Generated
    public ColorSetting internalMethod06394() {
        return this.internalField0665;
    }

    @Generated
    public BooleanSetting internalMethod08646() {
        return this.internalField1261;
    }

    @Generated
    public SliderSetting internalMethod04099() {
        return this.internalField0383;
    }

    @Generated
    public SliderSetting internalMethod04849() {
        return this.internalField0382;
    }

    @Generated
    public SliderSetting internalMethod08216() {
        return this.internalField1142;
    }

    @Generated
    public ModeSetting internalMethod07067() {
        return this.internalField0669;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod07933() {
        return this.internalField1066;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod07970() {
        return this.internalField1067;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09064() {
        return this.internalField1068;
    }

    @Generated
    public SliderSetting internalMethod08349() {
        return this.internalField1140;
    }

    @Generated
    public SliderSetting internalMethod07722() {
        return this.internalField1141;
    }

    @Generated
    public ManagedFramebuffer[] internalMethod00382() {
        return this.internalField0553;
    }

    @Generated
    public ManagedFramebuffer internalMethod00863() {
        return this.internalField0769;
    }

    @Generated
    public ManagedFramebuffer internalMethod01598() {
        return this.internalField0770;
    }

    @Generated
    public boolean internalMethod08451() {
        return this.internalField0276;
    }

    @Generated
    public GlowBloomRenderer internalMethod02452() {
        return this.internalField0320;
    }

    @Generated
    public boolean[] internalMethod04010() {
        return this.internalField0637;
    }

    @Generated
    public boolean[] internalMethod04064() {
        return this.internalField0636;
    }

    @Generated
    public float[] internalMethod04009() {
        return this.internalField0615;
    }

    @Generated
    public float[] internalMethod04063() {
        return this.internalField0616;
    }

    @Generated
    public EventListener<Render3DEvent> internalMethod05001() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<PreHudRenderEvent> internalMethod06235() {
        return this.internalField0158;
    }
}

