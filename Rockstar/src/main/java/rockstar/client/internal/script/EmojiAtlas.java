package rockstar.client.internal.script;



import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.RockstarClient;
import rockstar.client.internal.config.ResourceJsonHelper;

public final class EmojiAtlas {
    private static final Identifier internalField0354 = RockstarClient.id("textures/emoji/emoji_atlas.png");
    private static Map<String, Integer> internalField0543;

    private EmojiAtlas() {
    }

    public static Integer internalMethod02569(String string) {
        if (internalField0543 == null) {
            JsonObject jsonObject = ResourceJsonHelper.internalMethod01844(RockstarClient.id("emoji/emoji_atlas.json"), JsonObject.class);
            internalField0543 = new HashMap<String, Integer>(jsonObject.size());
            for (String string2 : jsonObject.keySet()) {
                internalField0543.put(string2, jsonObject.get(string2).getAsInt());
            }
        }
        return internalField0543.get(string);
    }

    public static void internalMethod02186(CustomDrawContext customDrawContext, int n, float f, float f2, float f3, float f4) {
        float f5 = 0.015625f;
        float f6 = (float)(n % 64) * f5;
        float f7 = (float)(n / 64) * f5;
        customDrawContext.drawTexture(internalField0354, f, f2, f3, f3, f6, f6 + f5, f7, f7 + f5, ColorRGBA.WHITE.withAlpha(f4));
    }
}

