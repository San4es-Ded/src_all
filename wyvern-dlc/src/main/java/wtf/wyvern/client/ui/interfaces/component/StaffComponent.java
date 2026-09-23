package wtf.wyvern.client.ui.interfaces.component;

import com.mojang.authlib.GameProfile;
import org.joml.Vector4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.render.display.StencilUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import lombok.Generated;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.world.GameMode;
import net.minecraft.client.util.DefaultSkinHelper;

public class StaffComponent extends DraggableHudElement {
    private static final float V2_MIN_WIDTH = 82.0F;
    private static final float V2_MAX_WIDTH = 124.0F;
    private final Map<String, StaffModule> modules = new LinkedHashMap<>();
    private final Set<String> staffPrefix = Set.of("helper", "ᴀдмин", "moder", "staff", "admin", "curator", "стажёр", "сотрудник", "помощник", "админ", "модер", "ꔗ", "ꔥ", "ꔡ", "ꔳ");
    private final Map<String, Identifier> skinTextureCache = new HashMap<>();
    private long lastStaffUpdate = 0L;
    private long lastSkinCacheClear = 0L;
    private final Set<String> currentStaffKeys = new HashSet<>();
    private final Animation widthAnimation;
    private final Animation alpha;
    private final boolean v2;

    public StaffComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align, boolean v2) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
        this.alpha = new Animation(200L, Easing.CUBIC_OUT);
        this.v2 = v2;
    }

    public void render(CustomDrawContext ctx) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastStaffUpdate > 50L && mc.getNetworkHandler() != null) {
            this.updateStaffList();
            this.lastStaffUpdate = currentTime;
        }

        if (currentTime - this.lastSkinCacheClear > 30000L) {
            this.skinTextureCache.clear();
            this.lastSkinCacheClear = currentTime;
        }

        this.modules.entrySet().removeIf((entry) -> entry.getValue().isDelete());
        float posX = this.getX();
        float posY = this.getY();
        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        ColorRGBA themeColor = theme.getColor();

        if (v2) {
            renderV2(ctx, posX, posY, themeColor);
        } else {
            renderClassic(ctx, posX, posY, themeColor);
        }
    }

    private void renderV2(CustomDrawContext ctx, float posX, float posY, ColorRGBA themeColor) {
        boolean isFound = false;
        float staffHeight = 0.0F;
        float maxLineContentWidth = 0.0F;
        List<StaffModule> activeStaff = new java.util.ArrayList<>();

        float onlineW = Fonts.MEDIUM.getWidth("online", 7.2F);

        for (Map.Entry<String, StaffModule> entry : this.modules.entrySet()) {
            entry.getValue().animation.update(this.currentStaffKeys.contains(entry.getKey()));
            float anim = entry.getValue().animation.getValue();
            if (anim > 0.01F) {
                float nameW = Fonts.MEDIUM.getWidth(entry.getValue().displayNameText.getString(), 7.2F);
                // Линия = Отступ иконки (6.5) + Иконка (8) + Зазор (2.5) + Имя + Зазор (10) + Статус
                float totalLineW = 6.5F + 8.0F + 2.5F + nameW + 10.0F + onlineW;
                maxLineContentWidth = Math.max(maxLineContentWidth, totalLineW);
                
                staffHeight += 12.0F * anim;
                activeStaff.add(entry.getValue());
                isFound = true;
            }
        }

        if (!isFound && !(mc.currentScreen instanceof ChatScreen)) {
            this.alpha.update(0.0F);
        } else {
            this.alpha.update(1.0F);
        }

        if (this.alpha.getValue() < 0.01F) return;

        // --- ГЕОМЕТРИЯ ---
        float headerHeight = 18.9F;
        float bodyHeight = staffHeight + 6.0F;
        float totalHeight = headerHeight + bodyHeight;

        float targetWidth = Math.max(V2_MIN_WIDTH, Math.min(V2_MAX_WIDTH, maxLineContentWidth + 6.5F));
        this.widthAnimation.update(targetWidth);
        float animWidth = this.widthAnimation.getValue();

        float cornerRadius = 5.5F;
        float currentAlpha = this.alpha.getValue();
        float strokeThickness = 1.0F;

        // --- ОБВОДКА FIGMA (#171717) ---
        Vector4f outlineVec = new Vector4f(0.09F, 0.09F, 0.09F, 1F * currentAlpha);
        ColorRGBA figmaOutlineColor = new ColorRGBA((int)(outlineVec.x * 255), (int)(outlineVec.y * 255), (int)(outlineVec.z * 255), (int)(outlineVec.w * 255));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, animWidth, totalHeight, BorderRadius.all(cornerRadius), figmaOutlineColor);

        // --- СТЕНСИЛЬ ---
        StencilUtil.push();
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + strokeThickness, posY + strokeThickness, animWidth - (strokeThickness * 2), totalHeight - (strokeThickness * 2), BorderRadius.all(cornerRadius - strokeThickness), ColorRGBA.BLACK);
        StencilUtil.read(1);

        // Шапка (#0B0B0B / 11 11 11)
        Vector4f topVec = new Vector4f(0.043F, 0.043F, 0.043F, 1.0F * currentAlpha);
        ColorRGBA topRectColor = new ColorRGBA((int)(topVec.x * 255), (int)(topVec.y * 255), (int)(topVec.z * 255), (int)(topVec.w * 255));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + strokeThickness, posY + strokeThickness, animWidth - (strokeThickness * 2), headerHeight - strokeThickness, new BorderRadius(cornerRadius - strokeThickness, cornerRadius - strokeThickness, 0.0F, 0.0F), topRectColor);

        // Тело (#111111 / 17 17 17)
        Vector4f bottomVec = new Vector4f(0.066F, 0.066F, 0.066F, 1.0F * currentAlpha);
        ColorRGBA bottomRectColor = new ColorRGBA((int)(bottomVec.x * 255), (int)(bottomVec.y * 255), (int)(bottomVec.z * 255), (int)(bottomVec.w * 255));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + strokeThickness, posY + headerHeight, animWidth - (strokeThickness * 2), bodyHeight - strokeThickness, new BorderRadius(0.0F, 0.0F, cornerRadius - strokeThickness, cornerRadius - strokeThickness), bottomRectColor);
        StencilUtil.pop();

        // --- ШАПКА: Иконка + Разделитель + "Staff" ---
        float iconSize = 10f;
        float textX = posX + 6.5F;
        ColorRGBA separatorColor = ColorRGBA.fromHex("#535353");

        // Иконка "O"
        ctx.drawText(Fonts.WYVERN.getFont(iconSize), "F", textX, posY + 6.0F, themeColor.withAlpha((int)(255 * currentAlpha)));
        
        // Слэш "/"
        float slashX = textX + Fonts.WYVERN.getWidth("F", iconSize) + 1.5F;
        ctx.drawText(Fonts.BOLD.getFont(7.5F), "/", slashX, posY + 6.6F, ColorRGBA.WHITE.withAlpha((int)(110 * currentAlpha)));
        
        // Текст "Staff"
        float titleX = slashX + Fonts.BOLD.getWidth("/", 7.5F) + 3.0F;
        float titleW = Fonts.MEDIUM.getWidth("Staff", 7.5F);
        float glowWidth = 60.0F;
        float glowHeight = 35.0F;
        ctx.drawTexture(Wyvern.id("icons/glow.png"), titleX + (titleW / 2.0F) - (glowWidth / 2.0F), posY + 7.0F + (7.5F / 2.0F) - (glowHeight / 2.0F), glowWidth, glowHeight, ColorRGBA.WHITE.withAlpha((int)(18 * currentAlpha)));
        ctx.drawText(Fonts.MEDIUM.getFont(7.5F), "Staff", titleX, posY + 7.0F, ColorRGBA.WHITE.withAlpha((int)(230 * currentAlpha)));

        String dots = "...";
        float dotsSize = 11.0F;
        float dotsWidth = Fonts.BOLD.getWidth(dots, dotsSize);
        ctx.drawText(Fonts.BOLD.getFont(dotsSize), dots, posX + animWidth - dotsWidth - 6.0F,
                posY + 3.3F, themeColor.withAlpha((int) (255 * currentAlpha)));

        // --- СПИСОК СТАФФА ---
        float currentY = posY + headerHeight + 1.5f;
        for (StaffModule module : activeStaff) {
            float anim = module.animation.getValue();
            Identifier skin = this.skinTextureCache.get(module.name);
            if (skin == null) skin = DefaultSkinHelper.getSteve().texture();

            int a = (int)(255 * currentAlpha * anim);

            // Головы
            DrawUtil.drawPlayerHeadWithRoundedShader(ctx.getMatrices(), skin, posX + 6.5F, currentY + 3.5F, 7.0F, BorderRadius.all(0.5F), ColorRGBA.WHITE.withAlpha(a));

            // Статус (вместо точек)
            String statusText = module.status == Status.NONE ? "online" : "spec";
            ColorRGBA statusColor = module.status == Status.NONE ? new ColorRGBA(32, 255, 32, a) : new ColorRGBA(255, 32, 32, a);
            float statusW = Fonts.MEDIUM.getWidth(statusText, 7.2F);
            float statusX = posX + animWidth - statusW - 6.5F;

            // Никнейм всегда заканчивается до статуса, даже во время анимации ширины.
            float nameX = posX + 15.5F;
            String displayName = fitText(module.displayNameText.getString(),
                    Math.max(0.0F, statusX - nameX - 5.0F), 7.2F);
            ctx.drawText(Fonts.MEDIUM.getFont(7.2F), displayName, nameX, currentY + 4.4F,
                    ColorRGBA.WHITE.withAlpha(a));
            ctx.drawText(Fonts.MEDIUM.getFont(7.2F), statusText, statusX, currentY + 4.4F, statusColor);

            currentY += 12.0F * anim;
        }

        this.width = animWidth;
        this.height = totalHeight;
    }

    private String fitText(String text, float maxWidth, float size) {
        if (maxWidth <= 0.0F) return "";
        if (Fonts.MEDIUM.getWidth(text, size) <= maxWidth) return text;
        String dots = "..";
        float dotsWidth = Fonts.MEDIUM.getWidth(dots, size);
        if (dotsWidth > maxWidth) return "";

        StringBuilder result = new StringBuilder();
        float width = 0.0F;
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            float characterWidth = Fonts.MEDIUM.getWidth(character, size);
            if (width + characterWidth + dotsWidth > maxWidth) break;
            result.append(character);
            width += characterWidth;
        }
        return result + dots;
    }

    private void renderClassic(CustomDrawContext ctx, float posX, float posY, ColorRGBA themeColor) {
        boolean isFound = false;

        for(Map.Entry<String, StaffModule> module : this.modules.entrySet()) {
            module.getValue().animation.update(this.currentStaffKeys.contains(module.getKey()));
            if (module.getValue().animation.getValue() > 0.01F) {
                isFound = true;
            }
        }

        if (!isFound && !(mc.currentScreen instanceof ChatScreen)) {
            this.alpha.update(0.0F);
        } else {
            this.alpha.update(1.0F);
        }

        if (mc.currentScreen instanceof ChatScreen) {
            this.alpha.update(1.0F);
        }

        float staffHeight = 0.0F;
        float maxNameWidth = 0.0F;

        for(Map.Entry<String, StaffModule> module : this.modules.entrySet()) {
            float anim = module.getValue().animation.getValue();
            if (anim > 0.01F) {
                float nameW = Fonts.REGULAR.getWidth(module.getValue().displayNameText.getString(), 7.2F);
                maxNameWidth = Math.max(maxNameWidth, nameW * anim);
                staffHeight += 11.0F * anim;
            }
        }

        float headerHeight = 15.0F;
        float footerHeight = 4.0F;
        float bodyHeight = staffHeight + footerHeight;
        float totalHeight = headerHeight + bodyHeight;
        float targetWidth = Math.max(maxNameWidth + 65.0F, 85.0F);
        this.widthAnimation.update(targetWidth);
        float currentWidth = this.widthAnimation.getValue();

        if (this.alpha.getValue() > 0.01F) {
            float rounding = 4.0F;
            ColorRGBA headerColor = new ColorRGBA(0, 0, 0, (int)(255 * this.alpha.getValue()));
            ColorRGBA bodyColor = new ColorRGBA(0, 0, 0, (int)(125 * this.alpha.getValue()));

            DrawUtil.drawBlur(ctx.getMatrices(), posX, posY, currentWidth, totalHeight, 15.0F, BorderRadius.all(rounding), ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue())));

            DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, currentWidth, totalHeight, BorderRadius.all(rounding), bodyColor);

            DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, currentWidth, headerHeight, new BorderRadius(rounding, rounding, 0, 0), headerColor);

            ctx.drawText(Fonts.MEDIUM.getFont(8.0F), "StaffList", posX + 7.0F, posY + 4.5F, ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue())));
            ctx.drawText(Fonts.NURIKI.getFont(9F), "O", posX + currentWidth - 14.0F, posY + 5.5F, themeColor.withAlpha((int)(255 * this.alpha.getValue())));

            float staffY = posY + headerHeight + 2.0F;
            float statusDotX = posX + currentWidth - 10.0F;

            for(Map.Entry<String, StaffModule> module : this.modules.entrySet()) {
                float anim = module.getValue().animation.getValue();
                if (anim > 0.01F) {
                    Identifier skinTexture = this.skinTextureCache.get(module.getValue().name);
                    if (skinTexture == null && mc.getNetworkHandler() != null) {
                        PlayerListEntry player = mc.getNetworkHandler().getPlayerList().stream()
                                .filter((p) -> p.getProfile() != null && module.getValue().name.equals(p.getProfile().getName()))
                                .findFirst().orElse(null);
                        if (player != null && player.getSkinTextures() != null) {
                            skinTexture = player.getSkinTextures().texture();
                            this.skinTextureCache.put(module.getValue().name, skinTexture);
                        }
                    }

                    if (skinTexture == null) {
                        skinTexture = DefaultSkinHelper.getSteve().texture();
                    }

                    ColorRGBA textC = ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue() * anim));

                    DrawUtil.drawPlayerHeadWithRoundedShader(ctx.getMatrices(), skinTexture, posX + 6.0F, staffY + 1.5F, 8.0F, BorderRadius.all(2.0F), ColorRGBA.WHITE.withAlpha(anim * 255.0F * this.alpha.getValue()));

                    ctx.drawText(Fonts.MEDIUM.getFont(7.2F), module.getValue().displayNameText.getString(), posX + 22.0F, staffY + 2.5F, textC);

                    ColorRGBA statusColor = module.getValue().status == Status.NONE ?
                            new ColorRGBA(32, 255, 32, (int)(255 * anim * this.alpha.getValue())) :
                            new ColorRGBA(255, 32, 32, (int)(255 * anim * this.alpha.getValue()));

                    DrawUtil.drawRoundedRect(ctx.getMatrices(), statusDotX, staffY + 3.5F, 4.0F, 4.0F, BorderRadius.all(2.0F), statusColor);

                    staffY += 11.0F * anim;
                }
            }
        }

        this.width = currentWidth;
        this.height = totalHeight;
    }

    private void updateStaffList() {
        if (mc.getNetworkHandler() != null) {
            this.currentStaffKeys.clear();

            for(PlayerListEntry entry : mc.getNetworkHandler().getPlayerList()) {
                GameProfile profile = entry.getProfile();
                Text displayName = entry.getDisplayName();
                if (displayName != null && profile != null) {
                    String display = displayName.getString();
                    String name = profile.getName();
                    String prefix = display.replace(name, "").trim();
                    String formattedPrefix = formatStaffPrefix(prefix);
                    if (formattedPrefix.length() >= 2 && this.containsAnyKeyword(formattedPrefix) && !formattedPrefix.contains("D.HELPER") && (!Wyvern.getInstance().getServerHandler().getServer().equals("LonyGrief") || !formattedPrefix.contains("D.ADMIN") && !formattedPrefix.contains("sTAFF"))) {
                        Status status = entry.getGameMode() == GameMode.SPECTATOR ? Status.VANISHED : Status.NONE;
                        this.modules.computeIfAbsent(display, (k) -> new StaffModule(displayName, display, name, status));
                        this.currentStaffKeys.add(display);
                    }
                }
            }

        }
    }

    /**
     * The ~90 literal replacements below used to run for every tab-list player 20
     * times per second (previously even as regex replaceAll). Tab prefixes are few
     * and rarely change, so the formatted result is cached per raw prefix string.
     */
    private final java.util.Map<String, String> prefixFormatCache = new java.util.HashMap<>();

    private String formatStaffPrefix(String prefix) {
        String cached = prefixFormatCache.get(prefix);
        if (cached != null) {
            return cached;
        }
        String formattedPrefix = prefix.replace("ꔗ", Formatting.BLUE + "MODER").replace("ꔥ", Formatting.BLUE + "ST.MODER").replace("ꔡ", Formatting.LIGHT_PURPLE + "MODER+").replace("ꔀ", Formatting.GRAY + "PLAYER").replace("ꔉ", Formatting.YELLOW + "HELPER").replace("◆", "@").replace("┃", "|").replace("ꔳ", Formatting.AQUA + "ML.ADMIN");
                    formattedPrefix = formattedPrefix.replace("ꔅ", Formatting.RED + "Y" + Formatting.WHITE + "T").replace("ꔂ", Formatting.BLUE + "D.MODER").replace("ꕠ", Formatting.YELLOW + "D.HELPER").replace("ꕄ", Formatting.RED + "DRACULA").replace("ꔖ", Formatting.AQUA + "OVERLORD").replace("ꕈ", Formatting.GREEN + "COBRA").replace("ꔨ", Formatting.LIGHT_PURPLE + "DRAGON").replace("ꔤ", Formatting.RED + "IMPERATOR").replace("ꔠ", Formatting.GOLD + "MAGISTER").replace("ꔄ", Formatting.BLUE + "HERO").replace("ꔒ", Formatting.GREEN + "AVENGER").replace("ꕒ", Formatting.WHITE + "RABBIT").replace("ꔈ", Formatting.YELLOW + "TITAN").replace("ꕀ", Formatting.DARK_GREEN + "HYDRA").replace("ꔶ", Formatting.GOLD + "TIGER").replace("ꔲ", Formatting.DARK_PURPLE + "BULL").replace("ꕖ", Formatting.BLACK + "BUNNY").replace("ꕗꕘ", Formatting.YELLOW + "SPONSOR").replace("\ud83d\udd25", "@").replace("ᴀ", "A").replace("ʙ", "B").replace("ᴄ", "C").replace("ᴅ", "D").replace("ᴇ", "E").replace("ғ", "F").replace("ɢ", "G").replace("ʜ", "H").replace("ɪ", "I").replace("ᴊ", "J").replace("ᴋ", "K").replace("ʟ", "L").replace("ᴍ", "M").replace("ɴ", "N").replace("ꜱ", "S").replace("ᴏ", "O").replace("ᴘ", "P").replace("ǫ", "Q").replace("ʀ", "R").replace("ᴛ", "T").replace("ᴜ", "U").replace("ᴠ", "V").replace("ᴡ", "W").replace("<b>ꜰ</b>", "F").replace("ʏ", "Y").replace("ᴢ", "Z");
        if (prefixFormatCache.size() > 256) {
            prefixFormatCache.clear();
        }
        prefixFormatCache.put(prefix, formattedPrefix);
        return formattedPrefix;
    }

    public boolean containsAnyKeyword(String text) {
        String lower = text.toLowerCase(Locale.US);

        for(String keyword : this.staffPrefix) {
            if (lower.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    private class StaffModule {
        private final Animation animation;
        private final Animation animationColor;
        private final Text displayNameText;
        private final String key;
        private final String name;
        private final Status status;
        private final long appearTime;

        public StaffModule(Text displayNameText, String key, String name, Status status) {
            this.animation = new Animation(250L, 0.01F, Easing.CUBIC_OUT);
            this.animationColor = new Animation(200L, Easing.QUAD_IN_OUT);
            this.displayNameText = displayNameText;
            this.key = key;
            this.name = name;
            this.status = status;
            this.appearTime = System.currentTimeMillis();
        }

        public boolean isDelete() {
            return this.animation.getValue() == 0.0F;
        }
    }

    public static enum Status {
        NONE,
        VANISHED;
    }

    public static class Staff {
        private Text prefix;
        private String name;
        private boolean isSpec;
        private Status status;

        @Generated
        public Text getPrefix() {
            return this.prefix;
        }

        @Generated
        public String getName() {
            return this.name;
        }

        @Generated
        public boolean isSpec() {
            return this.isSpec;
        }

        @Generated
        public Status getStatus() {
            return this.status;
        }

        @Generated
        public void setPrefix(Text prefix) {
            this.prefix = prefix;
        }

        @Generated
        public void setName(String name) {
            this.name = name;
        }

        @Generated
        public void setSpec(boolean isSpec) {
            this.isSpec = isSpec;
        }

        @Generated
        public void setStatus(Status status) {
            this.status = status;
        }

        @Generated
        public Staff(Text prefix, String name, boolean isSpec, Status status) {
            this.prefix = prefix;
            this.name = name;
            this.isSpec = isSpec;
            this.status = status;
        }
    }
}
