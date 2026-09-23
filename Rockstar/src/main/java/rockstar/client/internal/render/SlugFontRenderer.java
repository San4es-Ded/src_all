package rockstar.client.internal.render;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Generated;
import rockstar.client.compat.Defines;
import rockstar.client.compat.ShaderProgram;
import rockstar.client.compat.ShaderProgramKey;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import rockstar.client.render.PostProcessRenderer;
import rockstar.client.internal.core.FormattedTextProcessor;
import rockstar.client.internal.config.CoreResourceReader;
import rockstar.client.render.FontFamily;
import rockstar.client.internal.render.FontGlyphGeometry;
import rockstar.client.internal.render.FontGlyphAtlas;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.render.UiVertexBatch;
import rockstar.client.render.UiBatchRenderer;

public final class SlugFontRenderer {
    public static final ShaderProgramKey internalField0661 = new ShaderProgramKey(CoreResourceReader.internalMethod07486("slug_font/data"), VertexFormats.POSITION_TEXTURE_COLOR_LIGHT, Defines.EMPTY);
    private static boolean internalField0277;

    public static ShaderProgram internalMethod07504(float f) {
        return SlugFontRenderer.internalMethod00401(f, 1.0f);
    }

    public static ShaderProgram internalMethod00401(float f, float f2) {
        FontGlyphAtlas internalValue0010 = FontGlyphAtlas.internalMethod05614();
        internalValue0010.internalMethod04785();
        bindGlyphTable(0, internalValue0010.internalMethod04784());
        bindGlyphTable(1, internalValue0010.internalMethod04788());
        bindGlyphTable(2, internalValue0010.internalMethod08779());
        ShaderProgram shaderProgram = RenderSystem.setShader((ShaderProgramKey)internalField0661);
        shaderProgram.getUniform("Weight").set(f);
        shaderProgram.getUniform("Softness").set(f2);
        return shaderProgram;
    }

    public static void internalMethod06418(int n, int n2, int n3) {
        FontGlyphAtlas internalValue0010 = FontGlyphAtlas.internalMethod05614();
        internalValue0010.internalMethod04785();
        bindGlyphTable(n, internalValue0010.internalMethod04784());
        bindGlyphTable(n2, internalValue0010.internalMethod04788());
        bindGlyphTable(n3, internalValue0010.internalMethod08779());
    }

    private static void bindGlyphTable(int slot, int texture) {
        // The curve/strip tables are data, including an unsigned integer atlas.
        // A LINEAR sampler makes that integer texture incomplete in OpenGL.
        RenderSystem.setShaderTexture(slot, texture, com.mojang.blaze3d.textures.FilterMode.NEAREST);
    }

    public static void internalMethod04918() {
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.setShaderTexture((int)1, (int)0);
        RenderSystem.setShaderTexture((int)2, (int)0);
    }

    private static boolean internalMethod01318(int n) {
        return (n >>> 24 & 0xFF) == 0;
    }

    private static boolean internalMethod04728(FontFamily typedValue022, String string, float f, Matrix4f matrix4f, float f2, float f3, Consumer<Matrix4f> consumer) {
        if (internalField0277 || !PostProcessRenderer.internalMethod08007()) {
            return false;
        }
        String string2 = PostProcessRenderer.internalMethod03546(string);
        if (string2 == string) {
            return false;
        }
        return SlugFontRenderer.internalMethod05062(matrix4f, f2, f3, f, Math.max(typedValue022.internalMethod05670(string, f), typedValue022.internalMethod05670(string2, f)), consumer);
    }

    private static boolean internalMethod02090(FontFamily typedValue022, List<FormattedTextProcessor.InternalType0470> list, float f, Matrix4f matrix4f, float f2, float f3, Consumer<Matrix4f> consumer) {
        if (internalField0277 || !PostProcessRenderer.internalMethod08007()) {
            return false;
        }
        List<FormattedTextProcessor.InternalType0470> list2 = SlugFontRenderer.internalMethod00981(list);
        if (list2 == list) {
            return false;
        }
        float f4 = 0.0f;
        float f5 = 0.0f;
        for (int i = 0; i < list.size(); ++i) {
            f4 += typedValue022.internalMethod05670(list.get((int)i).internalField0248, f);
            f5 += typedValue022.internalMethod05670(list2.get((int)i).internalField0248, f);
        }
        return SlugFontRenderer.internalMethod05062(matrix4f, f2, f3, f, Math.max(f4, f5), consumer);
    }

    private static boolean internalMethod05062(Matrix4f matrix4f, final float f, final float f2, final float f3, final float f4, final Consumer<Matrix4f> consumer) {
        final Matrix4f matrix4f2 = new Matrix4f((Matrix4fc)matrix4f);
        return PostProcessRenderer.internalMethod04612(new PostProcessRenderer.InternalType0212(){

            @Override
            public int[] internalMethod02439() {
                return PostProcessRenderer.internalMethod04696(matrix4f2, f - 1.0f, f2 - f3 * 0.35f, f + f4 + 2.0f, f2 + f3 * 1.45f);
            }

            @Override
            public void internalMethod01050() {
                SlugFontRenderer.internalMethod01837(() -> consumer.accept(matrix4f2));
            }

            @Override
            public void internalMethod01085() {
                SlugFontRenderer.internalMethod01837(() -> consumer.accept(matrix4f2));
            }
        });
    }

    public static void internalMethod01837(Runnable runnable) {
        internalField0277 = true;
        try {
            runnable.run();
        }
        finally {
            internalField0277 = false;
        }
    }

    private static List<FormattedTextProcessor.InternalType0470> internalMethod00981(List<FormattedTextProcessor.InternalType0470> list) {
        ArrayList<FormattedTextProcessor.InternalType0470> arrayList = null;
        for (int i = 0; i < list.size(); ++i) {
            FormattedTextProcessor.InternalType0470 nestedValue0166 = list.get(i);
            String string = PostProcessRenderer.internalMethod03546(nestedValue0166.internalField0248);
            if (string == nestedValue0166.internalField0248) continue;
            if (arrayList == null) {
                arrayList = new ArrayList<FormattedTextProcessor.InternalType0470>(list);
            }
            arrayList.set(i, new FormattedTextProcessor.InternalType0470(string, nestedValue0166.internalField0227, nestedValue0166.internalField0277, nestedValue0166.internalField0276, nestedValue0166.internalField1099, nestedValue0166.internalField1100));
        }
        return arrayList == null ? list : arrayList;
    }

    public static void internalMethod07344(FontFamily typedValue022, String string, float f, int n, Matrix4f matrix4f, float f2, float f3, float f4) {
        if (SlugFontRenderer.internalMethod01318(n)) {
            return;
        }
        SlugFontRenderer.internalMethod03844(typedValue022, string, f, n, matrix4f, f2, f3, f4, false, 0.0f, 1.0f, 0.0f);
    }

    public static void internalMethod03844(FontFamily typedValue022, String string, float f, int n, Matrix4f matrix4f, float f2, float f3, float f4, boolean bl, float f5, float f6, float f7) {
        SlugFontRenderer.internalMethod05774(typedValue022, string, f, n, matrix4f, f2, f3, f4, bl, f5, f6, f7, 0.0f, 0.0f);
    }

    public static void internalMethod05774(FontFamily typedValue022, String string, float f, int n, Matrix4f matrix4f, float f2, float f3, float f4, boolean bl, float f5, float f6, float f7, float f8, float f9) {
        UiBatchRenderer typedValue250;
        if (SlugFontRenderer.internalMethod01318(n)) {
            return;
        }
        if (string == null) {
            string = "";
        }
        final String originalText = string;
        if (SlugFontRenderer.internalMethod04728(typedValue022, originalText, f, matrix4f, f2, f3, arg_0 -> SlugFontRenderer.internalMethod04020(typedValue022, originalText, f, n, f2, f3, f4, bl, f5, f6, f7, f8, f9, arg_0))) {
            return;
        }
        string = PostProcessRenderer.internalMethod03546(string);
        float f10 = 0.0f;
        float f11 = 0.5f;
        if (!internalField0277 && UiVertexBatch.internalMethod04253() != null) {
            typedValue022.internalMethod02357(matrix4f, (VertexConsumer)UiVertexBatch.internalMethod04253().internalMethod05457(), string, f, f2, f3, f4, n);
            return;
        }
        UiBatchRenderer typedValue251 = typedValue250 = internalField0277 ? null : UiBatchRenderer.internalMethod08317();
        if (typedValue250 != null) {
            if (!bl && f8 == 0.0f && f9 == 0.0f) {
                typedValue250.internalMethod03909(typedValue022, string, f, matrix4f, f2, f3, f4, n);
                return;
            }
            if (typedValue250.internalMethod05284(typedValue022, string, f, matrix4f, f2, f3, f4, n, f10, f11, bl, f2, f7, f5, f6, f8, f9)) {
                return;
            }
            UiBatchRenderer.internalMethod02576();
        }
        UiBatchRenderer.internalMethod08433();
        RenderSystem.disableCull();
        ShaderProgram shaderProgram = SlugFontRenderer.internalMethod00401(f10, f11);
        shaderProgram.getUniform("EnableFadeout").set(bl ? 1 : 0);
        shaderProgram.getUniform("FadeoutStart").set(f5);
        shaderProgram.getUniform("FadeoutEnd").set(f6);
        shaderProgram.getUniform("FadeinStart").set(f8);
        shaderProgram.getUniform("FadeinEnd").set(f9);
        shaderProgram.getUniform("MaxWidth").set(f7);
        shaderProgram.getUniform("TextPosX").set(f2);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        typedValue022.internalMethod05024(matrix4f, (VertexConsumer)bufferBuilder, string, f, f2, f3, f4, n, f11);
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        SlugFontRenderer.internalMethod04918();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void internalMethod01151(FontFamily typedValue022, String string, float f, int n, Matrix4f matrix4f, float f2, float f3, float f4, boolean bl, float f5, float f6) {
        float f7 = typedValue022.internalMethod05670(string, f) * 2.0f;
        SlugFontRenderer.internalMethod03844(typedValue022, string, f, n, matrix4f, f2, f3, f4, bl, f5, f6, f7);
    }

    public static void internalMethod04039(FontFamily typedValue022, String string, float f, int n, Matrix4f matrix4f, float f2, float f3, float f4, float f5, float f6) {
        UiBatchRenderer typedValue250;
        if (SlugFontRenderer.internalMethod01318(n)) {
            return;
        }
        if (string == null) {
            string = "";
        }
        final String originalText = string;
        if (SlugFontRenderer.internalMethod04728(typedValue022, originalText, f, matrix4f, f2, f3, arg_0 -> SlugFontRenderer.internalMethod00595(typedValue022, originalText, f, n, f2, f3, f4, f5, f6, arg_0))) {
            return;
        }
        string = PostProcessRenderer.internalMethod03546(string);
        if (!internalField0277 && UiVertexBatch.internalMethod04253() != null) {
            typedValue022.internalMethod02357(matrix4f, (VertexConsumer)UiVertexBatch.internalMethod04253().internalMethod05457(), string, f, f2, f3, f4, n);
            return;
        }
        UiBatchRenderer typedValue251 = typedValue250 = internalField0277 ? null : UiBatchRenderer.internalMethod08317();
        if (typedValue250 != null) {
            if (typedValue250.internalMethod05284(typedValue022, string, f, matrix4f, f2, f3, f4, n, f5, f6, false, f2, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)) {
                return;
            }
            UiBatchRenderer.internalMethod02576();
        }
        UiBatchRenderer.internalMethod08433();
        RenderSystem.disableCull();
        ShaderProgram shaderProgram = SlugFontRenderer.internalMethod00401(f5, f6);
        shaderProgram.getUniform("EnableFadeout").set(0);
        shaderProgram.getUniform("FadeoutStart").set(0.0f);
        shaderProgram.getUniform("FadeoutEnd").set(1.0f);
        shaderProgram.getUniform("MaxWidth").set(0.0f);
        shaderProgram.getUniform("TextPosX").set(f2);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        typedValue022.internalMethod05024(matrix4f, (VertexConsumer)bufferBuilder, string, f, f2, f3, f4, n, f6);
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        SlugFontRenderer.internalMethod04918();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void internalMethod05909(FontFamily typedValue022, Text text, float f, Matrix4f matrix4f, float f2, float f3, float f4) {
        SlugFontRenderer.internalMethod02777(typedValue022, text, f, matrix4f, f2, f3, f4, false, 0.0f, 1.0f, 0.0f);
    }

    public static void internalMethod02777(FontFamily typedValue022, Text text, float f, Matrix4f matrix4f2, float f2, float f3, float f4, boolean bl, float f5, float f6, float f7) {
        UiBatchRenderer typedValue250;
        float f8 = 0.0f;
        float f9 = 0.5f;
        List<FormattedTextProcessor.InternalType0470> list = FormattedTextProcessor.internalMethod03885(text, ThemeColors.internalField1312.getRGB());
        if (SlugFontRenderer.internalMethod02090(typedValue022, list, f, matrix4f2, f2, f3, matrix4f -> SlugFontRenderer.internalMethod02777(typedValue022, text, f, matrix4f, f2, f3, f4, bl, f5, f6, f7))) {
            return;
        }
        list = SlugFontRenderer.internalMethod00981(list);
        float f10 = f2;
        if (!internalField0277 && UiVertexBatch.internalMethod04253() != null) {
            for (FormattedTextProcessor.InternalType0470 nestedValue0166 : list) {
                if (!SlugFontRenderer.internalMethod01318(nestedValue0166.internalField0227)) {
                    typedValue022.internalMethod02357(matrix4f2, (VertexConsumer)UiVertexBatch.internalMethod04253().internalMethod05457(), nestedValue0166.internalField0248, f, f10, f3, f4, nestedValue0166.internalField0227);
                }
                f10 += typedValue022.internalMethod05670(nestedValue0166.internalField0248, f);
            }
            return;
        }
        UiBatchRenderer typedValue251 = typedValue250 = internalField0277 ? null : UiBatchRenderer.internalMethod08317();
        if (typedValue250 != null) {
            if (!bl) {
                for (FormattedTextProcessor.InternalType0470 nestedValue0166 : list) {
                    if (!SlugFontRenderer.internalMethod01318(nestedValue0166.internalField0227)) {
                        typedValue250.internalMethod03909(typedValue022, nestedValue0166.internalField0248, f, matrix4f2, f10, f3, f4, nestedValue0166.internalField0227);
                    }
                    f10 += typedValue022.internalMethod05670(nestedValue0166.internalField0248, f);
                }
                return;
            }
            if (typedValue250.internalMethod08432()) {
                for (FormattedTextProcessor.InternalType0470 nestedValue0166 : list) {
                    if (!SlugFontRenderer.internalMethod01318(nestedValue0166.internalField0227)) {
                        typedValue250.internalMethod05284(typedValue022, nestedValue0166.internalField0248, f, matrix4f2, f10, f3, f4, nestedValue0166.internalField0227, f8, f9, true, f2, f7, f5, f6, 0.0f, 0.0f);
                    }
                    f10 += typedValue022.internalMethod05670(nestedValue0166.internalField0248, f);
                }
                return;
            }
            UiBatchRenderer.internalMethod02576();
        }
        UiBatchRenderer.internalMethod08433();
        RenderSystem.disableCull();
        ShaderProgram shaderProgram = SlugFontRenderer.internalMethod00401(f8, f9);
        shaderProgram.getUniform("EnableFadeout").set(bl ? 1 : 0);
        shaderProgram.getUniform("FadeoutStart").set(f5);
        shaderProgram.getUniform("FadeoutEnd").set(f6);
        shaderProgram.getUniform("FadeinStart").set(0.0f);
        shaderProgram.getUniform("FadeinEnd").set(0.0f);
        shaderProgram.getUniform("MaxWidth").set(f7);
        shaderProgram.getUniform("TextPosX").set(f2);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        for (FormattedTextProcessor.InternalType0470 nestedValue0166 : list) {
            typedValue022.internalMethod05024(matrix4f2, (VertexConsumer)bufferBuilder, nestedValue0166.internalField0248, f, f10, f3, f4, nestedValue0166.internalField0227, f9);
            f10 += typedValue022.internalMethod05670(nestedValue0166.internalField0248, f);
        }
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        SlugFontRenderer.internalMethod04918();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void internalMethod05927(FontFamily typedValue022, Text text, float f, Matrix4f matrix4f, float f2, float f3, float f4, boolean bl, float f5, float f6) {
        float f7 = typedValue022.internalMethod04675(text, f) * 2.0f;
        SlugFontRenderer.internalMethod02777(typedValue022, text, f, matrix4f, f2, f3, f4, bl, f5, f6, f7);
    }

    public static void internalMethod03024(FontFamily typedValue022, int n, float f, float f2, float f3, int n2, Matrix4f matrix4f) {
        UiBatchRenderer typedValue250;
        if (SlugFontRenderer.internalMethod01318(n2)) {
            return;
        }
        if (typedValue022 == null) {
            return;
        }
        FontGlyphGeometry typedValue023 = typedValue022.internalMethod01567(n);
        if (typedValue023 == null) {
            return;
        }
        UiBatchRenderer typedValue251 = typedValue250 = internalField0277 ? null : UiBatchRenderer.internalMethod08317();
        if (typedValue250 != null) {
            typedValue250.internalMethod01632(typedValue022, n, f, f2, f3, matrix4f, n2);
            return;
        }
        float f4 = 0.0f;
        float f5 = 0.5f;
        UiBatchRenderer.internalMethod08433();
        RenderSystem.disableCull();
        ShaderProgram shaderProgram = SlugFontRenderer.internalMethod00401(f4, f5);
        shaderProgram.getUniform("EnableFadeout").set(0);
        shaderProgram.getUniform("FadeoutStart").set(0.0f);
        shaderProgram.getUniform("FadeoutEnd").set(1.0f);
        shaderProgram.getUniform("MaxWidth").set(0.0f);
        shaderProgram.getUniform("TextPosX").set(f);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        typedValue023.internalMethod01939(matrix4f, (VertexConsumer)bufferBuilder, f, f2, f3, n2);
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        SlugFontRenderer.internalMethod04918();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    @Generated
    private SlugFontRenderer() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static void internalMethod00595(FontFamily typedValue022, String string, float f, int n, float f2, float f3, float f4, float f5, float f6, Matrix4f matrix4f) {
        SlugFontRenderer.internalMethod04039(typedValue022, string, f, n, matrix4f, f2, f3, f4, f5, f6);
    }

    private static void internalMethod04020(FontFamily typedValue022, String string, float f, int n, float f2, float f3, float f4, boolean bl, float f5, float f6, float f7, float f8, float f9, Matrix4f matrix4f) {
        SlugFontRenderer.internalMethod05774(typedValue022, string, f, n, matrix4f, f2, f3, f4, bl, f5, f6, f7, f8, f9);
    }
}
