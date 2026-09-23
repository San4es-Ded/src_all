package haron.render.icons;

import haron.render.icons.IconTexture;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;
import ru.haron.Haron;

public final class HaronIcons {
    public static final String WATER = "water";
    public static final String ARROW_V = "arrow_v";
    public static final String KEYBOARD = "keyboard";
    public static final String ICON = "icon";
    public static final String BUBBLE_1 = "bubble_1";
    public static final String BUBBLE_2 = "bubble_2";
    public static final String LIGHTNING = "lightning";
    public static final String PULSE_ICON = "pulse_icon";
    public static final String SEARCH = "search";
    public static final String ARROW = "arrow";
    public static final String UP_ARROW = "up_arrow";
    public static final String LOGO = "logo";
    public static final String CALENDAR = "calendar";
    public static final String DEATH = "death";
    public static final String DIAMOND = "diamond";
    public static final String FAST = "fast";
    public static final String HOME = "home";
    public static final String LOCKED = "locked";
    public static final String MOUNTAIN = "mountain";
    public static final String REPAIR = "repair";
    public static final String SHIELD = "shield";
    public static final String TRASH = "trash";
    public static final String PLAY = "play";
    public static final String SHARE = "share";
    public static final String SAVE = "save";
    public static final String PLUS = "plus";
    public static final String KEY = "key";
    public static final String EDIT = "edit";
    public static final String LOAD = "load";
    public static final String PULSE_ICO = "pulse_ico";
    private static final String DEFAULT_KEY = "icon";
    private static final Map<String, IconTexture> TEXTURES = new HashMap<String, IconTexture>();

    private static void registerPotionAliases() {
        IconTexture hclqea2 = TEXTURES.get("potions");
        for (String string : new String[]{"absorption", "bad_omen", "blindness", "conduit_power", "dolphins_grace", "fire_resistance", "glowing", "haste", "health_boost", "hero_of_the_village", "hunger", "instant_damage", "instant_health", "invisibility", "jump_boost", "levitation", "luck", "mining_fatigue", "nausea", "night_vision", "poison", "regeneration", "resistance", "saturation", "slow_falling", "slowness", "speed", "strength", "unluck", "water_breathing", "weakness", "wither"}) {
            TEXTURES.put(string, hclqea2);
        }
    }

    private HaronIcons() {
    }

    public static Identifier get(String string) {
        IconTexture hclqea2 = HaronIcons.getInfo(string);
        return hclqea2 == null ? Identifier.of((String)"haron", (String)"textures/icon.png") : hclqea2.a();
    }

    public static void load() {
        if (TEXTURES.isEmpty()) {
            HaronIcons.register("shadow", "textures/shadow.png");
            HaronIcons.register("logo", "textures/logo.png");
            HaronIcons.register("logo_menu", "textures/logo_menu.png");
            HaronIcons.register("icon", "textures/icon.png");
            HaronIcons.register("bubble_1", "textures/bubble_1.png");
            HaronIcons.register("bubble_2", "textures/bubble_2.png");
            HaronIcons.register("up_arrow", "textures/up_arrow.png");
            HaronIcons.register("arrow", "textures/arrow.png");
            HaronIcons.register("target", "textures/target.png");
            HaronIcons.register("search", "textures/clickgui/search.png");
            HaronIcons.register("keyboard", "textures/clickgui/keyboard.png");
            HaronIcons.register("calendar", "textures/clickgui/calendar.png");
            HaronIcons.register("death", "textures/clickgui/death.png");
            HaronIcons.register("diamond", "textures/clickgui/diamond.png");
            HaronIcons.register("fast", "textures/clickgui/fast.png");
            HaronIcons.register("home", "textures/clickgui/home.png");
            HaronIcons.register("locked", "textures/clickgui/locked.png");
            HaronIcons.register("mountain", "textures/clickgui/mountain.png");
            HaronIcons.register("repair", "textures/clickgui/repair.png");
            HaronIcons.register("shield", "textures/clickgui/shield.png");
            HaronIcons.register("trash", "textures/clickgui/trash.png");
            HaronIcons.register("play", "textures/clickgui/play.png");
            HaronIcons.register("share", "textures/clickgui/share.png");
            HaronIcons.register("save", "textures/clickgui/save.png");
            HaronIcons.register("plus", "textures/clickgui/plus.png");
            HaronIcons.register("key", "textures/clickgui/key.png");
            HaronIcons.register("edit", "textures/clickgui/edit.png");
            HaronIcons.register("load", "textures/clickgui/load.png");
            HaronIcons.register("bell", "textures/clickgui/bell.png");
            HaronIcons.register("warning", "textures/clickgui/warning.png");
            HaronIcons.register("music", "textures/clickgui/music.png");
            HaronIcons.register("arrows_left", "textures/clickgui/arrows_left.png");
            HaronIcons.register("arrows_right", "textures/clickgui/arrows_right.png");
            HaronIcons.register("pause", "textures/clickgui/pause.png");
            HaronIcons.register("play_button", "textures/clickgui/play_button.png");
            HaronIcons.register("hotkeys", "textures/clickgui/hotkeys.png");
            HaronIcons.register("cooldowns", "textures/clickgui/cooldowns.png");
            HaronIcons.register("configs", "textures/clickgui/configs.png");
            HaronIcons.register("events", "textures/clickgui/events.png");
            HaronIcons.register("friends", "textures/clickgui/friends.png");
            HaronIcons.register("hud", "textures/clickgui/hud.png");
            HaronIcons.register("markers", "textures/clickgui/markers.png");
            HaronIcons.register("modules", "textures/clickgui/modules.png");
            HaronIcons.register("utilities", "textures/clickgui/utilities.png");
            HaronIcons.register("visuals", "textures/clickgui/visuals.png");
            HaronIcons.register("example_group", "textures/clickgui/example_group.png");
            HaronIcons.registerPotionAliases();
        }
    }

    private static void register(String string, String string2) {
        Identifier identifier = Identifier.of((String)"haron", (String)string2);
        int n = 1;
        int n2 = 1;
        try {
            InputStream inputStream = HaronIcons.class.getResourceAsStream(HaronIcons.$sf$0(string2));
            if (inputStream == null) {
                Haron.getLOGGER().warn("[HaronIcons] Texture file NOT FOUND on classpath: /assets/haron/{} (key={}, id={})", (Object)string2, (Object)string, (Object)identifier);
            } else {
                NativeImage nativeImage = NativeImage.read((InputStream)inputStream);
                n = nativeImage.getWidth();
                n2 = nativeImage.getHeight();
                nativeImage.close();
                if (string.equals("logo")) {
                    Haron.getLOGGER().info("[HaronIcons] Loaded LOGO texture: path=/assets/haron{} id={} size={}x{}", (Object)string2, (Object)identifier, (Object)n, (Object)n2);
                }
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        catch (IOException iOException) {
            Haron.getLOGGER().warn("[HaronIcons] Failed to read texture: /assets/haron/{}", (Object)string2, (Object)iOException);
        }
        TEXTURES.put(string, new IconTexture(identifier, n, n2));
    }

    private static String normalizeKey(String string) {
        return string == null || string.isBlank() || !string.matches("[a-z0-9_./-]+") ? "icon" : string;
    }

    public static IconTexture getInfo(String string) {
        HaronIcons.load();
        IconTexture hclqea2 = TEXTURES.get(HaronIcons.normalizeKey(string));
        return hclqea2 != null ? hclqea2 : TEXTURES.get("icon");
    }

    private static /* synthetic */ String $sf$0(String string) {
        int n = 272;
        return "/assets/haron/" + string;
    }
}

