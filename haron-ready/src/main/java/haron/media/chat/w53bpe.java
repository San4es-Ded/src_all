package haron.media.chat;

import haron.client.MinecraftClientAccess;
import haron.module.ModuleManager;
import haron.util.ColorUtils;
import java.awt.Color;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import ru.haron.Haron;

public final class w53bpe
implements MinecraftClientAccess {
    private static final String e = "[Haron] ";
    public static int a;
    public static boolean b;

    private w53bpe() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void b(Text text) {
        if (w53bpe.c.player != null) {
            w53bpe.c.inGameHud.getChatHud().addMessage((Text)w53bpe.b("[Haron] ").append((Text)Text.literal((String)"» ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)0xFFFFFF)))).append(text));
        }
    }

    public static MutableText b(String string) {
        MutableText mutableText = Text.literal((String)"");
        Color color = new Color(w53bpe.a());
        Color color2 = w53bpe.a(color);
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            mutableText.append((Text)Text.literal((String)String.valueOf(string.charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)ColorUtils.a(color, color2, n <= 1 ? 0.0f : (float)(i / ((n & 0xFFFFFFFE) - (~n & 1)))).getRGB()))));
        }
        return mutableText;
    }

    public static void a(Text text) {
        if (w53bpe.c.player != null) {
            w53bpe.c.player.sendMessage(text, false);
        }
    }

    public static void a(String string) {
        if (w53bpe.c.player != null) {
            w53bpe.c.player.networkHandler.sendChatCommand(string);
        }
    }

    public static void a(Object object) {
        if (object == null) {
            object = "null";
        }
        if (w53bpe.c.player == null) {
            Haron.getLOGGER().info(w53bpe.$sf$0(String.valueOf(object)));
        } else {
            w53bpe.c.inGameHud.getChatHud().addMessage((Text)w53bpe.b("[Haron] ").append((Text)Text.literal((String)"» ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)0xFFFFFF)))).append((Text)Text.literal((String)w53bpe.$sf$1(String.valueOf(object)).replace("&", "§"))));
        }
    }

    private static int a() {
        return ModuleManager.CLIENT_COLOR.o();
    }

    private static Color a(Color color) {
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return new Color(Color.HSBtoRGB(fArray[0], Math.max(0.0f, fArray[1] - 0.3f), Math.min(1.0f, fArray[2] + 0.2f)));
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "(CHAT) " + string;
    }

    private static /* synthetic */ String $sf$1(String string) {
        int n = 284;
        return "§7" + string;
    }
}

