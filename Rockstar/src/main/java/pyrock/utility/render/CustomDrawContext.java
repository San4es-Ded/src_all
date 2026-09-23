package pyrock.utility.render;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.Rect;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.SizedFont;
import rockstar.client.internal.core.IconFontRegistry;
import rockstar.client.render.CornerRadii;
import rockstar.client.internal.render.SlugFontRenderer;
import rockstar.client.ui.QuadColorGradient;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.render.SmoothItemLayerHelper;
import rockstar.client.render.UiBatchRenderer;
import rockstar.client.internal.core.IconSprite;
import rockstar.client.internal.ui.RockstarGuiItemRenderer;

public class CustomDrawContext
extends DrawContext
implements MinecraftClientAccess {
    private final DrawContext originalContext;
    private int itemBatchDepth;
    private boolean itemBatchHasVertices;
    private boolean itemBatchFlatLighting;
    private float[] itemBatchShaderColor;

    public CustomDrawContext(DrawContext drawContext) {
        super(MinecraftClient.getInstance(), drawContext.state, -1, -1);
        this.originalContext = drawContext;
        this.getMatrices().set(drawContext.getMatrices());
    }

    public static CustomDrawContext of(DrawContext drawContext) {
        return new CustomDrawContext(drawContext);
    }

    public InternalType0486 beginItemBatch() {
        if (this.itemBatchDepth++ == 0) {
            UiBatchRenderer.internalMethod02576();
            this.itemBatchHasVertices = false;
            this.itemBatchShaderColor = null;
        }
        return new InternalType0486();
    }

    void endItemBatch() {
        if (this.itemBatchDepth <= 0) {
            return;
        }
        if (--this.itemBatchDepth == 0) {
            this.itemBatchShaderColor = null;
        }
    }

    private void flushItemBatch() {
        this.itemBatchHasVertices = false;
        this.itemBatchShaderColor = null;
    }

    private boolean sameShaderColor(float[] fArray) {
        if (this.itemBatchShaderColor == null || fArray == null) {
            return false;
        }
        return Float.compare(this.itemBatchShaderColor[0], fArray[0]) == 0 && Float.compare(this.itemBatchShaderColor[1], fArray[1]) == 0 && Float.compare(this.itemBatchShaderColor[2], fArray[2]) == 0 && Float.compare(this.itemBatchShaderColor[3], fArray[3]) == 0;
    }

    public void drawEntity(int n, int n2, int n3, int n4, int n5, float f, float f2, float f3, LivingEntity livingEntity) {
        InventoryScreen.drawEntity(this, n, n2, n3, n4, n5, f, f2, f3, livingEntity);
    }

    public void drawClientRect(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.drawClientRect(f, f2, f3, f4, f5, f6, f7, ThemeColors.internalMethod02435().internalMethod01359(), false);
    }

    public void drawClientRect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.drawClientRect(f, f2, f3, f4, f5, f6, f7, f8, false);
    }

    public void drawClientRect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, boolean bl) {
        this.drawClientRect(f, f2, f3, f4, f5, f6, f7, f8, bl, false);
    }

    public void drawClientRect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, boolean bl, boolean bl2) {
        CornerRadii typedParameter015 = CornerRadii.internalMethod03908(f8);
        if (InterfaceModule.internalMethod09917()) {
            this.drawBlurredRect(f, f2, f3, f4, 45.0f, f7, typedParameter015, ColorRGBA.WHITE.withAlpha(255.0f * f5 * InterfaceModule.internalMethod07585()));
        }
        if (InterfaceModule.internalMethod09719() && !bl2) {
            this.drawLiquidGlass(f, f2, f3, f4, f7, 0.08f + 0.07f * f6, typedParameter015, ColorRGBA.WHITE.withAlpha(255.0f * f5 * InterfaceModule.internalMethod07584()));
        }
        this.drawSquircle(f, f2, f3, f4, f7, typedParameter015, ThemeColors.internalMethod07738().mulAlpha(MathUtils.internalMethod02587(1.0, 0.4f, bl2 ? 0.0 : (double)InterfaceModule.internalMethod07584())));
        if (bl) {
            this.drawSquircleBorder(f, f2, f3, f4, 0.5f, f7, typedParameter015, ThemeColors.internalField1616);
        }
    }

    public void pushMatrix() {
        this.getMatrices().pushMatrix();
    }

    public void popMatrix() {
        this.getMatrices().popMatrix();
    }

    public void drawRect(float f, float f2, float f3, float f4, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod02688(this.getMatrices(), f, f2, f3, f4, colorRGBA);
    }

    public void drawLine(Vec2f vec2f, Vec2f vec2f2, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod01986(this.getMatrices(), vec2f, vec2f2, colorRGBA);
    }

    public void drawBezier(Vec2f vec2f, Vec2f vec2f2, Vec2f vec2f3, Vec2f vec2f4, ColorRGBA colorRGBA, int n) {
        RenderPipeline.internalMethod05338(this.getMatrices(), vec2f, vec2f2, vec2f3, vec2f4, colorRGBA, n);
    }

    public void drawSmoothBezier(float f, float f2, float f3, float f4, Vec2f vec2f, Vec2f vec2f2, Vec2f vec2f3, Vec2f vec2f4, float f5, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod03090(this.getMatrices(), f, f2, f3, f4, vec2f, vec2f2, vec2f3, vec2f4, f5, colorRGBA);
    }

    public void drawAreaGradient(float[] fArray, float[] fArray2, float f, ColorRGBA colorRGBA, ColorRGBA colorRGBA2) {
        RenderPipeline.internalMethod07554(this.getMatrices(), fArray, fArray2, f, colorRGBA, colorRGBA2);
    }

    public void drawSquircle(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod04182(this.getMatrices(), f, f2, f3, f4, f5, typedParameter015, colorRGBA);
    }

    public void drawSquircle(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, QuadColorGradient typedValue016) {
        RenderPipeline.internalMethod04108(this.getMatrices(), f, f2, f3, f4, f5, typedParameter015, typedValue016);
    }

    public void drawSquircle(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, ColorRGBA colorRGBA, ColorRGBA colorRGBA2, ColorRGBA colorRGBA3, ColorRGBA colorRGBA4) {
        RenderPipeline.internalMethod01759(this.getMatrices(), f, f2, f3, f4, f5, typedParameter015, colorRGBA, colorRGBA2, colorRGBA3, colorRGBA4);
    }

    public void drawRoundedRect(float f, float f2, float f3, float f4, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod02035(this.getMatrices(), f, f2, f3, f4, typedParameter015, colorRGBA);
    }

    public void drawRoundedRect(float f, float f2, float f3, float f4, CornerRadii typedParameter015, QuadColorGradient typedValue016) {
        RenderPipeline.internalMethod03784(this.getMatrices(), f, f2, f3, f4, typedParameter015, typedValue016);
    }

    public void drawRocknetGlass(Rect rect, float f, float f2, float f3, float f4, float f5, float f6, CornerRadii typedParameter015, boolean bl) {
        typedParameter015 = new CornerRadii(typedParameter015.internalMethod05337() * f5 / 2.0f, typedParameter015.internalMethod05340() * f5 / 2.0f, typedParameter015.internalMethod08942() * f5 / 2.0f, typedParameter015.internalMethod08939() * f5 / 2.0f);
        ColorRGBA colorRGBA = ColorRGBA.WHITE;
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(RockstarClient.id(bl ? "rocknet/background.png" : "rocknet/blur.png"));
        RenderPipeline.internalMethod01221(rect, this.getMatrices(), f, f2, f3, f4, typedParameter015, colorRGBA, colorRGBA.getAlpha() / 255.0f, f4 == 240.0f ? 100 : 50, colorRGBA.withAlpha(255.0f), 1.0f, true, 0.0f, f6, f5, rockstar.client.render.FramebufferCompat.glId(abstractTexture.getGlTexture()));
    }

    public void drawLiquidGlass(float f, float f2, float f3, float f4, float f5, float f6, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        typedParameter015 = new CornerRadii(typedParameter015.internalMethod05337() * f5 / 2.0f, typedParameter015.internalMethod05340() * f5 / 2.0f, typedParameter015.internalMethod08942() * f5 / 2.0f, typedParameter015.internalMethod08939() * f5 / 2.0f);
        RenderPipeline.internalMethod03329(this.getMatrices(), f - 5.0f * InterfaceModule.internalMethod07585(), f2 - 5.0f * InterfaceModule.internalMethod07585(), f3 + 10.0f * InterfaceModule.internalMethod07585(), f4 + 10.0f * InterfaceModule.internalMethod07585(), typedParameter015, colorRGBA, colorRGBA.getAlpha() / 255.0f * InterfaceModule.internalMethod07584(), (ThemeColors.internalMethod02435().internalMethod08718() + (float)(f4 == 240.0f ? 2 : 1)) * InterfaceModule.internalMethod07584(), colorRGBA.withAlpha(255.0f), 1.0f, true, 0.0f, (f6 == 0.08f ? ThemeColors.internalMethod02435().internalMethod08719() : f6) * InterfaceModule.internalMethod07584(), f5, false);
    }

    public void drawLiquidGlass(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, ColorRGBA colorRGBA, boolean bl) {
        typedParameter015 = new CornerRadii(typedParameter015.internalMethod05337() * f5 / 2.0f, typedParameter015.internalMethod05340() * f5 / 2.0f, typedParameter015.internalMethod08942() * f5 / 2.0f, typedParameter015.internalMethod08939() * f5 / 2.0f);
        RenderPipeline.internalMethod03329(this.getMatrices(), f, f2, f3, f4, typedParameter015, colorRGBA, colorRGBA.getAlpha() / 255.0f, f4 == 240.0f ? 100.0f : 50.0f, colorRGBA.withAlpha(255.0f), 1.0f, true, 0.0f, 0.08f, f5, bl);
    }

    public void drawLoadingRect(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod05317(this.getMatrices(), f, f2, f3, f4, f5, typedParameter015, colorRGBA);
    }

    public void drawRoundedBorder(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod08588(this.getMatrices(), f, f2, f3, f4, f5, typedParameter015, colorRGBA);
    }

    public void drawDashedBorder(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, float f6, float f7, ColorRGBA colorRGBA, float f8, float f9, float f10, float f11) {
        RenderPipeline.internalMethod01338(this.getMatrices(), f, f2, f3, f4, f5, typedParameter015, f6, f7, colorRGBA, f8, f9, f10, f11);
    }

    public void drawSquircleBorder(float f, float f2, float f3, float f4, float f5, float f6, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod06077(this.getMatrices(), f, f2, f3, f4, f5, f6, typedParameter015, colorRGBA);
    }

    public void drawTexture(Identifier identifier, Rect rect) {
        this.drawTexture(identifier, rect, ColorRGBA.WHITE);
    }

    public void drawTexture(Identifier identifier, Rect rect, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod00463(this.getMatrices(), identifier, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), colorRGBA);
    }

    public void drawTexture(Identifier identifier, float f, float f2, float f3, float f4) {
        RenderPipeline.internalMethod00463(this.getMatrices(), identifier, f, f2, f3, f4, ColorRGBA.WHITE);
    }

    public void drawTexture(Identifier identifier, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod01117(this.getMatrices(), identifier, f, f2, f3, f4, f5, f6, f7, f8, colorRGBA);
    }

    public void drawTexture(Identifier identifier, float f, float f2, float f3, float f4, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod00463(this.getMatrices(), identifier, f, f2, f3, f4, colorRGBA);
    }

    public void drawSprite(IconSprite typedValue257, float f, float f2, float f3, float f4, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod02217(this.getMatrices(), typedValue257, f, f2, f3, f4, colorRGBA);
    }

    public void drawRoundedTexture(Identifier identifier, float f, float f2, float f3, float f4, CornerRadii typedParameter015) {
        RenderPipeline.internalMethod06642(this.getMatrices(), identifier, f, f2, f3, f4, typedParameter015);
    }

    public void drawRoundedTexture(Identifier identifier, float f, float f2, float f3, float f4, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod06728(this.getMatrices(), identifier, f, f2, f3, f4, typedParameter015, colorRGBA);
    }

    public void drawShadow(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod08818(this.getMatrices(), f, f2, f3, f4, f5, typedParameter015, colorRGBA);
    }

    public void drawBlurredRect(float f, float f2, float f3, float f4, float f5, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod08703(this.getMatrices(), f, f2, f3, f4, f5, typedParameter015, colorRGBA);
    }

    public void drawGlobalsBlur(Rect rect, float f, float f2, float f3, float f4, CornerRadii typedParameter015, float f5) {
        RenderPipeline.internalMethod05578(this.getMatrices(), rect, f, f2, f3, f4, typedParameter015, f5);
    }

    public void drawGlobalsBlur(Rect rect, float f, float f2, float f3, float f4, CornerRadii typedParameter015) {
        RenderPipeline.internalMethod05578(this.getMatrices(), rect, f, f2, f3, f4, typedParameter015, 1.0f);
    }

    public void drawBlurredRect(float f, float f2, float f3, float f4, float f5, float f6, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod06570(this.getMatrices(), f, f2, f3, f4, f5, f6, typedParameter015, colorRGBA);
    }

    public void drawBackdropBlur(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod01599(this.getMatrices(), f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, typedParameter015, colorRGBA);
    }

    public void drawText(SizedFont typedValue020, String string, float f, float f2, ColorRGBA colorRGBA) {
        SlugFontRenderer.internalMethod07344(typedValue020.internalMethod01335(), string, typedValue020.internalMethod07850(), colorRGBA.getRGB(), GuiMatrixCompat.toMatrix4f(this.getMatrices()), f, f2, 0.0f);
    }

    public void drawText(SizedFont typedValue020, Text text, float f, float f2) {
        SlugFontRenderer.internalMethod05909(typedValue020.internalMethod01335(), text, typedValue020.internalMethod07850(), GuiMatrixCompat.toMatrix4f(this.getMatrices()), f, f2, 0.0f);
    }

    public void drawFadeoutText(SizedFont typedValue020, String string, float f, float f2, ColorRGBA colorRGBA, float f3, float f4) {
        SlugFontRenderer.internalMethod01151(typedValue020.internalMethod01335(), string, typedValue020.internalMethod07850(), colorRGBA.getRGB(), GuiMatrixCompat.toMatrix4f(this.getMatrices()), f, f2, 0.0f, true, f3, f4);
    }

    public void drawFadeoutText(SizedFont typedValue020, String string, float f, float f2, ColorRGBA colorRGBA, float f3, float f4, float f5) {
        SlugFontRenderer.internalMethod03844(typedValue020.internalMethod01335(), string, typedValue020.internalMethod07850(), colorRGBA.getRGB(), GuiMatrixCompat.toMatrix4f(this.getMatrices()), f, f2, 0.0f, true, f3, f4, f5);
    }

    public void drawFadeText(SizedFont typedValue020, String string, float f, float f2, ColorRGBA colorRGBA, float f3, float f4, float f5) {
        float f6 = Math.max(1.0f, f5);
        float f7 = Math.max(0.0f, f3) / f6;
        float f8 = (f6 - Math.max(0.0f, f4)) / f6;
        SlugFontRenderer.internalMethod05774(typedValue020.internalMethod01335(), string, typedValue020.internalMethod07850(), colorRGBA.getRGB(), GuiMatrixCompat.toMatrix4f(this.getMatrices()), f, f2, 0.0f, true, f8, 1.0f, f6, 0.0f, f7);
    }

    public void drawCenteredText(SizedFont typedValue020, String string, float f, float f2, ColorRGBA colorRGBA) {
        this.drawText(typedValue020, string, f - typedValue020.internalMethod01335().internalMethod05670(string, typedValue020.internalMethod07850()) / 2.0f, f2, colorRGBA);
    }

    public void drawTextWithShadow(SizedFont typedValue020, String string, float f, float f2, ColorRGBA colorRGBA, ColorRGBA colorRGBA2, float f3, float f4, float f5) {
        if (colorRGBA2.getAlpha() > 1.0f) {
            float f6 = Math.max(1.0f, Math.min(10.0f, f5));
            float f7 = 0.0f;
            SlugFontRenderer.internalMethod04039(typedValue020.internalMethod01335(), string, typedValue020.internalMethod07850(), colorRGBA2.getRGB(), GuiMatrixCompat.toMatrix4f(this.getMatrices()), f + f3, f2 + f4, 0.0f, f7, f6);
        }
        this.drawText(typedValue020, string, f, f2, colorRGBA);
    }

    public void drawCenteredTextWithShadow(SizedFont typedValue020, String string, float f, float f2, ColorRGBA colorRGBA, ColorRGBA colorRGBA2, float f3, float f4, float f5) {
        this.drawTextWithShadow(typedValue020, string, f - typedValue020.internalMethod01335().internalMethod05670(string, typedValue020.internalMethod07850()) / 2.0f, f2, colorRGBA, colorRGBA2, f3, f4, f5);
    }

    public void drawRightText(SizedFont typedValue020, String string, float f, float f2, ColorRGBA colorRGBA) {
        this.drawText(typedValue020, string, f - typedValue020.internalMethod01335().internalMethod05670(string, typedValue020.internalMethod07850()), f2, colorRGBA);
    }

    public void drawIcon(String string, float f, float f2, float f3) {
        this.drawIcon(string, f, f2, f3, ColorRGBA.WHITE);
    }

    public void drawIcon(String string, float f, float f2, float f3, ColorRGBA colorRGBA) {
        Integer n = IconFontRegistry.internalMethod06164(string);
        if (n == null) {
            Object object = string.endsWith(".png") ? string : "icons/" + string + ".png";
            this.drawTexture(RockstarClient.id((String)object), f, f2, f3, f3, colorRGBA);
            return;
        }
        SlugFontRenderer.internalMethod03024(IconFontRegistry.internalMethod05006(), n, f, f2, f3, colorRGBA.getRGB(), GuiMatrixCompat.toMatrix4f(this.getMatrices()));
    }

    public void drawItem(Item item, float f, float f2, float f3) {
        this.drawItem(item.getDefaultStack(), f, f2, f3);
    }

    public void drawItem(ItemStack itemStack, float f, float f2, float f3) {
        UiBatchRenderer.internalMethod02576();
        this.getMatrices().pushMatrix();
        this.getMatrices().translate(f, f2);
        this.getMatrices().scale(f3, f3);
        RockstarGuiItemRenderer.enqueue(this, itemStack, 0, 0, 0, RenderSystem.getShaderColor()[3]);
        this.getMatrices().popMatrix();
    }

    public void drawHead(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float f2, float f3, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod01072(this.getMatrices(), abstractClientPlayerEntity, f, f2, f3, typedParameter015, colorRGBA);
    }

    public void drawHead(LivingEntity livingEntity, float f, float f2, float f3, CornerRadii typedParameter015, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod03763(this.getMatrices(), livingEntity, f, f2, f3, typedParameter015, colorRGBA);
    }

    public void drawArc(float f, float f2, float f3, float f4, float f5, float f6, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod01924(this.getMatrices(), f, f2, f3, f4, f5, f6, colorRGBA);
    }

    public void drawArc(float f, float f2, float f3, float f4, float f5, float f6, ColorRGBA colorRGBA, boolean bl) {
        RenderPipeline.internalMethod02355(this.getMatrices(), f, f2, f3, f4, f5, f6, colorRGBA, bl);
    }

    public void drawCircleProgress(float f, float f2, float f3, float f4, float f5, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod02451(this.getMatrices(), f, f2, f3, f4, f5, colorRGBA);
    }

    public void drawBatchItem(ItemStack itemStack, float f, float f2, float f3) {
        this.drawBatchItem(itemStack, f, f2, f3, 0);
    }

    public void drawBatchItem(ItemStack itemStack, float f, float f2, float f3, int n) {
        this.prepareItemDraw();
        this.getMatrices().pushMatrix();
        this.getMatrices().translate(f, f2);
        this.getMatrices().scale(f3, f3);
        this.drawBatchItemInternal((LivingEntity)CustomDrawContext.internalField0149.player, (World)CustomDrawContext.internalField0149.world, itemStack, 0.0f, 0.0f, 0, n);
        this.getMatrices().popMatrix();
    }

    public void drawBatchItem(ItemStack itemStack, float f, float f2) {
        this.drawBatchItem(itemStack, f, f2, 0);
    }

    public void drawBatchItem(ItemStack itemStack, float f, float f2, int n) {
        this.prepareItemDraw();
        this.drawBatchItemInternal((LivingEntity)CustomDrawContext.internalField0149.player, (World)CustomDrawContext.internalField0149.world, itemStack, f, f2, 0, n);
    }

    private void prepareItemDraw() {
        if (this.itemBatchDepth > 0 && UiBatchRenderer.internalMethod02581()) {
            this.flushItemBatch();
        }
        UiBatchRenderer.internalMethod02576();
    }

    private void drawBatchItemInternal(@Nullable LivingEntity livingEntity, @Nullable World world, ItemStack itemStack, float f, float f2, int n) {
        this.drawBatchItemInternal(livingEntity, world, itemStack, f, f2, n, 0);
    }

    private void drawBatchItemInternal(@Nullable LivingEntity livingEntity, @Nullable World world, ItemStack itemStack, float f, float f2, int n, int n2) {
        if (!itemStack.isEmpty()) {
            RockstarGuiItemRenderer.enqueue(this, itemStack, Math.round(f), Math.round(f2), n, RenderSystem.getShaderColor()[3]);
        }
    }

    public final class InternalType0486
    implements AutoCloseable {
        private boolean closed;

        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            this.closed = true;
            CustomDrawContext.this.endItemBatch();
        }
    }
}
