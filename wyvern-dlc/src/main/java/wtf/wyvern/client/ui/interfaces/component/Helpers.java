package wtf.wyvern.client.ui.interfaces.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.client.modules.impl.misc.FTHelper;
import wtf.wyvern.client.modules.impl.misc.HolyWorldHelper;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.render.display.Keyboard;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;

/** Компактные карточки биндов HolyWorldHelper и FTHelper. */
public final class Helpers extends DraggableHudElement {
    private static final float CARD_WIDTH = 40.5F;
    private static final float CARD_HEIGHT = 20.0F;
    private static final float CARD_GAP = 2.0F;
    private static final Pattern NON_WORD = Pattern.compile("[^\\p{L}\\p{N}]+");
    private static final Pattern TIME_PATTERN = Pattern.compile(
            "(?iu)(\\d+(?:[.,]\\d+)?)\\s*(мс|ms|секунд(?:а|ы)?|сек|с|seconds?|secs?)"
    );
    private static final Pattern COOLDOWN_MARKER = Pattern.compile(
            "(?iu)(кд|cooldown|cool-down|задержк\\w*|перезаряд\\w*|подожд\\w*|не так быстро|нельзя использовать)"
    );

    private final Animation widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
    private final Animation alpha = new Animation(200L, Easing.CUBIC_OUT);
    private final List<HelperEntry> entries = List.of(
            new HelperEntry("Стан", HolyWorldHelper.INSTANCE.useStun, Items.NETHER_STAR),
            new HelperEntry("Трапка", HolyWorldHelper.INSTANCE.useTrap, Items.POPPED_CHORUS_FRUIT),
            new HelperEntry("Взрывная трапка", HolyWorldHelper.INSTANCE.useExplosiveTrap, Items.PRISMARINE_SHARD),
            new HelperEntry("Взрывная штучка", HolyWorldHelper.INSTANCE.useExplosiveThing, Items.FIRE_CHARGE, "Взрывная штука"),
            new HelperEntry("Ком снега", HolyWorldHelper.INSTANCE.useSnowball, Items.SNOWBALL),
            new HelperEntry("Светильник Джейка", HolyWorldHelper.INSTANCE.useJakeLantern, Items.JACK_O_LANTERN),
            new HelperEntry("Взрывная палочка", HolyWorldHelper.INSTANCE.useExplosiveWand, Items.BLAZE_ROD),
            new HelperEntry("Прощальный гул", HolyWorldHelper.INSTANCE.useFarewellHum, Items.GOAT_HORN),
            new HelperEntry("Пузырь опыта", HolyWorldHelper.INSTANCE.useExperienceBubble, Items.EXPERIENCE_BOTTLE),
            new HelperEntry("Особый компас", HolyWorldHelper.INSTANCE.useSpecialCompass, Items.COMPASS),
            new HelperEntry("Дезориентация", FTHelper.INSTANCE.useDezor, Items.ENDER_EYE,
                    true, FTHelper.INSTANCE::isEnabled),
            new HelperEntry("Трапка", FTHelper.INSTANCE.useTrap, Items.NETHERITE_SCRAP,
                    true, FTHelper.INSTANCE::isEnabled),
            new HelperEntry("Явная пыль", FTHelper.INSTANCE.usePil, Items.SUGAR,
                    true, FTHelper.INSTANCE::isEnabled),
            new HelperEntry("Огненный смерч", FTHelper.INSTANCE.useSmerch, Items.FIRE_CHARGE,
                    true, FTHelper.INSTANCE::isEnabled),
            new HelperEntry("Пласт", FTHelper.INSTANCE.usePlast, Items.DRIED_KELP,
                    true, FTHelper.INSTANCE::isEnabled),
            new HelperEntry("Божья аура", FTHelper.INSTANCE.useAura, Items.PHANTOM_MEMBRANE,
                    true, FTHelper.INSTANCE::isEnabled),
            new HelperEntry("Снежок", FTHelper.INSTANCE.useSnow, Items.SNOWBALL,
                    true, FTHelper.INSTANCE::isEnabled)
    );
    private ItemStack lastUsedStack = ItemStack.EMPTY;

    public Helpers(String name, float initialX, float initialY, float windowWidth, float windowHeight,
                   float offsetX, float offsetY, Align align) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        EventManager.register(this);
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if (event.isSent() && event.getPacket() instanceof PlayerInteractItemC2SPacket interact
                && mc.player != null) {
            ItemStack stack = interact.getHand() == Hand.OFF_HAND
                    ? mc.player.getOffHandStack() : mc.player.getMainHandStack();
            if (findEntry(stack) != null) lastUsedStack = stack.copy();
            return;
        }

        if (!event.isReceive()) return;
        if (event.getPacket() instanceof GameMessageS2CPacket message) {
            parseMessage(message.content().getString());
            return;
        }
        if (event.getPacket() instanceof OverlayMessageS2CPacket message) {
            parseMessage(message.text().getString());
            return;
        }
        if (!(event.getPacket() instanceof CooldownUpdateS2CPacket packet)) return;

        Item item = Registries.ITEM.get(packet.cooldownGroup());
        ItemStack stack = findInventoryStack(item);
        if (stack == null && !lastUsedStack.isEmpty()) stack = lastUsedStack;
        HelperEntry entry = findEntry(stack);
        if (entry == null) return;

        if (packet.cooldown() <= 0) {
            entry.cooldownEnd = 0L;
        } else {
            entry.cooldownEnd = System.currentTimeMillis() + packet.cooldown() * 50L;
        }
    }

    private void parseMessage(String text) {
        if (!COOLDOWN_MARKER.matcher(text).find()) return;
        Matcher matcher = TIME_PATTERN.matcher(text);
        if (!matcher.find()) return;

        double amount;
        try {
            amount = Double.parseDouble(matcher.group(1).replace(',', '.'));
        } catch (NumberFormatException ignored) {
            return;
        }
        String unit = matcher.group(2).toLowerCase(Locale.ROOT);
        long duration = unit.equals("ms") || unit.equals("мс")
                ? Math.round(amount) : Math.round(amount * 1_000.0D);
        if (duration <= 0L || duration > 24L * 60L * 60L * 1_000L) return;

        String normalized = normalize(text);
        for (HelperEntry entry : entries) {
            for (String alias : entry.aliases) {
                if ((" " + normalized + " ").contains(" " + normalize(alias) + " ")) {
                    entry.cooldownEnd = System.currentTimeMillis() + duration;
                    return;
                }
            }
        }
        HelperEntry lastEntry = findEntry(lastUsedStack);
        if (lastEntry != null) lastEntry.cooldownEnd = System.currentTimeMillis() + duration;
    }

    @Override
    public void render(CustomDrawContext ctx) {
        updateEntries();

        boolean hasEntries = false;
        for (HelperEntry entry : entries) {
            if (entry.animation.getValue() > 0.01F) {
                hasEntries = true;
                break;
            }
        }
        boolean show = hasEntries || mc.currentScreen instanceof ChatScreen;
        alpha.update(show ? 1.0F : 0.0F);
        float currentAlpha = alpha.getValue();
        if (currentAlpha < 0.01F) return;

        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        ColorRGBA themeColor = theme.getColor();
        List<HelperEntry> visible = new ArrayList<>();
        for (HelperEntry entry : entries) {
            if (entry.animation.getValue() > 0.01F) visible.add(entry);
        }

        float animatedCardsWidth = 0.0F;
        for (HelperEntry entry : visible) {
            animatedCardsWidth += (CARD_WIDTH + CARD_GAP) * entry.animation.getValue();
        }
        float targetWidth = visible.isEmpty()
                ? CARD_WIDTH
                : Math.max(CARD_WIDTH, animatedCardsWidth - CARD_GAP);
        widthAnimation.update(targetWidth);
        float panelWidth = Math.max(CARD_WIDTH, widthAnimation.getValue());
        float currentX = x;
        for (HelperEntry entry : visible) {
            float itemAnimation = entry.animation.getValue();
            int itemAlpha = Math.max(0, Math.min(255, (int) (255 * currentAlpha * itemAnimation)));
            renderCard(ctx, entry, currentX, y, themeColor, itemAlpha, itemAnimation);
            currentX += (CARD_WIDTH + CARD_GAP) * itemAnimation;
        }

        width = panelWidth;
        height = visible.isEmpty() ? 0.0F : CARD_HEIGHT;
    }

    private void renderCard(CustomDrawContext ctx, HelperEntry entry, float cardX, float cardY,
                            ColorRGBA themeColor, int itemAlpha, float animation) {
        float progress = Math.max(0.0F, Math.min(1.0F, animation));
        float scale = 0.84F + 0.16F * progress;
        float centerX = cardX + CARD_WIDTH * 0.5F;
        float centerY = cardY + CARD_HEIGHT * 0.5F;
        ctx.getMatrices().push();
        ctx.getMatrices().translate(centerX, centerY + (1.0F - progress) * 4.0F, 0.0F);
        ctx.getMatrices().scale(scale, scale, 1.0F);
        ctx.getMatrices().translate(-centerX, -centerY, 0.0F);

        DrawUtil.drawRoundedRect(ctx.getMatrices(), cardX, cardY, CARD_WIDTH, CARD_HEIGHT,
                BorderRadius.all(5.5F), new ColorRGBA(23, 23, 23, itemAlpha));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), cardX + 1.0F, cardY + 1.0F,
                CARD_WIDTH - 2.0F, CARD_HEIGHT - 2.0F, BorderRadius.all(4.5F),
                new ColorRGBA(11, 11, 11, itemAlpha));

        String key = Keyboard.getKeyName(entry.bind.getKeyCode());
        if (key.isEmpty()) key = "-";
        key = key.length() > 5 ? key.substring(0, 5) : key;

        float keyWidth = Fonts.BOLD.getWidth(key, 8.0F);
        float textAreaX = cardX + 17.0F;
        float textAreaWidth = CARD_WIDTH - 19.0F;
        ctx.drawText(Fonts.BOLD.getFont(8.0F), key, textAreaX + (textAreaWidth - keyWidth) / 2.0F,
                cardY + 6.8F, themeColor.withAlpha(itemAlpha));

        boolean isTrap = entry.name.equals("Трапка");
        boolean isStun = entry.name.equals("Стан");
        float iconOffsetY = isTrap || isStun ? 3.5F : 4.0F;
        drawItemIcon(ctx, entry.stack, cardX + 5.0F, cardY + iconOffsetY, itemAlpha);

        String cooldown = entry.cooldownEnd > System.currentTimeMillis()
                ? formatCooldown(entry.cooldownEnd - System.currentTimeMillis()) : "—";
        float cooldownWidth = Fonts.MEDIUM.getWidth(cooldown, 5.8F);
        ctx.drawText(Fonts.MEDIUM.getFont(5.8F), cooldown,
                textAreaX + (textAreaWidth - cooldownWidth) / 2.0F, cardY + 13.0F,
                themeColor.withAlpha(itemAlpha));
        ctx.getMatrices().pop();
    }

    private void updateEntries() {
        for (HelperEntry entry : entries) {
            entry.active = entry.moduleEnabled.getAsBoolean() && entry.bind.getKeyCode() != -1;
            entry.animation.update(entry.active ? 1.0F : 0.0F);
            // entry.stack is only read when the card is visible; skip the 38-slot
            // inventory scan for fully faded-out entries.
            if (!entry.active && entry.animation.getValue() <= 0.01F) {
                continue;
            }
            ItemStack inventoryStack = entry.matchByItem
                    ? findInventoryStack(entry.fallbackItem)
                    : findInventoryStack(entry.aliases);
            entry.stack = inventoryStack == null || inventoryStack.isEmpty()
                    ? entry.fallbackCopy : inventoryStack;
        }
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

    private ItemStack findInventoryStack(List<String> aliases) {
        if (mc.player == null) return ItemStack.EMPTY;
        ItemStack main = findMatchingStack(mc.player.getMainHandStack(), aliases);
        if (!main.isEmpty()) return main;
        ItemStack off = findMatchingStack(mc.player.getOffHandStack(), aliases);
        if (!off.isEmpty()) return off;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = findMatchingStack(mc.player.getInventory().getStack(i), aliases);
            if (!stack.isEmpty()) return stack;
        }
        return ItemStack.EMPTY;
    }

    private ItemStack findMatchingStack(ItemStack stack, List<String> aliases) {
        if (stack == null || stack.isEmpty() || !stack.contains(net.minecraft.component.DataComponentTypes.CUSTOM_NAME)) {
            return ItemStack.EMPTY;
        }
        String itemName = " " + normalize(stack.getName().getString()) + " ";
        for (String alias : aliases) {
            // Aliases are pre-normalized in the HelperEntry constructor.
            if (itemName.contains(" " + alias + " ")) return stack;
        }
        return ItemStack.EMPTY;
    }

    private HelperEntry findEntry(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        for (HelperEntry entry : entries) {
            if (!entry.moduleEnabled.getAsBoolean()) continue;
            if (entry.matchByItem ? stack.isOf(entry.fallbackItem) : matches(stack, entry.aliases)) {
                return entry;
            }
        }
        return null;
    }

    private boolean matches(ItemStack stack, List<String> aliases) {
        return !findMatchingStack(stack, aliases).isEmpty();
    }

    private void drawItemIcon(CustomDrawContext ctx, ItemStack stack, float iconX, float iconY, int itemAlpha) {
        if (stack == null || stack.isEmpty()) return;
        ctx.getMatrices().push();
        ctx.getMatrices().translate(iconX, iconY, 0.0F);
        float iconAnimation = 0.69125F + 0.05F * (itemAlpha / 255.0F);
        ctx.getMatrices().scale(iconAnimation, iconAnimation, 1.0F);
        ctx.drawItem(stack, 0, 0);
        ctx.getMatrices().pop();
    }

    private static String formatCooldown(long milliseconds) {
        return Math.max(1L, (long) Math.ceil(milliseconds / 1_000.0D)) + "s";
    }

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    private static String normalize(String value) {
        return WHITESPACE.matcher(NON_WORD.matcher(value.toLowerCase(Locale.ROOT).replace('ё', 'е'))
                .replaceAll(" ").trim()).replaceAll(" ");
    }

    private static final class HelperEntry {
        private final String name;
        private final BindSetting bind;
        private final List<String> aliases;
        private final ItemStack fallbackStack;
        private final Item fallbackItem;
        private final boolean matchByItem;
        private final BooleanSupplier moduleEnabled;
        private final ItemStack fallbackCopy;
        private final Animation animation = new Animation(220L, Easing.CUBIC_OUT);
        private ItemStack stack = ItemStack.EMPTY;
        private long cooldownEnd;
        private boolean active;

        private HelperEntry(String name, BindSetting bind, Item fallbackItem, String... aliases) {
            this(name, bind, fallbackItem, false, HolyWorldHelper.INSTANCE::isEnabled, aliases);
        }

        private HelperEntry(String name, BindSetting bind, Item fallbackItem,
                            boolean matchByItem, BooleanSupplier moduleEnabled, String... aliases) {
            this.name = name;
            this.bind = bind;
            this.fallbackStack = fallbackItem.getDefaultStack();
            this.fallbackItem = fallbackItem;
            this.matchByItem = matchByItem;
            this.moduleEnabled = moduleEnabled;
            // Aliases are matched against normalized item names; normalize is
            // idempotent, so pre-normalizing here lets the per-slot matcher skip
            // re-normalizing every alias on every scan with identical results.
            List<String> names = new ArrayList<>();
            names.add(normalize(name));
            for (String alias : aliases) names.add(normalize(alias));
            this.aliases = List.copyOf(names);
            this.fallbackCopy = this.fallbackStack.copy();
        }
    }
}
