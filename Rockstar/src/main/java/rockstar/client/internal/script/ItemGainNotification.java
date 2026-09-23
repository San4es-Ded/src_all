package rockstar.client.internal.script;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.internal.script.ToastNotification;
import rockstar.client.render.CornerRadii;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.MathUtils;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.internal.render.UiVertexBatch;
import rockstar.client.internal.render.PlainGlyphBatch;

public class ItemGainNotification
extends ToastNotification {
    private final String internalField0248;
    private final String internalField0247;
    private final ItemStack internalField0878;
    private final RegistryEntry<StatusEffect> internalField0151;
    private final ColorRGBA internalField0777;

    public ItemGainNotification(String string, ItemStack itemStack) {
        super(2500L);
        this.internalField0248 = string;
        this.internalField0247 = null;
        this.internalField0878 = itemStack.copy();
        this.internalField0151 = null;
        this.internalField0777 = null;
    }

    public ItemGainNotification(String string, String string2, ItemStack itemStack) {
        super(2500L);
        this.internalField0248 = string;
        this.internalField0247 = string2;
        this.internalField0878 = itemStack.copy();
        this.internalField0151 = null;
        Integer n = itemStack.getRarity().getFormatting().getColorValue();
        this.internalField0777 = n != null ? ColorRGBA.fromInt(n) : new ColorRGBA(255.0f, 85.0f, 85.0f);
    }

    public ItemGainNotification(String string, String string2, ItemStack itemStack, ColorRGBA colorRGBA) {
        super(2500L);
        this.internalField0248 = string;
        this.internalField0247 = string2;
        this.internalField0878 = itemStack.copy();
        this.internalField0151 = null;
        this.internalField0777 = colorRGBA;
    }

    public ItemGainNotification(String string, String string2, RegistryEntry<StatusEffect> registryEntry) {
        super(2500L);
        this.internalField0248 = string;
        this.internalField0247 = string2;
        this.internalField0878 = null;
        this.internalField0151 = registryEntry;
        this.internalField0777 = ColorRGBA.fromInt(((StatusEffect)registryEntry.value()).getColor());
    }

    @Override
    public final void internalMethod06653(CustomDrawContext customDrawContext, float f) {
        SizedFont typedValue020 = Fonts.internalField0449.internalMethod01432(7.0f);
        float f2 = this.internalField0247 != null && this.internalField0248.contains(this.internalField0247) ? typedValue020.internalMethod00965(this.internalField0248) : typedValue020.internalMethod00965(this.internalField0248);
        float f3 = f2 + 26.0f;
        this.internalField1321.internalMethod06645(Easing.internalField1327);
        this.internalField1321.internalMethod07061(300L);
        float f4 = (float)customDrawContext.getScaledWindowWidth() / 2.0f - f3 / 2.0f;
        float f5 = (float)customDrawContext.getScaledWindowHeight() - 90.0f - this.internalField1321.internalMethod07059(f);
        float f6 = 20.0f;
        float f7 = 10.0f;
        float f8 = 5.0f;
        float f9 = this.internalField0808.internalMethod02881();
        int n = (int)(255.0f * f9);
        HudRenderUtils.internalMethod08976(customDrawContext.getMatrices(), f4 + f3 / 2.0f, f5 + f6 / 2.0f, 0.5f + 0.5f * f9);
        if (InterfaceModule.internalMethod09719()) {
            customDrawContext.drawLiquidGlass(f4, f5, f3, f6, 7.0f, 0.08f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(255.0f * f9 * InterfaceModule.internalMethod07584()));
            customDrawContext.drawSquircle(f4, f5, f3, f6, 7.0f, CornerRadii.internalMethod03908(7.0f), ThemeColors.internalMethod07738().withAlpha(255.0f * MathUtils.internalMethod02587(ThemeColors.internalMethod02435().internalMethod08704(), ThemeColors.internalMethod02435().internalMethod08705(), InterfaceModule.internalMethod07584()) * f9));
        } else {
            customDrawContext.drawBlurredRect(f4, f5, f3, f6, 45.0f, 7.0f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(255.0f * f9 * InterfaceModule.internalMethod07585()));
            customDrawContext.drawSquircle(f4, f5, f3, f6, 7.0f, CornerRadii.internalMethod03908(7.0f), new ColorRGBA(0.0f, 0.0f, 0.0f).withAlpha((int)(140.25f * f9)));
        }
        float f10 = f4 + f8;
        float f11 = f5 + (f6 - f7) / 2.0f;
        float f12 = RenderSystem.getShaderColor()[3];
        if (this.internalField0151 != null) {
            Identifier effectTexture = net.minecraft.client.gui.hud.InGameHud.getEffectTexture(this.internalField0151);
            // Effect identifiers are sprites in Minecraft's GUI atlas, not
            // standalone textures. Queue the sprite in the shared GUI state so
            // vanilla resolves its atlas UVs and draws it after our immediate
            // notification background has been flushed.
            customDrawContext.drawGuiTexture(
                RenderPipelines.GUI_TEXTURED,
                effectTexture,
                Math.round(f10),
                Math.round(f11),
                Math.round(f7),
                Math.round(f7),
                f9
            );
        } else if (this.internalField0878 != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f9);
            HudRenderUtils.internalMethod08976(customDrawContext.getMatrices(), f10, f11, f7 / 16.0f);
            customDrawContext.drawItem(this.internalField0878, (int)f10, (int)f11);
            HudRenderUtils.internalMethod00012(customDrawContext.getMatrices());
            MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_FLAT);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f12);
            RenderSystem.disableBlend();
        }
        float f13 = f4 + f7 + 10.0f;
        float f14 = f5 + (f6 - typedValue020.internalMethod04890()) / 2.0f;
        PlainGlyphBatch typedValue252 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, typedValue020.internalMethod01335());
        ColorRGBA colorRGBA = ColorRGBA.WHITE.withAlpha(n);
        if (this.internalField0247 != null && this.internalField0777 != null) {
            int n2 = this.internalField0248.indexOf(this.internalField0247);
            if (n2 != -1) {
                String string = this.internalField0248.substring(0, n2);
                String string2 = this.internalField0248.substring(n2 + this.internalField0247.length());
                float f15 = f13;
                if (!string.isEmpty()) {
                    customDrawContext.drawText(typedValue020, string, f15, f14, colorRGBA);
                    f15 += typedValue020.internalMethod00965(string);
                }
                customDrawContext.drawText(typedValue020, this.internalField0247, f15, f14, this.internalField0777.withAlpha(n));
                f15 += typedValue020.internalMethod00965(this.internalField0247);
                if (!string2.isEmpty()) {
                    customDrawContext.drawText(typedValue020, string2, f15, f14, colorRGBA);
                }
            } else {
                customDrawContext.drawText(typedValue020, this.internalField0248, f13, f14, colorRGBA);
            }
        } else {
            customDrawContext.drawText(typedValue020, this.internalField0248, f13, f14, colorRGBA);
        }
        ((UiVertexBatch)typedValue252).internalMethod09053();
        HudRenderUtils.internalMethod00012(customDrawContext.getMatrices());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}
