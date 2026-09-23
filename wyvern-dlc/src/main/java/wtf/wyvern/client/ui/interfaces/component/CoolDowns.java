package wtf.wyvern.client.ui.interfaces.component;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.render.display.StencilUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** HUD-панель серверных и ванильных задержек предметов. */
public final class CoolDowns extends DraggableHudElement {
    private static final float V2_MIN_WIDTH = 82.0F;
    private static final float V2_MAX_WIDTH = 124.0F;
    private static final String HEADER_ICON = "LZXCV";
    private static final Pattern TIME_PATTERN = Pattern.compile(
            "(?iu)(\\d+(?:[.,]\\d+)?)\\s*(мс|ms|секунд(?:а|ы)?|сек|с|seconds?|secs?)"
    );
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(?iu)\\b(\\d+(?:[.,]\\d+)?)\\b");
    private static final Pattern COOLDOWN_MARKER = Pattern.compile(
            "(?iu)(кд|cooldown|cool-down|задержк\\w*|перезаряд\\w*|подожд\\w*|не так быстро|нельзя использовать)"
    );
    private final Animation widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
    private final Animation alpha = new Animation(200L, Easing.CUBIC_OUT);
    private final boolean v2;
    private final Map<String, CooldownEntry> cooldowns = new LinkedHashMap<>();
    private String lastUsedItem;
    private ItemStack lastUsedStack = ItemStack.EMPTY;
    private long lastUseAt;

    public CoolDowns(String name, float initialX, float initialY, float windowWidth, float windowHeight,
                     float offsetX, float offsetY, Align align, boolean v2) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.v2 = v2;
        EventManager.register(this);
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if (event.isSent() && event.getPacket() instanceof PlayerInteractItemC2SPacket interact) {
            rememberUsedItem(interact.getHand());
            return;
        }

        if (!event.isReceive()) return;

        if (event.getPacket() instanceof CooldownUpdateS2CPacket cooldownPacket) {
            handleCooldownPacket(cooldownPacket);
        } else if (event.getPacket() instanceof GameMessageS2CPacket messagePacket) {
            parseMessage(messagePacket.content());
        } else if (event.getPacket() instanceof OverlayMessageS2CPacket overlayPacket) {
            parseMessage(overlayPacket.text());
        }
    }

    private void rememberUsedItem(Hand hand) {
        if (mc.player == null) return;
        ItemStack stack = hand == Hand.OFF_HAND ? mc.player.getOffHandStack() : mc.player.getMainHandStack();
        if (!stack.isEmpty()) {
            lastUsedItem = displayName(stack);
            lastUsedStack = stack.copy();
            lastUseAt = System.currentTimeMillis();
        }
    }

    private void handleCooldownPacket(CooldownUpdateS2CPacket packet) {
        Item item = Registries.ITEM.get(packet.cooldownGroup());
        int ticks = packet.cooldown();
        ItemStack stack = findInventoryStack(item);
        if (stack == null) stack = new ItemStack(item);
        String itemName = displayName(stack);

        String key = key(itemName);
        if (ticks <= 0) {
            CooldownEntry entry = cooldowns.get(key);
            if (entry != null) entry.setActive(false);
            return;
        }

        long now = System.currentTimeMillis();
        CooldownEntry entry = cooldowns.get(key);
        if (entry == null) {
            cooldowns.put(key, new CooldownEntry(itemName, now + ticks * 50L, stack.copy()));
        } else {
            entry.refresh(now + ticks * 50L, stack);
        }
    }

    private void parseMessage(Text message) {
        String text = message.getString();
        if (!COOLDOWN_MARKER.matcher(text).find()) return;

        Matcher timeMatcher = TIME_PATTERN.matcher(text);
        double amount;
        long duration;
        if (timeMatcher.find()) {
            amount = parseNumber(timeMatcher.group(1));
            String unit = timeMatcher.group(2).toLowerCase(Locale.ROOT);
            duration = unit.startsWith("мс") || unit.equals("ms")
                    ? Math.round(amount)
                    : Math.round(amount * 1_000.0D);
        } else {
            Matcher numberMatcher = NUMBER_PATTERN.matcher(text);
            if (!numberMatcher.find()) return;
            amount = parseNumber(numberMatcher.group(1));
            duration = Math.round(amount * 1_000.0D);
        }

        if (duration <= 0L || duration > 24L * 60L * 60L * 1_000L) return;

        String itemName = findMentionedItem(text);
        ItemStack stack = findStackByName(itemName);
        if (itemName == null && lastUsedItem != null && System.currentTimeMillis() - lastUseAt < 3_000L) {
            itemName = lastUsedItem;
            stack = lastUsedStack;
        }
        if (itemName == null) itemName = "Предмет";

        long now = System.currentTimeMillis();
        String cooldownKey = key(itemName);
        CooldownEntry entry = cooldowns.get(cooldownKey);
        if (entry == null) {
            cooldowns.put(cooldownKey, new CooldownEntry(itemName, now + duration,
                    stack == null ? ItemStack.EMPTY : stack.copy()));
        } else {
            entry.refresh(now + duration, stack == null ? ItemStack.EMPTY : stack);
        }
    }

    private String findMentionedItem(String text) {
        String normalized = text.toLowerCase(Locale.ROOT);
        for (CooldownEntry entry : cooldowns.values()) {
            if (normalized.contains(entry.name().toLowerCase(Locale.ROOT))) return entry.name();
        }
        if (lastUsedItem != null && normalized.contains(lastUsedItem.toLowerCase(Locale.ROOT))) return lastUsedItem;
        return null;
    }

    private ItemStack findInventoryStack(Item item) {
        if (mc.player == null) return null;
        ItemStack main = mc.player.getMainHandStack();
        if (main.isOf(item)) return main;
        ItemStack off = mc.player.getOffHandStack();
        if (off.isOf(item)) return off;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isOf(item)) return stack;
        }
        return null;
    }

    private ItemStack findStackByName(String name) {
        if (name == null || mc.player == null) return null;
        if (displayName(mc.player.getMainHandStack()).equals(name)) return mc.player.getMainHandStack();
        if (displayName(mc.player.getOffHandStack()).equals(name)) return mc.player.getOffHandStack();
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && displayName(stack).equals(name)) return stack;
        }
        return null;
    }

    private static String displayName(ItemStack stack) {
        return stack.contains(net.minecraft.component.DataComponentTypes.CUSTOM_NAME)
                ? stack.getName().getString()
                : stack.getItem().getName().getString();
    }

    private static String key(String name) {
        return name.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim();
    }

    private static double parseNumber(String value) {
        try {
            return Double.parseDouble(value.replace(',', '.'));
        } catch (NumberFormatException ignored) {
            return 0.0D;
        }
    }

    @Override
    public void render(CustomDrawContext ctx) {
        boolean hasVisibleEntries = updateEntries();
        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        ColorRGBA themeColor = theme.getColor();
        if (v2) renderV2(ctx, themeColor, hasVisibleEntries);
        else renderClassic(ctx, themeColor, hasVisibleEntries);
    }

    private List<CooldownEntry> visibleEntries() {
        List<CooldownEntry> entries = new ArrayList<>();
        for (CooldownEntry entry : cooldowns.values()) {
            if (entry.animation().getValue() > 0.01F) entries.add(entry);
        }
        return entries;
    }

    private boolean updateEntries() {
        long now = System.currentTimeMillis();
        boolean hasVisibleEntries = false;
        for (CooldownEntry entry : cooldowns.values()) {
            if (entry.active() && entry.endAt() <= now) entry.setActive(false);
            entry.animation().update(entry.active() ? 1.0F : 0.0F);
            if (entry.animation().getValue() > 0.01F) hasVisibleEntries = true;
        }
        cooldowns.values().removeIf(entry -> !entry.active() && entry.animation().getValue() <= 0.0F);
        return hasVisibleEntries;
    }

    private String remaining(CooldownEntry entry) {
        long left = Math.max(0L, entry.endAt() - System.currentTimeMillis());
        if (left < 10_000L) return String.format(Locale.ROOT, "%.1fs", left / 1_000.0D);
        return (long) Math.ceil(left / 1_000.0D) + "s";
    }

    private void renderClassic(CustomDrawContext ctx, ColorRGBA themeColor, boolean hasVisibleEntries) {
        List<CooldownEntry> entries = visibleEntries();
        boolean show = hasVisibleEntries || mc.currentScreen instanceof ChatScreen;
        alpha.update(show ? 1.0F : 0.0F);
        float currentAlpha = alpha.getValue();
        if (currentAlpha < 0.01F) return;

        float maxWidth = Fonts.MEDIUM.getWidth("CoolDowns", 7.5F) + 24.0F;
        for (CooldownEntry entry : entries) {
            maxWidth = Math.max(maxWidth, Fonts.REGULAR.getWidth(entry.name(), 7.0F)
                    + Fonts.REGULAR.getWidth(remaining(entry), 7.0F) + 31.0F);
        }
        widthAnimation.update(Math.max(82.0F, maxWidth));
        float panelWidth = widthAnimation.getValue();
        float headerHeight = 13.5F;
        float rowHeight = 12.0F;
        float rowsHeight = 0.0F;
        for (CooldownEntry entry : entries) rowsHeight += rowHeight * entry.animation().getValue();
        float totalHeight = headerHeight + rowsHeight + 6.0F;
        float alphaValue = currentAlpha;
        ColorRGBA darker = themeColor.darker(0.95F);

        DrawUtil.drawBlur(ctx.getMatrices(), x, y, panelWidth, headerHeight, 30.0F,
                new BorderRadius(4.0F, 4.0F, 0, 0), ColorRGBA.WHITE.withAlpha((int) (255 * alphaValue)));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), x, y, panelWidth, headerHeight,
                new BorderRadius(4.0F, 4.0F, 0, 0), darker.withAlpha((int) (250 * alphaValue)));
        float iconSize = 6.5F;
        float textSize = 7.5F;
        String icon = "L";
        float iconWidth = Fonts.ICONS.getWidth(icon, iconSize);
        float headerTextWidth = Fonts.REGULAR.getWidth("CoolDowns", textSize);
        float spacing = 3.0F;
        float totalHeaderWidth = iconWidth + spacing + headerTextWidth;
        float headerX = x + (panelWidth - totalHeaderWidth) / 2.0F;
        ctx.drawText(Fonts.ICONS.getFont(iconSize), icon, headerX - 15.0F, y + 5.0F,
                themeColor.withAlpha((int) (255 * alphaValue)));
        ctx.drawText(Fonts.REGULAR.getFont(textSize), "CoolDowns",
                headerX - 16.0F + iconWidth + spacing, y + 5.0F,
                ColorRGBA.WHITE.withAlpha((int) (255 * alphaValue)));
        drawHeaderDots(ctx, x, y, panelWidth, themeColor, alphaValue);

        float currentY = y + headerHeight + 3.0F;
        for (CooldownEntry entry : entries) {
            float anim = entry.animation().getValue();
            int rowAlpha = (int) (255 * alphaValue * anim);
            String time = remaining(entry);
            float timeWidth = Fonts.REGULAR.getWidth(time, 7.0F);
            float maxNameWidth = panelWidth - timeWidth - 29.0F;
            String itemName = truncate(entry.name(), maxNameWidth, 7.0F, false);
            DrawUtil.drawBlur(ctx.getMatrices(), x + 3.0F, currentY, panelWidth - 6.0F, 10.0F,
                    20.0F, BorderRadius.all(3.0F), ColorRGBA.WHITE.withAlpha((int) (200 * alphaValue * anim)));
            DrawUtil.drawRoundedRect(ctx.getMatrices(), x + 3.0F, currentY, panelWidth - 6.0F, 10.0F,
                    BorderRadius.all(3.0F), darker.withAlpha((int) (180 * alphaValue * anim)));
            drawItemIcon(ctx, entry.stack(), x + 5.0F, currentY + 0.2F, 0.55F, alphaValue * anim);
            ctx.drawText(Fonts.REGULAR.getFont(7.0F), itemName, x + 17.0F, currentY + 2.5F,
                    ColorRGBA.WHITE.withAlpha(rowAlpha));
            ctx.drawText(Fonts.REGULAR.getFont(7.0F), time, x + panelWidth - timeWidth - 6.0F,
                    currentY + 2.5F, themeColor.withAlpha(rowAlpha));
            currentY += rowHeight * anim;
        }
        width = panelWidth;
        height = totalHeight;
    }

    private void renderV2(CustomDrawContext ctx, ColorRGBA themeColor, boolean hasVisibleEntries) {
        List<CooldownEntry> entries = visibleEntries();
        boolean show = hasVisibleEntries || mc.currentScreen instanceof ChatScreen;
        alpha.update(show ? 1.0F : 0.0F);
        float currentAlpha = alpha.getValue();
        if (currentAlpha < 0.01F) return;

        float maxWidth = Fonts.MEDIUM.getWidth("CoolDowns", 7.5F) + 24.0F;
        for (CooldownEntry entry : entries) {
            maxWidth = Math.max(maxWidth, Fonts.MEDIUM.getWidth(entry.name(), 7.2F)
                    + Fonts.MEDIUM.getWidth(remaining(entry), 7.2F)
                    + Fonts.MEDIUM.getWidth("|", 6.5F) + 33.0F);
        }
        widthAnimation.update(Math.max(V2_MIN_WIDTH, Math.min(V2_MAX_WIDTH, maxWidth)));
        float panelWidth = widthAnimation.getValue();
        float headerHeight = 18.9F;
        float rowsHeight = 0.0F;
        for (CooldownEntry entry : entries) rowsHeight += 12.0F * entry.animation().getValue();
        float bodyHeight = rowsHeight + 6.0F;
        float totalHeight = headerHeight + bodyHeight;
        int a = (int) (255 * currentAlpha);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), x, y, panelWidth, totalHeight,
                BorderRadius.all(5.5F), new ColorRGBA(23, 23, 23, a));
        StencilUtil.push();
        DrawUtil.drawRoundedRect(ctx.getMatrices(), x + 1.0F, y + 1.0F, panelWidth - 2.0F, totalHeight - 2.0F,
                BorderRadius.all(4.5F), ColorRGBA.BLACK);
        StencilUtil.read(1);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), x + 1.0F, y + 1.0F, panelWidth - 2.0F, headerHeight - 1.0F,
                new BorderRadius(4.5F, 4.5F, 0.0F, 0.0F), new ColorRGBA(11, 11, 11, a));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), x + 1.0F, y + headerHeight, panelWidth - 2.0F, bodyHeight - 1.0F,
                new BorderRadius(0.0F, 0.0F, 4.5F, 4.5F), new ColorRGBA(17, 17, 17, a));
        StencilUtil.pop();

        float iconSize = 11.0F;
        float textX = x + 6.5F;
        ctx.drawText(Fonts.WYVERN.getFont(iconSize), HEADER_ICON, textX, y + 6.0F, themeColor.withAlpha(a));
        float slashX = textX + Fonts.WYVERN.getWidth(HEADER_ICON, iconSize) + 1.5F;
        ctx.drawText(Fonts.BOLD.getFont(7.5F), "/", slashX, y + 7.1F, ColorRGBA.WHITE.withAlpha((int) (110 * currentAlpha)));
        float titleX = slashX + Fonts.BOLD.getWidth("/", 7.5F) + 3.0F;
        float titleW = Fonts.MEDIUM.getWidth("CoolDowns", 7.5F);
        ctx.drawTexture(Wyvern.id("icons/glow.png"), titleX + (titleW / 2.0F) - 30.0F,
                y + 7.5F + (7.5F / 2.0F) - 17.5F, 60.0F, 35.0F,
                ColorRGBA.WHITE.withAlpha((int) (18 * currentAlpha)));
        ctx.drawText(Fonts.MEDIUM.getFont(7.5F), "CoolDowns", titleX, y + 7.5F, ColorRGBA.WHITE.withAlpha((int) (230 * currentAlpha)));
        drawHeaderDots(ctx, x, y, panelWidth, themeColor, currentAlpha);

        float currentY = y + headerHeight + 1.5F;
        ColorRGBA separatorColor = ColorRGBA.fromHex("#535353");
        for (CooldownEntry entry : entries) {
            float anim = entry.animation().getValue();
            int rowAlpha = (int) (255 * currentAlpha * anim);
            String time = remaining(entry);
            float timeWidth = Fonts.MEDIUM.getWidth(time, 7.2F);
            float separatorWidth = Fonts.MEDIUM.getWidth("|", 6.5F);
            float maxNameWidth = panelWidth - timeWidth - separatorWidth - 33.0F;
            String itemName = truncate(entry.name(), maxNameWidth, 7.2F, true);
            drawItemIcon(ctx, entry.stack(), x + 5.0F, currentY + 0.5F, 0.62F, currentAlpha * anim);
            ctx.drawText(Fonts.MEDIUM.getFont(7.2F), itemName, x + 18.0F, currentY + 3.4F,
                    ColorRGBA.WHITE.withAlpha(rowAlpha));
            float separatorX = x + panelWidth - timeWidth - separatorWidth - 9.0F;
            ctx.drawText(Fonts.MEDIUM.getFont(6.5F), "|", separatorX, currentY + 3.2F,
                    separatorColor.withAlpha(rowAlpha));
            ctx.drawText(Fonts.MEDIUM.getFont(7.2F), time, x + panelWidth - timeWidth - 6.5F,
                    currentY + 3.4F, themeColor.withAlpha(rowAlpha));
            currentY += 12.0F * anim;
        }
        width = panelWidth;
        height = totalHeight;
    }

    private void drawHeaderDots(CustomDrawContext ctx, float panelX, float panelY, float panelWidth,
                                ColorRGBA color, float alphaValue) {
        String dots = "...";
        float size = 11.0F;
        float dotsWidth = Fonts.BOLD.getWidth(dots, size);
        ctx.drawText(Fonts.BOLD.getFont(size), dots, panelX + panelWidth - dotsWidth - 6.0F,
                panelY + 3.3F, color.withAlpha((int) (255 * alphaValue)));
    }

    private String truncate(String text, float maxWidth, float size, boolean medium) {
        if (maxWidth <= 0.0F) return "";
        float textWidth = medium ? Fonts.MEDIUM.getWidth(text, size) : Fonts.REGULAR.getWidth(text, size);
        if (textWidth <= maxWidth) return text;

        String dots = "..";
        float dotsWidth = medium ? Fonts.MEDIUM.getWidth(dots, size) : Fonts.REGULAR.getWidth(dots, size);
        if (maxWidth <= dotsWidth) return "";
        StringBuilder result = new StringBuilder();
        float width = 0.0F;
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            float characterWidth = medium
                    ? Fonts.MEDIUM.getWidth(character, size)
                    : Fonts.REGULAR.getWidth(character, size);
            if (width + characterWidth + dotsWidth > maxWidth) break;
            result.append(character);
            width += characterWidth;
        }
        return result + dots;
    }

    private void drawItemIcon(CustomDrawContext ctx, ItemStack stack, float iconX, float iconY,
                              float scale, float alphaValue) {
        if (stack == null || stack.isEmpty()) return;
        ctx.getMatrices().push();
        ctx.getMatrices().translate(iconX, iconY, 0.0F);
        float iconAnimation = Math.max(0.01F, alphaValue);
        ctx.getMatrices().scale(scale * iconAnimation, scale * iconAnimation, 1.0F);
        ctx.drawItem(stack, 0, 0);
        ctx.getMatrices().pop();
    }

    private static final class CooldownEntry {
        private final String name;
        private final Animation animation = new Animation(250L, Easing.CUBIC_OUT);
        private long endAt;
        private ItemStack stack;
        private boolean active = true;

        private CooldownEntry(String name, long endAt, ItemStack stack) {
            this.name = name;
            this.endAt = endAt;
            this.stack = stack;
        }

        private void refresh(long endAt, ItemStack stack) {
            this.endAt = endAt;
            this.stack = stack.copy();
            this.active = true;
        }

        private String name() {
            return name;
        }

        private long endAt() {
            return endAt;
        }

        private ItemStack stack() {
            return stack;
        }

        private boolean active() {
            return active;
        }

        private void setActive(boolean active) {
            this.active = active;
        }

        private Animation animation() {
            return animation;
        }
    }
}
