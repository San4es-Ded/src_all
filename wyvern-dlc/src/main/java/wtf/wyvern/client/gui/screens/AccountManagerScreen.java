package wtf.wyvern.client.gui.screens;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.session.Session;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.mixin.accessors.MinecraftClientAccessor;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;

public final class AccountManagerScreen extends Screen {
    private static final float DESIGN_WIDTH = 884.0F;
    private static final float DESIGN_HEIGHT = 595.0F;
    private static final float LAYOUT_WIDTH = 540.0F;
    private static final float PANEL_HEIGHT = 238.0F;
    private static final float LEFT_WIDTH = 177.0F;
    private static final float PANEL_GAP = 12.0F;
    private static final float CONTROL_HEIGHT = 29.0F;
    private static final float CONTROL_GAP = 7.0F;
    private static final float CARD_GAP = 7.0F;
    private static final float CARD_HEIGHT = 52.0F;
    private static final int CARD_COLUMNS = 2;
    private static final int VISIBLE_ROWS = 3;
    private static final int MAX_USERNAME_LENGTH = 16;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final ColorRGBA WHITE = new ColorRGBA(238, 239, 244, 248);
    private static final ColorRGBA MUTED = new ColorRGBA(142, 146, 158, 200);
    private static final ColorRGBA FAINT = new ColorRGBA(96, 102, 118, 150);
    private static final ColorRGBA ICON = new ColorRGBA(122, 130, 153, 215);
    private static final ColorRGBA ERROR = new ColorRGBA(235, 114, 120, 242);
    // Same neutral-dark frosted-blur scheme as MainMenuScreen: the blur shader
    // multiplies the blurred backdrop by the tint, so grey = colorless dark glass.
    private static final ColorRGBA PANEL_TINT = new ColorRGBA(84, 86, 92, 235);
    private static final ColorRGBA PANEL_HOVER_TINT = new ColorRGBA(126, 128, 136, 245);
    private static final ColorRGBA SURFACE_TINT = new ColorRGBA(70, 72, 78, 215);

    private final Screen parent;
    private final List<AccountEntry> accounts = new ArrayList<>();
    private final wtf.wyvern.core.animations.base.Animation openAnim =
            new wtf.wyvern.core.animations.base.Animation(320L, wtf.wyvern.core.animations.base.Easing.CUBIC_OUT);
    private final wtf.wyvern.core.animations.base.Animation statusAnim =
            new wtf.wyvern.core.animations.base.Animation(200L, wtf.wyvern.core.animations.base.Easing.CUBIC_OUT);
    private final wtf.wyvern.core.animations.base.Animation scrollAnim =
            new wtf.wyvern.core.animations.base.Animation(240L, wtf.wyvern.core.animations.base.Easing.CUBIC_OUT);
    private final java.util.Map<String, wtf.wyvern.core.animations.base.Animation> hoverAnims = new java.util.HashMap<>();
    /** Accounts currently playing their removal animation. */
    private final java.util.Set<String> removingAccounts = new java.util.HashSet<>();
    private String username = "";
    private String editingAccount;
    private String status = "";
    private boolean inputActive = true;
    private boolean statusError;
    private boolean accountsLoaded;
    private int scrollRow;

    public AccountManagerScreen(Screen parent) {
        super(Text.literal("Account Manager"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        if (this.client != null && this.username.isEmpty()) {
            this.username = this.client.getSession().getUsername();
        }
        if (!this.accountsLoaded) {
            loadAccounts();
            this.accountsLoaded = true;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        CustomDrawContext draw = CustomDrawContext.of(context);
        float scale = layoutScale();
        float centerX = this.width * 0.5F;
        renderBackdrop(draw);

        // Entrance/exit animation: content fades (via fade()) and slides vertically.
        this.openAnim.update(!this.closing);
        float open = this.openAnim.getValue();
        if (this.closing && open <= 0.02F) {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
            return;
        }
        draw.getMatrices().push();
        draw.getMatrices().translate(0.0F, (1.0F - open) * 16.0F * scale, 0.0F);

        drawLogo(draw, centerX, this.height * 0.5F - 198.0F * scale, 42.0F * scale, scale);
        drawCentered(draw, Fonts.MEDIUM, "Account Manager", centerX,
                this.height * 0.5F - 148.0F * scale, 10.5F * scale, fade(accent()));

        String currentName = this.client == null ? "Unknown" : this.client.getSession().getUsername();
        drawCentered(draw, Fonts.MEDIUM, "Current account  " + currentName, centerX,
                this.height * 0.5F - 130.0F * scale, 6.7F * scale, fade(MUTED));

        float leftX = centerX - LAYOUT_WIDTH * scale * 0.5F;
        float panelY = this.height * 0.5F - 108.0F * scale;
        float leftWidth = LEFT_WIDTH * scale;
        float rightX = leftX + (LEFT_WIDTH + PANEL_GAP) * scale;
        float rightWidth = (LAYOUT_WIDTH - LEFT_WIDTH - PANEL_GAP) * scale;
        float panelHeight = PANEL_HEIGHT * scale;

        drawSurface(draw, leftX, panelY, leftWidth, panelHeight, 8.0F * scale);
        drawSurface(draw, rightX, panelY, rightWidth, panelHeight, 8.0F * scale);
        renderControls(draw, leftX, panelY, leftWidth, mouseX, mouseY, scale);
        renderAccountGrid(draw, rightX, panelY, rightWidth, mouseX, mouseY, scale, currentName);

        this.statusAnim.update(!this.status.isEmpty());
        float statusAlpha = this.statusAnim.getValue();
        if (!this.status.isEmpty() && statusAlpha > 0.01F) {
            ColorRGBA statusColor = this.statusError ? ERROR : accent();
            drawCentered(draw, Fonts.MEDIUM, this.status, centerX,
                    panelY + panelHeight + 15.0F * scale, 6.5F * scale,
                    fade(statusColor.withAlpha((int) (statusColor.getAlpha() * statusAlpha))));
        }

        draw.getMatrices().pop();
    }

    /** Applies the entrance-fade alpha to a color. */
    private ColorRGBA fade(ColorRGBA color) {
        float open = this.openAnim.getValue();
        return open >= 0.999F ? color : color.withAlpha((int) (color.getAlpha() * open));
    }

    /** Smoothly animated 0..1 hover value per control key. */
    private float hoverValue(String key, boolean hovered) {
        wtf.wyvern.core.animations.base.Animation anim = this.hoverAnims.computeIfAbsent(key,
                k -> new wtf.wyvern.core.animations.base.Animation(160L, wtf.wyvern.core.animations.base.Easing.CUBIC_OUT));
        anim.update(hovered);
        return anim.getValue();
    }

    /** Smoothly animated float that starts AT the target (no fly-in on first frame). */
    private float animFloat(String key, float target, long duration) {
        wtf.wyvern.core.animations.base.Animation anim = this.hoverAnims.get(key);
        if (anim == null) {
            anim = new wtf.wyvern.core.animations.base.Animation(duration,
                    wtf.wyvern.core.animations.base.Easing.CUBIC_OUT);
            anim.setValue(target);
            anim.setStartValue(target);
            this.hoverAnims.put(key, anim);
        }
        anim.update(target);
        return anim.getValue();
    }

    /** Entrance-fade plus per-card appear/remove alpha. */
    private ColorRGBA cardColor(ColorRGBA color, float appear) {
        ColorRGBA faded = fade(color);
        return appear >= 0.999F ? faded : faded.withAlpha((int) (faded.getAlpha() * appear));
    }

    private void renderControls(CustomDrawContext draw, float x, float y, float width,
                                int mouseX, int mouseY, float scale) {
        float padding = 12.0F * scale;
        float innerX = x + padding;
        float innerWidth = width - padding * 2.0F;
        float inputY = y + 14.0F * scale;

        drawInput(draw, innerX, inputY, innerWidth, CONTROL_HEIGHT * scale, mouseX, mouseY, scale);

        float addY = inputY + (CONTROL_HEIGHT + 15.0F) * scale;
        String addLabel = this.editingAccount == null ? "Add" : "Save";
        drawActionButton(draw, "btn:add", innerX, addY, innerWidth, addLabel,
                accent(), 0.35F, mouseX, mouseY, scale);

        float halfWidth = (innerWidth - CONTROL_GAP * scale) * 0.5F;
        float utilityY = addY + (CONTROL_HEIGHT + CONTROL_GAP) * scale;
        drawActionButton(draw, "btn:generate", innerX, utilityY, halfWidth, "Generate",
                accent(), 0.0F, mouseX, mouseY, scale);
        drawActionButton(draw, "btn:clear", innerX + halfWidth + CONTROL_GAP * scale, utilityY,
                halfWidth, "Clear all", accent(), 0.0F, mouseX, mouseY, scale);

        float backY = y + PANEL_HEIGHT * scale - CONTROL_HEIGHT * scale - 13.0F * scale;
        drawActionButton(draw, "btn:back", innerX, backY, innerWidth, "Back",
                WHITE, 0.0F, mouseX, mouseY, scale);
    }

    private void renderAccountGrid(CustomDrawContext draw, float x, float y, float width,
                                   int mouseX, int mouseY, float scale, String currentName) {
        float padding = 12.0F * scale;
        float headerY = y + 13.0F * scale;
        draw.drawText(Fonts.MEDIUM.getFont(8.0F * scale), "Saved accounts",
                x + padding, headerY, fade(WHITE));
        String count = Integer.toString(this.accounts.size());
        float countSize = 7.0F * scale;
        draw.drawText(Fonts.MEDIUM.getFont(countSize), count,
                x + width - padding - Fonts.MEDIUM.getWidth(count, countSize), headerY, fade(MUTED));

        float gridX = x + padding;
        float gridY = y + 35.0F * scale;
        float gridWidth = width - padding * 2.0F;
        float cardGap = CARD_GAP * scale;
        float cardWidth = (gridWidth - cardGap) * 0.5F;
        float cardHeight = CARD_HEIGHT * scale;
        float rowStep = (CARD_HEIGHT + CARD_GAP) * scale;
        float visibleHeight = VISIBLE_ROWS * rowStep - cardGap;

        // Smooth scrolling: the wheel changes scrollRow (the target); the anim glides.
        this.scrollAnim.update((float) this.scrollRow);
        float scrollPos = this.scrollAnim.getValue();

        if (this.accounts.isEmpty()) {
            float emptyHeight = CARD_HEIGHT * scale;
            drawPanel(draw, gridX, gridY, gridWidth, emptyHeight, 0.0F, 6.0F * scale);
            drawCentered(draw, Fonts.MEDIUM, "No saved accounts", gridX + gridWidth * 0.5F,
                    gridY + 19.5F * scale, 7.5F * scale, fade(MUTED));
        } else {
            draw.enableScissor((int) gridX, (int) gridY,
                    (int) Math.ceil(gridX + gridWidth), (int) Math.ceil(gridY + visibleHeight));
            List<String> finishedRemovals = null;
            for (int accountIndex = 0; accountIndex < this.accounts.size(); accountIndex++) {
                AccountEntry account = this.accounts.get(accountIndex);
                String name = account.name();
                int row = accountIndex / CARD_COLUMNS;
                int column = accountIndex % CARD_COLUMNS;
                // Cards glide to their slot when the layout reflows after a removal.
                float rowPos = animFloat("row:" + name, row, 220L);
                float colPos = animFloat("col:" + name, column, 220L);
                float cardX = gridX + colPos * (cardWidth + cardGap);
                float cardY = gridY + (rowPos - scrollPos) * rowStep;

                // Appear/removal animation: fresh cards fade+scale in from 0,
                // removing cards animate back to 0 and are deleted when done.
                boolean removing = this.removingAccounts.contains(name);
                float appear = hoverValue("life:" + name, !removing);
                if (removing && appear <= 0.02F) {
                    if (finishedRemovals == null) {
                        finishedRemovals = new ArrayList<>();
                    }
                    finishedRemovals.add(name);
                    continue;
                }

                if (cardY + cardHeight < gridY - rowStep || cardY > gridY + visibleHeight + rowStep) {
                    continue;
                }

                float cardScale = 0.85F + 0.15F * appear;
                float pivotX = cardX + cardWidth * 0.5F;
                float pivotY = cardY + cardHeight * 0.5F;
                draw.getMatrices().push();
                draw.getMatrices().translate(pivotX, pivotY, 0.0F);
                draw.getMatrices().scale(cardScale, cardScale, 1.0F);
                draw.getMatrices().translate(-pivotX, -pivotY, 0.0F);
                drawAccountCard(draw, account, cardX, cardY, cardWidth, cardHeight,
                        mouseX, mouseY, scale, currentName, appear);
                draw.getMatrices().pop();
            }
            draw.disableScissor();

            if (finishedRemovals != null) {
                for (String name : finishedRemovals) {
                    finishRemoval(name);
                }
            }
        }

        int totalRows = (this.accounts.size() + CARD_COLUMNS - 1) / CARD_COLUMNS;
        if (totalRows > VISIBLE_ROWS) {
            float trackHeight = VISIBLE_ROWS * rowStep - cardGap;
            float trackX = x + width - 5.0F * scale;
            float thumbHeight = trackHeight * VISIBLE_ROWS / totalRows;
            int maxScroll = totalRows - VISIBLE_ROWS;
            float thumbY = gridY + (trackHeight - thumbHeight)
                    * Math.min(Math.max(scrollPos, 0.0F), maxScroll) / maxScroll;
            DrawUtil.drawRoundedRect(draw.getMatrices(), trackX, gridY, 1.2F * scale, trackHeight,
                    BorderRadius.all(0.6F * scale), fade(new ColorRGBA(255, 255, 255, 18)));
            DrawUtil.drawRoundedRect(draw.getMatrices(), trackX, thumbY, 1.2F * scale, thumbHeight,
                    BorderRadius.all(0.6F * scale), fade(accent().withAlpha(175)));
        }

        draw.drawText(Fonts.MEDIUM.getFont(5.8F * scale),
                "Click to login  •  RMB to remove", gridX,
                y + PANEL_HEIGHT * scale - 12.0F * scale, fade(FAINT));
    }

    private void drawAccountCard(CustomDrawContext draw, AccountEntry account,
                                 float x, float y, float width, float height,
                                 int mouseX, int mouseY, float scale, String currentName,
                                 float appear) {
        boolean hovered = hovered(x, y, width, height, mouseX, mouseY);
        boolean active = account.name().equalsIgnoreCase(currentName);
        float hover = hoverValue("card:" + account.name(), hovered && appear > 0.9F);
        // No outlines: the active account is only slightly lighter than the rest.
        ColorRGBA tint = ColorRGBA.lerp(PANEL_TINT, PANEL_HOVER_TINT,
                Math.max(active ? 0.25F : 0.0F, hover));
        DrawUtil.drawBlur(draw.getMatrices(), x, y, width, height, 22.0F * layoutScale(),
                BorderRadius.all(6.0F * scale), cardColor(tint, appear));

        float headSize = 38.0F * scale;
        float headX = x + 7.0F * scale;
        float headY = y + (height - headSize) * 0.5F;
        Identifier skin = DefaultSkinHelper.getSkinTextures(offlineUuid(account.name())).texture();
        DrawUtil.drawPlayerHeadWithRoundedShader(draw.getMatrices(), skin, headX, headY, headSize,
                BorderRadius.all(6.0F * scale), cardColor(ColorRGBA.WHITE, appear));

        float textX = headX + headSize + 8.0F * scale;
        draw.drawText(Fonts.MEDIUM.getFont(8.2F * scale), account.name(),
                textX, y + 13.0F * scale, cardColor(WHITE, appear));
        draw.drawText(Fonts.MEDIUM.getFont(6.1F * scale), account.addedAt(),
                textX, y + 29.0F * scale, cardColor(MUTED, appear));
    }

    /** Input row: user-silhouette icon on the LEFT, then placeholder/text. */
    private void drawInput(CustomDrawContext draw, float x, float y, float width, float height,
                           int mouseX, int mouseY, float scale) {
        float hover = hoverValue("input", this.inputActive
                || hovered(x, y, width, height, mouseX, mouseY));
        drawPanel(draw, x, y, width, height, hover, 6.0F * scale);

        float textSize = 8.0F * scale;
        float iconSize = 10.0F * scale;
        float iconX = x + 10.0F * scale;
        float textY = y + 11.0F * scale;
        draw.drawText(Fonts.MAINMENU.getFont(iconSize), "f", iconX, textY,
                fade(this.inputActive ? accent() : ICON));

        String visibleText = this.username.isEmpty() ? "Nickname" : this.username;
        ColorRGBA textColor = this.username.isEmpty() ? MUTED : WHITE;
        float textX = iconX + Fonts.MAINMENU.getWidth("f", iconSize) + 7.0F * scale;
        draw.drawText(Fonts.MEDIUM.getFont(textSize), visibleText, textX, textY, fade(textColor));

        if (this.inputActive && (System.currentTimeMillis() / 500L) % 2L == 0L) {
            float cursorX = textX + Fonts.MEDIUM.getWidth(this.username, textSize) + 1.0F * scale;
            DrawUtil.drawRect(draw.getMatrices(), cursorX, y + 8.0F * scale,
                    Math.max(0.7F, 0.8F * scale), 12.0F * scale, fade(WHITE));
        }
    }

    /** Centered-label button (Add / Generate / Clear all / Back); baseLight lifts the idle tint. */
    private void drawActionButton(CustomDrawContext draw, String key, float x, float y, float width,
                                  String label, ColorRGBA labelColor, float baseLight,
                                  int mouseX, int mouseY, float scale) {
        float height = CONTROL_HEIGHT * scale;
        float hover = hoverValue(key, hovered(x, y, width, height, mouseX, mouseY));
        drawPanel(draw, x, y, width, height, Math.max(baseLight, hover), 6.0F * scale);

        float textSize = 7.6F * scale;
        float labelW = Fonts.MEDIUM.getWidth(label, textSize);
        ColorRGBA color = hover <= 0.001F ? labelColor : ColorRGBA.lerp(labelColor, WHITE, hover * 0.45F);
        draw.drawText(Fonts.MEDIUM.getFont(textSize), label,
                x + (width - labelW) * 0.5F, y + 11.0F * scale, fade(color));
    }

    private void renderBackdrop(CustomDrawContext draw) {
        DrawUtil.drawMainMenuBackground(draw.getMatrices(), this.width, this.height,
                MainMenuScreen.backdropTime());
    }

    private void drawSurface(CustomDrawContext draw, float x, float y, float width,
                             float height, float radius) {
        DrawUtil.drawBlur(draw.getMatrices(), x, y, width, height, 28.0F * layoutScale(),
                BorderRadius.all(radius), fade(SURFACE_TINT));
    }

    /**
     * Borderless neutral-dark frosted panel like the main menu buttons; the tint
     * smoothly brightens with the animated hover value.
     */
    private void drawPanel(CustomDrawContext draw, float x, float y, float width, float height,
                           float hoverProgress, float radius) {
        ColorRGBA tint = hoverProgress <= 0.001F ? PANEL_TINT
                : ColorRGBA.lerp(PANEL_TINT, PANEL_HOVER_TINT, hoverProgress);
        DrawUtil.drawBlur(draw.getMatrices(), x, y, width, height, 22.0F * layoutScale(),
                BorderRadius.all(radius), fade(tint));
    }

    private static final Identifier LOGO_GLOW = Identifier.of("wyvern", "icons/glow.png");

    /** WYVERN logo glyph with a soft layered accent glow behind it. */
    private void drawLogo(CustomDrawContext draw, float centerX, float y, float size, float scale) {
        ColorRGBA themeColor = accent();
        float glyphCenterY = y + size * 0.35F;
        float glowW = size * 3.4F;
        float glowH = size * 2.1F;
        draw.drawTexture(LOGO_GLOW, centerX - glowW * 0.5F, glyphCenterY - glowH * 0.5F,
                glowW, glowH, fade(themeColor.withAlpha(38)));
        draw.drawTexture(LOGO_GLOW, centerX - glowW * 0.3F, glyphCenterY - glowH * 0.3F,
                glowW * 0.6F, glowH * 0.6F, fade(themeColor.withAlpha(52)));
        drawCentered(draw, Fonts.WYVERN, "A", centerX, y, size, fade(themeColor));
    }

    private ColorRGBA accent() {
        return Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor().withAlpha(242);
    }

    private void drawCentered(CustomDrawContext draw, wtf.wyvern.core.font.MsdfFont font,
                              String text, float centerX, float y, float size, ColorRGBA color) {
        draw.drawText(font.getFont(size), text,
                centerX - font.getWidth(text, size) * 0.5F, y, color);
    }

    private float layoutScale() {
        return Math.max(0.65F, Math.min(this.width / DESIGN_WIDTH, this.height / DESIGN_HEIGHT));
    }

    private boolean hovered(float x, float y, float width, float height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private UUID offlineUuid(String account) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + account).getBytes(StandardCharsets.UTF_8));
    }

    private void login() {
        String cleanName = this.username.trim();
        if (!isValidUsername(cleanName)) {
            this.status = "Use 3-16 letters, numbers or _";
            this.statusError = true;
            return;
        }
        switchAccount(cleanName, true);
    }

    private void switchAccount(String account, boolean remember) {
        if (this.client == null) {
            return;
        }

        Session session = new Session(account, offlineUuid(account), "", Optional.empty(), Optional.empty(),
                Session.AccountType.LEGACY);
        ((MinecraftClientAccessor)this.client).wyvern$setSession(session);
        this.username = account;
        this.status = "Logged in as " + account;
        this.statusError = false;
        this.inputActive = false;

        if (remember) {
            AccountEntry previous = findAccount(this.editingAccount != null ? this.editingAccount : account);
            if (this.editingAccount != null) {
                this.accounts.removeIf(entry -> entry.name().equalsIgnoreCase(this.editingAccount));
            }
            this.accounts.removeIf(entry -> entry.name().equalsIgnoreCase(account));
            this.accounts.add(0, new AccountEntry(account,
                    previous == null ? LocalDate.now().format(DATE_FORMAT) : previous.addedAt(),
                    previous != null && previous.favorite()));
            this.editingAccount = null;
            sortFavorites();
            this.scrollRow = 0;
            saveAccounts();
        }
    }

    private AccountEntry findAccount(String account) {
        if (account == null) {
            return null;
        }
        for (AccountEntry entry : this.accounts) {
            if (entry.name().equalsIgnoreCase(account)) {
                return entry;
            }
        }
        return null;
    }

    private boolean isValidUsername(String value) {
        if (value.length() < 3 || value.length() > MAX_USERNAME_LENGTH) {
            return false;
        }
        for (int index = 0; index < value.length(); index++) {
            char chr = value.charAt(index);
            if (!Character.isLetterOrDigit(chr) && chr != '_') {
                return false;
            }
        }
        return true;
    }

    private static final String[] NICK_FIRST = {
            "Shadow", "Frost", "Night", "Silent", "Dark", "Crimson", "Astral", "Venom",
            "Ghost", "Storm", "Iron", "Lunar", "Nova", "Vortex", "Zero", "Hyper",
            "Toxic", "Blaze", "Echo", "Phantom", "Rune", "Grim", "Ember", "Static"
    };
    private static final String[] NICK_SECOND = {
            "Wolf", "Blade", "Reaper", "Hunter", "Knight", "Fox", "Raven", "Dragon",
            "Slayer", "Byte", "Wizard", "King", "Strike", "Ninja", "Rider", "Soul",
            "Flame", "Viper", "Falcon", "Core", "Shot", "Mancer", "Ling", "Smith"
    };

    /** Natural-looking random nicknames instead of WyvernNNNN. */
    private void randomizeUsername() {
        java.util.Random random = new java.util.Random();
        String first = NICK_FIRST[random.nextInt(NICK_FIRST.length)];
        String second = NICK_SECOND[random.nextInt(NICK_SECOND.length)];
        String nick = switch (random.nextInt(5)) {
            case 0 -> first + second;
            case 1 -> first + "_" + second;
            case 2 -> first.toLowerCase(java.util.Locale.ROOT) + second;
            case 3 -> first + second.toLowerCase(java.util.Locale.ROOT) + (10 + random.nextInt(90));
            default -> first + second + (1 + random.nextInt(9));
        };
        if (nick.length() > MAX_USERNAME_LENGTH) {
            nick = nick.substring(0, MAX_USERNAME_LENGTH);
        }
        this.username = nick;
        this.editingAccount = null;
        this.inputActive = true;
        this.status = "";
    }

    private Path accountsPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("wyvern").resolve("accounts.txt");
    }

    private void loadAccounts() {
        Path path = accountsPath();
        if (!Files.exists(path)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String[] values = line.split("\\|", -1);
                String account = values[0].trim();
                if (!isValidUsername(account) || findAccount(account) != null) {
                    continue;
                }
                String date = values.length > 1 && !values[1].isBlank()
                        ? values[1] : LocalDate.now().format(DATE_FORMAT);
                boolean favorite = values.length > 2 && Boolean.parseBoolean(values[2]);
                this.accounts.add(new AccountEntry(account, date, favorite));
            }
            sortFavorites();
        } catch (IOException exception) {
            this.status = "Could not load saved accounts";
            this.statusError = true;
        }
    }

    private void saveAccounts() {
        Path path = accountsPath();
        List<String> lines = this.accounts.stream()
                .map(entry -> entry.name() + "|" + entry.addedAt() + "|" + entry.favorite())
                .toList();
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            this.status = "Could not save accounts";
            this.statusError = true;
        }
    }

    private void sortFavorites() {
        this.accounts.sort((first, second) -> Boolean.compare(second.favorite(), first.favorite()));
    }

    private void toggleFavorite(int index) {
        if (index < 0 || index >= this.accounts.size()) {
            return;
        }
        AccountEntry entry = this.accounts.get(index);
        this.accounts.set(index, new AccountEntry(entry.name(), entry.addedAt(), !entry.favorite()));
        sortFavorites();
        this.scrollRow = 0;
        saveAccounts();
    }

    private void editAccount(AccountEntry entry) {
        this.username = entry.name();
        this.editingAccount = entry.name();
        this.inputActive = true;
        this.status = "Editing " + entry.name();
        this.statusError = false;
    }

    /** Starts the card's removal animation; the entry is deleted in finishRemoval. */
    private void removeAccount(int index) {
        if (index < 0 || index >= this.accounts.size()) {
            return;
        }
        this.removingAccounts.add(this.accounts.get(index).name());
    }

    private void finishRemoval(String name) {
        this.removingAccounts.remove(name);
        if (!this.accounts.removeIf(entry -> entry.name().equalsIgnoreCase(name))) {
            return;
        }
        if (name.equalsIgnoreCase(this.editingAccount)) {
            this.editingAccount = null;
        }
        this.hoverAnims.remove("row:" + name);
        this.hoverAnims.remove("col:" + name);
        this.hoverAnims.remove("life:" + name);
        this.hoverAnims.remove("card:" + name);
        clampScroll();
        saveAccounts();
        this.status = "Removed " + name;
        this.statusError = false;
    }

    private void clearAccounts() {
        this.accounts.clear();
        this.removingAccounts.clear();
        this.editingAccount = null;
        this.scrollRow = 0;
        saveAccounts();
        this.status = "Saved accounts cleared";
        this.statusError = false;
    }

    private int maxScrollRow() {
        int rows = (this.accounts.size() + CARD_COLUMNS - 1) / CARD_COLUMNS;
        return Math.max(0, rows - VISIBLE_ROWS);
    }

    private void clampScroll() {
        this.scrollRow = Math.max(0, Math.min(this.scrollRow, maxScrollRow()));
    }

    private boolean closing;

    /** Plays the exit animation; the actual screen switch happens in render(). */
    private void closeToParent() {
        this.closing = true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0 && button != 1) {
            return true;
        }

        float scale = layoutScale();
        float centerX = this.width * 0.5F;
        float leftX = centerX - LAYOUT_WIDTH * scale * 0.5F;
        float panelY = this.height * 0.5F - 108.0F * scale;
        float leftWidth = LEFT_WIDTH * scale;
        float rightX = leftX + (LEFT_WIDTH + PANEL_GAP) * scale;
        float rightWidth = (LAYOUT_WIDTH - LEFT_WIDTH - PANEL_GAP) * scale;
        float padding = 12.0F * scale;
        float innerX = leftX + padding;
        float innerWidth = leftWidth - padding * 2.0F;
        float inputY = panelY + 14.0F * scale;
        float controlHeight = CONTROL_HEIGHT * scale;
        float addY = inputY + (CONTROL_HEIGHT + 15.0F) * scale;
        float utilityY = addY + (CONTROL_HEIGHT + CONTROL_GAP) * scale;
        float halfWidth = (innerWidth - CONTROL_GAP * scale) * 0.5F;
        float backY = panelY + PANEL_HEIGHT * scale - controlHeight - 13.0F * scale;

        this.inputActive = button == 0
                && hovered(innerX, inputY, innerWidth, controlHeight, mouseX, mouseY);
        if (button == 0 && hovered(innerX, addY, innerWidth, controlHeight, mouseX, mouseY)) {
            login();
            return true;
        }
        if (button == 0 && hovered(innerX, utilityY, halfWidth, controlHeight, mouseX, mouseY)) {
            randomizeUsername();
            return true;
        }
        if (button == 0 && hovered(innerX + halfWidth + CONTROL_GAP * scale, utilityY,
                halfWidth, controlHeight, mouseX, mouseY)) {
            clearAccounts();
            return true;
        }
        if (button == 0 && hovered(innerX, backY, innerWidth, controlHeight, mouseX, mouseY)) {
            closeToParent();
            return true;
        }

        float gridX = rightX + padding;
        float gridY = panelY + 35.0F * scale;
        float gridWidth = rightWidth - padding * 2.0F;
        float cardGap = CARD_GAP * scale;
        float cardWidth = (gridWidth - cardGap) * 0.5F;
        float cardHeight = CARD_HEIGHT * scale;
        float rowStep = (CARD_HEIGHT + CARD_GAP) * scale;
        int startIndex = this.scrollRow * CARD_COLUMNS;
        int endIndex = Math.min(this.accounts.size(), startIndex + VISIBLE_ROWS * CARD_COLUMNS);

        for (int accountIndex = startIndex; accountIndex < endIndex; accountIndex++) {
            int visibleIndex = accountIndex - startIndex;
            int column = visibleIndex % CARD_COLUMNS;
            int row = visibleIndex / CARD_COLUMNS;
            float cardX = gridX + column * (cardWidth + cardGap);
            float cardY = gridY + row * rowStep;
            if (!hovered(cardX, cardY, cardWidth, cardHeight, mouseX, mouseY)) {
                continue;
            }
            if (this.removingAccounts.contains(this.accounts.get(accountIndex).name())) {
                return true;
            }
            if (button == 1) {
                removeAccount(accountIndex);
                return true;
            }
            // Icons are gone: the whole card is one login action (RMB removes).
            switchAccount(this.accounts.get(accountIndex).name(), false);
            return true;
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        float scale = layoutScale();
        float leftX = this.width * 0.5F - LAYOUT_WIDTH * scale * 0.5F;
        float rightX = leftX + (LEFT_WIDTH + PANEL_GAP) * scale;
        float panelY = this.height * 0.5F - 108.0F * scale;
        float rightWidth = (LAYOUT_WIDTH - LEFT_WIDTH - PANEL_GAP) * scale;
        if (hovered(rightX, panelY, rightWidth, PANEL_HEIGHT * scale, mouseX, mouseY)
                && maxScrollRow() > 0) {
            this.scrollRow += verticalAmount < 0.0D ? 1 : -1;
            clampScroll();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!this.inputActive || Character.isISOControl(chr) || this.username.length() >= MAX_USERNAME_LENGTH) {
            return true;
        }
        if (Character.isLetterOrDigit(chr) || chr == '_') {
            this.username += chr;
            this.status = "";
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            closeToParent();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            login();
            return true;
        }
        if (this.inputActive && keyCode == GLFW.GLFW_KEY_BACKSPACE && !this.username.isEmpty()) {
            this.username = this.username.substring(0, this.username.length() - 1);
            this.status = "";
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private record AccountEntry(String name, String addedAt, boolean favorite) {
    }
}
