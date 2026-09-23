package ru.haron.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.module.ModuleManager;
import haron.modules.hud.ClientColor;
import haron.modules.utilities.Optimizations;
import haron.player.HaronPlayerTracker;
import haron.player.PlayerCapeTracker;
import haron.render.icons.HaronIcons;
import haron.render.SelfNametagRenderState;
import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class})
public abstract class EntityRendererMixin<ConfigTextDialog extends Entity, ConfigBoundsDialog extends EntityRenderState> {
    @Unique
    private static final int ICON_SIZE = 8;
    @Unique
    private static final int ICON_PADDING = 2;
    @Unique
    private static final int BACKGROUND_HEIGHT = 9;
    @Unique
    private static final float BG_Z = 0.0f;
    @Unique
    private static final float ICON_Z = 0.0f;
    @Unique
    private static final float SEE_THROUGH_ALPHA = 0.1254902f;
    @Unique
    private static final float SNEAK_DARKEN = 0.5f;
    @Unique
    private boolean shouldRenderIcon = false;
    @Unique
    private boolean isRenderingName = false;
    @Unique
    private int yOffset = 0;
    @Unique
    private float textWidth = 0.0f;
    @Unique
    private boolean isEntitySneaking = false;
    @Unique
    private boolean isSeeThroughMode = false;
    @Unique
    private static final ConcurrentHashMap<String, long[]> ICON_CACHE = new ConcurrentHashMap();
    @Unique
    private static final long ICON_CACHE_TTL_MS = 2000L;

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Unique
    private static boolean isHaronUser(String name) {
        long now = System.currentTimeMillis();
        long[] cached = ICON_CACHE.get(name);
        if (cached != null && now - cached[0] < 2000L) {
            return cached[1] == 1L;
        }
        boolean has = PlayerCapeTracker.c().a(name) || HaronPlayerTracker.get().has(name);
        ICON_CACHE.put(name, new long[]{now, has ? 1L : 0L});
        return has;
    }

    @Inject(method={"renderLabelIfPresent"}, at={@At(value="HEAD")}, cancellable=true)
    private void haron$cullDistantLabels(ConfigBoundsDialog configboundsdialog, Text TextVar, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, int i, CallbackInfo callbackInfo) {
        if (ModuleManager.SELF_NAMETAG.k() && !SelfNametagRenderState.isSelfNametagRender && configboundsdialog instanceof PlayerEntityRenderState) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                PlayerEntityRenderState ps = (PlayerEntityRenderState)configboundsdialog;
                if (ps.id == client.player.getId() && !client.options.getPerspective().isFirstPerson()) {
                    callbackInfo.cancel();
                }
            }
        }
        if (!Optimizations.cullNametags()) {
            return;
        }
        ConfigBoundsDialog state = configboundsdialog;
        if (((EntityRenderState)state).squaredDistanceToCamera > Optimizations.maxNametagDistanceSq()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderLabelIfPresent"}, at={@At(value="HEAD")})
    private void onLabelStart(ConfigBoundsDialog configboundsdialog, Text TextVar, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, int i, CallbackInfo callbackInfo) {
        this.shouldRenderIcon = false;
        this.isRenderingName = true;
        this.isEntitySneaking = ((EntityRenderState)configboundsdialog).sneaking;
        this.isSeeThroughMode = !this.isEntitySneaking;
        this.yOffset = 0;
        if (configboundsdialog instanceof PlayerEntityRenderState) {
            PlayerEntityRenderState PlayerEntityRenderStateVar = (PlayerEntityRenderState)configboundsdialog;
            String playerName = PlayerEntityRenderStateVar.name;
            if (playerName != null) {
                String labelStr;
                if ("deadmau5".equals(playerName)) {
                    this.yOffset = -10;
                }
                if (EntityRendererMixin.isHaronUser(playerName) && (labelStr = TextVar.getString()).contains(playerName)) {
                    this.shouldRenderIcon = true;
                }
            }
        }
        this.textWidth = this.getTextRenderer().getWidth((StringVisitable)TextVar);
    }

    @Unique
    private float shiftedX(float f) {
        return this.shouldRenderIcon && this.isRenderingName ? f + 5.0f : f;
    }

    @ModifyArg(method={"renderLabelIfPresent"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal=0), index=1)
    private float shiftTextFirst(float f) {
        return this.shiftedX(f);
    }

    @ModifyArg(method={"renderLabelIfPresent"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal=1), index=1)
    private float shiftTextSecond(float f) {
        return this.shiftedX(f);
    }

    @Unique
    private int maybeKillBackground(int i) {
        if (this.shouldRenderIcon && this.isRenderingName) {
            return 0;
        }
        if (SelfNametagRenderState.isSelfNametagRender && !ModuleManager.SELF_NAMETAG.showBg.a()) {
            return 0;
        }
        return i;
    }

    @ModifyArg(method={"renderLabelIfPresent"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal=0), index=8)
    private int killBgFirst(int i) {
        return this.maybeKillBackground(i);
    }

    @ModifyArg(method={"renderLabelIfPresent"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal=1), index=8)
    private int killBgSecond(int i) {
        return this.maybeKillBackground(i);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"renderLabelIfPresent"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal=0)})
    private void renderCustomBackgroundAndIcon(ConfigBoundsDialog configboundsdialog, Text TextVar, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, int i, CallbackInfo callbackInfo) {
        if (this.shouldRenderIcon && this.isRenderingName) {
            float f = 10.0f + this.textWidth;
            float f2 = -f / 2.0f;
            int iGetTextBackgroundOpacity = ((int)(MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255.0f) & 0xFF) << 24;
            Matrix4f matrix4fGetPositionMatrix = MatrixStackVar.peek().getPositionMatrix();
            RenderSystem.depthMask((boolean)false);
            try {
                this.renderCustomBackground(matrix4fGetPositionMatrix, f2, f, iGetTextBackgroundOpacity, this.isSeeThroughMode);
                float f3 = (float)this.yOffset + 0.5f;
                if (this.isSeeThroughMode) {
                    this.renderIconTwoPass(matrix4fGetPositionMatrix, f2, f3);
                } else {
                    this.renderIconSingleNormal(matrix4fGetPositionMatrix, f2, f3);
                }
            }
            finally {
                RenderSystem.depthMask((boolean)true);
                RenderSystem.depthFunc((int)515);
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            }
        }
    }

    @Unique
    private void renderCustomBackground(Matrix4f matrix4f, float f, float f2, int i, boolean z) {
        int i2 = i >>> 24 & 0xFF;
        int i3 = i >>> 16 & 0xFF;
        int i4 = i >>> 8 & 0xFF;
        int i5 = i & 0xFF;
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc((int)(z ? 519 : 515));
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder BufferBuilderVarBegin = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        float f3 = f - 1.0f;
        float f4 = f + f2 + 1.0f;
        float f5 = this.yOffset - 1;
        float f6 = this.yOffset + 9 + 1;
        BufferBuilderVarBegin.vertex(matrix4f, f3, f5, 0.0f).color(i3, i4, i5, i2);
        BufferBuilderVarBegin.vertex(matrix4f, f3, f6, 0.0f).color(i3, i4, i5, i2);
        BufferBuilderVarBegin.vertex(matrix4f, f4, f6, 0.0f).color(i3, i4, i5, i2);
        BufferBuilderVarBegin.vertex(matrix4f, f4, f5, 0.0f).color(i3, i4, i5, i2);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)BufferBuilderVarBegin.end());
    }

    @Unique
    private void renderIconTwoPass(Matrix4f matrix4f, float f, float f2) {
        Identifier IdentifierVar = HaronIcons.get("logo");
        Color cc = ClientColor.currentColor();
        float cr = (float)cc.getRed() / 255.0f;
        float cg = (float)cc.getGreen() / 255.0f;
        float cb = (float)cc.getBlue() / 255.0f;
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture((int)0, (Identifier)IdentifierVar);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc((int)519);
        RenderSystem.defaultBlendFunc();
        this.drawIconQuad(matrix4f, f, f2, cr, cg, cb, 0.1254902f);
        RenderSystem.depthFunc((int)515);
        this.drawIconQuad(matrix4f, f, f2, cr, cg, cb, 1.0f);
    }

    @Unique
    private void renderIconSingleNormal(Matrix4f matrix4f, float f, float f2) {
        Identifier IdentifierVar = HaronIcons.get("logo");
        Color cc = ClientColor.currentColor();
        float cr = (float)cc.getRed() / 255.0f * 0.5f;
        float cg = (float)cc.getGreen() / 255.0f * 0.5f;
        float cb = (float)cc.getBlue() / 255.0f * 0.5f;
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc((int)515);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture((int)0, (Identifier)IdentifierVar);
        this.drawIconQuad(matrix4f, f, f2, cr, cg, cb, 1.0f);
    }

    @Unique
    private void drawIconQuad(Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6) {
        int i = (int)(f3 * 255.0f);
        int i2 = (int)(f4 * 255.0f);
        int i3 = (int)(f5 * 255.0f);
        int i4 = (int)(f6 * 255.0f);
        BufferBuilder BufferBuilderVarBegin = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        BufferBuilderVarBegin.vertex(matrix4f, f, f2, 0.0f).texture(0.0f, 0.0f).color(i, i2, i3, i4);
        BufferBuilderVarBegin.vertex(matrix4f, f, f2 + 8.0f, 0.0f).texture(0.0f, 1.0f).color(i, i2, i3, i4);
        BufferBuilderVarBegin.vertex(matrix4f, f + 8.0f, f2 + 8.0f, 0.0f).texture(1.0f, 1.0f).color(i, i2, i3, i4);
        BufferBuilderVarBegin.vertex(matrix4f, f + 8.0f, f2, 0.0f).texture(1.0f, 0.0f).color(i, i2, i3, i4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)BufferBuilderVarBegin.end());
    }
}

