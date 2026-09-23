package moscow.rockstar.mixin.minecraft.client.font;




import rockstar.client.render.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.modules.other.NameProtectModule;
import rockstar.client.render.PostProcessRenderer;
import rockstar.client.internal.core.NameProtectTextProcessor;
import rockstar.client.internal.render.TextDrawCaptureHelper;
import rockstar.client.RockstarClient;

@Mixin(value={TextRenderer.class})
public abstract class TextRendererMixin {
    @Inject(method={"draw(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$splitPlainText(String string, float f, float f2, int n, boolean bl, Matrix4f matrix4f, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType textLayerType, int n2, int n3, CallbackInfo callbackInfo) {
        if (TextDrawCaptureHelper.internalMethod01833() || !PostProcessRenderer.internalMethod08007()) {
            return;
        }
        NameProtectModule typedValue207 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
        if (typedValue207 == null || !typedValue207.internalMethod01816(string)) {
            return;
        }
        String string2 = typedValue207.internalMethod04954(string);
        if (string2.equals(string)) {
            return;
        }
        if (TextDrawCaptureHelper.internalMethod02847((TextRenderer)(Object)this, string, string2, f, f2, n, bl, matrix4f, textLayerType, n2, n3)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"draw(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$splitStyledText(OrderedText orderedText, float f, float f2, int n, boolean bl, Matrix4f matrix4f, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType textLayerType, int n2, int n3, CallbackInfo callbackInfo) {
        if (TextDrawCaptureHelper.internalMethod01833() || !PostProcessRenderer.internalMethod08007()) {
            return;
        }
        NameProtectModule typedValue207 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
        if (typedValue207 == null || !typedValue207.internalMethod01816(NameProtectTextProcessor.internalMethod04584(orderedText))) {
            return;
        }
        OrderedText orderedText2 = NameProtectTextProcessor.internalMethod04840(orderedText, typedValue207);
        if (orderedText2 == null) {
            return;
        }
        if (TextDrawCaptureHelper.internalMethod06108((TextRenderer)(Object)this, orderedText, orderedText2, f, f2, n, bl, matrix4f, textLayerType, n2, n3)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$splitText(Text text, float f, float f2, int n, boolean bl, Matrix4f matrix4f, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType textLayerType, int n2, int n3, CallbackInfo callbackInfo) {
        if (TextDrawCaptureHelper.internalMethod01833() || !PostProcessRenderer.internalMethod08007()) {
            return;
        }
        OrderedText orderedText = text.asOrderedText();
        NameProtectModule typedValue207 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
        if (typedValue207 == null || !typedValue207.internalMethod01816(NameProtectTextProcessor.internalMethod04584(orderedText))) {
            return;
        }
        OrderedText orderedText2 = NameProtectTextProcessor.internalMethod04840(orderedText, typedValue207);
        if (orderedText2 != null && TextDrawCaptureHelper.internalMethod06108((TextRenderer)(Object)this, orderedText, orderedText2, f, f2, n, bl, matrix4f, textLayerType, n2, n3)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"drawWithOutline"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$maskOutlinedText(OrderedText orderedText, float f, float f2, int n, int n2, Matrix4f matrix4f, VertexConsumerProvider vertexConsumerProvider, int n3, CallbackInfo callbackInfo) {
        if (TextDrawCaptureHelper.internalMethod01833() || !PostProcessRenderer.internalMethod08007()) {
            return;
        }
        NameProtectModule typedValue207 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
        if (typedValue207 == null || !typedValue207.internalMethod01816(NameProtectTextProcessor.internalMethod04584(orderedText))) {
            return;
        }
        OrderedText orderedText2 = NameProtectTextProcessor.internalMethod04840(orderedText, typedValue207);
        if (orderedText2 == null) {
            return;
        }
        TextDrawCaptureHelper.internalMethod00759((TextRenderer)(Object)this, orderedText2, f, f2, n, n2, matrix4f, vertexConsumerProvider, n3);
        callbackInfo.cancel();
    }
}
