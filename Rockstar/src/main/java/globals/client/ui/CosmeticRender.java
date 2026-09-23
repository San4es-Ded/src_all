package globals.client.ui;



import rockstar.client.render.*;
import rockstar.client.internal.script.*;
import globals.client.Cosmetics;
import globals.shared.proto.Packets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.render.PostProcessRenderer;
import rockstar.client.render.SizedFont;
import rockstar.client.internal.script.EmojiTextNode;

public final class CosmeticRender {
    private static final Pattern GRAPHEMES = Pattern.compile("\\X");
    private CosmeticRender() {
    }

    public static float width(SizedFont typedValue020, String string, String string2, float f) {
        float f2 = CosmeticRender.textWidth(typedValue020, string);
        if (Cosmetics.badge(string2) != null) {
            f2 += 3.0f + f;
        }
        return f2;
    }

    public static float draw(CustomDrawContext customDrawContext, SizedFont typedValue020, String string, float f, float f2, String string2, String string3, ColorRGBA colorRGBA, float f3) {
        return CosmeticRender.draw(customDrawContext, typedValue020, string, f, f2, string2, string3, colorRGBA, f3, 0.0f);
    }

    public static float draw(CustomDrawContext customDrawContext, SizedFont typedValue020, String string, float f, float f2, String string2, String string3, ColorRGBA colorRGBA, float f3, float f4) {
        float f5;
        float f6;
        Identifier identifier = Cosmetics.badge(string3);
        float f7 = identifier != null ? 3.0f + f3 : 0.0f;
        float f8 = f4 > 0.0f ? Math.max(0.0f, f4 - f7) : 0.0f;
        Packets.InternalType0424 nestedValue0154 = Cosmetics.nick(string2);
        String string4 = PostProcessRenderer.internalMethod03546(string);
        if (string4 != string) {
            f6 = Math.max(CosmeticRender.textWidth(typedValue020, string), CosmeticRender.textWidth(typedValue020, string4));
            int[] nArray = PostProcessRenderer.internalMethod04696(rockstar.client.render.GuiMatrixCompat.toMatrix4f(customDrawContext.getMatrices()), f - 1.0f, f2 - typedValue020.internalMethod07850() * 0.35f, f + f6 + 2.0f, f2 + typedValue020.internalMethod07850() * 1.45f);
            PostProcessRenderer.internalMethod01602(nArray, () -> PostProcessRenderer.internalMethod02021(() -> CosmeticRender.drawName(customDrawContext, typedValue020, string, f, f2, nestedValue0154, colorRGBA, f8)));
            PostProcessRenderer.internalMethod02021(() -> CosmeticRender.drawName(customDrawContext, typedValue020, string4, f, f2, nestedValue0154, colorRGBA, f8));
        } else {
            CosmeticRender.drawName(customDrawContext, typedValue020, string, f, f2, nestedValue0154, colorRGBA, f8);
        }
        f6 = CosmeticRender.textWidth(typedValue020, string);
        float f9 = f5 = f8 > 0.0f ? Math.min(f6, f8) : f6;
        if (identifier != null) {
            float f10 = f2 + (typedValue020.internalMethod04890() - f3) / 2.0f;
            customDrawContext.drawTexture(identifier, f + f5 + 3.0f, f10, f3, f3, ColorRGBA.WHITE.withAlpha(colorRGBA.getAlpha()));
            return f5 + f7;
        }
        return f5;
    }

    private static void drawName(CustomDrawContext customDrawContext, SizedFont typedValue020, String string, float f, float f2, Packets.InternalType0424 nestedValue0154, ColorRGBA colorRGBA, float f3) {
        if (EmojiTextNode.internalMethod05608(string)) {
            CosmeticRender.drawEmojiName(customDrawContext, typedValue020, string, f, f2, nestedValue0154, colorRGBA, f3);
            return;
        }
        if (nestedValue0154 == null) {
            if (f3 > 0.0f) {
                customDrawContext.drawFadeoutText(typedValue020, string, f, f2, colorRGBA, 0.7f, 1.0f, f3);
            } else {
                customDrawContext.drawText(typedValue020, string, f, f2, colorRGBA);
            }
            return;
        }
        CosmeticRender.drawGradient(customDrawContext, typedValue020, string, f, f2, nestedValue0154, colorRGBA.getAlpha(), f3);
    }

    private static void drawEmojiName(CustomDrawContext customDrawContext, SizedFont typedValue020, String string, float f, float f2, Packets.InternalType0424 nestedValue0154, ColorRGBA colorRGBA, float f3) {
        float f4 = EmojiTextNode.internalMethod06011(typedValue020, string);
        if (f4 <= 0.0f) {
            return;
        }
        float f5 = nestedValue0154 == null ? 0.0f : CosmeticRender.phase(nestedValue0154.effect());
        float f6 = nestedValue0154 != null && "shimmer".equals(nestedValue0154.effect()) ? CosmeticRender.pulse() : 1.0f;
        Matcher matcher = GRAPHEMES.matcher(string);
        float f7 = 0.0f;
        while (matcher.find()) {
            ColorRGBA colorRGBA2;
            String string2 = matcher.group();
            if (f3 > 0.0f && f7 >= f3) break;
            float f8 = EmojiTextNode.internalMethod06011(typedValue020, string2);
            ColorRGBA colorRGBA3 = colorRGBA2 = nestedValue0154 == null ? colorRGBA.withAlpha(colorRGBA.getAlpha() * CosmeticRender.fade(f7, f3)) : CosmeticRender.colorAt(nestedValue0154.stops(), CosmeticRender.frac((f7 + f8 / 2.0f) / f4 * 0.5f + f5)).withAlpha(colorRGBA.getAlpha() * CosmeticRender.fade(f7, f3));
            if (f6 != 1.0f) {
                colorRGBA2 = CosmeticRender.brighten(colorRGBA2, f6);
            }
            EmojiTextNode.internalMethod06314(customDrawContext, typedValue020, string2, f + f7, f2, typedValue020.internalMethod04890(), colorRGBA2);
            f7 += f8;
        }
    }

    private static void drawGradient(CustomDrawContext customDrawContext, SizedFont typedValue020, String string, float f, float f2, Packets.InternalType0424 nestedValue0154, float f3, float f4) {
        float f5 = typedValue020.internalMethod00965(string);
        if (f5 <= 0.0f) {
            return;
        }
        float f6 = CosmeticRender.phase(nestedValue0154.effect());
        float f7 = "shimmer".equals(nestedValue0154.effect()) ? CosmeticRender.pulse() : 1.0f;
        for (int i = 0; i < string.length(); ++i) {
            String string2 = String.valueOf(string.charAt(i));
            float f8 = typedValue020.internalMethod00965(string.substring(0, i));
            if (f4 > 0.0f && f8 >= f4) break;
            float f9 = (f8 + typedValue020.internalMethod00965(string2) / 2.0f) / f5;
            ColorRGBA colorRGBA = CosmeticRender.colorAt(nestedValue0154.stops(), CosmeticRender.frac(f9 * 0.5f + f6)).withAlpha(f3 * CosmeticRender.fade(f8, f4));
            if (f7 != 1.0f) {
                colorRGBA = CosmeticRender.brighten(colorRGBA, f7);
            }
            customDrawContext.drawText(typedValue020, string2, f + f8, f2, colorRGBA);
        }
    }

    private static float textWidth(SizedFont typedValue020, String string) {
        return EmojiTextNode.internalMethod05608(string) ? EmojiTextNode.internalMethod06011(typedValue020, string) : typedValue020.internalMethod00965(string);
    }

    private static float fade(float f, float f2) {
        if (f2 <= 0.0f) {
            return 1.0f;
        }
        float f3 = f / f2;
        if (f3 <= 0.7f) {
            return 1.0f;
        }
        return Math.max(0.0f, 1.0f - (f3 - 0.7f) / 0.3f);
    }

    private static ColorRGBA colorAt(List<Packets.InternalType0386> list, float f) {
        Packets.InternalType0386 nestedValue0140 = list.get(0);
        Packets.InternalType0386 nestedValue0141 = list.get(list.size() - 1);
        if ((double)f <= nestedValue0140.offset()) {
            return CosmeticRender.rgb(nestedValue0140.color());
        }
        if ((double)f >= nestedValue0141.offset()) {
            return CosmeticRender.rgb(nestedValue0141.color());
        }
        for (int i = 1; i < list.size(); ++i) {
            Packets.InternalType0386 nestedValue0142 = list.get(i - 1);
            Packets.InternalType0386 nestedValue0143 = list.get(i);
            if ((double)f > nestedValue0143.offset()) continue;
            float f2 = (float)(nestedValue0143.offset() - nestedValue0142.offset());
            float f3 = f2 <= 0.0f ? 0.0f : (float)(((double)f - nestedValue0142.offset()) / (double)f2);
            return CosmeticRender.rgb(nestedValue0142.color()).mix(CosmeticRender.rgb(nestedValue0143.color()), f3);
        }
        return CosmeticRender.rgb(nestedValue0141.color());
    }

    private static float phase(String string) {
        float f = switch (string == null ? "" : string) {
            case "shine" -> 1500.0f;
            case "shimmer" -> 2600.0f;
            default -> 3000.0f;
        };
        return CosmeticRender.frac((float)(System.currentTimeMillis() % (long)f) / f);
    }

    private static float pulse() {
        float f = CosmeticRender.frac((float)(System.currentTimeMillis() % 2600L) / 2600.0f);
        float f2 = (float)(0.5 - 0.5 * Math.cos(Math.PI * 2 * (double)f));
        return 1.0f + 0.54999995f * f2;
    }

    private static ColorRGBA brighten(ColorRGBA colorRGBA, float f) {
        return new ColorRGBA(colorRGBA.getRed() * f, colorRGBA.getGreen() * f, colorRGBA.getBlue() * f, colorRGBA.getAlpha());
    }

    private static ColorRGBA rgb(int n) {
        return new ColorRGBA(n >> 16 & 0xFF, n >> 8 & 0xFF, n & 0xFF);
    }

    private static float frac(float f) {
        float f2 = f % 1.0f;
        return f2 < 0.0f ? f2 + 1.0f : f2;
    }
}

