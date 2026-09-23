package rockstar.client.internal.render;



import rockstar.client.render.*;
import rockstar.client.*;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import rockstar.client.render.PostProcessRenderer;

public final class TextDrawCaptureHelper {
    private static boolean internalField0277;

    private TextDrawCaptureHelper() {
    }

    public static boolean internalMethod01833() {
        return internalField0277;
    }

    public static boolean internalMethod02847(final TextRenderer textRenderer, final String string, final String string2, final float f, final float f2, final int n, final boolean bl, Matrix4f matrix4f, final TextRenderer.TextLayerType textLayerType, final int n2, final int n3) {
        final Matrix4f matrix4f2 = new Matrix4f((Matrix4fc)matrix4f);
        return PostProcessRenderer.internalMethod04612(new PostProcessRenderer.InternalType0212(){

            @Override
            public int[] internalMethod02439() {
                return PostProcessRenderer.internalMethod01605(matrix4f2, f, f2, Math.max(textRenderer.getWidth(string), textRenderer.getWidth(string2)));
            }

            @Override
            public void internalMethod01050() {
                TextDrawCaptureHelper.internalMethod04754(() -> textRenderer.draw(string, f, f2, n, bl, matrix4f2, (VertexConsumerProvider)PostProcessRenderer.internalMethod04244(), textLayerType, n2, n3));
            }

            @Override
            public void internalMethod01085() {
                TextDrawCaptureHelper.internalMethod04754(() -> textRenderer.draw(string2, f, f2, n, bl, matrix4f2, (VertexConsumerProvider)PostProcessRenderer.internalMethod04244(), textLayerType, n2, n3));
            }
        });
    }

    public static boolean internalMethod06108(final TextRenderer textRenderer, final OrderedText orderedText, final OrderedText orderedText2, final float f, final float f2, final int n, final boolean bl, Matrix4f matrix4f, final TextRenderer.TextLayerType textLayerType, final int n2, final int n3) {
        final Matrix4f matrix4f2 = new Matrix4f((Matrix4fc)matrix4f);
        return PostProcessRenderer.internalMethod04612(new PostProcessRenderer.InternalType0212(){

            @Override
            public int[] internalMethod02439() {
                return PostProcessRenderer.internalMethod01605(matrix4f2, f, f2, Math.max(textRenderer.getWidth(orderedText), textRenderer.getWidth(orderedText2)));
            }

            @Override
            public void internalMethod01050() {
                TextDrawCaptureHelper.internalMethod04754(() -> textRenderer.draw(orderedText, f, f2, n, bl, matrix4f2, (VertexConsumerProvider)PostProcessRenderer.internalMethod04244(), textLayerType, n2, n3));
            }

            @Override
            public void internalMethod01085() {
                TextDrawCaptureHelper.internalMethod04754(() -> textRenderer.draw(orderedText2, f, f2, n, bl, matrix4f2, (VertexConsumerProvider)PostProcessRenderer.internalMethod04244(), textLayerType, n2, n3));
            }
        });
    }

    public static void internalMethod00759(TextRenderer textRenderer, OrderedText orderedText, float f, float f2, int n, int n2, Matrix4f matrix4f, VertexConsumerProvider vertexConsumerProvider, int n3) {
        TextDrawCaptureHelper.internalMethod00646(() -> textRenderer.drawWithOutline(orderedText, f, f2, n, n2, matrix4f, vertexConsumerProvider, n3));
    }

    private static void internalMethod00646(Runnable runnable) {
        internalField0277 = true;
        try {
            runnable.run();
        }
        finally {
            internalField0277 = false;
        }
    }

    public static void internalMethod04754(Runnable runnable) {
        TextDrawCaptureHelper.internalMethod00646(() -> {
            runnable.run();
            PostProcessRenderer.internalMethod04244().draw();
        });
    }
}
